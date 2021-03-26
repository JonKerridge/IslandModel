package island_model

import com.github.javaparser.Problem
import jcsp.lang.CSProcess
import jcsp.lang.ChannelOutput

/**
 * The process that outputs ProblemSpecification objects on its Output Channel output.  After
 * each instance is written to output the value of the seeds can be altered by calling
 * the method modifySeeds.  The default implementation does not change the values.
 *
 * @param problemSpecification contains the property values that define the problem being processed
 * @output the channel to which the problemSpecification object is written
 */
class EmitProblem implements CSProcess{

  ProblemSpecification problemSpecification
  ChannelOutput output
  int instances

  void run(){
    for ( i in 0 ..< instances) {
      ProblemSpecification ps = problemSpecification.copySpecification()
      ps.instance = i
      output.write(ps)
      problemSpecification.modifySeeds(i+1)
    }
    println "EP: terminating"
  }
}
