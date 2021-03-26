package island_model_test

import island_model.ProblemSpecification
import org.junit.Test

import static org.junit.Assert.assertTrue

class Test_modifySeedsPlus1 {
// this test requires that modifySeeds adds 1 to each seed value
  @Test
  void test() {
    def ps = new ProblemSpecification(seeds: [1,2,3])
    ps.modifySeeds(1)
    println "seeds = ${ps.seeds}"
    assertTrue (ps.seeds == [2,3,4])
  }
}
