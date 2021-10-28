package queensIsland

import island_model.IslandEngine
import island_model.IslandProblemSpecification

String outFile = "./SingleTest.csv"
File outputFile = new File(outFile)
if (outputFile.exists()) outputFile.delete()
def printWriter = outputFile.newPrintWriter()

def queensSpecification = new IslandProblemSpecification()
queensSpecification.nodes = 4
queensSpecification.instances = 10
queensSpecification.dataFileName = null
queensSpecification.populationClass = QueensIslandPopulation.getName()
queensSpecification.geneLength = 32
queensSpecification.populationPerNode = 16
queensSpecification.migrationInterval = 16
queensSpecification.migrationSize = 4
queensSpecification.crossoverPoints = 4
queensSpecification.maxGenerations = 20000
queensSpecification.crossoverProbability = 1.0
queensSpecification.mutationProbability = 0.8
queensSpecification.convergenceLimit = 0.0
queensSpecification.minOrMax = "MIN"
queensSpecification.doSeedModify = true
queensSpecification.seeds = [3, 211, 419, 631, 839, 1039, 1249, 1451,
                             1657, 1861, 2063, 4073, 6079, 8081, 10091, 10301]
RingTopology topology = new RingTopology()

def islandEngine = new IslandEngine(problemSpecification: queensSpecification,
    topology: topology,
    instances: queensSpecification.instances,
    doSeedModify: queensSpecification.doSeedModify,
    nodes: queensSpecification.nodes,
    printWriter: printWriter)
islandEngine.invoke()



