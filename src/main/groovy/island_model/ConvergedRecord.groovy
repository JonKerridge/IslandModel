package island_model

class ConvergedRecord {
  Individual convergedIndividual
  int findingNode
  int generationsTaken
  int instance
  double seedValue

  String toString(){
    String s = "Fitness: ${convergedIndividual.getFitness()}, " +
        "Node: $findingNode, " +
        "Generations: $generationsTaken, " +
        "Instance: $instance, " +
        "Seed: $seedValue"
    return s
  }
}
