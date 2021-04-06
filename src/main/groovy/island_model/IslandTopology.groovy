package island_model

import groovy_jcsp.ChannelOutputList

interface IslandTopology {
  void distribute( List <MigrantRecord> forMigration,
                   ChannelOutputList toNodes)
}