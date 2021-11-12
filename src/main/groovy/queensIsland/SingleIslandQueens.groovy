package queensIsland

import island_model.IslandEngine
import island_model.IslandProblemSpecification

String outFile = "./SingleIslandTest.csv"
File outputFile = new File(outFile)
if (outputFile.exists()) outputFile.delete()
def printWriter = outputFile.newPrintWriter()

def queensSpecification = new IslandProblemSpecification()
queensSpecification.nodes = 8
queensSpecification.instances = 11
queensSpecification.dataFileName = null
queensSpecification.populationClass = QueensIslandPopulation.getName()
queensSpecification.geneLength = 64
queensSpecification.populationPerNode = 16
queensSpecification.migrationInterval = 8
queensSpecification.migrationSize = 4
queensSpecification.crossoverPoints = 2
queensSpecification.maxGenerations = 2000
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



