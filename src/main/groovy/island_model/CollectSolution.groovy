package island_model

import jcsp.lang.CSProcess
import jcsp.lang.ChannelInput

class CollectSolution implements CSProcess{

  ChannelInput input
  int instances
  PrintWriter printWriter

  void run(){
    String outString
    for ( i in 0 ..< instances) {
      // initially the CollectSolution process reads the problem specification
      ProblemSpecification spec = input.read() as ProblemSpecification
      long startTime = System.currentTimeMillis()
      // now read result
      List result = input.read()
      long endTime = System.currentTimeMillis()
      outString = "$i, spec, ${spec.toString()} result, "
      if (result == [])
        outString = outString +  " null, , , , , ${endTime - startTime}"
      else {
        // more than one node might find a solution; we want minimum generations
        int minGenerations = spec.maxGenerations + 1
        int minSolution
        List <ConvergedRecord> solutions = result as List <ConvergedRecord>
        for ( s in 0 ..< solutions.size())
          if (solutions[s].generationsTaken < minGenerations){
            minGenerations = solutions[s].generationsTaken
            minSolution = s
          }
        outString = outString +  " ${solutions.size()}, " +
            "${solutions[minSolution].convergedIndividual.getFitness()}, " +
            "${solutions[minSolution].findingNode}, " +
            "${solutions[minSolution].generationsTaken}, " +
            "${solutions[minSolution].seedValue}, ${endTime - startTime} "
      }
      println "$outString"
      printWriter.println(outString)
    }
//    println "CS: terminated"
  }

}
