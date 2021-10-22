package mainland_model

import island_model.Individual
import jcsp.lang.CSProcess
import jcsp.lang.ChannelInput

class MainlandCollectSolution implements CSProcess{

  ChannelInput input
  PrintWriter printWriter
  int instances


  void run(){
//    println "Collect process running $instances"
    for ( i in 0 ..< instances){
      //  read the problem specification for instance i
//      println "Collect: reading specification $i"
      MainlandProblemSpecification spec = input.read() as MainlandProblemSpecification
      assert i == spec.instance:"instance being processed and loop control do not match"
      String outString
      long startTime = System.currentTimeMillis()
      // now read result
      List result = input.read()
      String outcome = result[0]
      Individual bestOutcome = result[1]
      int generations = result[2]
      long endTime = System.currentTimeMillis()
//      outString = "Collect $i, of ${instances-1}, " +
//          "\nSpecification: ${spec.toString()} " +
//          "\nresult: $outcome -> ${bestOutcome.toString()} in $generations generations, time = "
//      outString = outString + " ${endTime - startTime}"
      outString = "$i, ${instances-1}, $outcome, " +
          "${spec.toString()}, " +
          " ->, ${bestOutcome.toString()}, $generations, ${endTime - startTime}"
      println "$outString"
      printWriter.println(outString)
    } // end of for loop
    printWriter.flush()
    printWriter.close()
//    println "Collect terminating"
  } // run

}
