package localityMax

import locality_model.LocalityIndividual
import locality_model.LocalityPopulation
import maxOneMainland.MaxOneIndividual

class LocalityMaxOnePopulation implements  LocalityPopulation{

  // properties of the class
  int individuals   // active population plus the space for reproduction offspring
  int geneLength    // the length of each individuals chromosome
  int crossoverPoints // the EVEN number of points used to break the chromosome when doing reproduction
  int maxGenerations  // maximum number of iterations before termination
  int replaceInterval  // iterations required before new individuals created with no change in best fitness
  double crossoverProbability // the probability that reproduction will lead to an acutal crossover operation
  double mutateProbability  // the probability that mutation will occur after crossover
  String dataFileName // the name of any data file used to provide data for the evaluate fitness function
  int convergenceLimit // the limit to be achieved for successful convergence

  // constructed objects of the class
  List <LocalityMaxOneIndividual> population // to hold the list of individuals that form the population
  List evaluateData   // the data structure used to hold the fitness evaluation data if required


  LocalityMaxOnePopulation (
      int individuals,
      int geneLength,
      int crossoverPoints,
      int maxGenerations,
      int replaceInterval,
      double crossoverProbability,
      double mutateProbability,
      String datafileName)
  {
    this.individuals = individuals
    this.geneLength = geneLength
    this.convergenceLimit = convergenceLimit
    this.crossoverPoints = crossoverPoints
    this.replaceInterval = replaceInterval
    this.maxGenerations = maxGenerations
    this.crossoverProbability = crossoverProbability
    this.mutateProbability = mutateProbability
    this.dataFileName = datafileName
    population = []
    for ( i in 0 ..< individuals)
      population << new LocalityMaxOneIndividual(geneLength)
  }

  LocalityMaxOnePopulation (){
    population = []
  }


  /** reproduce creates the children resulting from a crossover operation
   * The parameters are all subscripts of individuals in the population List
   * @param parent1
   * @param parent2
   * @param child1
   * @param child2
   * @param rng the random number generator to be used, specific to each node
   *
   */
  @Override
  void reproduce(int parent1, int parent2, int child1, int child2, Random rng) {
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
    }
  }

  /** replace possibly overwrites the overwrite individuals with the children resulting from a crossover operation
   * The parameters are all subscripts of individuals in the population List
   * @param child1
   * @param child2
   * @param overwrite1
   * @param overwrite2
   * this method may be null if the whole population is sorted
   */
  @Override
  void replaceCandidates(int child1, int child2, int overwrite1, int overwrite2) {
    int child1Fit = population[child1].getFitness()
    int child2Fit = population[child2].getFitness()
    int overwrite1Fit = population[overwrite1].getFitness()
    int overwrite2Fit = population[overwrite2].getFitness()
    // assume overwrite2Fit is at least as good as overwrite1Fit and is probably better
    // due to the ordering of the overwrites in the population
    // we do not know the ordering of child fitness; it is a maximise problem
    if ( child1Fit > child2Fit){
      if (child2Fit > overwrite2Fit){
        // both overwrites can be replaced by the children
        // the ordering does not matter as they will be sorted on return to Root
        population.swap(child1, overwrite1)
        population.swap(child2, overwrite2)
      } else {
        // only child1 can overwrite overwrite1
        population.swap(child1, overwrite1)
      }
    } else { //child2 is the better
      if (child1Fit > overwrite2Fit){
        // both overwrites can be replaced by the children
        // the ordering does not matter as they will be sorted on return to Root
        population.swap(child1, overwrite1)
        population.swap(child2, overwrite2)
      } else {
        // only child2 can overwrite overwrite1
        population.swap(child2, overwrite1)
      }
    }
  }

  /**
   * bestSolution is used to find the individual that has the best solution once the
   * maximum number of generations has been exceeded.  It does not require knowledge of
   * the convergence criteria as it is based solely on the relative values of Individual.fitness
   * @return the individual that has the best solution within maxGenerations
   */
  @Override
  LocalityIndividual bestSolution() {
    return null
  }

  /**
   * processDataFile used to read content of file with name dataFilename
   * and place them into the List object evaluateData within population used in the
   * Individual evaluateFitness method
   *
   * @param dataFileName the name of the file containing the evaluation data or null
   * @return a List containing the evaluation data
   */
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
