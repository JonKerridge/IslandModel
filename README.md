The tspIsland Model project aims to create a multi-core solution for
the running of Evolutionary Algorithms.
The architecture assumes that many applications will be processed using the 
architecture and thus all the application classes are specified by their names.

The application is defined by a ProblemSpecification class

Two interfaces are provided for the Individual and Population classes 
definitions of the application.

The IslandEngine comprises four process types:
EmitProblem,
IslandCoordinator
IslandNode
CollectSolution

In due course a version of the IslandEngine will be provided that runs 
all but the IslandNode processes on a single workstation, with each IslandNode
process running on its own workstation.  Thereby making tha application run in 
parallel on a workstation cluster.