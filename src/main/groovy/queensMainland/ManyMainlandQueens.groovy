package queensMainland

import mainland_model.MainlandEngine
import mainland_model.MainlandProblemSpecification

int geneLength = 64
int instances = 11
boolean doSeedModify = true
def problem = new MainlandProblemSpecification()

List nodeRange = [4, 8, 12, 16]   // subscript nr
List crossoverRange = [1.0, 0.9, 0.8]   // subscript cr
List mutateRange = [0.7, 0.8, 0.9]   // subscript mr
List replaceRange = [4, 6, 8]   // subscript rr
List <Integer> xOverPointsRange = [2,4,6,8]
problem.minOrMax = "MIN"
problem.instances = instances
problem.geneLength = geneLength
problem.maxGenerations = 4000
problem.dataFileName = null
problem.convergenceLimit = 0.0
problem.doSeedModify = doSeedModify
problem.populationClass = QueensMainlandPopulation.getName()
problem.individualClass = QueensMainlandIndividual.getName()
problem.seeds = [3, 211, 419, 631, 839, 1039, 1249, 1451,
                 1657, 1861, 2063, 4073, 6079, 8081, 10091, 10301,
                 10487, 10687, 10883, 11083, 11273, 11471, 11689, 11867,
                 12043, 12241, 122412, 12583, 12763, 12959, 13147, 13331]


nodeRange.each { nr ->
  problem.nodes = nr
  problem.populationPerNode = 4   //
  crossoverRange.each{cr ->
    problem.crossoverProbability = cr
    mutateRange.each { mr ->
      problem.mutationProbability = mr
      replaceRange.each { rr ->
        problem.replaceInterval = rr
        xOverPointsRange.each {xp ->
          problem.crossoverPoints = xp

          String outFile = "./ManyMainlandQueens64.csv"
          def fw = new FileWriter(outFile, true)
          def bw = new BufferedWriter(fw)
          def printWriter = new PrintWriter(bw)

          def mainlandEngine = new MainlandEngine(
            problemSpecification: problem,
            nodes: nr,
            instances: instances,
            printWriter: printWriter
          )
          mainlandEngine.run()
        }
      }
    }
  }
}

