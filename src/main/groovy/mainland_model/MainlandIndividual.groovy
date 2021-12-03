package mainland_model

/**
 * Any Individual using the Mainland Model Engine uses this interface to specify
 * the methods an Individual class must implement.
 *
 * A constructor that creates an empty individual is required, where genelength is the number
 * of elements in a chromosome which should be a list that is set to the empty List<br>
 * Individual (int geneLength)<br>
 *
 *
 */
interface MainlandIndividual {

  /**
   * Any Individual using the Mainland Model Engine uses this interface to specify
   * the methods an Individual class must implement.  The class will have the following properties:<br>
   *
   def fitness    // this can be any comparable type
   List chromosome
   int geneLength

   * A constructor that creates an empty individual is required, where genelength is the number
   * of elements in a chromosome which should be a list that is set to the empty List<br>
   *
   * ExampleIndividual (int geneLength)<br>
   *
   * fitness is the current value of the fitness function applied to this individual
   * chromosome is the set of values that make up the individuals data points
   * geneLength is the number of elements in the chromosome
   * replacements is filled when a solution is foun and contains the number fo times a replace operation was carried out
   *
   * The initialise method is used to create the values in each individual in the population

   * initialise property values in an individual.
   * @param rng a random number generator passed from a node; each node has a different rng
   */
  void initialise(Random rng)

  /**
   *
   * calculates the fitness value(s) of an individual
   *
   * @param evaluateData contains any data required to calculate the fitness function and is null if not required
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
  Object getFitness()

  /**
   * It is recommended that a String toString() method is supplied
   */
}