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
      println "CS - $i: ${spec.toString()}"
    }
    println "CS: terminated"
  }

}
