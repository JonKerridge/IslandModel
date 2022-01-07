package localityTSP

import locality_model.LocalityIndividual
import locality_model.LocalityPopulation

class LocalityTSPPopulation implements  LocalityPopulation{

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
  @Override
  void reproduce(int parent1, int parent2, int child1, int child2, Random rng) {

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
}
