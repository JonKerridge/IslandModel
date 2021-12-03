package mainland_model

import groovy_jcsp.ChannelInputList
import groovy_jcsp.ChannelOutputList
import island_model.IslandIndividual
import jcsp.lang.CSProcess
import jcsp.lang.ChannelInput
import jcsp.lang.ChannelOutput

class MainlandRoot implements CSProcess{

  // properties
  ChannelInput input
  ChannelOutput output
  ChannelOutputList toNodes
  ChannelInputList fromNodes
  int instances //number of different problems instances to be created from the same specification

  int partition(List <IslandIndividual> m, int start, int end){
    BigDecimal pivotValue
    pivotValue = m[start].getFitness()
    // getFitness returns a BigDecimal
//    println "P1: $start, $end, $pivotValue"
    int left, right
    left = start+1
    right = end
    boolean done
    done = false
    while (!done){
//      println "P2: $left, $right $pivotValue"
      while ((left <= right) && (m[left].getFitness() < pivotValue)) left = left + 1
//      println "P3: $left, $right, $pivotValue, ${m[left].getProperty(fitPropertyName)}"
      while ((m[right].getFitness() >= pivotValue) && (right >= left)) right = right - 1
//      println "P4: $left, $right, $pivotValue, $pivotValue, ${m[right].getProperty(fitPropertyName)}"
      if (right < left)
        done = true
      else {
        m.swap(left, right)
//        println "swap $left with $right for $pivotValue"
      }
    }
    m.swap(start, right)
    return right
  }

  void quickSortRun(List <IslandIndividual> b, int start, int end){
//    println "QSR1: $start, $end"
    if (start < end) {
      int splitPoint = partition(b, start, end)
//      println "QSR2: $start, $end, $splitPoint"
      quickSortRun(b, start, splitPoint-1)
      quickSortRun(b, splitPoint+1, end)
    }
  }

  void quickSort(List <IslandIndividual> population, int finalIndex) {
    // always sorts into ascending order
    quickSortRun ( population, 0, finalIndex)
    // finalIndex vries depending on inclusion of offspring locations
  }

  void run() {
//    println "Root process starting $instances"
    int bestFitIndex, lastIndex, totalIndex, nodes, populationPerNode
    String minOrMax
    List evaluateData
    def convergenceLimit
    // now process each instance of the problem
    for ( i in 0 ..< instances){
      MainlandProblemSpecification specification = input.read() as MainlandProblemSpecification
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
        if (minOrMax  =="MIN")
          bestFitIndex = 0
        else
          bestFitIndex = totalIndex
//        println "Root: fixed values $nodes, $populationPerNode, $minOrMax, $lastIndex, $totalIndex, $bestFitIndex"
      } // end of initialisation
      // now create an empty population
      Class populationClass = Class.forName(specification.populationClass)
      Class individualClass = Class.forName(specification.individualClass)
      MainlandPopulation populationData = populationClass.newInstance(
          totalIndex + 1 ,  // the number of individuals that are created
          specification.geneLength,
          specification.crossoverPoints,
          specification.maxGenerations,
          specification.replaceInterval,
          specification.crossoverProbability,
          specification.mutationProbability,
          specification.dataFileName,
          bestFitIndex
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
      // can now sort the initial population
      quickSort(populationData.population, totalIndex)
//      println "Root: nodes initialised for instance $i"
//      int indiv = 0
//      populationData.population.each {println " $indiv: $it"
//        indiv = indiv + 1
//      }
//      println "End of population for instance $i"
      // now interact with nodes until convergence
      int generationCount, replaceCount, replacements
      def currentFitness
      MainlandIndividual result
      generationCount = 0
      replaceCount = 0
      replacements = 0
      currentFitness = populationData.population[bestFitIndex].getFitness()
      while (( (result = populationData.convergence(convergenceLimit)) == null ) &&
          (generationCount < specification.maxGenerations)){
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
          quickSort(populationData.population, totalIndex)  // sort the population
          replaceCount = 0
          replacements = replacements + 1
        } //end replacement counting
        generationCount = generationCount + 1
        for (n in 0 ..< nodes) {
          toNodes[n].write("REPRODUCE")
        }
        for (n in 0 ..< nodes) {
          fromNodes[n].read() // a signal that reproduction is complete
        }
        quickSort(populationData.population, lastIndex)  // sort the population lastIndex ?
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
