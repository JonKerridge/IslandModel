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
    long totalTime
    int totalGenerations
    int totalRep
    int n
    List timeData
    List genData
    List repData
    totalTime = 0
    totalGenerations = 0
    totalRep = 0
    n = 0
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
      String outcome = result[0]
      MainlandIndividual bestOutcome = result[1]
      int generations = result[2]
      long endTime = System.currentTimeMillis()
      long elapsed = endTime - startTime
//      outString = "Collect $i, of ${instances-1}, " +
//          "\nSpecification: ${spec.toString()} " +
//          "\nresult: $outcome -> ${bestOutcome.toString()} in $generations generations, time = "
//      outString = outString + " ${endTime - startTime}"
      int replaceNumber
      if (bestOutcome == null)
        replaceNumber = 0
      else
        replaceNumber = bestOutcome.replacements
      outString = "$i, " +
          "${spec.toString()} " +
          "->, $replaceNumber, $generations, $elapsed"
      println "$outString"
      printWriter.println(outString)
      if ( i > 0 ){
        timeData << elapsed
        totalTime = totalTime + elapsed
        genData << generations
        totalGenerations = totalGenerations + generations
        if (bestOutcome != null) {
          repData << bestOutcome.replacements
          totalRep = totalRep + bestOutcome.replacements
        }
        n = n + 1
      }
    } // end of for loop
    // assume instances > 2
    // determine SD
    double timeAverage = (double)totalTime / (double)n
    double genAverage = (double)totalGenerations / (double)n
    double  repAverage = (double)totalRep / (double)n

    println " , ${spec.toString()} , " +
        "$timeAverage, ${sd(timeData, timeAverage, n)}, " +
        "$genAverage, ${sd(genData, genAverage, n)}, " +
        "$repAverage, ${sd(repData, repAverage, n)}"
    printWriter.println " , ${spec.toString()} , , , , ," +
        "$timeAverage, ${sd(timeData, timeAverage, n)}, " +
        "$genAverage, ${sd(genData, genAverage, n)}, " +
        "$repAverage, ${sd(repData, repAverage, n)}"
    printWriter.flush()
    printWriter.close()
//    println "Collect terminating"
  } // run

}
