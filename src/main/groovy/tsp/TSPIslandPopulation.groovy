package tsp

import island_model.Individual
import island_model.IslandPopulation


class TSPIslandPopulation implements IslandPopulation{
  List <TSPindividual> population
  int individuals
  int geneLength
  double crossoverProbability
  double mutateProbability
  String dataFileName
  BigDecimal convergenceLimit
  List <List <Integer>> distances
  Random rng
  int nodeID, instance

  TSPIslandPopulation(int individuals,
                      int geneLength,
                      double crossoverProbability,
                      double mutateProbability,
                      String dataFileName,
                      BigDecimal convergenceLimit,
                      Random rng,
                      int nodeId){
    this.individuals = individuals
    this.rng = rng
    this.geneLength = geneLength
    this.crossoverProbability = crossoverProbability
    this.mutateProbability = mutateProbability
    this.dataFileName = dataFileName
    this.convergenceLimit = convergenceLimit
    population = []
    this.nodeID = nodeId
    processDataFile() // must be done first so that it can bu used to evaluate individual fitness
    for ( i in 0 ..< individuals){
      population << new TSPindividual(geneLength, rng, distances)
    }

  }

  @Override
  List<Integer> selectParents() {
    int best, secondBest
    BigDecimal bestFitness, secondBestFitness
    // initialise the search for the best two parent individuals
    // this is a minimisation problem
    bestFitness = population[0].getFitness()
    best = 0
    secondBestFitness = population[1].getFitness()
    secondBest = 1
    if (secondBestFitness < bestFitness){
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
      if ((currentFit = population[i].getFitness())  < bestFitness) {
        secondBest = best
        secondBestFitness = bestFitness
        best = i
        bestFitness = currentFit
      } else if ((currentFit = population[i].getFitness())  < secondBestFitness){
        secondBest = i
        secondBestFitness = currentFit
      }
    }
    return [best, secondBest]
  }

  @Override
  List<Integer> selectLeastFit() {
    int worst, secondWorst
    BigDecimal worstFitness, secondWorstFitness
    worstFitness = population[0].getFitness()
    worst = 0
    secondWorstFitness = population[1].getFitness()
    secondWorst = 1
    if (secondWorstFitness > worstFitness){
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
      if ((currentFit = population[i].getFitness())  > worstFitness) {
        secondWorst = worst
        secondWorstFitness = worstFitness
        worst = i
        worstFitness = currentFit
      } else if ((currentFit = population[i].getFitness())  > secondWorstFitness){
        secondWorst = i
        secondWorstFitness = currentFit
      }
    }
    return [worst, secondWorst]
  }

  // methods used in the reproduce process
  static def FindSubscriptsOfBinA(List <Integer> a, List <Integer> b){
    // returns the subscripts in a where elements of b can be found
//    print "\t\tFind $b in $a -> returns "
    List <Integer> subscripts = []
    for (  i in 0 ..< a.size())
      if ( b.contains(a[i])) subscripts << i
//    print "$subscripts"
    return subscripts
  }

  static def extractParts(Integer start, Integer end, TSPindividual source){
    // copies source[start ]..< source[end] into result
    List<Integer> result = []
    for ( i in start ..< end) result << source.route[i]
    return result
  }

  List <TSPindividual> offspring

  static def doMultiPointCrossover(List <List <Integer>>  partsOf1,
                                   List <List <Integer>>  partsOf2,
                                   TSPindividual child,
                                   int crossoverPoints){
    /*
    the number of crossover Points is even
    The number of subsections in partsOf1 is points + 1
    there are points sections in partsOf2
    p1Values holds the sum of the odd subsections of partsOf1
    p2Values holds the sum of the even sections of partsOf2
    The even numbered subsections of partsOf1 will be those
    that are involved in the crossover operation
     */
    List <Integer> p1Values, p2Values, reallocate, common, searchSet, subscripts
    p1Values = []
    p2Values = []
    int bitOf1, bitOf2
    bitOf1 = 1
    bitOf2 = 0
//    println "\n\ndoMPC\n parts1= $partsOf1\nparts2 = $partsOf2\n"
    while (bitOf1 < crossoverPoints) {
      p1Values = p1Values + partsOf1[bitOf1]
      bitOf1 = bitOf1 + 2
    }
    while ( bitOf2 < partsOf2.size()) {
      p2Values = p2Values + partsOf2[bitOf2]
      bitOf2++
    }
//    println "p1V= $p1Values, p2V=$p2Values"
    /*
    reallocate is the value(s) that need to be reassigned in the result
    common is those value common to both p1Values and p2Values
    searchSet are those values that need to be replaced by
    members of reallocate.
    each even subsection of partsOf1 is searched to find the subscripts of any elements in both sets
    any values found in the subsection of partsO1 can be replaced by taking a value from reallocate
     */
    reallocate = p1Values - p2Values
    common = p1Values.intersect(p2Values)
    searchSet = p2Values - common
//    println "reallocate = $reallocate, common = $common, searchSet= $searchSet"
    assert reallocate.size() == searchSet.size(): "Set sizes in dMPC not equal"
    bitOf1 = 0
    while ( bitOf1 < partsOf1.size()) {
      subscripts = FindSubscriptsOfBinA(partsOf1[bitOf1], searchSet)
      subscripts.each{s -> partsOf1[bitOf1][s] = reallocate.pop()}
      bitOf1 = bitOf1 + 2
    }
    /* now rebuild the replacement child by appending the now modified even subsections of partsOf1
    and the unaltered subsections of partsOf2 in sequence.
    The parts are appended to a 1 value as the zeroth element of a route is always 1
    The final updated version of the individual's route is obtained by flatten()ing
     */
    child.route = [1]
    bitOf1 = 0
    bitOf2 = 0
    while ( bitOf1 < crossoverPoints) {
      child.route << (partsOf1[bitOf1] )
      child.route << (partsOf2[bitOf2] )
      bitOf1 = bitOf1 + 2
      bitOf2++
    }
    child.route << (partsOf1[crossoverPoints] )
    child.route << 1  // last city is always 1
    child.route = child.route.flatten() as List<Integer>
//    child.evaluateFitness() // for printing only?
//    println "\nChild $child "
  }

