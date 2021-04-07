package maxOnes

import island_model.Individual
import island_model.Population

class MaxOnePopulation implements Population{
  int individuals
  List <Individual> population
  int geneLength
  double crossoverProbability
  double mutateProbability
  String dataFileName
  Random rng

  MaxOnePopulation (int individuals,
                    int geneLength,
                    double crossoverProbability,
                    double mutateProbability,
                    String dataFileName,
                    Random rng) {
    this.individuals = individuals
    this.rng = rng
    this.geneLength = geneLength
    this.crossoverProbability = crossoverProbability
    this.mutateProbability = mutateProbability
    this.dataFileName = dataFileName
    population = []
    for ( i in 0 ..< individuals){
      population << new MaxOneIndividual(geneLength, rng)
    }
    processDataFile()
  }

  @Override
  List <Integer> selectParents() {
    int best, secondBest
    BigDecimal bestFitness, secondBestFitness
    // initialise the search for the best two parent individuals
    bestFitness = population[0].getFitness()
    best = 0
    secondBestFitness = population[1].getFitness()
    secondBest = 1
    if (secondBestFitness > bestFitness){
      BigDecimal tempFit
      tempFit = secondBestFitness
      secondBestFitness = bestFitness
      bestFitness = tempFit
      int tempIndex
      tempIndex = secondBest
      secondBest = best
      best = tempIndex

    }
    BigDecimal currentFit
    for ( i in 2 ..< individuals){
      if ((currentFit = population[i].getFitness())  > bestFitness) {
        secondBest = best
        secondBestFitness = bestFitness
        best = i
        bestFitness = currentFit
      } else if ((currentFit = population[i].getFitness())  > secondBestFitness){
        secondBest = i
        secondBestFitness = currentFit
      }
    }
    return [best, secondBest]
  }

  @Override
  List <Integer> selectLeastFit() {
    int worst, secondWorst
    BigDecimal worstFitness, secondWorstFitness
    worstFitness = population[0].getFitness()
    worst = 0
    secondWorstFitness = population[1].getFitness()
    secondWorst = 1
    if (secondWorstFitness < worstFitness){
      BigDecimal tempFit
      tempFit = secondWorstFitness
      secondWorstFitness = worstFitness
      worstFitness = tempFit
      int tempIndex
      tempIndex = secondWorst
      secondWorst = worst
      worst = tempIndex

    }
    BigDecimal currentFit
    for ( i in 2 ..< individuals){
      if ((currentFit = population[i].getFitness())  < worstFitness) {
        secondWorst = worst
        secondWorstFitness = worstFitness
        worst = i
        worstFitness = currentFit
      } else if ((currentFit = population[i].getFitness())  < secondWorstFitness){
        secondWorst = i
        secondWorstFitness = currentFit
      }
    }
    return [worst, secondWorst]
  }

  void replace (int popIndex, MaxOneIndividual replacement){
    for ( g in 0 ..< geneLength)
      ((MaxOneIndividual)population[popIndex]).chromosome[g] = replacement.chromosome[g]
    population[popIndex].evaluateFitness()
  }

  @Override
  void reproduce(int crossoverPoints) {
    // select parents and potential individuals the offspring will replace
    // pre-existing individuals replaced iff offspring are a better fit
    // this version uses a single crossover point
    List <Integer> parents = selectParents()
    List <Integer> possibleOverwrites = selectLeastFit()
    // now do the crossover in this case a single point crossover
    int xOverPoint = rng.nextInt(geneLength)
    List <MaxOneIndividual> offspring = []
    for ( i in 0 .. 1) offspring[i] = new MaxOneIndividual(geneLength)
    for (i in 0 ..< xOverPoint)
      offspring[0].chromosome[i] = ((MaxOneIndividual)population[parents[0]]).chromosome[i]
    for (i in xOverPoint ..< geneLength)
      offspring[0].chromosome[i] = ((MaxOneIndividual)population[parents[1]]).chromosome[i]
    for (i in 0 ..< xOverPoint)
      offspring[1].chromosome[i] = ((MaxOneIndividual)population[parents[1]]).chromosome[i]
    for (i in xOverPoint ..< geneLength)
      offspring[1].chromosome[i] = ((MaxOneIndividual)population[parents[0]]).chromosome[i]
    offspring[0].evaluateFitness()
    offspring[1].evaluateFitness()
//    println "Reproducing $parents, $possibleOverwrites, $crossoverPoints, $xOverPoint\n " +
//        "P0: ${population[parents[0]]}\n " +
//        "P1: ${population[parents[1]]}\n   " +
//        "O0: ${offspring[0]}\n   " +
//        "O1: ${offspring[1]}"
    // now do mutations on the offspring
    if (rng.nextDouble() < mutateProbability)
      offspring[0].mutate(rng)
    if (rng.nextDouble() < mutateProbability)
      offspring[1].mutate(rng)
    // order offspring so index 0 is the better
    if (offspring[1].getFitness() > offspring[0].getFitness()) offspring.swap(0,1)
    // possible overwrites are already so ordered
    // now see if fitness of offspring[0] > possibleOverwrites[0]
    // the process will overwrite both possibles if the offspring are both fitter
    BigDecimal overwrite0Fit, overwrite1Fit, offspring0Fit, offspring1Fit
    overwrite0Fit = population[possibleOverwrites[0]].getFitness()
    overwrite1Fit = population[possibleOverwrites[1]].getFitness()
    offspring0Fit = offspring[0].getFitness()
    offspring1Fit = offspring[1].getFitness()
    if (offspring0Fit > overwrite0Fit) {
      // replace overwrite0 with offspring0
      replace(possibleOverwrites[0], offspring[0])
      if (offspring1Fit > overwrite1Fit) {
        //repalce overwrite1 with offspring1
        replace(possibleOverwrites[1], offspring[1])
      }
    }
    else if (offspring0Fit > overwrite1Fit){
      // replace overwrite1 with offspring0 - only one replacement feasible
      replace(possibleOverwrites[1], offspring[0])
    }
  }

  @Override
  List <Integer> selectMigrants(int migrationSize) {
    // just return some of the individuals without any selection
    // assumes more than 14 Individuals in population
    // return [3,5,7,11,13]
    List <Integer> migrants = []
    for ( i in 0 ..< migrationSize){
      int index = rng.nextInt(individuals)
      while (migrants.contains(index)) index = rng.nextInt(individuals)
      migrants << index
    }
//    println "Migrants $migrants"
    return migrants
  }

  List <Individual> getMigrants(List <Integer> migrantIndices){
    List <Individual> migrants
    migrants = []
    for ( m in 0 ..< migrantIndices.size())
      migrants << population[migrantIndices[m]]
    return migrants
  }


  @Override
  void includeImmigrants(List<Individual> incomers, List <Integer> migrantIndices) {
    assert incomers.size() == migrantIndices.size() : "includeImmigrants: Mismatch in sizes of input Lists"
    for ( m in 0 ..< migrantIndices.size())
      population[migrantIndices[m]] = incomers[m]
  }

  @Override
  Individual convergence() {
    int p = 0
    while ((p < individuals) && (population[p].getFitness() < geneLength)) p = p+1
    if ( p < individuals)
      return population[p]  // the individual that has converged
    else
      return null
  }

  @Override
  void processDataFile() {
    if (dataFileName != null){
      println "Processing data file $dataFileName"
      // process the data file into a user defined data structure
    }
  }
}
