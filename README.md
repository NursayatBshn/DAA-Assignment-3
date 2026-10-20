# Design and Analysis of Algorithms — Assignment 3: Graphs (DSU, MST, SCC, Shortest Paths)

**Student**: Nursayat Bashan  
**Group**: SE-2521  
**GitHub Repository**: [https://github.com/NursayatBshn/DAA-Assignment-3.git](https://github.com/NursayatBshn/DAA-Assignment-3.git) (branch `main`, tag `v1.0`)

---

## Overview
This project implements the core graph algorithms required for Assignment 3:
1. **Graph Core & File Parser**: Single adjacency list representation (`Graph`), supporting both directed and undirected graphs, handling self-loops, parallel edges, and disconnected components. Input file format: `n m directed|undirected` followed by `u v w` lines.
2. **Disjoint Set Union (DSU)**: Implements both union by rank and path compression heuristics, with operation counters (`findCalls`, `successfulUnions`).
3. **Minimum Spanning Trees**:
   - `KruskalMST`: Sorts edges by weight, uses DSU to prevent cycles, and returns a spanning forest if the graph is disconnected.
   - `PrimMST`: Grows MST using `PriorityQueue`, supports disconnected graphs (spanning forest), and verifies equal total weight with Kruskal.
4. **SCC & Topological Sorting**:
   - `KosarajuSCC`: Two-pass DFS with an **explicit iterative stack** that is safe for $n = 100\,000$ vertices without `StackOverflowError`, recording `maxDepth`.
   - `KahnTopologicalSort`: Uses in-degrees and a queue, detecting cycles if fewer than $n$ vertices leave the queue, and validating $pos[u] < pos[v]$ for all edges $u \to v$.
5. **DAG Shortest Paths**:
   - `DagShortestPath`: Single-pass relaxation in topological order in $O(V + E)$ from a virtual super-source (all vertices with $\text{in-degree} = 0$ start at 0). Handles negative weights and restores the shortest path using a `parent[]` array.
6. **Deterministic Generator & Benchmark**:
   - Exact generator using fixed seed `Random(42)`.
   - Automated self-checking benchmark across 10 graphs (5 sizes $\times$ 2 densities: sparse $m=4n$, dense $m=20n$).
   - Generates `results/results.csv` (50 rows) matching the reference values 100%.

Comprehensive theoretical proofs, empirical counter analyses, log-log slope regressions, and "which algorithm when" decision tables are detailed in [REPORT.md](REPORT.md).

---

## Prerequisites
- **Java**: OpenJDK 21+
- **Maven**: 3.6+
- **Python**: 3.8+ with `matplotlib` (for generating visual plots)

---

## Build and Test
To compile the project and execute all unit tests (cross-checks against brute force, differential tests, and stack safety):
```bash
mvn clean test
```

Unit tests included:
- `DsuTest`: Differential check against naive reference implementation after random unions;
- `MstTest`: Verifies Kruskal and Prim total weights and edge counts on 100 random connected graphs and disconnected graphs;
- `SccTest`: Cross-checks Kosaraju against brute-force BFS reachability ($n \le 200$) and verifies iterative DFS stack safety on a $100\,000$-vertex chain;
- `TopologicalSortTest`: Validates DAG order and cycle detection;
- `DagShortestPathTest`: Compares shortest paths with Bellman-Ford (including negative edge weights) and checks path restoration;
- `GraphTest`: Verifies loading from sample files in `data/`.

---

## Running Benchmarks
To run the automated benchmark across all 10 graphs and 5 algorithms (50 benchmark evaluations, median of 5 runs per case) and verify reference numbers:
```bash
mvn compile
java -Xmx4g -cp target/classes daa.Main
```
The program will print progress and generate:
- `results/results.csv` (50 rows matching exact reference values).

---

## Generating Plots
To generate the three required PNG charts:
```bash
python3 plot.py
```
Generated plots in `results/plots/`:
- `plot1_operations_vs_n.png`: Operations vs $n$ (log-log axes for sparse and dense graphs);
- `plot2_scans_per_edge.png`: Scans per edge ($edgeScans / m$ vs $n$, confirming linear work);
- `plot3_kruskal_examined_share.png`: Share of edges Kruskal examines on sparse vs dense graphs.

---

## AI-Use Acknowledgement
In accordance with course academic integrity guidelines, generative AI assistance (Google Antigravity / Gemini) was used during this assignment for:
1. Brainstorming and structuring the project layout;
2. Assisting with boilerplate syntax for JUnit 5 test setups and matplotlib plotting code;
3. Double-checking theoretical proof formulations (Cut Property and topological induction).

All core algorithm implementations (iterative Kosaraju stack, Kahn, DAG relaxation, Kruskal, Prim, DSU) and benchmark validations were reviewed, verified, and run by the student.

---

## Submission Package
Archive name format:
`DAA_Assignment3_Nursayat_Bashan_SE-2521.zip`
