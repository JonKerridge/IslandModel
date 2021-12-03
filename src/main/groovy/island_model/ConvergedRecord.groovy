package island_model

class ConvergedRecord {
  IslandIndividual convergedIndividual
  int findingNode
  int generationsTaken
  int instance
  long seedValue

  String toString(){
    String s = "Fitness: ${convergedIndividual.getFitness()}, " +
        "Node: $findingNode, " +
        "Generations: $generationsTaken, " +
        "Instance: $instance, " +
        "Seed: $seedValue"
    return s
  }
}
