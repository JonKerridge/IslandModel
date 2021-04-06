package island_model

import groovy_jcsp.ChannelInputList
import groovy_jcsp.ChannelOutputList
import groovy_jcsp.PAR
import jcsp.lang.Channel

class IslandEngine  {

  ProblemSpecification problemSpecification
  int instances
  int nodes
  IslandTopology topology

  void  invoke() {
    def emitToIC = Channel.one2one()
    def icToCollect = Channel.one2one()
    def icToNodes = Channel.one2oneArray(nodes)
    def nodesToIC = Channel.one2oneArray(nodes)
    def ic2Nodes = new ChannelOutputList(icToNodes)
    def nodes2IC = new ChannelInputList((nodesToIC))

    def emit = new EmitProblem( problemSpecification: problemSpecification,
        instances: instances,
        output: emitToIC.out())
    def coordinator = new IslandCoordinator (input: emitToIC.in(),
        instances: instances,
        output: icToCollect.out(),
        toNodes: ic2Nodes,
        topology: topology,
        nodes: nodes,
        fromNodes: nodes2IC)
    def collect = new CollectSolution(input: icToCollect.in(),
        instances: instances)

    def nodeProcesses = (0 ..< nodes).collect() { i ->
      return new IslandNode(fromCoordinator: icToNodes[i].in(),
          toCoordinator: nodesToIC[i].out(),
          nodeID: i,
          instances: instances )
    }
    new PAR(nodeProcesses + [emit, coordinator, collect] ).run()
  }
}
