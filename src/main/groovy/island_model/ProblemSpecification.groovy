package island_model

/**
 * The ProblemSpecification class contains a set of properties that must be initialised to
 * the values required by the problem being solved.
 * An object instance is passed as a parameter to the EmitProblem process which then
 * uses the values to set up the processing.
 *
 * @param individualClass the name of the class that implements the Individual interface that
 * defines an individual of the problem
 * @param populationClass the name of the class that implements the problem's population object
 * @param dataFileName the name of a file that holds data required by the application, for example,
 * in a TSP application it would hold the distance matrix
 * @param instance the run number of problem to be sent for solution (modified in EmitProblem)
 * @param populationPerNode the number of individuals maintained by each node
 * @param migrationInterval the number of generation between each migration/immigration phase
 * @param crossoverPoints the number of crossover points to be used in reproduction
 * @param maxGenerations the maximum number of generations before the application is stopped
 * if convergence has not been found
 * @param crossoverProbability the probability of a crossover operation taking place
 * @param mutationProbability the probability of a mutation taking place in a reproduction offspring
 * @param seeds the initial seeds used by each node for their random number generator (one per node)
 *
 *
 */
class ProblemSpecification {
  String individualClass, populationClass, dataFileName
  int instance, populationPerNode, migrationInterval, crossoverPoints, maxGenerations
  double crossoverProbability, mutationProbability
  List <Long> seeds
  /**
   * After each problemSpecification instance is written to the output the values of the
   * seeds are altered using this method.  The value of instance and the seeds are the
   * only properties that changes value.  The default implementation increments each seed
   * value by 1 with each new instance.
   * The value of instance is updated in the EmitProblem process.
   */
  void modifySeeds(int i){
    //seeds.each{long s -> s = s}
    // a more interesting implementation would be to change each seed by a constant amount
    // in this example 1 is added to each seed
    for (s in 0 ..< seeds.size()) seeds[s] = seeds[s] + (i*1)
  }
  /**
   * In several places a deep copy of the specification is required.
   */
  ProblemSpecification copySpecification(){
    ProblemSpecification ps = new ProblemSpecification()
    ps.instance = instance
    ps.individualClass = individualClass
    ps.populationClass = populationClass
    ps.dataFileName = dataFileName
    ps.populationPerNode = populationPerNode
    ps.migrationInterval = migrationInterval
    ps.crossoverPoints = crossoverPoints
    ps.maxGenerations  = maxGenerations
    ps.crossoverProbability = crossoverProbability
    ps.mutationProbability = mutationProbability
    ps.seeds = []
    for ( s in 0 ..< seeds.size())
      ps.seeds[s] = seeds[s]
    return ps
  }


  @Override
  public String toString() {
    return "ProblemSpecification{" + "individualClass='" + individualClass + '\'' + ", populationClass='" + populationClass + '\'' + ", dataFileName='" + dataFileName + '\'' + ", instance=" + instance + ", populationPerNode=" + populationPerNode + ", migrationInterval=" + migrationInterval + ", crossoverPoints=" + crossoverPoints + ", maxGenerations=" + maxGenerations + ", crossoverProbability=" + crossoverProbability + ", mutationProbability=" + mutationProbability + ", seeds=" + seeds + '}';
  }
}
