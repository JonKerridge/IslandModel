package mainland_model

import jcsp.lang.CSProcess
import jcsp.lang.ChannelOutput

/**
 * The process that outputs ProblemSpecification objects on its Output Channel output.  After
 * each instance is written to output the value of the seeds can be altered by calling
 * the method modifySeeds.  The default implementation adds 1 to each seed value after each
 * specification is written to output.
 *
 * @param problemSpecification contains the property values that define the problem being processed
 * @param output the channel to which the problemSpecification object is written
 */
class MainlandEmitProblem implements CSProcess{

  MainlandProblemSpecification problemSpecification
  ChannelOutput output
  int instances

  void run(){
    boolean doSeedModify = problemSpecification.doSeedModify
    for ( i in 0 ..< instances) {
//      println "Emit sending instance $i"
      MainlandProblemSpecification ps = problemSpecification.copySpecification()
      ps.instance = i
      output.write(ps)
      problemSpecification.modifySeeds(doSeedModify)
    }
//    println "EP: terminating"
  }
}