package tspMainland

import mainland_model.MainlandIndividual
import mainland_model.MainlandPopulation


class TSPMainlandPopulation implements MainlandPopulation{

  // the properties of the class
  int individuals   // active population plus the space for reproduction offspring
  int geneLength    // the length of each individuals chromosome
  int crossoverPoints // the EVEN number of points used to break the chromosome when doing reproduction
  int maxGenerations  // maximum number of iterations before termination
  int replaceInterval  // iterations required before new individuals created with no change in best fitness
  double crossoverProbability // the probability that reproduction will lead to an acutal crossover operation
  double mutateProbability  // the probability that mutation will occur after crossover
  String dataFileName // the name of any data file used to provide data for the evaluate fitness function
  int bestFitIndex    // subscript in population with the best solution

  // the created objects of the class
  List <TSPMainlandIndividual> population // to hold the list of individuals that form the population
  List evaluateData   // the data structure used to hold the fitness evaluation data if required

  TSPMainlandPopulation(
  int individuals,   // active population plus the space for reproduction offspring
  int geneLength,    // the length of each individuals chromosome
  int crossoverPoints, // the EVEN number of points used to break the chromosome when doing reproduction
  int maxGenerations,  // maximum number of iterations before termination
  int replaceInterval,  // iterations required before new individuals created with no change in best fitness
  double crossoverProbability, // the probability that reproduction will lead to an actual crossover operation
  double mutateProbability,  // the probability that mutation will occur after crossover
  String dataFileName, // the name of any data file used to provide data for the evaluate fitness function
  int bestFitIndex    // subscript in population with the best solution
  ){
    this.individuals = individuals
    this.geneLength = geneLength
    this.crossoverPoints = crossoverPoints
    this.maxGenerations = maxGenerations
    this.replaceInterval = replaceInterval
    this.crossoverProbability = crossoverProbability
    this.mutateProbability = mutateProbability
    this.dataFileName = dataFileName
//    this.convergenceLimit = convergenceLimit
    this.bestFitIndex = bestFitIndex
    population = []
//    processDataFile()
    for (i in 0 ..< individuals){
      population << new TSPMainlandIndividual(geneLength)
    }
  }

  static def extractParts(Integer start, Integer end, TSPMainlandIndividual source){
    // copies source[start ]..< source[end] into result
    List<Integer> result = []
    for ( i in start ..< end) result << source.chromosome[i]
    return result
  }

  static def FindSubscriptsOfBinA(List <Integer> a, List <Integer> b){
    // returns the subscripts in a where elements of b can be found
//    print "\t\tFind $b in $a -> returns "
    List <Integer> subscripts = []
    for (  i in 0 ..< a.size())
      if ( b.contains(a[i])) subscripts << i
//    print "$subscripts"
    return subscripts
  }


  static def doMultiPointCrossover(List <List <Integer>>  partsOf1,
                                   List <List <Integer>>  partsOf2,
                                   TSPMainlandIndividual child,
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
    The final updated version of the individual's board is obtained by flatten()ing
     */
    child.chromosome = [1]
    bitOf1 = 0
    bitOf2 = 0
    while ( bitOf1 < crossoverPoints) {
      child.chromosome << (partsOf1[bitOf1] )
      child.chromosome << (partsOf2[bitOf2] )
      bitOf1 = bitOf1 + 2
      bitOf2++
    }
    child.chromosome << (partsOf1[crossoverPoints] )
    // now add final 1 to return to starting point
    child.chromosome << [1]
    child.chromosome = child.chromosome.flatten() as List<Integer>
//    child.evaluateFitness() // for printing only?
//    println "\nChild $child "
  }

