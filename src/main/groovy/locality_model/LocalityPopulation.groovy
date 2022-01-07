package locality_model

interface LocalityPopulation {
  /**
   * The user is required to write a constructor for any class using this interface.
   * The comment can be copied directly into any class implementing this interface
   *
   // the properties of the class
   int individuals   // active population plus the space for reproduction offspring
   int geneLength    // the length of each individuals chromosome
   int crossoverPoints // the EVEN number of points used to break the chromosome when doing reproduction
   int maxGenerations  // maximum number of iterations before termination
   int replaceInterval  // iterations required before new individuals created with no change in best fitness
   double crossoverProbability // the probability that reproduction will lead to an acutal crossover operation
   double mutateProbability  // the probability that mutation will occur after crossover
   String dataFileName // the name of any data file used to provide data for the evaluate fitness function

   // the created objects of the class
   List <LocalityIndividual> population // to hold the list of individuals that form the population
   List evaluateData   // the data structure used to hold the fitness evaluation data if required

   */

  List <LocalityIndividual> population
  List evaluateData

  /** reproduce creates the children resulting from a crossover operation
   * The parameters are all subscripts of individuals in the population List
   * @param parent1
   * @param parent2
   * @param child1
   * @param child2
   * @param overwrite1
   * @param overwrite2
   * @param rng the random number generator to be used,l specific to each node
   *
   */
  void reproduce(
      int parent1,
      int parent2,
      int child1,
      int child2,
      Random rng)

  /** replace possibly overwrites the overwrite individuals with the children resulting from a crossover operation
   * The parameters are all subscripts of individuals in the population List
   * @param child1
   * @param child2
   * @param overwrite1
   * @param overwrite2
   * this method may be null if the whole population is sorted
   */
  void replaceCandidates(
      int child1,
      int child2,
      int overwrite1,
      int overwrite2
  )

  /**
   * bestSolution is used to find the individual that has the best solution once the
   * maximum number of generations has been exceeded.  It does not require knowledge of
   * the convergence criteria as it is based solely on the relative values of Individual.fitness
   * @return the individual that has the best solution within maxGenerations
   */
  LocalityIndividual bestSolution()

  /**
   * processDataFile used to read content of file with name dataFilename
   * and place them into the List object evaluateData within population used in the
   * Individual evaluateFitness method
   *
   * @param dataFileName the name of the file containing the evaluation data or null
   * @return a List containing the evaluation data
   */
  List processDataFile (String dataFileName)

}