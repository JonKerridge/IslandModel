package island_model

import jcsp.lang.CSProcess
import jcsp.lang.ChannelInput

class CollectSolution implements CSProcess{

  ChannelInput input
  int instances

  void run(){
    // initially the CollectSolution process reads the problem specification
    for ( i in 0 ..< instances) {
      ProblemSpecification spec = input.read() as ProblemSpecification
      long startTime = System.currentTimeMillis()
      println "CS - $i: ${spec.toString()}"
      // now read result
      List result = input.read()
      long endTime = System.currentTimeMillis()
      if (result == [])
        println " Collect: no solution found for instance $i in ${endTime - startTime} msecs"
      else {
        List <ConvergedRecord> solutions = result as List <ConvergedRecord>
        solutions.each{
          println "$it"
        }
        println "Elapsed Time = ${endTime - startTime}"
      }
    }
//    println "CS: terminated"
  }

}
