package island_model

import jcsp.lang.CSProcess
import jcsp.lang.ChannelInput

class IslandCollectSolution implements CSProcess{

  ChannelInput input
  int instances
  PrintWriter printWriter

  double sd( List dataValues, double mean, int n){
    double sum = 0.0
    dataValues.each { v ->
      sum = sum + (( v - mean) * ( v - mean))
    }
    sum = sum / n
    return Math.sqrt(sum)
  }

  void run(){
    String outString
    IslandProblemSpecification spec
    long totalTime
    int totalGenerations
    int n, found, none
    List timeData
    List genData
    totalTime = 0
    totalGenerations = 0
    int minGenerations
    n = 0
    found = 0
    none = 0
    timeData = []
    genData = []
    for ( i in 0 ..< instances) {
      // initially the CollectSolution process reads the problem specification
      spec = input.read() as IslandProblemSpecification
      long startTime = System.currentTimeMillis()
      // now read result
      Object result = input.read()
      long endTime = System.currentTimeMillis()
      long elapsed = endTime - startTime
      outString = "$i, ${spec.toString()} "
      //TODO need to select the 'best' solution the one with the best Fitness value
      // but this varies with maximise or minimise problem
      if (result instanceof TerminationCollection) {
        // no solution found
        none = none + 1
        // each node has sent its best solution; so need to determine the overall best
        // for this we need to know it it is a minimise of maximise problem
//        println "ICS: ${((TerminationCollection)result).bestRecords.size()} termination records read"
        BigDecimal bestFit
        int bestLocation
        minGenerations = spec.maxGenerations
        bestLocation = 0
        bestFit = ((IslandIndividual)((TerminationCollection)result).bestRecords[bestLocation]).getFitness()
        boolean better
        for ( r in 1 ..< ((TerminationCollection)result).bestRecords.size()) {
          better = spec.minOrMax == "MIN" ?
              ((TerminationCollection)result).bestRecords[r].getFitness() < bestFit
              : ((TerminationCollection)result).bestRecords[r].getFitness() > bestFit
          if (better) {
            bestFit = ((TerminationCollection)result).bestRecords[r].getFitness()
            bestLocation = r
          }
        }

        outString = outString + " NONE, " +
            "${((IslandIndividual)((TerminationCollection)result).bestRecords[bestLocation]).toString()} ," +
            "${spec.maxGenerations}, " +
            " $elapsed"
      }
      else {
        // solution found but
        found = found + 1
        // more than one node might find a solution; we want minimum generations
        minGenerations = spec.maxGenerations + 1
        int minSolution
        List <ConvergedRecord> solutions = result as List <ConvergedRecord>
        for ( s in 0 ..< solutions.size())
          if (solutions[s].generationsTaken < minGenerations){
            minGenerations = solutions[s].generationsTaken
            minSolution = s
          }
        outString = outString +  " FOUND, ${solutions.size()}, " +
            "${solutions[minSolution].convergedIndividual.toString()}, " +
            "${solutions[minSolution].generationsTaken}, " +
            "$elapsed, ${solutions[minSolution].seedValue}"
      } // end else
      println "$outString"
//      printWriter.println(outString)
      if ( i > 0 ){
        totalTime = totalTime + elapsed
        timeData << elapsed
        genData << minGenerations
        totalGenerations = totalGenerations + minGenerations
      }
    } // end for loop for  instances
//    println "CS: terminated"
    n = instances - 1
    double timeAverage = (double)totalTime / (double)n
    double genAverage = (double)totalGenerations / (double)n
    println "${spec.toString()}, " +
        "$timeAverage, ${sd(timeData, timeAverage, n)}, " +
        "$genAverage, ${sd(genData, genAverage, n)}, $found, $none"
    printWriter.println "${spec.toString()}, " +
        "$timeAverage, ${sd(timeData, timeAverage, n)}, " +
        "$genAverage, ${sd(genData, genAverage, n)},  $found, $none"

    printWriter.flush()
    printWriter.close()
  } // run

}
