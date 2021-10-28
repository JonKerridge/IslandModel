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

  void run(){
    Random rng

//    println "Node $nodeID invoked"
    for ( i in 0 ..< instances){
      IslandProblemSpecification spec = fromCoordinator.read() as IslandProblemSpecification
      int instance = spec.instance
      assert instance == i : "Mismatch between Node instance value and local counter"
      int migrationInterval = spec.migrationInterval
      int migrationSize = spec.migrationSize
      int crossoverPoints = spec.crossoverPoints
      int maxGenerations = spec.maxGenerations
      double crossoverProbability = spec.crossoverProbability
      double mutationProbability = spec.mutationProbability
      BigDecimal convergenceLimit = spec.convergenceLimit
      rng = new Random(spec.seeds[nodeID])
//      println "IN: created rng for node $nodeID with seed ${spec.seeds[nodeID]}"
      //create a population and its population
      Class popClass = Class.forName(spec.populationClass)
      IslandPopulation pop = popClass.newInstance(spec.populationPerNode,
          spec.geneLength,
          spec.crossoverProbability,
          spec.mutationProbability,
          spec.dataFileName,
          spec.convergenceLimit,
          rng, nodeID)
      IslandIndividual foundIndividual
      ConvergedRecord convergent = null
      int generation = 0
      int migrationCount = 0
      boolean converged = false
      boolean terminated = false
      while ((! converged)  && (! terminated) && (generation < maxGenerations) ){
        // undertake generation processing
//        println "Node $nodeID: starting gen loop, $converged, $generation, $migrationCount"
        if (rng.nextDouble() < crossoverProbability) {
          pop.reproduce(crossoverPoints)
        }
        foundIndividual = pop.convergence(convergenceLimit)
        if ( foundIndividual != null) {
          converged = true
          // record the details of the found individual
          convergent = new ConvergedRecord(
              convergedIndividual: foundIndividual,
              generationsTaken: generation,
              seedValue:  spec.seeds[nodeID],
              findingNode: nodeID,
              instance: i )
          toCoordinator.write(convergent)
//          println "IN-$nodeID sent convergent ${convergent}"
        }
        // only communicate with IslandCoordinator when migration
        //is possible so that we can control deadlock in the Node process
        if (migrationCount == migrationInterval){
//          println "IN -$nodeID: $converged, $generation, $migrationCount"
          // undertake migration between nodes
          if (!converged) {
            //undertaking migration
            List <Integer> migrantIndices = pop.selectMigrants(migrationSize)
            toCoordinator.write(new MigrantRecord(
                migratingIndividuals:pop.getMigrants(migrantIndices)))
//            println "IN-$nodeID sent MigrantRecord"
            // returned object could be either MigrantRecord or TerminateRecord
            Object returnedData
            returnedData = fromCoordinator.read()
            if (returnedData instanceof TerminateRecord) {
              // termination record has value false in this case
              assert !((TerminateRecord) returnedData).terminate :
                  "Node $nodeID: Terminate Record has wrong value, false expected"
              terminated = true
              //println "IN-$nodeID received TerminateRecord"
            }
            else {
              // have got some new immigrants to be incorporated
              assert returnedData instanceof MigrantRecord :
                  "Node $nodeID:  Expecting a Migrant record"
              List <IslandIndividual> immigrants
              immigrants = ((MigrantRecord)returnedData).migratingIndividuals
//              println "Immigrants for Node $nodeID"
//              immigrants.each{println "$it"}
              pop.includeImmigrants(immigrants, migrantIndices)
              migrationCount = 0
              //println "IN-$nodeID received Immigrant Record"
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
      if (( ! terminated) && (! converged))
          // have definitely exceeded maxGenerations
          toCoordinator.write (new TerminateRecord(terminate: true, bestSolution: pop.bestSolution()))
          // TODO send the best solution to IslandCoordinator as part of the TerminateRecord
      //println "Node $nodeID instance $i terminated $converged, $terminated"
    }  // each instance
    //println "Node $nodeID terminated"
  }
}
