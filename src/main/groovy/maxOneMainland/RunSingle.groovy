package maxOneMainland

import mainland_model.MainlandEngine
import mainland_model.MainlandProblemSpecification

def problem = new MainlandProblemSpecification()
int nodes = 8
int populationPerNode = 4
int geneLength = 512
int instances = 10
boolean doSeedModify = true

problem.minOrMax = "MAX"
problem.instances = instances
problem.nodes = nodes
problem.geneLength = geneLength
problem.maxGenerations = 4000
problem.replaceInterval = 5
problem.crossoverPoints = 1
problem.crossoverProbability = 1.0
problem.mutationProbability = 0.5
problem.dataFileName = null
problem.convergenceLimit = geneLength
problem.populationPerNode = populationPerNode
problem.doSeedModify = doSeedModify
problem.populationClass = MaxOnePopulation.getName()
problem.individualClass = MaxOneIndividual.getName()
problem.seeds = [3, 211, 419, 631, 839, 1039, 1249, 1451,
                 1657, 1861, 2063, 4073, 6079, 8081, 10091, 10301]
// many more required
String outFile = "./simpleTest.csv"
File outputFile = new File(outFile)
if (outputFile.exists()) outputFile.delete()
def printWriter = outputFile.newPrintWriter()

def mainlandEngine = new MainlandEngine(
   problemSpecification: problem,
   nodes: nodes,
   instances: instances,
   printWriter: printWriter
)
mainlandEngine.run()