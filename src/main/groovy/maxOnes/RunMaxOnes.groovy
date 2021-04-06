package maxOnes

import island_model.IslandEngine
import island_model.ProblemSpecification

int nodes = 16
int instances = 4

def maxOnesSpecification = new ProblemSpecification()
maxOnesSpecification.nodes = nodes
maxOnesSpecification.instances = instances
maxOnesSpecification.dataFileName = null
maxOnesSpecification.populationClass = MaxOnePopulation.getName()
maxOnesSpecification.geneLength = 512
maxOnesSpecification.populationPerNode = 16
maxOnesSpecification.migrationInterval = 1000
maxOnesSpecification.crossoverPoints = 1
maxOnesSpecification.maxGenerations = 10000
maxOnesSpecification.crossoverProbability = 1.0
maxOnesSpecification.mutationProbability = 0.80
maxOnesSpecification.seeds = [3, 211, 419, 631, 839, 1039, 1249, 1451,
                     1657, 1861, 2063, 4073, 6079, 8081, 10091, 10301]

RingTopology topology = new RingTopology()

def islandEngine = new IslandEngine (
    problemSpecification: maxOnesSpecification,
    topology: topology,
    instances: instances,
    nodes: nodes)
islandEngine.invoke()
