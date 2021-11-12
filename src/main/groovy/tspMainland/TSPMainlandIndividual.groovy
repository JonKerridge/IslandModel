package tspMainland

import mainland_model.MainlandIndividual

class TSPMainlandIndividual implements MainlandIndividual{

  int fitness
  List chromosome
  int geneLength

  TSPMainlandIndividual(int geneLength){
    this.geneLength = geneLength
    chromosome = []
    for ( i in 0 ..< geneLength) chromosome << 0
  }

  /**
   * Any Individual using the Mainland Model Engine uses this interface to specify
   * the methods an Individual class must implement.  The class will have the following properties:<br>
   *
   int fitness
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
  @Override
  void initialise(Random rng) {
    for ( i in 0 ..< geneLength) chromosome[i] = 0
    chromosome[0] = 1
    chromosome[geneLength-1] = 1
    int cities = geneLength -1
    int place = rng.nextInt(cities) + 1   //1.. cities
    for ( i in 1 ..< cities ) {
      while ((place == 1) || (chromosome.contains(place))) place = rng.nextInt(cities) + 1
      chromosome[i] = place
    }
//    println "Initialised chromosome: $chromosome"
  }

  /**
   *
   * calculates the fitness value(s) of an individual
   *
   * @param evaluateData any data used to determine the fitness of an individual
   *
   * if omitted evaluateData will default to null<br>
   */
  @Override
  void evaluateFitness(List evaluateData) {
    fitness = 0
    for ( int i in 1 ..< geneLength){
//      println "EF: $i, ${i-1}, ${chromosome} "
      fitness = fitness + evaluateData[chromosome[i-1]] [chromosome[i]]
    }
//    println "E: $chromosome, has $fitness"
  }

  /**
   * undertakes the mutation operation of this individual
   *
   * @param rng the Random number generator used by the node to which
   * this individual belongs
   */
  @Override
  void mutate(Random rng) {
    int place1 = rng.nextInt(geneLength - 2) + 1  //1..genelength-1
    int place2 = rng.nextInt(geneLength - 2) + 1
    while (place1 == place2) place2 = rng.nextInt(geneLength - 2) + 1
    chromosome.swap(place1, place2)
//    println "swapping $place1, $place2"
  }

  /**
   *  used to obtain the chromosome from an Individual, typically when no
   *  convergence has been found and the best solution so far is required
   *
   * @return the chromosome part of a solution
   */
  @Override
  Object getSolution() {
    return [ chromosome, fitness]
  }

  /**
   * returns the fitness value of an Individual
   * @return the fitness value of an Individual
   */
  @Override
  BigDecimal getFitness() {
    return fitness as BigDecimal
  }
}
