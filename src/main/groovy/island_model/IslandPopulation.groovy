package island_model

/**
 * Defines the population of Individuals at each node.
 * The comments give an indication of the methods that will be required.
 * The user is required to create a public constructor.  Any properties referred to in the
 * following that is not obtained as a parameter is available in the ProblemSpecification class.
 *
 * @param population  a List of Individuals
 * @selectParents identifies the fittest and possibly second fittest
 * Individuals and returns their subscripts in a List
 * @selectLeastFit identifies the least fit Individuals and returns
 * their subscripts in a List
 * @reproduce undertakes the crossover operation using the indices indicated by selectParents as
 * the source Individuals and writes the children created to the indices indicated by selectLeastFit
 * @selectMigrants returns a list of subscripts of Individuals selected for migration
 * @getMigrants returns a List of Individuals for the specified migrant indices
 * @includeImmigrants overwrites the Individuals in migrantIndices with the Individuals
 * contained in incomers
 * @convergence returns an Individual that satisfies the
 * model's convergence criteria if achieved or null otherwise
 * @processDataFile if the application requires a data file, for
 * example TSP needs a set of cities and distances, then this method is used
 * to read the file with the path dataFileName. The dataFileName is null if no data file is required.
 * @convergenceLimit the value used, if required, to determine convergence
 */
interface IslandPopulation {

  List <Individual> population // to hold the list of individuals
  List <Integer> selectParents()
  List <Integer> selectLeastFit()
  /**
   *
   * @param crossoverPoints the number of crossover points in the reproduction algorithm
   * assuming the method is sufficiently flexible
   */
  void reproduce(int crossoverPoints)
  /**
   *
   * @param migrationSize the number of migrants to be selected from the population
   * @return a List of the subscripts of the Individuals in the population that
   * have been selected for migration
   */
  List <Integer> selectMigrants(int migrationSize)
  /**
   *
   * @param migrantIndices the subscripts of the population Individuals to be migrated
   * @return a List containing the selected Individuals
   */
  List <Individual> getMigrants(List <Integer> migrantIndices)
  /**
   *
   * @param incomers a List of Individuals that are immigrating into this population
   * @param migrantIndices the subscripts in the population where the immigrant incomers
   * s are to be located
   */
  void includeImmigrants(List <Individual> incomers, List <Integer> migrantIndices)
  /**
   * @param convergenceLimit the value used to determine if convergence as occurred
   * @return the Individual that has satisfied the convergence criteria or null otherwise
   */
  Individual convergence (BigDecimal convergenceLimit)
  /**
   * bestSolution is used to find the individual that has the best solution once the
   * maximum number of generations has been exceeded.  It does not require knowledge of
   * the convergence criteria as it is based solely on the relative values of Individual.fitness
   * @return the individual that has the best solution within maxGenerations
   */
  Individual bestSolution()
  /**
   * processDataFile used to read content of file with name dataFilename
   * and place them into an List object within population used in the
   * Individual evaluateFitness method
   */
  void processDataFile ()

}