package island_model

/**
 * The ProblemSpecification class contains a set of properties that must be initialised to
 * the values required by the problem being solved.
 * An object instance is passed as a parameter to the EmitProblem process which then
 * uses the values to set up the processing.
 * EmitProblem uses the specification to create a number of specifications, each with a different
 * instance value. This value will be checked internally to ensure consistency.
 * The public constructor for the population class must initialise all the properties
 *
 * @param populationClass the name of the class that implements the problem's population object
 * @param dataFileName the name of a file that holds data required by the application, for example,
 * in a TSP application it would hold the distance matrix, null if not required
 * @param instance the run number of the problem to be sent for solution (modified in EmitProblem)
 * @param geneLength the number of genes in a chromosome
 * @param populationPerNode the number of population maintained by each node
 * @param migrationInterval the number of generations between each migration/immigration phase
 * @param migrationSize the number of population to be chosen during migration phase
 * @param crossoverPoints the number of crossover points to be used in reproduction
 * @param maxGenerations the maximum number of generations before the application is stopped
 * if convergence has not been found
 * @param crossoverProbability the probability of a crossover operation taking place
 * @param mutationProbability the probability of a mutation taking place in a reproduction offspring
 * @param convergenceLimit the value used to determine convergence
 * @param minOrMax MIN = a minimisation or MAX = a maximisation problem
 * @param seeds the initial seeds used by each node for their random number generator (one per node)
 * @param doSeedModify controls the modification of seeds for each instance
 *
 * @param nodes the number of nodes
 * @param instances the number of instances
 * these last two are for documentation only
 *
 */
class IslandProblemSpecification {
  String populationClass, dataFileName, minOrMax
  int instance, geneLength, populationPerNode
  int migrationInterval, migrationSize, crossoverPoints, maxGenerations
  double crossoverProbability, mutationProbability
  int nodes, instances
  boolean doSeedModify
  BigDecimal convergenceLimit
  List <Long> seeds

  /**
   * After each ProblemSpecification instance is written to the output the values of the
   * seeds are altered using this method.  The value of instance and the seeds are the
   * only properties that changes value.  The default implementation increments each seed
   * value by 2 with each new instance. The value of instance is updated in the EmitProblem process,
   * provided doSeedModify is true.  The default implementation adds 2 to each seed.
   *
   * @param doSeedModify the seed modification is undertaken only if true
   */
  void modifySeeds(boolean doSeedModify){
    if (doSeedModify)
      for (s in 0 ..< seeds.size()) seeds[s] = seeds[s] + 2
  }
  /**
   * In several places a deep copy of the specification is required.
   */
  IslandProblemSpecification copySpecification(){
    IslandProblemSpecification ps = new IslandProblemSpecification()
    ps.instance = instance
    ps.populationClass = populationClass
    ps.dataFileName = dataFileName
    ps.populationPerNode = populationPerNode
    ps.geneLength = geneLength
    ps.migrationInterval = migrationInterval
    ps.migrationSize = migrationSize
    ps.crossoverPoints = crossoverPoints
    ps.maxGenerations  = maxGenerations
    ps.crossoverProbability = crossoverProbability
    ps.mutationProbability = mutationProbability
    ps.minOrMax = minOrMax
    ps.nodes = nodes
    ps.instances = instances
    ps.convergenceLimit = convergenceLimit
    ps.seeds = []
    for ( s in 0 ..< seeds.size())
      ps.seeds[s] = seeds[s]
    return ps
  }


  @Override
//  public String toString() {
//    return "ProblemSpecification{" +
//        "\n nodes=" + nodes +
//        ",\n instances=" + instances +
//        ",\n populationClass='" + populationClass + '\'' +
//        ",\n dataFileName='" + dataFileName + '\'' +
//        ",\n instance=" + instance +
//        ",\n populationPerNode=" + populationPerNode +
//        ",\n geneLength=" + geneLength +
//        ",\n migrationInterval=" + migrationInterval +
//        ",\n crossoverPoints=" + crossoverPoints +
//        ",\n maxGenerations=" + maxGenerations +
//        ",\n crossoverProbability=" + crossoverProbability +
//        ",\n mutationProbability=" + mutationProbability +
//        ",\n seeds=" + seeds + '}';
//  }

  @Override
  String toString() {
//    return "$nodes, $instances, $populationPerNode, " +
//        "$geneLength, $migrationInterval, $migrationSize, $crossoverPoints, " +
//        "$maxGenerations, $crossoverProbability, $mutationProbability, $convergenceLimit, $minOrMax, "
    return "$nodes, $populationPerNode, $geneLength, " +
        "$migrationInterval, $migrationSize, " +
        "$crossoverPoints, $crossoverProbability, $mutationProbability"
  }
}
