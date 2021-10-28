package islandMaxOnes

import island_model.IslandEngine
import island_model.IslandProblemSpecification

def problem = new IslandProblemSpecification()
int nodes = 16
int ppn = 4  // 8 16 24 32
int geneLength = 512
int instances = 11
boolean doSeedModify = true

List nodeRange = [4, 8, 12, 16]   // subscript nr
List crossoverRange = [1.0, 0.9, 0.8]   // subscript cr
List mutateRange = [0.7, 0.8, 0.9]   // subscript mr
List migrateIntervals = [4]   // subscript mi
List migrateSizes= [2]   // subscript ms

problem.nodes = nodes
problem.instances = instances
problem.dataFileName = null
problem.populationClass = MaxOnePopulation.getName()
problem.geneLength = geneLength
problem.populationPerNode = ppn
problem.migrationInterval = 16
problem.migrationSize = 4
problem.crossoverPoints = 1
problem.maxGenerations = 4000
problem.crossoverProbability = 1.0
problem.mutationProbability = 0.8
problem.convergenceLimit = problem.geneLength
problem.minOrMax = "MAX"
problem.doSeedModify = doSeedModify
// seeds chosen such that 100 instances can be run consecutively without
// any seed being repeated, assuming seeds incremented by 2 for each new instance
problem.seeds = [3, 211, 419, 631, 839, 1039, 1249, 1451,
                 1657, 1861, 2063, 4073, 6079, 8081, 10091, 10301,
                 10487, 10687, 10883, 11083, 11273, 11471, 11689, 11867,
                 12043, 12241, 122412, 12583, 12763, 12959, 13147, 13331]

RingTopology topology = new RingTopology()

nodeRange.each { nr ->
  problem.nodes = nr
  problem.populationPerNode = ppn
  crossoverRange.each { cr ->
    problem.crossoverProbability = cr
    mutateRange.each { mr ->
      problem.mutationProbability = mr
      migrateIntervals.each { mi ->
        problem.migrationInterval = mi
        migrateSizes.each { ms -> problem.migrationSize = ms

          String outFile = "./islandMaxOnes${ppn}.csv"
          def fw = new FileWriter(outFile, true)
          def bw = new BufferedWriter(fw)
          def printWriter = new PrintWriter(bw)
          def islandEngine = new IslandEngine(problemSpecification: problem,
              topology: topology,
              instances: problem.instances,
              doSeedModify: problem.doSeedModify,
              nodes: problem.nodes,
              printWriter: printWriter)
          islandEngine.invoke()

        }
      }
    }
  }
}






