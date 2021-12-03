package queensMainland


import mainland_model.MainlandEngine
import mainland_model.MainlandProblemSpecification

String outFile = "./SingleMainlandTest.csv"
def fw = new FileWriter(outFile, true)
def bw = new BufferedWriter(fw)
def printWriter = new PrintWriter(bw)

def queensSpecification = new MainlandProblemSpecification()
queensSpecification.nodes = 8
queensSpecification.instances = 11
queensSpecification.dataFileName = null
queensSpecification.populationClass = QueensMainlandPopulation.getName()
queensSpecification.individualClass = QueensMainlandIndividual.getName()
queensSpecification.geneLength = 32
queensSpecification.populationPerNode = 4
queensSpecification.replaceInterval = 4
queensSpecification.crossoverPoints = 2
queensSpecification.maxGenerations = 2000
queensSpecification.crossoverProbability = 1.0
queensSpecification.mutationProbability = 0.8
queensSpecification.convergenceLimit = 0.0
queensSpecification.minOrMax = "MIN"
queensSpecification.doSeedModify = true
queensSpecification.seeds = [3, 211, 419, 631, 839, 1039, 1249, 1451,
                             1657, 1861, 2063, 4073, 6079, 8081, 10091, 10301,
                             10487, 10687, 10883, 11083, 11273, 11471, 11689, 11867,
                             12043, 12241, 122412, 12583, 12763, 12959, 13147, 13331]

def mainlandEngine = new MainlandEngine(problemSpecification: queensSpecification,
    instances: queensSpecification.instances,
    nodes: queensSpecification.nodes,
    printWriter: printWriter)
mainlandEngine.run()



