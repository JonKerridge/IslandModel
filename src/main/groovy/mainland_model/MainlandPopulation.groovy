package mainland_model

import island_model.Individual

interface MainlandPopulation {
  /**
   * The user is required to write a constructor for any class using this interface.
   */

  List <Individual> population

  /**
   * The parameters are all subscripts of individuals in the population List
   * @param parent1
   * @param parent
   * @param child1
   * @param child2
   * @param candidate1
   * @param candidate2
   * @param rng the random number generator to be used,l specific to each node
   *
   */
  void reproduce(
      int parent1,
      int parent,
      int child1,
      int child2,
      int candidate1,
      int candidate2,
      Random rng)

  /**
   * @param convergenceLimit the value used to determine if convergence as occurred
   * @return the Individual that has satisfied the convergence criteria or null otherwise
   */
  Individual convergence ()

  /**
   * bestSolution is used to find the individual that has the best solution once the
   * maximum number of generations has been exceeded.  It does not require knowledge of
   * the convergence criteria as it is based solely on the relative values of Individual.fitness
   * @return the individual that has the best solution within maxGenerations
   */
  Individual bestSolution()

  /**
   * processDataFile used to read content of file with name dataFilename
   * and place them into the List object evaluateData within population used in the
   * Individual evaluateFitness method
   */
  void processDataFile ()

}