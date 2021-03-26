package island_model

import jcsp.lang.CSProcess
import jcsp.lang.ChannelInput
import jcsp.lang.ChannelOutput

/**
 *
 */
class IslandNode implements CSProcess{

  ChannelInput fromCoordinator
  ChannelOutput toCoordinator
  int nodeID
  int instances

  void run(){
    println "Node $nodeID invoked"
    for ( i in 0 ..< instances){
      ProblemSpecification spec = fromCoordinator.read() as ProblemSpecification
      // for testing purposes nodes responds with
      // [ nodeId, spec.instance, seed for this node]
      int instance = spec.instance
      double seed = spec.seeds[nodeID]
      toCoordinator.write([nodeID, instance, seed])
    }
    println "Node $nodeID terminated"
  }
}
