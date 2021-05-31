package mainland_model

import groovy_jcsp.ChannelInputList
import groovy_jcsp.ChannelOutputList
import island_model.Individual
import jcsp.lang.CSProcess
import jcsp.lang.ChannelInput
import jcsp.lang.ChannelOutput

class MainlandRoot implements CSProcess{

  // properties
  ChannelInput input
  ChannelOutput output
  ChannelOutputList toNodes
  ChannelInputList fromNodes
  int instances

  int partition(List <Individual> m, int start, int end){
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

  void quickSortRun(List <Individual> b, int start, int end){
//    println "QSR1: $start, $end"
    if (start < end) {
      int splitPoint = partition(b, start, end)
//      println "QSR2: $start, $end, $splitPoint"
      quickSortRun(b, start, splitPoint-1)
      quickSortRun(b, splitPoint+1, end)
    }
  }

  void quickSort( List <Individual> population, int finalIndex) {
    // always sorts into ascending order
    quickSortRun ( population, 0, finalIndex)
    // finalIndex vries depending on inclusion of offspring locations
  }

  void run() {
    int best, worst
    int lastIndex, totalIndex, nodes, populationPerNode
    String minOrMax
    for ( i in 0 ..< instances) {
      MainlandProblemSpecification specification = input.read() as MainlandProblemSpecification
      output.write(specification)
      // obtain values from specification
      nodes = specification.nodes
      populationPerNode = specification.populationPerNode
      Class populationClass = Class.forName(specification.populationClass)
      Class individualClass = Class.forName(specification.individualClass)
      minOrMax = specification.minOrMax
      MainlandPopulation populationData = populationClass.newInstance()

      // determine population indices
      lastIndex = (nodes * populationPerNode) -1
      totalIndex = lastIndex + (nodes*2)

      for (n in 0 ..< nodes) {
        toNodes[n].write(specification, populationData)
      }


    }

  }
}
