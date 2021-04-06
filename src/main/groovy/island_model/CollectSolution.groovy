package island_model

import jcsp.lang.CSProcess
import jcsp.lang.ChannelInput

class CollectSolution implements CSProcess{

  ChannelInput input
  int instances

  void run(){
    // initially the CollectSolution process reads the problem specification
    for ( i in 0 ..< instances) {
      ProblemSpecification spec = input.read() as ProblemSpecification
      long startTime = System.currentTimeMillis()
      print "$i, , ${spec.toString()}"
      // now read result
      List result = input.read()
      long endTime = System.currentTimeMillis()
      if (result == [])
        print " null, , , , ${endTime - startTime} \n"
      else {
        // more than one node could find a solution; we want minimum generations
        int minGenerations = spec.maxGenerations + 1
        int minSolution
        List <ConvergedRecord> solutions = result as List <ConvergedRecord>
        for ( s in 0 ..< solutions.size())
          if (solutions[s].generationsTaken < minGenerations){
            minGenerations = solutions[s].generationsTaken
            minSolution = s
          }
        print " ${solutions.size()}, " +
            "${solutions[minSolution].findingNode}, " +
            "${solutions[minSolution].generationsTaken}, " +
            "${solutions[minSolution].seedValue}, ${endTime - startTime} \n"
      }
    }
//    println "CS: terminated"
  }

}
