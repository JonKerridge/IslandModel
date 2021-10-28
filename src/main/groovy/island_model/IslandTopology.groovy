package island_model

import groovy_jcsp.ChannelOutputList

/**
 * IslandTopology describes how each of the MigrantRecords containing migrants from
 * each of the IslandNodes is to be distributed back to the IslandNodes as immigrants.
 * Each tspIsland Node must send and receive the same number of migrants and immigrants.
 *
 * @distribute the method that implements the migration strategy
 */

interface IslandTopology {
  /**
   *The method that distributes migrants from one IslandNode to the other IslandNodes.
   *
   * @param forMigration a List of MigrantRecords each containing the migrants from
   * one of the nodes.  Each MigrantRecord should contain the same number of Individual records.
   * @param toNodes the communication channels from IslandCoordinator to each of the IslandNodes
   * to which the method will write the immigrants for each IslandNode as a MigrantRecord
   */
  void distribute( List <MigrantRecord> forMigration,
                   ChannelOutputList toNodes)
}