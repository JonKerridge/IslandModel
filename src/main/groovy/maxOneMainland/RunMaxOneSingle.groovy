package maxOneMainland

import mainland_model.MainlandEngine
import mainland_model.MainlandProblemSpecification

def problem = new MainlandProblemSpecification()
int nodes = 4
int populationPerNode = 4
int geneLength = 64
int instances = 11
boolean doSeedModify = true

problem.minOrMax = "MAX"
problem.instances = instances
problem.nodes = nodes
problem.geneLength = geneLength
problem.maxGenerations = 1000
problem.replaceInterval = 6
problem.crossoverPoints = 1
problem.crossoverProbability = 1.0
problem.mutationProbability = 0.9
problem.dataFileName = null
problem.convergenceLimit = geneLength
problem.populationPerNode = populationPerNode
problem.doSeedModify = doSeedModify
problem.populationClass = MaxOnePopulation.getName()
problem.individualClass = MaxOneIndividual.getName()
problem.seeds = [3, 211, 419, 631, 839, 1039, 1249, 1451,
                 1657, 1861, 2063, 4073, 6079, 8081, 10091, 10301,
                 10487, 10687, 10883, 11083, 11273, 11471, 11689, 11867,
                 12043, 12241, 122412, 12583, 12763, 12959, 13147, 13331]

String outFile = "./mainlandMaxOnes4.csv"
def fw = new FileWriter(outFile, true)
def bw = new BufferedWriter(fw)
def printWriter = new PrintWriter(bw)

def mainlandEngine = new MainlandEngine(problemSpecification: problem,
    nodes: nodes,
    instances: instances,
    printWriter: printWriter)
mainlandEngine.run()

