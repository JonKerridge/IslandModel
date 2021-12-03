package tspIsland

import island_model.IslandEngine
import island_model.IslandProblemSpecification

String outFile = "./SingleTSPIsland42.csv"
def fw = new FileWriter(outFile, true)
def bw = new BufferedWriter(fw)
def printWriter = new PrintWriter(bw)

def tspSpecification = new IslandProblemSpecification()
tspSpecification.nodes = 8
tspSpecification.instances = 11
tspSpecification.dataFileName = "./dantzig42.tsp" //"./10cities.tsp" "./dantzig42.tsp"
tspSpecification.populationClass = TSPIslandPopulation.getName()
tspSpecification.geneLength = 43    //  11  43
tspSpecification.populationPerNode = 16
tspSpecification.migrationInterval = 16
tspSpecification.migrationSize = 4
tspSpecification.crossoverPoints = 2
tspSpecification.maxGenerations = 20000
tspSpecification.crossoverProbability = 1.0
tspSpecification.mutationProbability = 0.8
tspSpecification.convergenceLimit = 850.0
tspSpecification.minOrMax = "MIN"
tspSpecification.doSeedModify = true
tspSpecification.seeds = [3, 211, 419, 631, 839, 1039, 1249, 1451,
                          1657, 1861, 2063, 4073, 6079, 8081, 10091, 10301,
                          10487, 10687, 10883, 11083, 11273, 11471, 11689, 11867,
                          12043, 12241, 122412, 12583, 12763, 12959, 13147, 13331]
RingTopology topology = new RingTopology()

def islandEngine = new IslandEngine(
    problemSpecification: tspSpecification,
    topology: topology,
    instances: tspSpecification.instances,
    doSeedModify: tspSpecification.doSeedModify,
    nodes: tspSpecification.nodes,
    printWriter: printWriter)
islandEngine.invoke()



