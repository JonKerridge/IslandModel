package tsp

import island_model.IslandEngine
import island_model.IslandProblemSpecification

String outFile = "./SingleTSPTest.csv"
File outputFile = new File(outFile)
if (outputFile.exists()) outputFile.delete()
def printWriter = outputFile.newPrintWriter()

def tspSpecification = new IslandProblemSpecification()
tspSpecification.nodes = 12
tspSpecification.instances = 10
tspSpecification.dataFileName = "./dantzig42.tsp"   // "./10cities.tsp"
tspSpecification.populationClass = TSPIslandPopulation.getName()
tspSpecification.geneLength = 43 // 11
tspSpecification.populationPerNode = 16
tspSpecification.migrationInterval = 16
tspSpecification.migrationSize = 4
tspSpecification.crossoverPoints = 2
tspSpecification.maxGenerations = 20000
tspSpecification.crossoverProbability = 1.0
tspSpecification.mutationProbability = 0.8
tspSpecification.convergenceLimit = 800.0
tspSpecification.minOrMax = "MIN"
tspSpecification.doSeedModify = true
tspSpecification.seeds = [3, 211, 419, 631, 839, 1039, 1249, 1451,
                             1657, 1861, 2063, 4073, 6079, 8081, 10091, 10301]
RingTopology topology = new RingTopology()

def islandEngine = new IslandEngine(
    problemSpecification: tspSpecification,
    topology: topology,
    instances: tspSpecification.instances,
    doSeedModify: tspSpecification.doSeedModify,
    nodes: tspSpecification.nodes,
    printWriter: printWriter)
islandEngine.invoke()



