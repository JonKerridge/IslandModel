package queens

import island_model.IslandEngine
import island_model.IslandProblemSpecification

int nodes = 4

int geneLength = 128                        // queens to be placed
int instances = 10                          // 10 runs per combination
int migrationInterval = 40
int populationPerNode = 32

List <Integer> nodeFactor = [4,8,12,16]
List <Double> mutateProb = [1.0, 0.8]       // mutation probability factor, always do crossover

// these factors adjust the number of migrant records depending on populationPerNode
//List <Integer> migrationFactor = [6, 12, 24]  // 96 per node
//List <Integer> migrationFactor = [5, 10, 20]  // 80 per node
//List <Integer> migrationFactor = [4, 8, 16]  // 64 per node
// List <Integer> migrationFactor = [3, 4, 6, 8, 12, 16]  // 48 per node
List <Integer> migrationFactor = [4, 8, 16]  // 32 per node
//List <Integer> migrationFactor = [2,4,8]  // 16 per node

List queens = [512]   //[128, 256, 512, 1024]
List nodeSizes = [8,12,16]
List popPerNode = [8, 16]
Map migrationSizeRatio = [8:[2, 4],16:[2,4,8], 32:[2,4,8,16]]
Map migrationIntervals = [8:[4,8,16], 16:[8,16,24], 32:[16,24,32]]
Map xOverPoints = [128:[2,4,8], 256:[4,8,16], 512:[8,16,32]]

queens.each {q ->
  nodeSizes.each {n ->
    String outFile = "./Queens${q}Nodes${n}.csv"
    File outputFile = new File(outFile)
    if (outputFile.exists()) outputFile.delete()
    def printWriter = outputFile.newPrintWriter()
    popPerNode.each {ppn ->
      List sizeRatio = migrationSizeRatio[ppn]
      sizeRatio.each {r ->
        List interval = migrationIntervals[ppn]
        interval.each {i ->
          mutateProb.each {mp ->
            List xOver = xOverPoints[q]
            xOver.each { x ->
              println "Queens, $q, Nodes, $n, PPN, $ppn, Ratio, $r, Interval, $i, Mutate, $mp, Xpnts, $x, MS, ${ppn/r}"
              printWriter.println "Queens, $q, Nodes, $n, PPN, $ppn, Ratio, $r, Interval, $i, Mutate, $mp, Xpnts, $x, MS, ${ppn/r}"
              def queensSpecification = new IslandProblemSpecification()
              queensSpecification.nodes = n
              queensSpecification.instances = instances
              queensSpecification.dataFileName = null
              queensSpecification.populationClass = QueensIslandPopulation.getName()
              queensSpecification.geneLength = q
              queensSpecification.populationPerNode = ppn
              queensSpecification.migrationInterval = i
              queensSpecification.migrationSize = ppn / r
              queensSpecification.crossoverPoints = x
              queensSpecification.maxGenerations = 20000
              queensSpecification.crossoverProbability = 1.0
              queensSpecification.mutationProbability = mp
              queensSpecification.convergenceLimit = 0.0
              queensSpecification.minOrMax = "MIN"
              queensSpecification.doSeedModify = true
              queensSpecification.seeds = [3, 211, 419, 631, 839, 1039, 1249, 1451,
                                           1657, 1861, 2063, 4073, 6079, 8081, 10091, 10301]
              RingTopology topology = new RingTopology()

              def islandEngine = new IslandEngine(problemSpecification: queensSpecification,
                  topology: topology,
                  instances: instances,
                  doSeedModify: queensSpecification.doSeedModify,
                  nodes: n,
                  printWriter: printWriter)
              islandEngine.invoke()
            } // x
          } // mp
        } // i
      } // r
    } // ppn
  } // n
} // q

//def queensSpecification = new ProblemSpecification()
//queensSpecification.nodes = nodes
//queensSpecification.instances = instances
//queensSpecification.dataFileName = null
//queensSpecification.populationClass = QueensPopulation.getName()
//queensSpecification.geneLength = geneLength
//queensSpecification.populationPerNode = populationPerNode
//queensSpecification.migrationInterval = migrationInterval
//queensSpecification.migrationSize = 4
//queensSpecification.crossoverPoints = 6
//queensSpecification.maxGenerations = 20000
//queensSpecification.crossoverProbability = 1.0
//queensSpecification.mutationProbability = 0.8
//queensSpecification.doSeedModify = true
//// seeds chosen such that 100 instances can be run consecutively without
//// any seed being repeated, assuming seeds incremented by 2 for each new instance
//queensSpecification.seeds = [3, 211, 419, 631, 839, 1039, 1249, 1451,
//                              1657, 1861, 2063, 4073, 6079, 8081, 10091, 10301]
//
//RingTopology topology = new RingTopology()
//
//String outFile = "./${geneLength}queens${populationPerNode}ppn${migrationInterval}mig.csv"
//
//File outputFile = new File(outFile)
//if (outputFile.exists()) outputFile.delete()
//def printWriter = outputFile.newPrintWriter()
//
//def islandEngine = new IslandEngine(problemSpecification: queensSpecification,
//    topology: topology,
//    instances: instances,
//    doSeedModify: queensSpecification.doSeedModify,
//    nodes: nodes,
//    printWriter: printWriter)
//islandEngine.invoke()
//
//printWriter.flush()
//printWriter.close()
