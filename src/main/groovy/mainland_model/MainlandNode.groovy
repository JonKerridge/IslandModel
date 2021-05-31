package mainland_model

import jcsp.lang.CSProcess
import jcsp.lang.ChannelInput
import jcsp.lang.ChannelOutput

class MainlandNode implements CSProcess{
  ChannelInput fromRoot
  ChannelOutput toRoot
  int nodeID
  int instances
  Random rng

  void run() {

  }
}
