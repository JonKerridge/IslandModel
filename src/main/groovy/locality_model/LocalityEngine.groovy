package locality_model

import groovy_jcsp.ChannelInputList
import groovy_jcsp.ChannelOutputList
import groovy_jcsp.PAR
import jcsp.lang.Channel

class LocalityEngine {

  int nodes  // number of worker nodes
  int instances  //number of different problems instances to be created from the same specification
  PrintWriter printWriter  //output result written here
  LocalityProblemSpecification problemSpecification

  void run() {
//    println "MainlandEngine running: $nodes, $instances"
    assert instances > 0: "There must be at least one instance of the problem: specified $instances"
    assert instances == problemSpecification.instances:"Property instances and Specification value must match"
    def emitToRoot = Channel.one2one()
    def rootToCollect = Channel.one2one()
    def rootToNodes = Channel.one2oneArray(nodes)
    def nodesToRoot = Channel.one2oneArray(nodes)
    def root2Nodes = new ChannelOutputList(rootToNodes)
    def nodes2Root = new ChannelInputList(nodesToRoot)
    def emit = new LocalityEmitProblem(
        problemSpecification: problemSpecification,
        instances: instances,
        output: emitToRoot.out())
    def root = new LocalityRoot(
        instances: instances,
        input: emitToRoot.in(),
        output: rootToCollect.out(),
        toNodes: root2Nodes,
        fromNodes: nodes2Root)
    def collect = new LocalityCollectSolution(
        instances: instances,
        input: rootToCollect.in(),
        printWriter: printWriter)
    def nodeProcesses = (0 ..< nodes).collect() { i ->
      return new locality_model.LocalityNode(
          instances: instances,
          fromRoot: rootToNodes[i].in(),
          toRoot: nodesToRoot[i].out(),
          nodeID: i)
    }
    new PAR(nodeProcesses + [emit, root, collect] ).run()
//    println "MainlandEngine terminating"
  }

}
