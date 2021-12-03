package maxOneMainland

import mainland_model.MainlandIndividual
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
//            // now evaluate the child fitness - evaluationData is null
//            population[child1].evaluateFitness(evaluateData)
//            population[child2].evaluateFitness(evaluateData)
        }
    }
/** replace possibly overwrites the candidate individuals with the children resulting from a crossover operation
 * The parameters are all subscripts of individuals in the population List
 * @param child1
 * @param child2
 * @param candidate1
 * @param candidate2
 * this method may be null if the whole population is sorted
 */

    @Override
    void replaceCandidates(int child1, int child2, int candidate1, int candidate2) {
        int child1Fit = population[child1].getFitness()
        int child2Fit = population[child2].getFitness()
        int candidate1Fit = population[candidate1].getFitness()
        int candidate2Fit = population[candidate2].getFitness()
        // assume candidate2Fit is at least as good as candidate1Fit and is probably better
        // due to the ordering of the candidates in the population
        // we do not know the ordering of child fitness; it is a maximise problem
        if ( child1Fit > child2Fit){
            if (child2Fit > candidate2Fit){
                // both candidates can be replaced by the children
                // the ordering does not matter as they will be sorted on return to Root
                population.swap(child1, candidate1)
                population.swap(child2, candidate2)
            } else {
                // only child1 can overwrite candidate1
                population.swap(child1, candidate1)
            }
        } else { //child2 is the better
            if (child1Fit > candidate2Fit){
                // both candidates can be replaced by the children
                // the ordering does not matter as they will be sorted on return to Root
                population.swap(child1, candidate1)
                population.swap(child2, candidate2)
            } else {
                // only child2 can overwrite candidate1
                population.swap(child2, candidate1)
            }
        }
    }

    @Override
    MainlandIndividual convergence(def convergenceLimit) {
        if (population[bestFitIndex].fitness == convergenceLimit)
            return population[bestFitIndex]
        else
            return null
    }

    @Override
    MainlandIndividual bestSolution() {
        return null
    }

    @Override
    List processDataFile(String dataFileName) {
        return null
    }

    String toString(){
        String s
        s = "populationData: $individuals, $geneLength, $maxGenerations, $population"
        return s
    }
}
