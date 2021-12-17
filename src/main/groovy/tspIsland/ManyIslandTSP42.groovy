package tspIsland

import islandMaxOnes.MaxOnePopulation
import island_model.IslandEngine
import island_model.IslandProblemSpecification
import queensIsland.QueensIslandPopulation

int geneLength = 43
int instances = 11                          // 10 runs per combination

List <Integer> nodeRange = [4,8,12,16]    //nr
List <Double> mutateRange = [0.9, 0.8, 0.7]       // mutation probability factor, always do crossover
List <Double> crossoverRange = [1.0, 0.9, 0.8]   // subscript cr
List <Integer> populationRange = [4, 8, 12, 16]   // pr
List <Integer> migrationRange = [[2], [2, 4], [2,4,6], [2,4,6,8]]   // mr varies with population
List <Integer> intervalRange = [8, 12, 16, 24]
List <Integer> xOverPointsRange = [2,4,6,8]
def problem = new IslandProblemSpecification()

problem.instances = instances
problem.dataFileName = "./dantzig42.tsp"
problem.populationClass = TSPIslandPopulation.getName()
problem.geneLength = geneLength
problem.maxGenerations = 4000
problem.convergenceLimit = 850.0
problem.minOrMax = "MIN"
problem.doSeedModify = true
// seeds chosen such that 100 instances can be run consecutively without
// any seed being repeated, assuming seeds incremented by 2 for each new instance
problem.seeds = [3, 211, 419, 631, 839, 1039, 1249, 1451,
                              1657, 1861, 2063, 4073, 6079, 8081, 10091, 10301,
                              10487, 10687, 10883, 11083, 11273, 11471, 11689, 11867,
                              12043, 12241, 122412, 12583, 12763, 12959, 13147, 13331]

islandMaxOnes.RingTopology topology = new islandMaxOnes.RingTopology()


for ( n in 0 ..< nodeRange.size()) {
  problem.nodes = nodeRange[n]
  for (m in 0..<mutateRange.size()) {
    problem.mutationProbability = mutateRange[m] as Double
    for (c in 0 ..< crossoverRange.size()) {
      problem.crossoverProbability = crossoverRange[c]
      for ( p in 0 ..< populationRange.size()) {
        problem.populationPerNode = populationRange[p]
        List <Integer> migrateValues = migrationRange[p]
        for (v in 0..<migrateValues.size()) {
          problem.migrationSize = migrateValues[v]
          for (r in 0..<intervalRange.size()) {
            problem.migrationInterval = intervalRange[r]
            for ( pr in 0 ..< xOverPointsRange.size()) {
              problem.crossoverPoints = xOverPointsRange[pr]

              String outFile = "./ManyIslandTSP42.csv"
              def fw = new FileWriter(outFile, true)
              def bw = new BufferedWriter(fw)
              def printWriter = new PrintWriter(bw)

              def islandEngine = new IslandEngine(problemSpecification: problem,
                  topology: topology,
                  instances: instances,
                  doSeedModify: problem.doSeedModify,
                  nodes: problem.nodes,
                  printWriter: printWriter)
              islandEngine.invoke()
            }
          }
        }
      }
    }
  }
}
