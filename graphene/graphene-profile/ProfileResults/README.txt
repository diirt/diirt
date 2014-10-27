Purpose
-------

The ProfileResults directory contains the results of Graphene profiling.

The profiling is intended for class files extending Graph2DRenderer for
the purpose of ensuring the renderer is capable of running in real-time.


File Structure
--------------

There are several file types used to provide deeper insight into the
profiling.

1) "Graph Renderer Name"
    Contains an ongoing list that tracks the profiling results of a single
    graph renderer type of the average result times in milliseconds.

2) "Date-Graph Renderer Name-Table"
    Contains a table that plots the average result times in milliseconds
    based on a image resolution and on a dataset size rendered.
	
3) "Date-Graph Renderer Name-Table Difference-DateA vs DateB"
    Contains a table that has the percentage differences from
    a profile table of DateA versus that of DateB.
    The percent difference is of DateA minus DateB.