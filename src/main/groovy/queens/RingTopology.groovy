package queens

import groovy_jcsp.ChannelOutputList
import island_model.IslandTopology
import island_model.MigrantRecord

class RingTopology implements IslandTopology{

  @Override
  void distribute(List<MigrantRecord> forMigration, ChannelOutputList toNodes) {
    int nodes = toNodes.size()
    for ( n in 0 ..< nodes) {
      toNodes[(n + 1) % nodes].write(forMigration[n] as MigrantRecord)
//      println "Topology Transferring from $n to ${(n + 1) % nodes} : "
    }
  }
}
