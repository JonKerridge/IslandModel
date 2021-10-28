package island_model

/**
 * Any Individual using the tspIsland or Mainland Model Engine uses this interface to specify
 * the methods an Individual class must implement.
 *
 * The user is required to create TWO public constructors.<br>
 * One which actually populates an individual<br>
 * Individual ( int geneLength, Random rng, List evaluateData)<br>
 *
 * The other creates an empty individual<br>
 * Individual (int geneLength, List evaluateData)<br>
 *
 * if omitted evaluateData will default to null<br>
 *
 * where <br>
 * geneLength is the number of data points in a chromosome<br>
 * rng is the random number generator used by the node to which this Individual belongs <br>
 * evaluateData is any data used in the evaluation of the fitness function or null otherwise<br>
 */
interface IslandIndividual {

  /**
   *
   * calculates the fitness value(s) of an individual
   *
   * @param evaluateData any data used to determine the fitness of an individual
   */
  void evaluateFitness(List evaluateData)
  /**
   * undertakes the mutation operation of this individual
   *
   * @param rng the Random number generator used by the node to which
   * this individual belongs
   */
  void mutate(Random rng)

  /**
   *  used to obtain the chromosome from an Individual, typically when no
   *  convergence has been found and the best solution so far is required
   *
   * @return the chromosome part of a solution
   */
  Object getSolution ()

  /**
   * returns the fitness value of an Individual
   * @return the fitness value of an Individual
   */
  BigDecimal getFitness()
}