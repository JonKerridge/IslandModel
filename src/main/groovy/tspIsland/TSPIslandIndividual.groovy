package tspIsland

import island_model.IslandIndividual

class TSPIslandIndividual implements IslandIndividual{
  int fitness
  List <Integer> route
  int cities
  List <List <Integer>> distances

  TSPIslandIndividual(int geneLength, Random rng,
                      List<List<Integer>> distances){
    this.distances = distances
    this.cities = distances.size() -1 // the first row is just [0]
    route = []
    route[0] = 1   // cities are subscripted from 1 upwards
    route[cities] = 1   // last city is always 1
    int place = rng.nextInt(cities) + 1   //1.. cities
    for ( i in 1 ..< cities ) {
      while ((place == 1) || (route.contains(place))) place = rng.nextInt(cities) + 1
      route[i] = place
    }
    evaluateFitness(distances)
//    println "Route: $route, $cities $fitness"

  }

  TSPIslandIndividual(int cities,
                      List<List<Integer>> distances ){
    this.distances = distances
    this.cities = distances.size() -1
    route = []
    route[0] = 1   // cities are subscripted from 1 upwards to cities-1
    route[cities] = 1
    for ( i in 1 ..< cities ) route[i] = 0  // just initialise, will be overwritten
  }

  @Override
  void evaluateFitness(List evaluateData) {
    fitness = 0
    for ( int i in 1 .. cities){
      fitness = fitness + distances[route[i-1]] [route[i]]
    }
  }

  @Override
  void mutate(Random rng) {
    int place1 = rng.nextInt(cities - 1) + 1  //1..cities-1
    int place2 = rng.nextInt(cities - 1) + 1
    while (place1 == place2) place2 = rng.nextInt(cities - 1) + 1
    route.swap(place1, place2)
  }

  @Override
  List <Integer> getSolution() {
    return route
  }

  @Override
  BigDecimal getFitness() {
    return fitness
  }
}
