package maxOnes

import island_model.IslandEngine
import island_model.IslandProblemSpecification

def maxOnesSpecification = new IslandProblemSpecification()

maxOnesSpecification.nodes = 8
maxOnesSpecification.instances = 10
maxOnesSpecification.dataFileName = null
maxOnesSpecification.populationClass = MaxOnePopulation.getName()
maxOnesSpecification.geneLength = 512
maxOnesSpecification.populationPerNode = 16
maxOnesSpecification.migrationInterval = 8
maxOnesSpecification.migrationSize = 4
maxOnesSpecification.crossoverPoints = 1
maxOnesSpecification.maxGenerations = 10000
maxOnesSpecification.crossoverProbability = 1.0
maxOnesSpecification.mutationProbability = 0.5
maxOnesSpecification.convergenceLimit = maxOnesSpecification.geneLength
maxOnesSpecification.minOrMax = "MAX"
maxOnesSpecification.doSeedModify = true
// seeds chosen such that 100 instances can be run consecutively without
// any seed being repeated, assuming seeds incremented by 2 for each new instance
maxOnesSpecification.seeds = [3, 211, 419, 631, 839, 1039, 1249, 1451,
                              1657, 1861, 2063, 4073, 6079, 8081, 10091, 10301]

RingTopology topology = new RingTopology()

String outFile = "./simpleRunTest.csv"
File outputFile = new File(outFile)
if (outputFile.exists()) outputFile.delete()
def printWriter = outputFile.newPrintWriter()


def islandEngine = new IslandEngine(problemSpecification: maxOnesSpecification,
    topology: topology,
    instances: maxOnesSpecification.instances,
    doSeedModify: maxOnesSpecification.doSeedModify,
    nodes: maxOnesSpecification.nodes,
    printWriter: printWriter)
islandEngine.invoke()


