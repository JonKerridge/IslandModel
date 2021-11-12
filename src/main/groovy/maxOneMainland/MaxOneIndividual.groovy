package maxOneMainland

import mainland_model.MainlandIndividual

class MaxOneIndividual implements MainlandIndividual{

    int fitness
    List <Integer> chromosome
    int geneLength

    // used to create an Individual with initialised chromosome
    void initialise (Random rng){
        for (g in 0 ..< geneLength)
            chromosome << (Integer) rng.nextInt(2)
        evaluateFitness(null)
    }

    // used to create an Individual with empty chromosome
    MaxOneIndividual (int geneLength){
        this.geneLength = geneLength
        chromosome = []
    }

    @Override
    void evaluateFitness(List evaluateData) {
        fitness = 0
        for ( g in 0 ..< geneLength) {
            fitness = fitness + chromosome[g]
        }
    }

    @Override
    void mutate(Random rng) {
        int subscript = rng.nextInt(geneLength)
        chromosome[subscript] = 1 - chromosome[subscript]
    }

    @Override
    Object getSolution() {
        return chromosome
    }

    @Override
    BigDecimal getFitness() {
        return fitness as BigDecimal
    }

    @Override
    public String toString() {
//        return "MaxOneIndividual{" + "fitness=" + fitness +
//            ", replacements=" + replacements +
////            ", chromosome=" + chromosome +
//            ", geneLength=" + geneLength + '}';
        return "$fitness, "
    }
}
