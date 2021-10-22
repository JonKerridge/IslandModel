package mainland_model

import island_model.Individual
import jcsp.lang.CSProcess
import jcsp.lang.ChannelInput
import jcsp.lang.ChannelOutput
import maxOneMainland.MaxOneIndividual

class MainlandNode implements CSProcess{
  ChannelInput fromRoot
  ChannelOutput toRoot
  int nodeID, instances


  void run() {
//    println "Running Node $nodeID"
    int ppn, nodes, maxIndex, instance
    // parents will be used in crossover
    // children will be produced from the  crossover operation
    // candidates are those individuals that might be overwritten if a child is a better fit
    int parent1, parent2, child1, child2, candidate1, candidate2
    List <Integer> otherIndividuals = []
    boolean minimise
    Random rng
    for ( i in 0 ..< instances){
      List inputList = fromRoot.read()
//      println "Node $nodeID: read specification for instance $i"
      MainlandProblemSpecification problem = inputList[0]
      MainlandPopulation populationAtNode = inputList[1]
      List evaluateData = inputList[2]
      assert i == problem.instance:"instance being processed and loop control do not match"
      if ( i == 0){
        // the indices into population remain unaltered which each instance so do it once
        ppn = problem.populationPerNode
        nodes = problem.nodes
        minimise = (problem.minOrMax == "MIN")
        int extras = ppn - 4
        if (minimise){
          maxIndex = (nodes * ppn) - 1
          child1 = (maxIndex+1) + (nodeID*2)
          child2 = child1 + 1
          parent1 = nodeID * 2
          parent2 = parent1 + 1
          candidate1 = maxIndex - nodeID
          candidate2 = candidate1 - nodes
          if (extras > 0){
            for (e in 0 ..< extras)
              otherIndividuals << ((nodes * 2) + (e * nodes) + nodeID)
          }
        }
        else {
          // its a maximise problem
          maxIndex = (nodes * ppn) + (nodes * 2) -1
          child1 = nodeID*2
          child2 = child1 + 1
          parent1 = maxIndex - (nodeID*2)
          parent2 = parent1-1
          candidate1 = nodeID + (nodes * 2)
          candidate2 = candidate1 + nodes
          if (extras > 0){
            for (e in 0 ..< extras)
              otherIndividuals << ((nodes * ppn) - (e * nodes) - nodeID -1)
          }
        } // end  minimise if
//        println "MN $nodeID: $instances, $i, $ppn, $minimise, ${problem.minOrMax}, $parent1, $parent2, $candidate1, $candidate2, $child1, $child2, $otherIndividuals"
      }// end test for first iteration
      // initialise data for this specification instance
      rng = new Random(problem.seeds[nodeID])
      //println "Node $nodeID: using seed value ${problem.seeds[nodeID]} for instance $i = ${rng.nextInt(maxIndex)}"
      // now initialise the individuals this node can access
      populationAtNode.population[parent1].initialise(rng)
      populationAtNode.population[parent2].initialise(rng)
      populationAtNode.population[child1].initialise(rng)
      populationAtNode.population[child2].initialise(rng)
      populationAtNode.population[candidate1].initialise(rng)
      populationAtNode.population[candidate2].initialise(rng)
      // now initialise any intermediate individuals not otherwise initialised ie ppn > 4
      otherIndividuals.each{populationAtNode.population[it].initialise(rng)}
      //  tell root initialisation is complete
      toRoot.write(1)
      // now undertake processing of this problem specification
      boolean running = true
      while (running){
        String action = fromRoot.read() as String
        switch (action){
          case "REPLACE":
            // overwrite child1 and child2
            //  tell root replacement is complete
            toRoot.write(1)
            break
          case "REPRODUCE":
            // reproduce parent1 and parent 2 creating child and child2
            populationAtNode.reproduce(parent1, parent2,
              child1, child2, candidate1, candidate2, rng)
            //  tell root reproduction is complete
            toRoot.write(1)
            break
          case "FINISH":
            // terminate the processing of this instance
            running = false
        } // end switch
      } // running loop
    }// end of for loop

//    println "Node $nodeID terminating"
  }
}
