package locality_model

import groovy_jcsp.ChannelInputList
import groovy_jcsp.ChannelOutputList
import jcsp.lang.CSProcess
import jcsp.lang.ChannelInput
import jcsp.lang.ChannelOutput

class LocalityRoot implements CSProcess{

  // properties
  ChannelInput input
  ChannelOutput output
  ChannelOutputList toNodes
  ChannelInputList fromNodes
  int instances //number of different problems instances to be created from the same specification

  void run() {
//    println "Root process starting $instances"
    int bestFitIndex, lastIndex, totalIndex, nodes, populationPerNode
    String minOrMax
    List evaluateData
    def convergenceLimit
    // now process each instance of the problem
    for ( i in 0 ..< instances){
      LocalityProblemSpecification specification = input.read() as LocalityProblemSpecification
//      println "Root has read specification $i"
      output.write(specification)   // to the Mainland Collect Solution process every instance
      if (i == 0){
//        println "Root processing specification 0"
        // obtain non-changing values from specification
        convergenceLimit = specification.convergenceLimit
        nodes = specification.nodes
        populationPerNode = specification.populationPerNode
        assert populationPerNode >= 4:"Population Per Node must be at least 4; $populationPerNode specified"
        minOrMax = specification.minOrMax
        // determine population indices
        lastIndex = (nodes * populationPerNode) -1
        totalIndex = lastIndex + (nodes*2)
        bestFitIndex = 0
//        println "Root: fixed values $nodes, $populationPerNode, $minOrMax, $lastIndex, $totalIndex"
      } // end of initialisation
      // now create an empty population
      Class populationClass = Class.forName(specification.populationClass)
      Class individualClass = Class.forName(specification.individualClass)
      LocalityPopulation populationData = populationClass.newInstance(
          totalIndex + 1 ,  // the number of individuals that are created
          specification.geneLength,
          specification.crossoverPoints,
          specification.maxGenerations,
          specification.replaceInterval,
          specification.crossoverProbability,
          specification.mutationProbability,
          specification.dataFileName
      )
      // read in the data file to evaluateData iff first iteration
      if (i == 0) evaluateData = populationData.processDataFile(specification.dataFileName) // extract evaluationData from file
      //create the empty individual entries in populationData
      for (p in 0 ..< totalIndex)
        populationData.population[p] = individualClass.newInstance(specification.geneLength)
//      println "Root: writing to nodes to initialise instance $i"
      for (n in 0 ..< nodes) {
        toNodes[n].write([specification, populationData, evaluateData])
      }
      for (n in 0 ..< nodes) {
        fromNodes[n].read() // a signal that initialisation is complete
      }
      // now interact with nodes until convergence
      int generationCount, replaceCount, replacements
      def currentFitness
      List outcomes
      boolean converged
      LocalityIndividual result
      def determineConvergence = {List fitValues ->
        // process the returned outcomes from the nodes [[index, fitness], ...]
        // has to take into account the value of minimise
        // will update converged, bestFitIndex and result
        int bestSoFarLocation = fitValues[0][0]
        def bestSoFarFitness = fitValues[0][1]
        if (minOrMax  =="MIN"){ // minimise problem
          for ( v in 1 ..< fitValues.size()){
            if (fitValues[v][1] < bestSoFarFitness){
              bestSoFarFitness = fitValues[v][1]
              bestSoFarLocation = fitValues[v][0]
            }
          } // end for
          if (bestSoFarFitness <= convergenceLimit) {
            converged = true
            result = populationData.population[bestSoFarLocation]
          }
          else {
            bestFitIndex = bestSoFarLocation
          }
        }
        else { // maximise problem
          for ( v in 1 ..< fitValues.size()){
            if (fitValues[v][1] > bestSoFarFitness){
              bestSoFarFitness = fitValues[v][1]
              bestSoFarLocation = fitValues[v][0]
            }
          } // end for
          if (bestSoFarFitness >= convergenceLimit) {
            converged = true
            result = populationData.population[bestSoFarLocation]
          }
          else {
            bestFitIndex = bestSoFarLocation
          }
        }
      } // end determineConvergence
      generationCount = 0
      replaceCount = 0
      replacements = 0
      converged = false
      currentFitness == populationData.population[1].getFitness() // choose one individual as best fitness so far
//      currentFitness = populationData.population[bestFitIndex].getFitness()
      //TODO modify Root to include a convergence method based on minimise and convergence limit
      // assume that the loop will be carried out at least once
      while (( !converged ) && (generationCount < specification.maxGenerations)){
        if (currentFitness == populationData.population[bestFitIndex].getFitness())
          replaceCount = replaceCount + 1
        else {
          replaceCount = 0
          currentFitness = populationData.population[bestFitIndex].getFitness()
        }
        if (replaceCount == specification.replaceInterval){
          for (n in 0 ..< nodes) {
            toNodes[n].write("REPLACE")
          }
          for (n in 0 ..< nodes) {
            fromNodes[n].read() // a signal that replacement is complete
          }
          replaceCount = 0
          replacements = replacements + 1
        } //end replacement counting
        generationCount = generationCount + 1
        for (n in 0 ..< nodes) {
          toNodes[n].write("REPRODUCE")
        }
        //TODO modify so root reads the best fit individual from each node
        outcomes = []
        for (n in 0 ..< nodes) {
          // each node send a List comprising [population subscript, fitness value]
          outcomes << fromNodes[n].read() // a signal that reproduction is complete
        }
        determineConvergence(outcomes)
//        println "generation finished $generationCount"
      } //end while loop
      // write outcome to CollectSolution
      long seedValue = specification.seeds[0]
      output.write([seedValue, result, generationCount, replacements])
      for (n in 0 ..< nodes) {
        toNodes[n].write("FINISH")
      }
    }// for loop
//    println "Root process terminating"
  } // end run

}
