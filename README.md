# TemporalCloseness
Code associated with the paper: *P. Crescenzi, C. Magnien, A. Marino, "Finding Top-k Nodes for Temporal Closeness in Large Temporal Graphs"*.

Once you have compiled the Java source code by executing `javac Main.java`, you can compute:

* The exact temporal closeness of all nodes by executing `java Main <file> E`, where `file` is the name of the file containing the temporal edges (one per line) sorted in non decreasing order with respect to their appearing time (node indices start from 0).

* The exact temporal closeness of one node by executing `java Main <file> E <node>`, where `file` is the name of the file containing the temporal edges (one per line) sorted in non decreasing order with respect to their appearing time (node indices start from 0) and `node` is the index of the node whose temporal closeness has to be computed.

* The approximate temporal closeness of all nodes by executing `java Main <file> A <sample>`, where file is the name of the file containing the temporal edges (one per line) sorted in non increasing order with respect to their appearing time (node indices start from 0) and `sample` is the size of the sample that has to be used for computing the approximate temporal closeness.
