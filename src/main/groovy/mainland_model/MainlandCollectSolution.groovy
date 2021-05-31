package mainland_model

import island_model.ConvergedRecord
import island_model.Individual
import island_model.TerminationCollection
import jcsp.lang.CSProcess
import jcsp.lang.ChannelInput

class MainlandCollectSolution implements CSProcess{

  ChannelInput input
  int instances
  PrintWriter printWriter


  void run(){
    String outString
    for ( i in 0 ..< instances) {
      // initially the CollectSolution process reads the problem specification
      MainlandProblemSpecification spec = input.read() as MainlandProblemSpecification
      long startTime = System.currentTimeMillis()
      // now read result
      Object result = input.read()
      long endTime = System.currentTimeMillis()
      outString = "$i, spec, ${spec.toString()} result, "
      //TODO need to select the 'best' solution the one with the best Fitness value
      // but this varies with maximise or minimise problem
      if (result instanceof TerminationCollection) {
        // each node has sent its best solution; so need to determine the overall best
        // for this we need to know it it is a minimise of maximise problem
//        println "ICS: ${((TerminationCollection)result).bestRecords.size()} termination records read"
        BigDecimal bestFit
        int bestLocation
        bestLocation = 0
        bestFit = ((Individual)((TerminationCollection)result).bestRecords[bestLocation]).getFitness()
        boolean better
        for ( r in 1 ..< ((TerminationCollection)result).bestRecords.size()) {
          better = spec.minOrMax == "MIN" ?
              ((TerminationCollection)result).bestRecords[r].getFitness() < bestFit
              : ((TerminationCollection)result).bestRecords[r].getFitness() > bestFit
          if (better) {
            bestFit = ((TerminationCollection)result).bestRecords[r].getFitness()
            bestLocation = r
          }
        }

        outString = outString + " NONE, " +
            "${((TerminationCollection)result).bestRecords[bestLocation].fitness}, " +
            "${((Individual)((TerminationCollection)result).bestRecords[bestLocation]).getSolution()} , , ," +
            " ${endTime - startTime}"
      }
      else {
        // solution found but
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
            "${solutions[minSolution].convergedIndividual.getSolution()}, " +
            "${solutions[minSolution].generationsTaken}, " +
            "${solutions[minSolution].seedValue}, " +
            "${endTime - startTime} "
      }
      println "$outString"
      printWriter.println(outString)
    } // instances
//    println "CS: terminated"
    printWriter.flush()
    printWriter.close()
  } // run

}
