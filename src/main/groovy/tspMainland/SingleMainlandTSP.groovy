package tspMainland


import mainland_model.MainlandEngine
import mainland_model.MainlandProblemSpecification

String outFile = "./SingleMainlandTest.csv"
File outputFile = new File(outFile)
if (outputFile.exists()) outputFile.delete()
def printWriter = outputFile.newPrintWriter()

def tspProblem = new MainlandProblemSpecification()
tspProblem.nodes = 16
tspProblem.instances = 11
tspProblem.dataFileName = "./10cities.tsp"
tspProblem.populationClass = TSPMainlandPopulation.getName()
tspProblem.individualClass = TSPMainlandIndividual.getName()
tspProblem.geneLength = 11
tspProblem.populationPerNode = 4
tspProblem.replaceInterval = 32
tspProblem.crossoverPoints = 2
tspProblem.maxGenerations = 4000
tspProblem.crossoverProbability = 1.0
tspProblem.mutationProbability = 1.0
tspProblem.convergenceLimit = 42.0
tspProblem.minOrMax = "MIN"
tspProblem.doSeedModify = true
tspProblem.seeds = [3, 211, 419, 631, 839, 1039, 1249, 1451,
                             1657, 1861, 2063, 4073, 6079, 8081, 10091, 10301,
                             10487, 10687, 10883, 11083, 11273, 11471, 11689, 11867,
                             12043, 12241, 122412, 12583, 12763, 12959, 13147, 13331]

def mainlandEngine = new MainlandEngine(problemSpecification: tspProblem,
    instances: tspProblem.instances,
    nodes: tspProblem.nodes,
    printWriter: printWriter)
mainlandEngine.run()