  void replace (int popIndex, TSPindividual replacement){
    for ( g in 0 .. geneLength)
      ((TSPindividual)population[popIndex]).route[g] = replacement.route[g]
    ((TSPindividual)population[popIndex]).fitness = replacement.fitness
  }


  /**
   *
   * @param crossoverPoints the number of crossover points in the reproduction algorithm
   * assuming the method is sufficiently flexible
   */
  @Override
  void reproduce(int crossoverPoints) { // crossoverPoints must be even
    assert crossoverPoints % 2 == 0 : "Reproduce: crossover points must be even = $crossoverPoints"
    List <Integer> parents = selectParents()
    List <Integer> possibleOverwrites = selectLeastFit()
    // must ensure crossover points are not 0 and cities, which are both fixed at 1
    List <Integer> randoms = [1]  // first flexible city is in route[1]
    for (n in 1 .. crossoverPoints ){
      int c = rng.nextInt(geneLength-1) + 1
      while ( randoms.contains(c)) c = rng.nextInt(geneLength-1) + 1
      randoms << c
    }
    randoms = randoms.sort()
    randoms << geneLength - 1// last city is always 1 and must be ignored
//    println "random points is $randoms"
    // randoms contains a sorted list of random points
    //offspring holds the results of the reproduction
    offspring = []
    for ( i in 0 .. 1) offspring[i] = new TSPindividual(geneLength, distances)
    List <List <Integer>> partsOf1 = []   // all the parts of first parent
    for ( i in 0 .. crossoverPoints){
      partsOf1[i] = extractParts(randoms[i], randoms[i+1], population[parents[0]])
    }
    List <List <Integer>> partsOf2 = []   // odd parts of second parent
    // crossover is between the odd subsections of partsOf1 and each subsection
    // of partsOf2 in turn
    int section = 1
    while (section < crossoverPoints) {
      partsOf2 << extractParts(randoms[section] , randoms[section+1], population[parents[1]])
      section = section + 2
    }
//    println "parts 0: $partsOf1, $partsOf2"
    doMultiPointCrossover(partsOf1, partsOf2, offspring[0], crossoverPoints)

    // now do it the other way round between the parents and to a different child
    partsOf1 = []
    partsOf2 = []
    for ( i in 0 .. crossoverPoints){
      partsOf1[i] = extractParts(randoms[i] as int, randoms[i+1] as int, population[parents[1]])
    }
    section = 1
    while (section < crossoverPoints) {
      partsOf2 << extractParts(randoms[section] as int, randoms[section+1] as int, population[parents[0]])
      section = section + 2   // we take the odd sections for processing
    }
//    println "parts 1: $partsOf1, $partsOf2"
    doMultiPointCrossover(partsOf1, partsOf2, offspring[1],crossoverPoints)
    //println "$nodeID - undertaken both crossovers"
    // now do mutations on the offspring
    if (rng.nextDouble() < mutateProbability) {
      offspring[0].mutate(rng)
      //println "$nodeID - mutation 1 done"
    }
    if (rng.nextDouble() < mutateProbability) {
      offspring[1].mutate(rng)
      //println "$nodeID - mutation 2 done"
    }
    offspring[0].evaluateFitness(distances)
    offspring[1].evaluateFitness(distances)

    // order offspring so index 0 is the better
    if (offspring[1].getFitness() < offspring[0].getFitness()) offspring.swap(0,1)
    //println "$nodeID - undertaken any mutations and swapping Fit0: ${offspring[0].getFitness()}, Fit1: ${offspring[1].getFitness()}"
//    println "after mutation and reordering:\n offspring[0] = ${offspring[0]}\n offspring[1] = ${offspring[1]}"
    // possible overwrites are already so ordered
    // now see if fitness of offspring[0] < possibleOverwrites[0]
    // the process will overwrite both possibles if the offspring are both fitter
    BigDecimal overwrite0Fit, overwrite1Fit, offspring0Fit, offspring1Fit
    overwrite0Fit = population[possibleOverwrites[0]].getFitness()
    overwrite1Fit = population[possibleOverwrites[1]].getFitness()
    offspring0Fit = offspring[0].getFitness()
    offspring1Fit = offspring[1].getFitness()
//    println "$nodeID - offspring\n\t0 - ${offspring[0].toString()}\n\t1 - ${offspring[1].toString()}"
    if (offspring0Fit < overwrite0Fit) {
      // replace overwrite0 with offspring0
      replace(possibleOverwrites[0], offspring[0])
      //println "$nodeID - overwrite 0: undertaken"
      if (offspring1Fit < overwrite1Fit) {
        //repalce overwrite1 with offspring1
        replace(possibleOverwrites[1], offspring[1])
        //println "$nodeID - overwrite 1: undertaken"

      }
    }
    else if (offspring0Fit < overwrite1Fit){
      // replace overwrite1 with offspring0 - only one replacement feasible
      replace(possibleOverwrites[1], offspring[0])
      //println "$nodeID - overwrite 2: undertaken"
    }
    else {
      //println "$nodeID - no overwrites possible"
    }
  }

