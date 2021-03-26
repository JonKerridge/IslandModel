package island_model

import groovy_jcsp.ChannelInputList
import groovy_jcsp.ChannelOutputList
import jcsp.lang.CSProcess
import jcsp.lang.ChannelInput
import jcsp.lang.ChannelOutput

class IslandCoordinator implements CSProcess{

  ChannelInput input
  ChannelOutput output
  ChannelOutputList toNodes
  ChannelInputList fromNodes
  int nodes, instances

  void run(){
    println "IC: starting $instances, $nodes"
    for ( i in 0 ..< instances) {
      ProblemSpecification spec = input.read() as ProblemSpecification
      output.write(spec)  // only thing sent to collect process
      println "IC - $i : read and written the spec"
      for (n in 0..< nodes) {
        // need to send a distinct copy of the spec to each node
        ProblemSpecification nodeSpec = spec.copySpecification()
        toNodes[n].write(nodeSpec)
      }
      // at this point the code would iterate until convergence
      // for architecture testing the  node reads the spec and returns
      // a List containing the [nodeId, instance, seed] values
      // this is a parallel read
      List rc = fromNodes.read()
      println "IC - $i: read $rc"
//      for ( n in 0 ..< nodes) {
//        println "IC - $i: read ${rc[n]} from node $n"
//      }
    }
    println "IC: terminating"
  }

}
