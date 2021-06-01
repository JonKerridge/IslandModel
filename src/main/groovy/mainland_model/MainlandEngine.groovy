package mainland_model

import groovy_jcsp.ChannelInputList
import groovy_jcsp.ChannelOutputList
import groovy_jcsp.PAR
import jcsp.lang.Channel

class MainlandEngine {

  int nodes, instances
  boolean doSeedModify
  PrintWriter printWriter
  MainlandProblemSpecification problemSpecification

  void run() {
    def emitToRoot = Channel.one2one()
    def rootToCollect = Channel.one2one()
    def rootToNodes = Channel.one2oneArray(nodes)
    def nodesToRoot = Channel.one2oneArray(nodes)
    def root2Nodes = new ChannelOutputList(rootToNodes)
    def nodes2Root = new ChannelInputList(nodesToRoot)
    def emit = new MainlandEmitProblem(
        problemSpecification: problemSpecification,
        instances: instances,
        doSeedModify: doSeedModify,
        output: emitToRoot.out())
    def root = new MainlandRoot(
        input: emitToRoot.in(),
        instances: instances,
        output: rootToCollect.out(),
        toNodes: root2Nodes,
        fromNodes: nodes2Root)
    def collect = new MainlandCollectSolution(
        input: rootToCollect.in(),
        printWriter: printWriter,
        instances: instances)
    def nodeProcesses = (0 ..< nodes).collect() { i ->
      return new MainlandNode(fromRoot: rootToNodes[i].in(),
          toRoot: nodesToRoot[i].out(),
          nodeID: i,
          instances: instances )
    }
    new PAR(nodeProcesses + [emit, root, collect] ).run()
  }
}
