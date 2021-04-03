package maxOnes

import island_model.IslandEngine
import island_model.ProblemSpecification

int nodes = 2
int instances = 5

def maxOnesSpecification = new ProblemSpecification()
//maxOnesSpecification.individualClass = "maxOnes.MaxOneIndividual"
maxOnesSpecification.populationClass = MaxOnePopulation.getName()
maxOnesSpecification.geneLength = 8
maxOnesSpecification.populationPerNode = 4
maxOnesSpecification.migrationInterval = 100
maxOnesSpecification.crossoverPoints = 2
maxOnesSpecification.maxGenerations = 1000
maxOnesSpecification.crossoverProbability = 0.75
maxOnesSpecification.mutationProbability = 0.10
maxOnesSpecification.seeds = [3, 5, 7, 11, 13, 17, 19, 23,
                     29, 31, 37, 41, 43, 47, 53, 59,
                     61, 67, 71, 73, 79, 83, 89, 97,
                     101, 103, 107, 109, 113, 127, 131, 137 ]

def islandEngine = new IslandEngine(
    problemSpecification: maxOnesSpecification,
    instances: instances,
    nodes: nodes)
islandEngine.invoke()
