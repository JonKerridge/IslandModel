package mainland_model

import island_model.IslandIndividual
import jcsp.lang.CSProcess
import jcsp.lang.ChannelInput

class MainlandCollectSolution implements CSProcess{

  ChannelInput input
  PrintWriter printWriter
  int instances

  double sd( List dataValues, double mean, int n){
    double sum = 0.0
    dataValues.each { v ->
      sum = sum + (( v - mean) * ( v - mean))
    }
    sum = sum / n
    return Math.sqrt(sum)
  }


  void run(){
//    println "Collect process running $instances"
    MainlandProblemSpecification spec
    long totalTime, seedValue
    int totalGenerations
    int totalRep
    int n
    int found, none
    List timeData
    List genData
    List repData
    totalTime = 0
    totalGenerations = 0
    totalRep = 0
    n = 0
    found = 0
    none = 0
    timeData = []
    genData = []
    repData = []
    for ( i in 0 ..< instances){
      //  read the problem specification for instance i
//      println "Collect: reading specification $i"
      spec = input.read() as MainlandProblemSpecification
      assert i == spec.instance:"instance being processed and loop control do not match"
      String outString
      long startTime = System.currentTimeMillis()
      // now read result
      List result = input.read()
      seedValue = result[0]
      MainlandIndividual bestOutcome = result[1]
      int generations = result[2]
      int replacements = result[3]
      long endTime = System.currentTimeMillis()
      long elapsed = endTime - startTime
//      outString = "Collect $i, of ${instances-1}, " +
//          "\nSpecification: ${spec.toString()} " +
//          "\nresult: $outcome -> ${bestOutcome.toString()} in $generations generations, time = "
//      outString = outString + " ${endTime - startTime}"
      if ( i > 0 ){
        timeData << elapsed
        totalTime = totalTime + elapsed
        genData << generations
        totalGenerations = totalGenerations + generations
        repData << replacements
        totalRep = totalRep + replacements
      } // end if
      if (bestOutcome == null) {
        none = none + 1
        outString = "$i, " +
            "${spec.toString()}, " +
            "NONE, $replacements, $generations, $elapsed, $seedValue "
      }
      else {
       found = found + 1
        outString = "$i, " +
            "${spec.toString()}, " +
            "FOUND, $replacements, $generations, $elapsed, ${bestOutcome.toString()}, $seedValue "
      } // end if for bestOutcome test
      println "$outString"
//      printWriter.println(outString) do not print out the intermediate values


    } // end of for loop
    // assume instances > 2
    // determine SD
    n = instances -1
    double timeAverage = (double)totalTime / (double)n
    double genAverage = (double)totalGenerations / (double)n
    double  repAverage = (double)totalRep / (double)n

    println "${spec.toString()}, " +
        "$timeAverage, ${sd(timeData, timeAverage, n)}, " +
        "$genAverage, ${sd(genData, genAverage, n)}, " +
        "$repAverage, ${sd(repData, repAverage, n)}, $found, $none"
    printWriter.println "${spec.toString()}," +
        "$timeAverage, ${sd(timeData, timeAverage, n)}, " +
        "$genAverage, ${sd(genData, genAverage, n)}, " +
        "$repAverage, ${sd(repData, repAverage, n)}, $found, $none"
    printWriter.flush()
    printWriter.close()
//    println "Collect terminating"
  } // run

}
