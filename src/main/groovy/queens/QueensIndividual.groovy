package queens

import island_model.Individual

// based on http://citeseerx.ist.psu.edu/viewdoc/download?doi=10.1.1.129.720&rep=rep1&type=pdf

class QueensIndividual implements Individual{
  double fitness
  List <Integer> chromosome = []   // holds the board chromosome[0] is NOT used; set to null
  int geneLength              // number of Queens

  // constructors
  QueensIndividual (int queens, Random rng){
    geneLength = queens
    chromosome = [null]
    for (g in 1 .. geneLength) chromosome << g
    // now swap queens to mix up initial locations
    for (q1 in 1 .. geneLength) {
      int q2 = rng.nextInt(queens) + 1
      chromosome.swap(q1, q2)
    }
    evaluateFitness()
  }

  QueensIndividual(int queens){
    geneLength = queens
    chromosome = [null]
    for (g in 1 ..< geneLength) chromosome << 0
  }
  @Override
  void evaluateFitness() {
//    println "EF: $chromosome"
    // loops start at 1 because chromosome[0] is null
    List <Integer> leftDiagonal = []
    List <Integer> rightDiagonal = []
    double sum = 0.0D

    for ( i in 1 .. 2*geneLength) {
      leftDiagonal[i] = 0
      rightDiagonal[i] = 0
    }
    for (int i in 1 .. geneLength) {
      int idxL = i + chromosome[i] - 1
//      println "idxl = $idxL"
      leftDiagonal[idxL]++
      int idxR = geneLength - i + chromosome[i]
      rightDiagonal[idxR]++
//            rightDiagonal[N-i+board[i]]++
    }
    for ( i in 1 .. ((2*geneLength) - 1)) {
      int counter = 0
      if ( leftDiagonal[i] > 1)
        counter += leftDiagonal[i] - 1
      if ( rightDiagonal[i] > 1)
        counter += rightDiagonal[i] - 1
      sum += counter / (geneLength - Math.abs(i-geneLength))
    }
    // target fitness is 0.0
    // sum can be negative so return absolute value
    fitness = Math.abs(sum)
//    println "Fit = $fitness"
  }

  @Override
  void mutate(Random rng) {
    // only move ONE queen first element is null so add 1
    int column1 = rng.nextInt(geneLength) + 1
    int column2 = rng.nextInt(geneLength) + 1
    while (column1 == column2) column2 = rng.nextInt(geneLength) + 1
//    println "mutate: $column1, $column2"
    chromosome.swap(column1, column2)
  }

  @Override
  BigDecimal getFitness() {
    return fitness as BigDecimal
  }

  String toString(){
    return "Individual length: $geneLength, Fit: $fitness, $chromosome"
  }
}