  /**
   * The parameters are all subscripts of individuals in the population List
   * @param parent1
   * @param parent
   * @param child1
   * @param child2
   * @param candidate1
   * @param candidate2
   * @param rng the random number generator to be used,l specific to each node
   *
   */
  @Override
  void reproduce(int parent1, int parent2,
                 int child1, int child2,
                 Random rng) {
    List<Integer> randoms = [1]  // first flexible city is in location 1 of route (chromosome)
    for (n in 1..crossoverPoints) {
      int c = rng.nextInt(geneLength -3) + 1
      while (randoms.contains(c)) c = rng.nextInt(geneLength -3) + 1
      randoms << c
    }
    randoms << geneLength - 1 // last city is always 1 and must be ignored
    randoms = randoms.sort()
//    println "randoms = $randoms, \n${population[parent1].chromosome}, \n${population[parent2].chromosome}"
    // randoms contains a sorted list of random points
    // determine parts to be crossed over
    List<List<Integer>> partsOf1 = []   // all the parts of first parent
    for (i in 0..crossoverPoints) {
      partsOf1[i] = extractParts(randoms[i], randoms[i + 1], population[parent1])
    }
    List<List<Integer>> partsOf2 = []   // odd parts of second parent
    // crossover is between the odd subsections of partsOf1 and each subsection
    // of partsOf2 in turn
    int section = 1
    while (section < crossoverPoints) {
      partsOf2 << extractParts(randoms[section], randoms[section + 1], population[parent2])
      section = section + 2
    }
//    println "parts 0a: $partsOf1, $partsOf2"
    doMultiPointCrossover(partsOf1, partsOf2, population[child1], crossoverPoints)
//    println "parts 0: pnt1= ${population[parent1].chromosome},  p1= $partsOf1, " +
//        "\npnt2= ${population[parent2].chromosome}, p2= $partsOf2, \nc1= ${population[child1].chromosome}"

    // now do it the other way round between the parents and to a different child
    partsOf1 = []
    partsOf2 = []
    for (i in 0..crossoverPoints) {
      partsOf1[i] = extractParts(randoms[i] as int, randoms[i + 1] as int, population[parent2])
    }
    section = 1
    while (section < crossoverPoints) {
      partsOf2 << extractParts(randoms[section] as int, randoms[section + 1] as int, population[parent1])
      section = section + 2   // we take the odd sections for processing
    }
//    println "parts 1a: $partsOf1, $partsOf2"
    doMultiPointCrossover(partsOf1, partsOf2, population[child2], crossoverPoints)
//    println "parts 1: pnt1= ${population[parent1].chromosome},  p1= $partsOf1, " +
//        "\npnt2= ${population[parent2].chromosome}, p2= $partsOf2, \nc1= ${population[child2].chromosome}"
//    println "pnt1= ${population[parent1].chromosome}" +
//        "\npnt2= ${population[parent2].chromosome}" +
//        " \nc1= ${population[child1].chromosome}" +
//    " \nc2= ${population[child2].chromosome}"

    // now do mutations on the offspring
    if (rng.nextDouble() < mutateProbability) {
      population[child1].mutate(rng)
      //println "$nodeID - mutation 1 done"
//      println "mut1 = ${population[child1].chromosome}"
    }

    if (rng.nextDouble() < mutateProbability) {
      population[child2].mutate(rng)
      //println "$nodeID - mutation 2 done"
//      println "mut2 = ${population[child2].chromosome}"
    }
//    population[child1].evaluateFitness(evaluateData)
//    population[child2].evaluateFitness(evaluateData)
  }


    /**
   * @param convergenceLimit the value used to determine if convergence as occurred
   * @return the Individual that has satisfied the convergence criteria or null otherwise
   */
/** replace possibly overwrites the candidate individuals with the children resulting from a crossover operation
 * The parameters are all subscripts of individuals in the population List
 * @param child1
 * @param child2
 * @param candidate1
 * @param candidate2
 * this method may be null if the whole population is sorted
 */
  @Override
  void replaceCandidates(int child1, int child2, int candidate1, int candidate2) {
    int child1Fit = population[child1].getFitness()
    int child2Fit = population[child2].getFitness()
    int candidate2Fit = population[candidate2].getFitness()
    // assume candidate2Fit is at least as good as candidate1Fit and is probably better
    // due to the ordering of the candidates in the population
    // we do not know the ordering of child fitness; it is a minimise problem
    if ( child1Fit < child2Fit){
      if (child2Fit < candidate2Fit){
        // both candidates can be replaced by the children
        // the ordering does not matter as they will be sorted on return to Root
        population.swap(child1, candidate1)
        population.swap(child2, candidate2)
      } else {
        // only child1 can overwrite candidate1
        population.swap(child1, candidate1)
      }
    } else { //child2 is the better
      if (child1Fit < candidate2Fit){
        // both candidates can be replaced by the children
        // the ordering does not matter as they will be sorted on return to Root
        population.swap(child1, candidate1)
        population.swap(child2, candidate2)
      } else {
        // only child2 can overwrite candidate1
        population.swap(child2, candidate1)
      }
    }
  }

  @Override
  MainlandIndividual convergence(def convergenceLimit) {
    if (population[bestFitIndex].fitness < convergenceLimit)
      return population[bestFitIndex]
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
  MainlandIndividual bestSolution() {
    return population[bestFitIndex]
  }

  /**
   * processDataFile used to read content of file with name dataFilename
   * and place them into the List object evaluateData within population used in the
   * Individual evaluateFitness method*/
  @Override
  List processDataFile(String dataFileName) {
//    println "process $dataFileName"
    List evaluateData = []
    evaluateData[0] = [0]  // no city with subscript 0
    int row
    row = 1
    new File(dataFileName).eachLine {line ->
//      println "$line"
      evaluateData[row]= [0]
      List <String> values = line.tokenize(',')
      for ( v in 0 ..< values.size()) evaluateData[row][v+1] = Integer.parseInt(values[v])
      row += 1
    }
    int rows = evaluateData.size()
    for ( r in 1 ..< rows)
      for ( rc in r+1 ..< rows)
        evaluateData[r][rc] = evaluateData[rc][r]

//    println "\nSquare : $rows\n"
//    evaluateData.each{println "$it"}
    return evaluateData
  }
}
