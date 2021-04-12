package island_model

/**
 * Any Individual using the IslandModel Engine uses this interface to specify
 * the methods an Individual class must implement.
 *
 * The user is required to create a public constructor.
 *
 * @evaluateFitness updates the BigDecimal fitness value(s) of this individual
 * @mutate undertakes a mutation operation on this individual
 * @getFitness returns the individual's fitness value as a BigDecimal
 */
interface Individual {
  void evaluateFitness()
  /**
   *
   * @param rng the Random number generator used by the IslandNode to which
   * this individual belongs
   */
  void mutate(Random rng)
  BigDecimal getFitness()
}