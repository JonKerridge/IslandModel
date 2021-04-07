package island_model

/**
 * Defines the Population of Individuals at each node
 * The comments give and indication of the variables that will be required
 * The user is required to create a public constructor
 *
 * @param population  a List of Individuals
 * @param selectParents identifies the fittest and possibly second fittest
 * Individuals and returns their subscripts in a List
 * @param selectLeastFit identifies the least fit Individuals and returns
 * their subscripts in a List
 * @param reproduce undertakes the crossover operation using the indices in parents as
 * the source Individuals and writes the children created to the indices in offspring
 * @param selectMigrants returns a list of subscripts of Individuals selected for migration
 * @param getMigrants returns a List of Individuals for the specified migrant indices
 * @param includeImmigrants overwrites the Individuals in migrantIndices with the Individuals
 * contained in incomers
 * @param convergence returns an Individual that satisfies the
 * model's convergence criteria if achieved or null otherwise
 * @param processDataFile, if the application requires a data file, for
 * example TSP needs a set of cities and distances, then this method is used
 * to read the file with the path dataFileName.
 * dataFileName is null if no data file is required.
 */
interface Population {
//  int best, secondBest, worst, secondWorse
//  List <Individual> population
//  List <Integer> migrantSubscripts
//  Individual child1, child2
  List <Individual> population // to hold the list of individuals
  List <Integer> selectParents()
  List <Integer> selectLeastFit()
  void reproduce(int crossoverPoints)
  List <Integer> selectMigrants(int migrationSize)
  List <Individual> getMigrants(List <Integer> migrantIndices)
  void includeImmigrants(List <Individual> incomers, List <Integer> migrantIndices)
  Individual convergence ()
  void processDataFile ()
}