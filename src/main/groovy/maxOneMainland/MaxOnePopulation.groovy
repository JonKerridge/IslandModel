package maxOneMainland

import island_model.Individual
import mainland_model.MainlandPopulation

class MaxOnePopulation implements MainlandPopulation{

    // properties of the class
    int individuals   // active population plus the space for reproduction offspring
    int geneLength    // the length of each individuals chromosome
    int crossoverPoints // the EVEN number of points used to break the chromosome when doing reproduction
    int maxGenerations  // maximum number of iterations before termination
    int replaceInterval  // iterations required before new individuals created with no change in best fitness
    double crossoverProbability // the probability that reproduction will lead to an acutal crossover operation
    double mutateProbability  // the probability that mutation will occur after crossover
    String dataFileName // the name of any data file used to provide data for the evaluate fitness function
    BigDecimal convergenceLimit // the limit to be achieved for successful convergence
    int bestFitIndex    // subscript in population with the best solution

    // constructed objects of the class
    List <MaxOneIndividual> population // to hold the list of individuals that form the population
    List evaluateData   // the data structure used to hold the fitness evaluation data if required


    MaxOnePopulation (
        int individuals,
        int geneLength,
        int crossoverPoints,
        int maxGenerations,
        int replaceInterval,
        double crossoverProbability,
        double mutateProbability,
        String datafileName,
        BigDecimal convergenceLimit,
        int bestFitIndex)
    {
        this.individuals = individuals
        this.geneLength = geneLength
        this.convergenceLimit = convergenceLimit
        this.crossoverPoints = crossoverPoints
        this.replaceInterval = replaceInterval
        this.maxGenerations = maxGenerations
        this.crossoverProbability = crossoverProbability
        this.mutateProbability = mutateProbability
        this.bestFitIndex = bestFitIndex
        this.dataFileName = datafileName
        processDataFile()
        population = []
        for ( i in 0 ..< individuals)
            population << new MaxOneIndividual(geneLength)
    }

    MaxOnePopulation (){
        population = []
    }

    @Override
    void reproduce(
        int parent1,
        int parent2,
        int child1,
        int child2,
        int candidate1,
        int candidate2,
        Random rng) {
        // this uses a single point crossover for MaxOnes
        int crossoverPoint = rng.nextInt(geneLength)
        if ( rng.nextDouble() < crossoverProbability){
            // doing the crossover
            for ( p in 0 ..< crossoverPoint){
                population[child1].chromosome[p] = population[parent1].chromosome[p]
                population[child2].chromosome[p] = population[parent2].chromosome[p]
            }
            for ( p in crossoverPoint ..< geneLength){
                population[child1].chromosome[p] = population[parent2].chromosome[p]
                population[child2].chromosome[p] = population[parent1].chromosome[p]
            }
            // now see if we undertake a mutation on each child
            if (rng.nextDouble() < mutateProbability)
                population[child1].mutate(rng)
            if (rng.nextDouble() < mutateProbability)
                population[child2].mutate(rng)
            // now evaluate the child fitness - evaluationData is null
            population[child1].evaluateFitness(evaluateData)
            population[child2].evaluateFitness(evaluateData)
        }
    }

    @Override
    Individual convergence() {
        if (population[bestFitIndex].fitness == convergenceLimit)
            return population[bestFitIndex]
        else
            return null
    }

    @Override
    Individual bestSolution() {
        return null
    }

    @Override
    void processDataFile() {
        evaluateData = null
    }

    String toString(){
        String s
        s = "populationData: $individuals, $geneLength, $maxGenerations, $population"
        return s
    }
}