  /**
   *
   * @param migrationSize the number of migrants to be selected from the population
   * @return a List of the subscripts of the Individuals in the population that
   * have been selected for migration
   */
  @Override
  List<Integer> selectMigrants(int migrationSize) {
    // return some randomly chosen individuals without any selection
    List <Integer> migrants = []
    for ( i in 0 ..< migrationSize){
      int index = rng.nextInt(individuals)
      while (migrants.contains(index)) index = rng.nextInt(individuals)
      migrants << index
    }
    return migrants
  }

  /**
   *
   * @param migrantIndices the subscripts of the population Individuals to be migrated
   * @return a List containing the selected Individuals
   */
  @Override
  List<Individual> getMigrants(List<Integer> migrantIndices) {
    List <Individual> migrants
    migrants = []
    for ( m in 0 ..< migrantIndices.size())
      migrants << population[migrantIndices[m]]
    return migrants
  }

  /**
   *
   * @param incomers a List of Individuals that are immigrating into this population
   * @param migrantIndices the subscripts in the population where the immigrant incomers
   * s are to be located
   */
  @Override
  void includeImmigrants(List<Individual> incomers, List<Integer> migrantIndices) {
    assert incomers.size() == migrantIndices.size() : "includeImmigrants: Mismatch in sizes of input Lists"
    for ( m in 0 ..< migrantIndices.size())
      population[migrantIndices[m]] = incomers[m] as TSPindividual
  }

  /**
   *
   * @return the Individual that has satisfied the convergence criteria or null otherwise
   */
  @Override
  Individual convergence(BigDecimal convergenceLimit) {
    int p = 0
    while ((p < individuals) && (population[p].getFitness() > convergenceLimit)) p = p+1
    if ( p < individuals)
      return population[p]  // the individual that has converged
    else
      return null
  }

  /**
   * bestSolution is used to find the individual that has the best solution once the
   * maximum number of generations has been exceeded.  It does not require knowledge of
   * the convergence criteria as it is based solely on the relative values of Individual.fitness
   * @return the individual that has the best solution within maxGenerations
   */
  @Override
  Individual bestSolution() {
    int bestLocation = 0
    BigDecimal bestFit = population[bestLocation].getFitness()
    for ( i in 1 ..< individuals){
      if (population[i].getFitness() < bestFit){
        bestFit = population[i].getFitness()
        bestLocation = i
      }
    }
    return population[bestLocation]
  }

  @Override
  void processDataFile() {
//    println "process $dataFileName"
    distances = []
    distances[0] = [0]  // no city with subscript 0
    int row
    row = 1
    new File(dataFileName).eachLine {line ->
//      println "$line"
      distances[row]= [0]
      List <String> values = line.tokenize(',')
      for ( v in 0 ..< values.size()) distances[row][v+1] = Integer.parseInt(values[v])
      row += 1
    }
    int rows = distances.size()
    for ( r in 1 ..< rows)
      for ( rc in r+1 ..< rows)
        distances[r][rc] = distances[rc][r]

//    println "\nSquare : $rows\n"
//    distances.each{println "$it"}

  }
}
