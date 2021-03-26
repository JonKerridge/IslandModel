package island_model

int nodes = 3
int instances = 5

def ps = new ProblemSpecification(
    individualClass: "TestIndividual",
    populationClass: "TestPopulation",
    dataFileName: "TestDataFu=ileName",
    instance: -1,
    populationPerNode: 0,
    migrationInterval: 0,
    crossoverPoints: 2,
    crossoverProbability: 0.75,
    mutationProbability: 0.01,
    seeds: [3,5,7])

def islandEngine = new IslandEngine(problemSpecification: ps,
    instances: instances,
    nodes: nodes)
islandEngine.invoke()
