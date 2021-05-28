package island_model

import groovy_jcsp.ChannelInputList
import groovy_jcsp.ChannelOutputList
import jcsp.lang.CSProcess
import jcsp.lang.ChannelInput
import jcsp.lang.ChannelOutput

class IslandCoordinator implements CSProcess{
  ChannelInput input
  ChannelOutput output
  ChannelOutputList toNodes
  ChannelInputList fromNodes
  int nodes, instances
  IslandTopology topology

  void run(){
//    println "IC: starting $instances, $nodes"
    for ( i in 0 ..< instances) {
      int migrationCounter = 0
      IslandProblemSpecification spec = input.read() as IslandProblemSpecification
      output.write(spec)  // sent to collect process
//      println "IC - intance  $i : read and written the spec"
      for (n in 0..< nodes) {
        // need to send a distinct copy of the spec to each node
        IslandProblemSpecification nodeSpec = spec.copySpecification()
        toNodes[n].write(nodeSpec)
      }
      // at this point the code iterates until convergence or maxGenerations exceeded
      boolean running = true
      while (running){
        // read inputs from each node in parallel
//        println "starting coordinator loop"
        List returns = fromNodes.read()
//        println "IC ${returns.size()} returns read"
        // each return could be ConvergedRecord or TerminateRecord(true)
        // or MigrantRecord  need to determine specific composition
        List <Integer> convergedIndices = []
        List <Integer> terminatedIndices = []
        List <Integer> migratingIndices = []

        List <ConvergedRecord> convergedData = []
        for ( n in 0 ..< nodes){
          if (returns[n] instanceof ConvergedRecord)
            convergedIndices << n
          else if (returns[n] instanceof TerminateRecord)
            terminatedIndices << n
          else migratingIndices << n
        } // process returns from Nodes
        List <Integer> allReturnIndices = convergedIndices + terminatedIndices + migratingIndices
        assert allReturnIndices.size() == nodes :
            "Coordinator: Node count in returns ${allReturnIndices.size()} should be $nodes"
        // now determine what to do
        if (convergedIndices != []) {
          // at least one converged node has been found
          convergedIndices.each {
            convergedData << (ConvergedRecord) returns[it]
          }
          output.write(convergedData)
          // now terminate the nodes wanting to migrate
          // if the remaining nodes want to Terminate then they will already have stopped
          // processing this instance.  This will only happen if convergence occurs after
          // the last possible migration phase before termination; in this case migratingIndices
          // will be empty
          migratingIndices.each {
            toNodes[it].write(new TerminateRecord(terminate: false))
          }
          running = false
        }
        else  {
          // either they should all have terminated
          // or are all wanting to undertake migration
          assert (terminatedIndices.size() == nodes) || ( migratingIndices.size() == nodes) :
              "Terminated: ${terminatedIndices.size()},  Migrated: ${migratingIndices.size()}  Nodes: $nodes"
          if (terminatedIndices.size() == nodes) {
//            println "IC : terminating"
            running = false
            // TODO modify so that the best solution is output even though convergence not reached
            TerminationCollection terminationCollection = new TerminationCollection(bestRecords: [])
            terminatedIndices.each{
              terminationCollection.bestRecords << ((TerminateRecord) returns[it]).bestSolution
            }
//            println "IC: ${terminationCollection.bestRecords.size()} termination records"
            output.write(terminationCollection) // informs collect that no convergence was found
          }
          else {
            // nodes wanting to do migration
            // need to make migration user defined or enable different topologies
            // for testing will send migrants to next node in sequence ie a ring topology
//            println "IC : MIGRATION $migrationCounter"
            migrationCounter = migrationCounter +1
            topology.distribute(returns , toNodes)
          }
        }// convergence testing
      } // running loop
    } // instance processing
//    println "IC: terminating"
  }

}
