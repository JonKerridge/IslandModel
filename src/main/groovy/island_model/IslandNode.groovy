package island_model

import jcsp.lang.CSProcess
import jcsp.lang.ChannelInput
import jcsp.lang.ChannelOutput
import maxOnes.MaxOneIndividual

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
    println "Node $nodeID invoked"
    for ( i in 0 ..< instances){
      ProblemSpecification spec = fromCoordinator.read() as ProblemSpecification
      String dataFileName = spec.dataFileName
      int instance = spec.instance
      int migrationInterval = spec.migrationInterval
      int crossoverPoints = spec.crossoverPoints
      int maxGenerations = spec.maxGenerations
      double crossoverProbability = spec.crossoverProbability
      double mutationProbability = spec.mutationProbability
      rng = new Random(spec.seeds[nodeID])
      //create a population and its individuals
      Class popClass = Class.forName(spec.populationClass)
      Population pop = popClass.newInstance(spec.populationPerNode,
          spec.crossoverProbability, spec.mutationProbability,
          spec.geneLength, rng )
      println "Initially - Node: $nodeID, instance: $i population"
      pop.population.each{println "$it"}
      int generation = 0
      int migrationCount = 0
      boolean converged = false
      while ((generation < maxGenerations)  && (! converged)){
        if (migrationCount == migrationInterval){
          // undertake migration between nodes
          List <Integer> migrantIndices = pop.selectMigrants()
          toCoordinator.write(pop.getMigrants(migrantIndices))
          List <MaxOneIndividual> immigrants
          immigrants = fromCoordinator.read()
          pop.includeImmigrants(immigrants, migrantIndices)
          migrationCount = 0
        }
        // undertake generation processing
        if (rng.nextDouble() < crossoverProbability) {
          pop.reproduce(crossoverPoints)
        }
        Individual foundIndividual = pop.convergence()
        if ( foundIndividual == null){
          // not yet converged
          generation = generation + 1
          migrationCount = migrationCount + 1
        }
        else
          converged = true
      } // generation loop
      // determine what to send to IslandCoordinator
    }  // each instance
    println "Node $nodeID terminated"
  }
}
