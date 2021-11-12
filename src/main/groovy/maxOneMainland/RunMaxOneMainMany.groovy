package maxOneMainland

import mainland_model.MainlandEngine
import mainland_model.MainlandProblemSpecification

def problem = new MainlandProblemSpecification()
int nodes = 16
int populationPerNode = 4
int geneLength = 512
int instances = 11
boolean doSeedModify = true


List nodeRange = [4, 8, 12, 16, 24]   // subscript nr
List crossoverRange = [1.0, 0.9, 0.8]   // subscript cr
List mutateRange = [0.7, 0.8, 0.9]   // subscript mr
List replaceRange = [4, 6, 8, 10]   // subscript rr

problem.minOrMax = "MAX"
problem.instances = instances
problem.nodes = nodes
problem.geneLength = geneLength
problem.maxGenerations = 4000
problem.replaceInterval = 5
problem.crossoverPoints = 1
problem.crossoverProbability = 1.0
problem.mutationProbability = 0.8
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

//String outFile = "./mainlandMaxOnes.csv"
//def fw = new FileWriter(outFile, true)
//def bw = new BufferedWriter(fw)
//def printWriter = new PrintWriter(bw)

nodeRange.each { nr ->
  problem.nodes = nr
  problem.populationPerNode = 4
  crossoverRange.each{cr ->
    problem.crossoverProbability = cr
    mutateRange.each { mr ->
      problem.mutationProbability = mr
      replaceRange.each { rr ->
        problem.replaceInterval = rr

        String outFile = "./mainlandMaxOnes3.csv"
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

//def mainlandEngine = new MainlandEngine(
//   problemSpecification: problem,
//   nodes: nodes,
//   instances: instances,
//   printWriter: printWriter
//)
//mainlandEngine.run()