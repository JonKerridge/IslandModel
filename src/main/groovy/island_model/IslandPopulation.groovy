package island_model

/**
 * Defines the population of Individuals at each node.
 * The comments give an indication of the methods that will be required.
 * The user is required to create a public constructor.  Any properties referred to in the
 * following that is not obtained as a parameter is available in the ProblemSpecification class.
 *
 * @param population  a List of Individuals <br>
 *
 * The following properties must be initialised in a public constructor
 * in the following order
 *
 * @param individuals the number of individuals in population
 * @param geneLength the size of an Individuals chromosome
 * @param crossoverProbability the crossover operation will be undertaken provided
 * a random number is in 0.0 <= random-value < crossoverProbability
 * @param mutateProbability the mutate operation will be undertaken on a crossover offspring
 * provided a random number is in 0.0 <= random-value < mutateProbability
 * @param dataFileName the name of a file containing data used in the fitness evaluation
 * of an individual
 * @param convergenceLimit the value used to determine whether the solution has converged
 * @param rng the random number generator to be used
 * @param nodeID the node subscript that identifies the node where this population is created
 *
 */
interface IslandPopulation {

  List <IslandIndividual> population // to hold the list of Individuals that form the population
  int individuals
  int geneLength
  double crossoverProbability
  double mutateProbability
  String dataFileName
  BigDecimal convergenceLimit
  Random rng
  int nodeID

  /**
   * Identifies the fittest and possibly second fittest individuals in population
   *
   * @return the selected individual subscripts
   */
  List <Integer> selectParents()


  /**
   * Identifies the least fit Individuals  in population
   *
   * @return the selected individual subscripts
   */
  List <Integer> selectLeastFit()

  /**
   * undertakes the crossover and mutate operations using the indices indicated by selectParents as
   * the source Individuals and writes the children created to the indices indicated by selectLeastFit
   *
   * @param crossoverPoints the number of crossover points in the reproduction algorithm
   * assuming the method is sufficiently flexible
   */
  void reproduce(int crossoverPoints)

  /**
   * Returns a list of subscripts of Individuals selected for migration
   *
   * @param migrationSize the number of migrants to be selected from the population
   * @return a List of the subscripts of the Individuals in the population that
   * have been selected for migration
   */
  List <Integer> selectMigrants(int migrationSize)

  /**
   * Returns a List of Individuals for the specified migrant indices
   *
   * @param migrantIndices the subscripts of the population Individuals to be migrated
   * @return  the selected Individuals
   */
  List <IslandIndividual> getMigrants(List <Integer> migrantIndices)

  /**
   * Overwrites the Individuals in migrantIndices with the Individuals
   * contained in incomers
   * @param incomers a List of Individuals that are immigrating into this population
   * @param migrantIndices the subscripts in the population where the immigrant incomers
   * s are to be located
   */
  void includeImmigrants(List <IslandIndividual> incomers, List <Integer> migrantIndices)

  /**
   * Returns an Individual that satisfies the
   * model's convergence criteria if achieved or null otherwise
   *
   * @param convergenceLimit the value used to determine if convergence has occurred
   * @return the Individual that has satisfied the convergence criteria or null otherwise
   */
  IslandIndividual convergence (BigDecimal convergenceLimit)

  /**
   * bestSolution is used to find the individual that has the best solution once the
   * maximum number of generations has been exceeded.  It does not require knowledge of
   * the convergence criteria as it is based solely on the relative values of Individual.fitness
   *
   * @return the individual that has the best solution within maxGenerations
   */
  IslandIndividual bestSolution()

  /**
   * Reads content of file with name dataFilename
   * and place them into an List object within used in the
   * Individual evaluateFitness method
   */
  void processDataFile ()

}