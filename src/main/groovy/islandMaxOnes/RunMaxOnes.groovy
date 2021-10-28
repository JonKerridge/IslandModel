package islandMaxOnes

import island_model.IslandEngine
import island_model.IslandProblemSpecification

//int nodes = 8
int geneLength = 512
int instances = 10                          // 10 runs per combination
int migrationInterval = 100
int populationPerNode = 96

List <Integer> nodeFactor = [4,8,12,16]
List <Double> mutateProb = [1.0, 0.8]       // mutation probability factor, always do crossover

// these factors adjust the number of migrant records depending on populationPerNode
List <Integer> migrationFactor = [6, 12, 24]  // 96 per node
//List <Integer> migrationFactor = [5, 10, 20]  // 80 per node
//List <Integer> migrationFactor = [4, 8, 16]  // 64 per node
// List <Integer> migrationFactor = [3, 4, 6, 8, 12, 16]  // 48 per node
//List <Integer> migrationFactor = [4, 8, 16]  // 32 per node
//List <Integer> migrationFactor = [2,4,8]  // 16 per node



def maxOnesSpecification = new IslandProblemSpecification()

maxOnesSpecification.nodes = 0
maxOnesSpecification.instances = instances
maxOnesSpecification.dataFileName = null
maxOnesSpecification.populationClass = MaxOnePopulation.getName()
maxOnesSpecification.geneLength = geneLength
maxOnesSpecification.populationPerNode = populationPerNode
maxOnesSpecification.migrationInterval = migrationInterval
maxOnesSpecification.migrationSize = 0
maxOnesSpecification.crossoverPoints = 1
maxOnesSpecification.maxGenerations = 10000
maxOnesSpecification.crossoverProbability = 1.0
maxOnesSpecification.mutationProbability = 0.0
maxOnesSpecification.convergenceLimit = geneLength
maxOnesSpecification.minOrMax = "MAX"
maxOnesSpecification.doSeedModify = true
// seeds chosen such that 100 instances can be run consecutively without
// any seed being repeated, assuming seeds incremented by 2 for each new instance
maxOnesSpecification.seeds = [3, 211, 419, 631, 839, 1039, 1249, 1451,
                     1657, 1861, 2063, 4073, 6079, 8081, 10091, 10301]

RingTopology topology = new RingTopology()

String outFile = "./${geneLength}ones${populationPerNode}ppn${migrationInterval}mig.csv"

File outputFile = new File(outFile)
if (outputFile.exists()) outputFile.delete()
def printWriter = outputFile.newPrintWriter()

for ( n in 0 ..< nodeFactor.size()) {
  maxOnesSpecification.nodes = nodeFactor[n]
  for (r in 0..<mutateProb.size()) {
    maxOnesSpecification.mutationProbability = mutateProb[r] as Double
    for (m in 0..<migrationFactor.size()) {
      maxOnesSpecification.migrationSize = (maxOnesSpecification.populationPerNode / migrationFactor[m]) as Integer
      def islandEngine = new IslandEngine(problemSpecification: maxOnesSpecification,
          topology: topology,
          instances: instances,
          doSeedModify: maxOnesSpecification.doSeedModify,
          nodes: nodeFactor[n],
          printWriter: printWriter)
      islandEngine.invoke()
    }
  }
}


