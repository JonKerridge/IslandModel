package mainland_model

import island_model.Individual

interface MainlandPopulation {
  List <Individual> population // to hold the list of population
  /**
   *
   * @param crossoverPoints the number of crossover points in the reproduction algorithm
   * assuming the method is sufficiently flexible
   */
  void reproduce(int crossoverPoints)

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