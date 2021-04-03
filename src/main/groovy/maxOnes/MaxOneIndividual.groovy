package maxOnes

import island_model.Individual

class MaxOneIndividual implements Individual{
  int fitness
  List <Integer> chromosome
  int geneLength

  // used to create an Individual with initialised chromosome
  MaxOneIndividual (int geneLength, Random rng){
    this.geneLength = geneLength
    chromosome = []
    for (g in 0 ..< geneLength)
      chromosome << (Integer) rng.nextInt(2)
    evaluateFitness()
  }

  // used to create an Individual with empty chromosome
  MaxOneIndividual (int geneLength){
    this.geneLength = geneLength
    chromosome = []
    for (g in 0 ..< geneLength)
      chromosome << (Integer) 0
  }

  @Override
  void evaluateFitness() {
    fitness = 0
    for ( g in 0 ..< geneLength) {
      fitness = fitness + chromosome[g]
    }
  }

  @Override
  void mutate(Random rng) {
    int subscript = rng.nextInt(geneLength)
    chromosome[subscript] = 1 - chromosome[subscript]
  }

  @Override
  BigDecimal getFitness() {
    return fitness as BigDecimal
  }

  @Override
  public String toString() {
    return "MaxOneIndividual{" + "fitness=" + fitness +
        ", chromosome=" + chromosome +
        ", rng=" + rng +
        ", geneLength=" + geneLength + '}';
  }
}
