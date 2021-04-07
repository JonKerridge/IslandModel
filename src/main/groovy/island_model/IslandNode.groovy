package island_model

import jcsp.lang.CSProcess
import jcsp.lang.ChannelInput
import jcsp.lang.ChannelOutput


/**
 *
 */

class IslandNode implements CSProcess{

  ChannelInput fromCoordinator
  ChannelOutput toCoordinator
  int nodeID
  int instances
  Random rng

  void run(){
//    println "Node $nodeID invoked"
    for ( i in 0 ..< instances){
      ProblemSpecification spec = fromCoordinator.read() as ProblemSpecification
      int instance = spec.instance
      assert instance == i : "Mismatch between Node instance value and local counter"
      int migrationInterval = spec.migrationInterval
      int migrationSize = spec.migrationSize
      int crossoverPoints = spec.crossoverPoints
      int maxGenerations = spec.maxGenerations
      double crossoverProbability = spec.crossoverProbability
      double mutationProbability = spec.mutationProbability
      rng = new Random(spec.seeds[nodeID])
//      println "IN: created rng for node $nodeID with seed ${spec.seeds[nodeID]}"
      //create a population and its individuals
      Class popClass = Class.forName(spec.populationClass)
      Population pop = popClass.newInstance(spec.populationPerNode,
          spec.geneLength,
          spec.crossoverProbability,
          spec.mutationProbability,
          spec.dataFileName, rng )
      Individual foundIndividual
      ConvergedRecord convergent = null
      int generation = 0
      int migrationCount = 0
      boolean converged = false
      boolean terminated = false
      while ((generation < maxGenerations)  && (! converged) && (! terminated)){
        // undertake generation processing
        if (rng.nextDouble() < crossoverProbability) {
          pop.reproduce(crossoverPoints)
        }
        foundIndividual = pop.convergence()
        if ( foundIndividual != null) {
          converged = true
          // record the details of the found individual
          convergent = new ConvergedRecord(
              convergedIndividual: foundIndividual,
              generationsTaken: generation,
              seedValue:  spec.seeds[nodeID],
              findingNode: nodeID,
              instance: i )
        }
        // only communicate with IslandCoordinator when migration
        //is possible so that we can control deadlock in the Node process
        if (migrationCount == migrationInterval){
          // undertake migration between nodes
          // or deal with a node that has converged
          if (converged)
            // send convergent record instead of migrants
            toCoordinator.write(convergent)
          else {
            //undertaking migration
            List <Integer> migrantIndices = pop.selectMigrants(migrationSize)
            toCoordinator.write(new MigrantRecord(
                migratingIndividuals:pop.getMigrants(migrantIndices)))
            // returned object could be either MigrantRecord or TerminateRecord
            Object returnedData
            returnedData = fromCoordinator.read()
            if (returnedData instanceof TerminateRecord) {
              // termination record has value false in this case
              assert !((TerminateRecord) returnedData).terminate :
                  "Node $nodeID: Terminate Record has wrong value, false expected"
              terminated = true
            }
            else {
              // have got some new immigrants to be incorporated
              assert returnedData instanceof MigrantRecord :
                  "Node $nodeID:  Expecting a Migrant record"
              List <Individual> immigrants
              immigrants = ((MigrantRecord)returnedData).migratingIndividuals
//              println "Immigrants for Node $nodeID"
//              immigrants.each{println "$it"}
              pop.includeImmigrants(immigrants, migrantIndices)
              migrationCount = 0
            } // returned record processing
          } // converged test
        } // end of migration count processing
        generation = generation + 1
        migrationCount = migrationCount + 1

      } // generation loop
      // send termination record indicating maxGenerations exceeded
      // expectation would be all nodes exceed maxGenerations
      // but there could be late convergence in one of the nodes
      // These cases can only occur iff the node has not been
      // terminated due to another node converging
      if ( ! terminated)
        if (converged)
          toCoordinator.write(convergent)
        else
          // have definitely exceeded maxGenerations
          toCoordinator.write (new TerminateRecord(terminate: true))
//      println "Node $nodeID ended processing instance $i"
    }  // each instance
//    println "Node $nodeID terminated"
  }
}
