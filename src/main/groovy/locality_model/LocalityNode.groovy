package locality_model

import jcsp.lang.CSProcess
import jcsp.lang.ChannelInput
import jcsp.lang.ChannelOutput

class LocalityNode implements CSProcess {

  ChannelInput fromRoot
  ChannelOutput toRoot
  int nodeID, instances

  void run() {
//    println "Running Node $nodeID"
    int ppn, nodes, maxIndex, baseIndex
    // parents will be used in crossover
    // children will be produced from the  crossover operation
    // overwrites are those individuals that might be overwritten if a child is a better fit
    int parent1, parent2, child1, child2, overwrite1, overwrite2
    List<Integer> otherIndividuals = []
    boolean minimise
    Random rng
    for (i in 0..<instances) {
      List inputList = fromRoot.read()
//      println "Node $nodeID: read specification for instance $i"
      LocalityProblemSpecification problem = inputList[0]
      LocalityPopulation populationAtNode = inputList[1]
      List evaluateData = inputList[2]
      assert i == problem.instance: "instance being processed and loop control do not match"
      //TODO closures to find the best outcome after reproduction
      // The result is a list comprising individual subscript and fitness value
      def compareMinOutcome = { int p1, int p2, int o1, int o2 ->
        int bestLocation = p1
        def bestFit = populationAtNode.population[p1].fitness // type can be anything
        if (populationAtNode.population[p2].fitness < bestFit) {
          bestFit = populationAtNode.population[p2].fitness
          bestLocation = p2
        }
        if (populationAtNode.population[o1].fitness < bestFit) {
          bestFit = populationAtNode.population[o1].fitness
          bestLocation = o1
        }
        if (populationAtNode.population[o2].fitness < bestFit) {
          bestFit = populationAtNode.population[o2].fitness
          bestLocation = o2
        }
        return [bestLocation, bestFit]
      }

      def compareMaxOutcome = { int p1, int p2, int o1, int o2 ->
        int bestLocation = p1
        def bestFit = populationAtNode.population[p1].fitness // type can be anything
        if (populationAtNode.population[p2].fitness > bestFit) {
          bestFit = populationAtNode.population[p2].fitness
          bestLocation = p2
        }
        if (populationAtNode.population[o1].fitness > bestFit) {
          bestFit = populationAtNode.population[o1].fitness
          bestLocation = o1
        }
        if (populationAtNode.population[o2].fitness > bestFit) {
          bestFit = populationAtNode.population[o2].fitness
          bestLocation = o2
        }
        return [bestLocation, bestFit]
      }

      if (i == 0) {
        // the indices into population remain unaltered which each instance so do it once
        ppn = problem.populationPerNode
        nodes = problem.nodes
        minimise = (problem.minOrMax == "MIN")
        int extras = ppn - 4
        baseIndex = 0
        maxIndex = (nodes * ppn) - 1
        child1 = (maxIndex + 1) + (nodeID * 2)
        child2 = child1 + 1
        parent1 = nodeID * 2
        parent2 = parent1 + 1
        overwrite1 = maxIndex - nodeID
        overwrite2 = overwrite1 - nodes
        if (extras > 0) {
          for (e in 0..<extras) otherIndividuals << ((nodes * 2) + (e * nodes) + nodeID)
        }
        println "MN $nodeID: $instances, $i, $ppn, $minimise, p1-$parent1, p2-$parent2, o1-$overwrite1, o2-$overwrite2, c1-$child1, c2-$child2, $otherIndividuals"
      }// end test for first iteration
      // initialise data for this specification instance
      rng = new Random(problem.seeds[nodeID])
//      println "Node $nodeID: using seed value ${problem.seeds[nodeID]} for instance $i "
      // now initialise the individuals this node can access
      populationAtNode.population[parent1].initialise(rng)
      populationAtNode.population[parent2].initialise(rng)
      populationAtNode.population[child1].initialise(rng)
      populationAtNode.population[child2].initialise(rng)
      populationAtNode.population[overwrite1].initialise(rng)
      populationAtNode.population[overwrite2].initialise(rng)
      populationAtNode.population[parent1].evaluateFitness(evaluateData)
      populationAtNode.population[parent2].evaluateFitness(evaluateData)
      populationAtNode.population[child1].evaluateFitness(evaluateData)
      populationAtNode.population[child2].evaluateFitness(evaluateData)
      populationAtNode.population[overwrite1].evaluateFitness(evaluateData)
      populationAtNode.population[overwrite2].evaluateFitness(evaluateData)

      // now initialise any intermediate individuals not otherwise initialised ie ppn > 4
      otherIndividuals.each {
        populationAtNode.population[it].initialise(rng)
        populationAtNode.population[it].evaluateFitness(evaluateData)
      }
      //  tell root initialisation is complete
      toRoot.write(1)
      // now undertake processing of this problem specification
      boolean running = true
      while (running) {
        String action = fromRoot.read() as String
        switch (action) {
          case "REPLACE":
            Map locationFitness = [:]
            for (index in baseIndex..maxIndex) {
              locationFitness.put(index, populationAtNode.population[index].getFitness())
            }
//            println locationFitness
            //TODO modify to deal with min or max-imise variants
            Map sortedMap
            if (minimise) sortedMap = locationFitness.sort { a, b -> a.value <=> b.value
            } else sortedMap = locationFitness.sort { a, b -> b.value <=> a.value
            }

//            println sortedMap
            Set locationSet = sortedMap.keySet()
            // overwrite child1 and child2
            populationAtNode.population[child1].initialise(rng)
            populationAtNode.population[child2].initialise(rng)
            populationAtNode.population[child1].evaluateFitness(evaluateData)
            populationAtNode.population[child2].evaluateFitness(evaluateData)
            populationAtNode.replaceCandidates(child1, child2,
                locationSet[overwrite1], locationSet[overwrite2])
            //  tell root replacement is complete
            toRoot.write(1)
            break
          case "REPRODUCE":
            Map locationFitness = [:]
            for (index in baseIndex..maxIndex) {
              locationFitness.put(index, populationAtNode.population[index].getFitness())
            }
//            println "Node $nodeID: map = $locationFitness"
            //TODO modify to deal with min or max-imise variants
            Map sortedMap
            if (minimise) sortedMap = locationFitness.sort { a, b -> a.value <=> b.value
            } else sortedMap = locationFitness.sort { a, b -> b.value <=> a.value
            }

//            println "Node $nodeID: map = $sortedMap"
            Set locationSet = sortedMap.keySet()
//            println "Node $nodeID: p1-$parent1, p2-$parent2, c1-$overwrite1, c2-$overwrite2, o1-$child1, o2-$child2, " +
//                "${locationSet[parent1]}, ${locationSet[parent2]}, $child1, $child2 "
            // reproduce parent1 and parent 2 creating child1 and child2
            populationAtNode.reproduce(locationSet[parent1], locationSet[parent2],
                child1, child2, rng)
            populationAtNode.population[child1].evaluateFitness(evaluateData)
            populationAtNode.population[child2].evaluateFitness(evaluateData)
            populationAtNode.replaceCandidates(child1, child2,
                locationSet[overwrite1], locationSet[overwrite2])
            //  tell root reproduction is complete
            //TODO send the location of the best fit at this node parent1, parent2, overwrite1, overwrite2
            // has to take account of minimise value
            // return value id [individual subscript, fitness]
            if (minimise)
              toRoot.write(compareMinOutcome(locationSet[parent1], locationSet[parent1],
                locationSet[overwrite1], locationSet[overwrite2]))
            else
              toRoot.write(compareMaxOutcome(locationSet[parent1], locationSet[parent1],
                locationSet[overwrite1], locationSet[overwrite2]))
            break
          case "FINISH":
            // terminate the processing of this instance
            running = false
        } // end switch
      } // running while loop
    }// end of instance for loop

//    println "Node $nodeID terminating"
  } // run
}
