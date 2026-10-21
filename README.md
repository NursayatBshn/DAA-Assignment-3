# Design and Analysis of Algorithms — Assignment 3: Graphs (DSU, MST, SCC, Shortest Paths)

**Student**: Nursayat Bashan  
**Group**: SE-2521  
**Discipline**: Design and Analysis of Algorithms (DAA)  
**Instructor**: Taubakabyl Nurlybek  
**GitHub Repository**: [https://github.com/NursayatBshn/DAA-Assignment-3.git](https://github.com/NursayatBshn/DAA-Assignment-3.git) (branch `main`, tag `v1.0`)

---

## Quick Start & Execution Commands (Быстрый запуск)

### 1. Build and Run All JUnit 5 Tests (Сборка и запуск тестов)
```bash
mvn clean test
```
- **Tests included**: `DsuTest`, `MstTest`, `SccTest`, `TopologicalSortTest`, `DagShortestPathTest`, `GraphTest`.
- All 13 tests run automatically and cross-check algorithms against brute-force and Bellman-Ford.

### 2. Run Automated Benchmark & Generate `results.csv` (Запуск бенчмарка)
```bash
mvn compile
java -Xmx4g -cp target/classes main.java.daa.Main
```
> [!IMPORTANT]
> Because package declarations follow `main.java.daa`, the entry point class is `main.java.daa.Main`.  
> Running `java -Xmx4g -cp target/classes main.java.daa.Main` executes 50 benchmark runs (10 graphs x 5 algorithms, median of 5 runs per case) and reproduces all reference numbers with 100% exact match into `results/results.csv`.

### 3. Generate Visual Plots (Построение графиков)
```bash
python3 plot.py
# or using local venv:
.venv/bin/python plot.py
```
Outputs 3 charts in `results/plots/`:
- `plot1_operations_vs_n.png` (Operations vs n on log-log scale)
- `plot2_scans_per_edge.png` (Edge scans per m, verifying constant factors)
- `plot3_kruskal_examined_share.png` (Share of edges Kruskal examines: sparse vs dense)

### 4. AI-Use Acknowledgement
In accordance with course academic integrity guidelines, generative AI assistance (Google Antigravity / Gemini) was consulted for:
1. Brainstorming modular Maven project structuring and surefire execution plugins;
2. Syntax templates for JUnit 5 randomized differential assertions and matplotlib log-log visualization;
3. Double-checking theoretical formulations for Cut Property induction and DAG topological invariants.
All core algorithm logic (DSU, Kruskal, Prim, iterative Kosaraju, Kahn, DAG shortest paths), counter integrations, and benchmark verifications were implemented and validated directly by the student.

---

# TABLE OF CONTENTS (ОГЛАВЛЕНИЕ ПОЛНОГО РУКОВОДСТВА К ЗАЩИТЕ)
1. [PART 0: Project Verification, Test Results & Bug Fixes](#part-0-project-verification-test-results--bug-fixes)
2. [PART 1: Project Architecture & Grading Rubric Mapping](#part-1-project-architecture--grading-rubric-mapping)
3. [PART 2: Step-by-Step Algorithm Traces (Hand Traces & Whiteboard Guide)](#part-2-step-by-step-algorithm-traces-hand-traces--whiteboard-guide)
4. [PART 3: Class-by-Class Code Analysis (Code, English B1, Russian, What-If)](#part-3-class-by-class-code-analysis)
5. [PART 4: Time and Space Complexity Deep-Dive & Empirical Counter Proofs](#part-4-time-and-space-complexity-deep-dive)
6. [PART 5: Comprehensive Plot Analysis & Slope Derivation](#part-5-comprehensive-plot-analysis--slope-derivation)
7. [PART 6: Mathematical Proofs & Correctness Arguments](#part-6-mathematical-proofs--correctness-arguments)
8. [PART 7: Algorithm Selection Guide & 10 Practical Scenarios](#part-7-algorithm-selection-guide--10-practical-scenarios)
9. [PART 8: Comprehensive Theory Glossary](#part-8-comprehensive-theory-glossary)
10. [PART 9: 5-Minute Oral Defense Speech (Bilingual Script)](#part-9-5-minute-oral-defense-speech)
11. [PART 10: 60+ Teacher Defense Examination Questions & Answers](#part-10-60-teacher-defense-examination-questions--answers)
12. [PART 11: 15 Live Coding Defense Tasks with Solutions](#part-11-15-live-coding-defense-tasks-with-solutions)
13. [PART 12: Weak Places, Criticisms & Defenses](#part-12-weak-places-criticisms--defenses)
14. [MASTER CHEAT SHEET: 25 Golden Defense Rules](#master-cheat-sheet-25-golden-defense-rules)

---

# PART 0: PROJECT VERIFICATION, TEST RESULTS & BUG FIXES

### 0.1. Build & Test Report
- **Command**: `mvn clean test`
- **Result**: `BUILD SUCCESS` (Compiled with Java 21, OpenJDK 64-Bit).
- **JUnit 5 Suite Summary**:
  - `test.java.daa.SccTest`: 2 tests passed (Brute force BFS reachability + 100 000 vertices stack safety).
  - `test.java.daa.MstTest`: 2 tests passed (100 random connected graphs Kruskal vs Prim weight equality + disconnected graph forest).
  - `test.java.daa.DagShortestPathTest`: 2 tests passed (Differential verification against Bellman-Ford with negative edge weights + path restoration).
  - `test.java.daa.TopologicalSortTest`: 2 tests passed (Valid topological ordering verification + directed cycle detection).
  - `test.java.daa.GraphTest`: 3 tests passed (Edge cases: self-loops, disconnected components, and DAG file parsing).
  - `test.java.daa.DsuTest`: 2 tests passed (Basic component checks + 2000 randomized operations against a reference `Set<Integer>` implementation).
  - **Total**: 13 runs, 0 failures, 0 errors, 0 skipped.

---

### 0.2. 100% Exact Verification Against PDF Reference Values

| Graph ($n$ / $m$) | Density | MST Total Weight (Kruskal & Prim) | PDF Expected MST | SCC Count (Kosaraju) | PDF Expected SCC | Sum of DAG Distances | PDF Expected DAG | Status |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| **1 000 / 4 000** | Sparse ($4n$) | **147 970** | 147 970 | **27** | 27 | **396 274** | 396 274 | **MATCH 100%** |
| **1 000 / 20 000** | Dense ($20n$) | **30 363** | 30 363 | **1** | 1 | **252 411** | 252 411 | **MATCH 100%** |
| **3 162 / 12 648** | Sparse ($4n$) | **468 911** | 468 911 | **137** | 137 | **1 271 911** | 1 271 911 | **MATCH 100%** |
| **3 162 / 63 240** | Dense ($20n$) | **97 589** | 97 589 | **1** | 1 | **723 157** | 723 157 | **MATCH 100%** |
| **10 000 / 40 000** | Sparse ($4n$) | **1 486 139** | 1 486 139 | **373** | 373 | **3 941 828** | 3 941 828 | **MATCH 100%** |
| **10 000 / 200 000** | Dense ($20n$) | **309 550** | 309 550 | **1** | 1 | **2 381 342** | 2 381 342 | **MATCH 100%** |
| **31 623 / 126 492** | Sparse ($4n$) | **4 715 515** | 4 715 515 | **1 188** | 1 188 | **12 551 891** | 12 551 891 | **MATCH 100%** |
| **31 623 / 632 460** | Dense ($20n$) | **968 517** | 968 517 | **1** | 1 | **7 376 612** | 7 376 612 | **MATCH 100%** |
| **100 000 / 400 000** | Sparse ($4n$) | **15 012 094** | 15 012 094 | **3 923** | 3 923 | **40 096 539** | 40 096 539 | **MATCH 100%** |
| **100 000 / 2 000 000** | Dense ($20n$) | **3 056 681** | 3 056 681 | **1** | 1 | **23 512 087** | 23 512 087 | **MATCH 100%** |

---

### 0.3. Counter Invariant Verification Rules
The assignment specifies three strict edge scan rules:
1. **Kosaraju `edgeScans == 2 * m`**: Evaluated on both passes (Pass 1 forwards + Pass 2 backwards). Matches across all sizes (e.g., $m = 2\,000\,000 \implies edgeScans = 4\,000\,000$).
2. **Kahn `edgeScans == m`**: Every directed edge $u \to v$ is traversed exactly once when vertex $u$ is dequeued. Matches $100\%$ ($edgeScans = m$).
3. **DAG Shortest Path `edgeScans == m`**: Single-pass relaxation in topological order relaxes every edge exactly once. Matches $100\%$ ($edgeScans = m$).

---

### 0.4. Critical Discrepancies and Defense Fixes
1. **Entry Point Execution**:
   - *Issue*: Running `java -Xmx4g -cp target/classes daa.Main` produces `ClassNotFoundException: daa.Main`.
   - *Fix*: Package refactoring places the compiled class at `target/classes/main/java/daa/Main.class`. Run:
     ```bash
     java -Xmx4g -cp target/classes main.java.daa.Main
     ```
2. **ZIP File Alignment**:
   - The original ZIP archive `DAA_Assignment3_Nursayat_Bashan_SE-2521.zip` was generated prior to package alignment. Re-pack using:
     ```bash
     zip -r DAA_Assignment3_Nursayat_Bashan_SE-2521.zip pom.xml README.md REPORT.md plot.py DAA_Assignment3_Graphs.pdf data src results
     ```

# PART 1: PROJECT ARCHITECTURE & GRADING RUBRIC MAPPING

### 1.1. Package & Class Organization

#### `main.java.daa.graph`
- **`Edge`**:
  - *English (B1)*: An immutable record of a graph edge storing endpoints `u`, `v`, and an integer `weight`.
  - *Русский*: Неизменяемый класс ребра графа, содержащий начальную вершину `u`, конечную `v` и целочисленный вес `weight`.
- **`Graph`**:
  - *English (B1)*: Adjacency list representation using `List<List<Edge>>`. Supports both directed and undirected graphs with a single boolean flag `directed`. Includes `addEdge()`, `getTranspose()`, and file parsing via `fromFile()`.
  - *Русский*: Граф на списках смежности (`List<List<Edge>>`). Поддерживает ориентированные и неориентированные графы через один булев флаг `directed`. Включает методы `addEdge()`, `getTranspose()` и загрузку из файла `fromFile()`.
- **`GraphGenerator`**:
  - *English (B1)*: Generates deterministic test graphs using `Random(42)`. Builds undirected connected graphs with a random spanning tree, directed random graphs, and DAGs using randomized vertex ranks.
  - *Русский*: Детерминированный генератор графов с фиксированным сидом `Random(42)`. Строит связные неориентированные графы со случайным остовным деревом, ориентированные графы и DAG на основе случайных рангов.

#### `main.java.daa.dsu`
- **`DisjointSetUnion`**:
  - *English (B1)*: Disjoint Set Union data structure. Uses union by rank and recursive path compression. Counts `findCalls` and `successfulUnions`.
  - *Русский*: Система непересекающихся множеств. Использует объединение по рангу и рекурсивное сжатие путей. Подсчитывает `findCalls` и `successfulUnions`.

#### `main.java.daa.algorithms`
- **`KruskalMST`**:
  - *English (B1)*: Computes a Minimum Spanning Tree or Forest. Sorts edges by weight, adds non-cyclic edges via DSU, and supports early stopping at $n-1$ edges.
  - *Русский*: Строит минимальное остовное дерево или лес. Сортирует ребра по весу, добавляет не образующие циклов ребра через DSU и поддерживает ранний выход при достижении $n-1$ ребер.
- **`PrimMST`**:
  - *English (B1)*: Computes MST using a `PriorityQueue<Edge>`. Iterates over all vertices to build a spanning forest on disconnected graphs, skipping edges whose endpoints are both visited.
  - *Русский*: Строит MST с помощью очереди `PriorityQueue<Edge>`. Обрабатывает все компоненты связности для построения остовного леса, пропуская ребра с обоими посещенными концами.
- **`KosarajuSCC`**:
  - *English (B1)*: Finds Strongly Connected Components in two DFS passes. Uses an explicit iterative stack of flat arrays (`stackVertex`, `stackEdgeIdx`) to avoid JVM stack overflow at 100 000 vertices.
  - *Русский*: Находит компоненты сильной связности за два прохода DFS. Использует явный итеративный стек на массивах (`stackVertex`, `stackEdgeIdx`), исключая падение по переполнению стека JVM при 100 000 вершин.
- **`KahnTopologicalSort`**:
  - *English (B1)*: Topologically sorts a DAG using in-degrees and an `ArrayDeque`. Detects cycles when fewer than $n$ vertices leave the queue.
  - *Русский*: Выполняет топологическую сортировку DAG с помощью полустепеней захода и очереди `ArrayDeque`. Детектирует циклы, если из очереди вышло меньше $n$ вершин.
- **`DagShortestPath`**:
  - *English (B1)*: Finds shortest paths in a DAG in $O(V + E)$ by relaxing edges in topological order. Starts all in-degree 0 vertices at distance 0 (virtual super-source), supports negative weights, and stores `parent[]` for path recovery.
  - *Русский*: Находит кратчайшие пути в DAG за $O(V + E)$ путем релаксации ребер в топологическом порядке. Назначает вершинам с in-degree=0 расстояние 0 (виртуальный супер-источник), поддерживает отрицательные веса и сохраняет `parent[]`.
- **`MstResult`, `SccResult`, `TopoResult`, `PathResult`**:
  - *English (B1)*: Encapsulate algorithm outputs and operation metrics.
  - *Русский*: Классы-результаты, возвращающие структуры данных вместе с объектом метрик.

#### `main.java.daa.metrics` and `main.java.daa.bench`
- **`Metrics`**:
  - *English (B1)*: Tracks `dsuOps`, `edgeScans`, `comparisons`, and `maxDepth` inside algorithm routines.
  - *Русский*: Считает операции `dsuOps`, `edgeScans`, `comparisons` и `maxDepth` непосредственно внутри алгоритмов.
- **`Benchmark`**:
  - *English (B1)*: Orchestrates 50 benchmark runs in a single loop, calculating the median of 5 execution times and self-checking results against expected invariants.
  - *Русский*: Запускает 50 тестов бенчмарка в едином цикле, считает медиану из 5 замеров времени и сверяет результаты с эталонными значениями.
- **`CsvWriter`**:
  - *English (B1)*: Exports benchmark rows to `results/results.csv`.
  - *Русский*: Экспортирует результаты в `results/results.csv`.

---

### 1.2. Architecture and Data Flow

```
+-------------------------------------------------------------------------------+
|                                  Benchmark.java                               |
|                                                                               |
|  Loop over 10 graphs (5 sizes x 2 densities):                                 |
|    1. GraphGenerator.generateUndirected(n, m)                                 |
|       --> KruskalMST.findMst() <----+                                         |
|       --> PrimMST.findMst()    <----+ Assert Total Weight Equal & Matches PDF |
|                                                                               |
|    2. GraphGenerator.generateDirected(n, m)                                   |
|       --> KosarajuSCC.findSCC()     Assert SCC Count & edgeScans == 2m        |
|                                                                               |
|    3. GraphGenerator.generateDag(n, m)                                        |
|       --> KahnTopologicalSort.sort()    Assert Valid Order & edgeScans == m   |
|       --> DagShortestPath.findPaths()   Assert Distance Sum & edgeScans == m  |
+-------------------------------------------------------------------------------+
                                      |
                                      v
                             results/results.csv
                                      |
                                      v
                                   plot.py
                         (Generates 3 PNG visual plots)
```

---

### 1.3. PDF Rubric Mapping Table

| Criterion | Weight | PDF High-Performance Indicators | Implementation (Class & Method) |
| :--- | :---: | :--- | :--- |
| **Graph Core & DSU** | **20%** | Adjacency list; both DSU heuristics; counters inside operations; edge cases handled (self-loops, parallel edges, isolated vertices). | `Graph.java` (`addEdge`, `fromFile`), `DisjointSetUnion.java` (`makeSet`, `find`, `union`), `GraphTest.java`. |
| **MST: Kruskal & Prim** | **20%** | Kruskal uses DSU; Prim uses priority queue; identical total weight; forest on disconnected graph. | `KruskalMST.java` (`findMst`), `PrimMST.java` (`findMst`), `MstTest.java`. |
| **SCC & Topological Order** | **20%** | Iterative DFS safe at 100 000 vertices; correct components; cycle detected instead of wrong order; validation `pos[u] < pos[v]`. | `KosarajuSCC.java` (`findSCC`), `KahnTopologicalSort.java` (`sort`), `SccTest.java`, `TopologicalSortTest.java`. |
| **Shortest Paths in DAGs** | **10%** | Single $O(V + E)$ pass; virtual super-source; negative weights handled; path restored via `parent[]`. | `DagShortestPath.java` (`findShortestPaths`), `PathResult.java` (`getPath`), `DagShortestPathTest.java`. |
| **Metrics & Report** | **20%** | Reference values reproduced 100%; counter-vs-formula verification; clear "which algorithm when" table; slope analysis. | `Benchmark.java`, `results.csv`, `REPORT.md`, `plot.py`. |
| **Testing & Git** | **10%** | JUnit 5 tests; cross-checks against brute force and Bellman-Ford; feature branches; tag `v1.0`; single-command build. | Feature branches, tag `v1.0`, `SccTest.java`, `DagShortestPathTest.java`. |

---

### 1.4. Git History & Branches
- **Branches**:
  - `main`: Release branch tagged with `v1.0`.
  - `feature/dsu`: Disjoint Set Union implementation with union by rank and path compression.
  - `feature/mst`: Kruskal and Prim MST algorithms with automated cross-validation.
  - `feature/scc`: Iterative DFS Kosaraju and Kahn topological sort with cycle detection.
  - `feature/paths`: Shortest paths in DAG with virtual super-source and Bellman-Ford test.
  - `feature/metrics`: Generator with fixed seed 42, automated benchmark, CSV export, and plotting.
- **Release Tag**: `v1.0` marks complete delivery.

---

# PART 2: STEP-BY-STEP ALGORITHM TRACES (WHITEBOARD DEFENSE GUIDE)

---

## 2.1. Disjoint Set Union (DSU)

### 1. Simple Idea (B1 English + Russian)
- *English (B1)*: DSU manages disjoint groups of elements. `find(x)` finds the group root and compresses the path by pointing nodes directly to the root. `union(x, y)` attaches the tree of smaller rank beneath the root of larger rank.
- *Русский*: DSU управляет непересекающимися группами элементов. Метод `find(x)` находит корень группы и сжимает путь, привязывая вершины прямо к корню. Метод `union(x, y)` присоединяет дерево меньшего ранга к корню дерева большего ранга.

### 2. Hand Trace on 5 Elements {0, 1, 2, 3, 4}
Initial: `parent = [0, 1, 2, 3, 4]`, `rank = [0, 0, 0, 0, 0]`, `components = 5`.

| Step | Operation | Root X / Root Y | Rank Comparison | Array Changes | Components | Note |
| :---: | :---: | :---: | :---: | :---: | :---: | :--- |
| **0** | `makeSet(5)` | — | — | `parent=[0,1,2,3,4]`, `rank=[0,0,0,0,0]` | 5 | Every element is its own root |
| **1** | `union(0, 1)` | 0 / 1 | $rank[0] == rank[1] (0 == 0)$ | `parent[1]=0`, `rank[0]=1` | 4 | Tree 1 attaches to 0; rank of 0 increments |
| **2** | `union(2, 3)` | 2 / 3 | $rank[2] == rank[3] (0 == 0)$ | `parent[3]=2`, `rank[2]=1` | 3 | Tree 3 attaches to 2; rank of 2 increments |
| **3** | `union(1, 2)` | `find(1)=0`, `find(2)=2` | $rank[0] == rank[2] (1 == 1)$ | `parent[2]=0`, `rank[0]=2` | 2 | Tree 2 attaches to root 0 |
| **4** | `union(0, 3)` | `find(0)=0`, `find(3)=0` | Roots equal ($0 == 0$) | No changes | 2 | **Cycle!** Returns `false` |
| **5** | `find(3)` | Path: $3 	o 2 	o 0$ | Path compression | `parent[3]=0` | 2 | Node 3 points directly to root 0 |

#### Tree Structure Before and After Step 5:
```
Before find(3):              After find(3) with Path Compression:
       0 (rank=2)                      0 (rank=2)
      / \                             / |      1   2                           1  2  3  <-- Flattened!
         |
         3
```

### 3. Code Line Mapping (`DisjointSetUnion.java`)
- Initialization: lines 25-31 (`parent[i] = i`, `rank[i] = 0`).
- Path compression: lines 41-43 (`if (parent[x] != x) parent[x] = find(parent[x]); return parent[x];`).
- Union by rank: lines 61-68 (`if (rank[rootX] < rank[rootY]) parent[rootX] = rootY ...`).

### 4. Typical Student Mistakes
1. **Omitting assignment in `find`**: Writing `find(parent[x]); return parent[x];` without updating `parent[x] = ...` fails to compress paths.
2. **Incrementing rank unconditionally**: Incrementing rank on every union turns rank into subtree size rather than tree depth bound.
3. **Attaching nodes instead of roots**: Writing `parent[x] = rootY` instead of `parent[rootX] = rootY`.

### 5. What-If Questions
- *What if we remove path compression and keep only union by rank?* Time per operation becomes $O(\log n)$ instead of $O(\\alpha(n))$.
- *What if we remove union by rank and keep only path compression?* Operations degrade to $O(\log n)$ amortized, with worst-case sequences taking $O(n)$.
- *What if we call `union(x, x)`?* `rootX == rootY` is detected at line 56, returning `false` safely without altering state.

---

## 2.2. Kruskal's MST Algorithm

### 1. Simple Idea (B1 English + Russian)
- *English (B1)*: Sort all edges by weight from lowest to highest. Iterate through sorted edges and accept an edge into the MST if its endpoints belong to different DSU sets. Stop early once $n - 1$ edges are added.
- *Русский*: Сортирует все ребра графа по возрастанию веса. Перебирает отсортированные ребра и добавляет ребро в MST, если его концы находятся в разных множествах DSU. Останавливается досрочно при наборе $n - 1$ ребер.

### 2. Hand Trace on 4-Vertex Graph
Vertices $\{0, 1, 2, 3\}$, edges: `(0-1, w=1)`, `(1-2, w=2)`, `(0-2, w=3)`, `(2-3, w=4)`, `(0-3, w=5)`.  
Sorted edges: `[(0-1, 1), (1-2, 2), (0-2, 3), (2-3, 4), (0-3, 5)]`. Target: $n - 1 = 3$ edges.

| Step | Edge $(u, v, w)$ | `find(u)` | `find(v)` | Condition `find(u) != find(v)` | Action | MST Size | Total Weight |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| **1** | `(0, 1, w=1)` | 0 | 1 | $0 
eq 1$ (True) | Accept edge; `union(0, 1)` | 1 | 1 |
| **2** | `(1, 2, w=2)` | 0 | 2 | $0 
eq 2$ (True) | Accept edge; `union(0, 2)` | 2 | $1 + 2 = 3$ |
| **3** | `(0, 2, w=3)` | 0 | 0 | $0 == 0$ (False) | **Reject (forms cycle 0-1-2-0)** | 2 | 3 |
| **4** | `(2, 3, w=4)` | 0 | 3 | $0 
eq 3$ (True) | Accept edge; `union(0, 3)` | 3 | $3 + 4 = 7$ |
| **—** | `(0, 3, w=5)` | — | — | — | **Early break: size == 3 == n-1** | **3** | **7** |

Edge `(0, 3, w=5)` is never scanned because early stopping triggers at line 46!

### 3. Code Line Mapping (`KruskalMST.java`)
- Edge sorting: lines 26-29 (`edges.sort(...)`).
- Edge scan metric: line 36 (`metrics.addEdgeScan()`).
- Cycle check: line 40 (`if (dsu.find(u) != dsu.find(v))`).
- Early exit: lines 46-48 (`if (mstEdges.size() == n - 1 && dsu.components() == 1) break;`).

### 4. Typical Student Mistakes
1. Checking cycles using DFS/BFS instead of DSU ($O(E \cdot V)$ instead of $O(E \log E)$).
2. Subtracting weights `e1.getWeight() - e2.getWeight()` leading to integer overflow bugs.
3. Breaking early on disconnected graphs where $n-1$ edges can never be reached.

### 5. What-If Questions
- *What if the graph has self-loops?* `find(u) == find(v)` immediately evaluates to true; self-loops are discarded at line 40.
- *What if the graph is disconnected?* DSU retains components $> 1$; Kruskal runs through all edges and returns a minimum spanning forest.
- *Why is sorting first essential?* Greedy choice requires considering the globally lightest available edge across any cut.

---

## 2.3. Prim's MST Algorithm

### 1. Simple Idea (B1 English + Russian)
- *English (B1)*: Start at vertex 0 and grow a single tree. Keep candidate outgoing edges in a `PriorityQueue`. Repeatedly extract the cheapest edge; if its other end is not yet visited, expand the tree to that vertex and push its edges.
- *Русский*: Начинает с вершины 0 и непрерывно растит одно дерево. Хранит исходящие ребра в `PriorityQueue`. Извлекает легчайшее ребро; если его второй конец еще не посещен, присоединяет вершину к дереву и добавляет ее ребра в очередь.

### 2. Hand Trace on 4-Vertex Graph
Vertices $\{0, 1, 2, 3\}$. Edges: `(0-1, 1)`, `(1-2, 2)`, `(0-2, 3)`, `(2-3, 4)`, `(0-3, 5)`. Start vertex: $0$.

| Step | Current Vertex | Edges Pushed to PQ | PQ Contents [weight:(u-v)] | Polled Edge | Target Visited? | Action | MST Size | Total Weight |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| **0** | Start $0$ | `(0-1,1), (0-2,3), (0-3,5)` | `[1:(0-1), 3:(0-2), 5:(0-3)]` | — | — | `visited[0]=true` | 0 | 0 |
| **1** | — | — | `[3:(0-2), 5:(0-3)]` | `(0-1, w=1)` | $1$ unvisited | Add `(0-1)`; `visited[1]=true` | 1 | 1 |
| **1b**| Vertex $1$ | `(1-2, 2)` | `[2:(1-2), 3:(0-2), 5:(0-3)]` | — | — | Push edges from $1$ | 1 | 1 |
| **2** | — | — | `[3:(0-2), 5:(0-3)]` | `(1-2, w=2)` | $2$ unvisited | Add `(1-2)`; `visited[2]=true` | 2 | $1+2=3$ |
| **2b**| Vertex $2$ | `(2-3, 4)` | `[3:(0-2), 4:(2-3), 5:(0-3)]` | — | — | Push edges from $2$ | 2 | 3 |
| **3** | — | — | `[4:(2-3), 5:(0-3)]` | `(0-2, w=3)` | **$0$ & $2$ both visited!**| **Skip (stale edge)** | 2 | 3 |
| **4** | — | — | `[5:(0-3)]` | `(2-3, w=4)` | $3$ unvisited | Add `(2-3)`; `visited[3]=true` | 3 | $3+4=7$ |
| **5** | — | — | Empty | `(0-3, w=5)` | **$0$ & $3$ both visited!**| **Skip (stale edge)** | 3 | 7 |

Total Weight = **7**. Matches Kruskal exactly!

### 3. Code Line Mapping (`PrimMST.java`)
- PriorityQueue initialization: lines 26-29 (`PriorityQueue<Edge> pq = new PriorityQueue<>(...)`).
- Component loop for forest support: lines 32-35 (`for (int start = 0; start < n; start++)`).
- Stale edge filtering: lines 52-54 (`if (visited[u] && visited[v]) continue;`).
- Tree expansion and incident edge push: lines 57-69.

### 4. Typical Student Mistakes
1. Forgetting to handle disconnected graphs, assuming the entire graph is visited from vertex 0.
2. Forgetting `if (visited[u] && visited[v]) continue;`, which introduces cycles.

### 5. What-If Questions
- *Does the starting vertex change total MST weight?* No. By the Cut Property, the minimum weight edge across any valid cut is safe.
- *How does Prim handle parallel edges?* The lighter edge is dequeued first; when the heavier duplicate is dequeued, both endpoints are visited and it is discarded.
- *Why does Prim execute $2m$ edge scans on undirected graphs?* Every undirected edge is stored twice in adjacency lists and scanned from both endpoints.

---

## 2.4. Kosaraju's SCC Algorithm

### 1. Simple Idea (B1 English + Russian)
- *English (B1)*: Pass 1 runs DFS on the graph to record vertex finish order. Then we reverse all edges (transpose). Pass 2 runs DFS in reverse finish order; each DFS tree discovers exactly one Strongly Connected Component.
- *Русский*: Первый проход DFS обходит граф и сохраняет порядок выхода вершин. Затем все ребра разворачиваются (транспонируются). Второй проход DFS идет по транспонированному графу в порядке убывания выхода; каждое дерево дает ровно одну компоненту сильной связности.

### 2. Hand Trace on `data/cyclic.txt`
Vertices $\{0, 1, 2, 3\}$. Directed edges: `0 -> 1`, `1 -> 2`, `2 -> 0` (cycle), `2 -> 3`.

#### Pass 1: Original Graph (DFS Finish Order)
1. Visit $0 	o$ push to stack. Visit neighbor $1 	o$ push. Visit neighbor $2 	o$ push.
2. From $2$, neighbor $0$ is visited. Visit unvisited neighbor $3 	o$ push.
3. Vertex $3$ has no outgoing edges: **finish 3**. `finishOrder = [3]`.
4. Backtrack to $2$: all edges explored $	o$ **finish 2**. `finishOrder = [3, 2]`.
5. Backtrack to $1$: all edges explored $	o$ **finish 1**. `finishOrder = [3, 2, 1]`.
6. Backtrack to $0$: all edges explored $	o$ **finish 0**. `finishOrder = [3, 2, 1, 0]`.
- Reverse finish order for Pass 2: `[0, 1, 2, 3]`.

#### Graph Transpose ($G^T$):
Reversed edges: `1 -> 0`, `2 -> 1`, `0 -> 2`, `3 -> 2`.

#### Pass 2: DFS on $G^T$ in order `0, 1, 2, 3`
1. **Root 0**: Explore in $G^T$: $0 	o 2 	o 1 	o 0$ (cycle). Node $3$ cannot be reached because $3 	o 2$ is directed toward 2.  
   $\implies$ **SCC 1: $\{0, 2, 1\}$**.
2. **Nodes 1 and 2**: Already visited, skipped.
3. **Root 3**: Explore in $G^T$: node $2$ already visited.  
   $\implies$ **SCC 2: $\{3\}$**.
Total components: **2**. Matches expected values!

### 3. Explicit Stack Architecture (Surviving 100 000 Vertices)
- Standard recursion places each stack frame on the JVM thread call stack (`-Xss1m`). Depth of 100 000 triggers `StackOverflowError`.
- Our implementation allocates flat arrays in the heap:
  ```java
  int[] stackVertex = new int[n];
  int[] stackEdgeIdx = new int[n];
  int stackPtr = 0;
  ```
- Memory consumed is under $1$ MB of heap space, tracking `metrics.updateMaxDepth(stackPtr)`.

---

## 2.5. Kahn's Topological Sort Algorithm

### 1. Simple Idea (B1 English + Russian)
- *English (B1)*: Compute the in-degree of all vertices. Vertices with in-degree 0 have no prerequisites and enter a queue. As each vertex leaves the queue, decrement its neighbors' in-degrees. If fewer than $n$ vertices leave the queue, the graph has a cycle.
- *Русский*: Вычисляет полустепени захода всех вершин. Вершины с in-degree=0 не имеют зависимостей и попадают в очередь. При извлечении вершины уменьшаются степени захода ее соседей. Если из очереди вышло меньше $n$ вершин, в графе обнаружен цикл.

### 2. Hand Trace on `data/small_dag.txt`
Vertices $\{0, 1, 2, 3, 4, 5\}$, edges: `0->1(10)`, `0->2(15)`, `1->3(12)`, `2->3(10)`, `3->4(2)`, `3->5(8)`.

| Step | In-Degree State `[v:deg]` | Queue | Popped `u` | Outgoing Edges | Decremented `inDegree[v]` | Output Order |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| **0** | `[0:0, 1:1, 2:1, 3:2, 4:1, 5:1]` | `[0]` | — | — | — | `[]` |
| **1** | `in[1]=0, in[2]=0` | `[1, 2]` | **0** | `0->1, 0->2` | $1 	o 0, 2 	o 0$ | `[0]` |
| **2** | `in[3]=1` | `[2]` | **1** | `1->3` | $3 	o 1$ | `[0, 1]` |
| **3** | `in[3]=0` | `[3]` | **2** | `2->3` | $3 	o 0$ (Enqueue 3) | `[0, 1, 2]` |
| **4** | `in[4]=0, in[5]=0` | `[4, 5]` | **3** | `3->4, 3->5` | $4 	o 0, 5 	o 0$ (Enqueue 4, 5)| `[0, 1, 2, 3]` |
| **5** | No outgoing edges | `[5]` | **4** | — | — | `[0, 1, 2, 3, 4]` |
| **6** | No outgoing edges | `[]` | **5** | — | — | `[0, 1, 2, 3, 4, 5]` |

Queue is empty, `order.size() == 6 == n`. Order is valid ($pos[u] < pos[v]$ for all edges).

---

## 2.6. Shortest Paths in DAGs

### 1. Simple Idea (B1 English + Russian)
- *English (B1)*: Process vertices in topological order. All vertices with in-degree 0 start with distance 0 as a super-source. Because edges only flow forward, a single pass of relaxations computes shortest paths in $O(V + E)$, even with negative edge weights.
- *Русский*: Обрабатывает вершины в топологическом порядке. Все вершины с in-degree=0 получают расстояние 0 (виртуальный супер-источник). Поскольку ребра направлены только вперед, один проход релаксации находит кратчайшие пути за $O(V + E)$ даже с отрицательными весами.

### 2. Hand Trace on `data/small_dag.txt`
Topological order: `[0, 1, 2, 3, 4, 5]`. Initial `dist = [0, INF, INF, INF, INF, INF]`, `parent = [-1, -1, -1, -1, -1, -1]`.

| Step | Vertex `u` | `dist[u]` | Edge `u -> v` ($w$) | Relaxation Condition `dist[u] + w < dist[v]` | New `dist[v]` | New `parent[v]` |
| :---: | :---: | :---: | :---: | :---: | :---: | :---: |
| **1** | **0** | 0 | `0 -> 1` ($w=10$) | $0 + 10 < \infty \implies$ True | `dist[1] = 10` | `parent[1] = 0` |
| **1b**| **0** | 0 | `0 -> 2` ($w=15$) | $0 + 15 < \infty \implies$ True | `dist[2] = 15` | `parent[2] = 0` |
| **2** | **1** | 10 | `1 -> 3` ($w=12$) | $10 + 12 < \infty \implies$ True | `dist[3] = 22` | `parent[3] = 1` |
| **3** | **2** | 15 | `2 -> 3` ($w=10$) | $15 + 10 < 22 \implies 25 < 22$ (False) | No change | No change |
| **4** | **3** | 22 | `3 -> 4` ($w=2$) | $22 + 2 < \infty \implies$ True | `dist[4] = 24` | `parent[4] = 3` |
| **4b**| **3** | 22 | `3 -> 5` ($w=8$) | $22 + 8 < \infty \implies$ True | `dist[5] = 30` | `parent[5] = 3` |

- Resulting distances: `dist = [0, 10, 15, 22, 24, 30]`. Sum = $101$.
- Path to vertex 4: $4 	o parent[4]=3 	o parent[3]=1 	o parent[1]=0 \implies [0, 1, 3, 4]$ (Cost: $10 + 12 + 2 = 24$).

# PART 3: CODE ANALYSIS, CLASS BY CLASS

---

## 3.1. `Graph.java` & `Edge.java`

```java
// CODE: Graph.java (Lines 38-54)
public void addEdge(int u, int v, int weight) {
    if (u < 0 || u >= this.v || v < 0 || v >= this.v) {
        throw new IllegalArgumentException("Номер вершины выходит за границы [0, " + (this.v - 1) + "]");
    }

    Edge edge = new Edge(u, v, weight);
    adj.get(u).add(edge);
    allEdges.add(edge);

    if (!directed) {
        adj.get(v).add(new Edge(v, u, weight));
    }

    e++;
}
```

- **ENGLISH (B1)**:
  - *What it does*: Adds an edge between vertex `u` and vertex `v` with a given `weight`.
  - *How it works step by step*: Validates vertex bounds. Creates an `Edge` object. Adds it to the adjacency list of `u` and the global `allEdges` list. If the graph is undirected, adds a reversed edge `v -> u` to the adjacency list of `v`. Increments edge count `e`.
  - *Why I wrote it this way*: Single class supports both directed and undirected graphs. `allEdges` enables Kruskal to sort edges without scanning all adjacency lists.
- **РУССКИЙ**:
  - *Что это делает*: Добавляет ребро между вершинами `u` и `v` с весом `weight`.
  - *Как это работает по шагам*: Проверяет границы индексов. Создает объект `Edge`. Добавляет его в список смежности вершины `u` и в общий список `allEdges`. Если граф неориентированный, добавляет обратное ребро `v -> u` в список смежности вершины `v`. Увеличивает счетчик `e`.
  - *Зачем так написано*: Один класс поддерживает ориентированные и неориентированные графы, а список `allEdges` позволяет Краскалу сразу получить все ребра для сортировки.
- **WHAT IF**:
  - *Delete bounds check (lines 39-41)*: Invalid vertex indices cause an uninformative `IndexOutOfBoundsException` deep in `adj.get(u)`.
  - *Delete `if (!directed)` block (lines 48-51)*: Undirected graphs become directed, breaking Prim and Kruskal MST algorithms.
  - *Graph has self-loops (`u == v`)*: Handled safely: added to `adj.get(u)` and later ignored by DSU / visited arrays.

---

## 3.2. `DisjointSetUnion.java`

```java
// CODE: DisjointSetUnion.java (Lines 39-73)
public int find(int x) {
    findCalls++;
    if (parent[x] != x) {
        parent[x] = find(parent[x]); // рекурсивное сжатие пути
    }
    return parent[x];
}

public boolean union(int x, int y) {
    int rootX = find(x);
    int rootY = find(y);

    if (rootX == rootY) {
        return false;
    }

    if (rank[rootX] < rank[rootY]) {
        parent[rootX] = rootY;
    } else if (rank[rootX] > rank[rootY]) {
        parent[rootY] = rootX;
    } else {
        parent[rootY] = rootX;
        rank[rootX]++;
    }

    numComponents--;
    successfulUnions++;
    return true;
}
```

- **ENGLISH (B1)**:
  - *What it does*: Implements DSU with path compression and union by rank.
  - *How it works step by step*: In `find(x)`, if `x` is not root, recursively finds the root and points `parent[x]` directly to it. In `union(x, y)`, finds roots; if equal, returns `false`. Otherwise, attaches the tree of lower rank beneath the root of higher rank. If ranks are equal, increments root rank by 1. Decrements component count.
  - *Why I wrote it this way*: Combining both heuristics guarantees almost constant amortized time $O(\\alpha(n)) \le 4$.
- **РУССКИЙ**:
  - *Что это делает*: Реализует DSU со сжатием путей и объединением по рангу.
  - *Как это работает по шагам*: В `find(x)`, если `x` не корень, рекурсивно ищет корень и перепривязывает `parent[x]` напрямую к нему. В `union(x, y)` находит корни; если они равны, возвращает `false`. Иначе цепляет дерево меньшего ранга к корню большего. При равенстве рангов увеличивает ранг корня на 1. Уменьшает число компонент.
  - *Зачем так написано*: Комбинация обеих эвристик гарантирует почти константное амортизированное время $O(\\alpha(n)) \le 4$.
- **WHAT IF**:
  - *Change `parent[x] = find(parent[x])` to `return find(parent[x])`*: Disables path compression; operations degrade to $O(\log n)$.
  - *Delete rank comparison and always do `parent[rootY] = rootX`*: Trees can degrade into chains of depth $O(n)$, causing worst-case runtime to spike.
  - *Delete `if (rootX == rootY) return false;`*: Cycle detection fails, and component counter incorrectly decrements.

---

## 3.3. `KruskalMST.java`

```java
// CODE: KruskalMST.java (Lines 26-50)
edges.sort((e1, e2) -> {
    metrics.addComparison();
    return Integer.compare(e1.getWeight(), e2.getWeight());
});

long totalWeight = 0;
List<Edge> mstEdges = new ArrayList<>();

for (Edge edge : edges) {
    metrics.addEdgeScan();
    int u = edge.getU();
    int v = edge.getV();

    if (dsu.find(u) != dsu.find(v)) {
        dsu.union(u, v);
        mstEdges.add(edge);
        totalWeight += edge.getWeight();

        if (mstEdges.size() == n - 1 && dsu.components() == 1) {
            break;
        }
    }
}
```

- **ENGLISH (B1)**:
  - *What it does*: Finds MST by sorting edges and adding non-cyclic edges via DSU.
  - *How it works step by step*: Sorts edge copy using `Integer.compare`, tracking comparisons. Iterates over edges, tracking `edgeScans`. If `find(u) != find(v)`, calls `union(u, v)`, records the edge, and accumulates weight. Breaks early when $n-1$ edges connect all vertices.
  - *Why I wrote it this way*: Counting comparisons and scans directly inside the routine yields exact hardware-independent metrics.
- **РУССКИЙ**:
  - *Что это делает*: Находит MST через сортировку ребер и добавление ацикличных ребер через DSU.
  - *Как это работает по шагам*: Сортирует ребра через `Integer.compare`, считая сравнения. Перебирает ребра, считая `edgeScans`. Если `find(u) != find(v)`, вызывает `union(u, v)`, сохраняет ребро и суммирует вес. Досрочно останавливается при наборе $n-1$ ребер для связного графа.
  - *Зачем так написано*: Прямой подсчет операций внутри алгоритма обеспечивает воспроизводимые метрики на любой машине.
- **WHAT IF**:
  - *Replace `Integer.compare` with subtraction `e1.weight - e2.weight`*: Risk of integer underflow/overflow if weights are negative or exceed $2 \cdot 10^9$.
  - *Remove early break*: Code scans all remaining edges unnecessarily, increasing edge scans on dense graphs from $15\%$ to $100\%$.

---

## 3.4. `PrimMST.java`

```java
// CODE: PrimMST.java (Lines 26-70)
PriorityQueue<Edge> pq = new PriorityQueue<>((e1, e2) -> {
    metrics.addComparison();
    return Integer.compare(e1.getWeight(), e2.getWeight());
});

for (int start = 0; start < n; start++) {
    if (visited[start]) continue;
    components++;
    visited[start] = true;

    for (Edge edge : graph.getAdj(start)) {
        metrics.addEdgeScan();
        pq.add(edge);
    }

    while (!pq.isEmpty()) {
        Edge minEdge = pq.poll();
        int u = minEdge.getU();
        int v = minEdge.getV();

        if (visited[u] && visited[v]) continue;

        int nextVertex = !visited[u] ? u : v;
        visited[nextVertex] = true;
        mstEdges.add(minEdge);
        totalWeight += minEdge.getWeight();

        for (Edge nextEdge : graph.getAdj(nextVertex)) {
            metrics.addEdgeScan();
            int target = (nextEdge.getU() == nextVertex) ? nextEdge.getV() : nextEdge.getU();
            if (!visited[target]) {
                pq.add(nextEdge);
            }
        }
    }
}
```

- **ENGLISH (B1)**:
  - *What it does*: Builds an MST or forest using a priority queue.
  - *How it works step by step*: Outer loop iterates through all vertices to handle multiple components. Priority queue orders edges by weight. Skips stale edges whose ends are both visited. Marks the unvisited endpoint as visited, adds edge to MST, and inserts incident unvisited edges into the queue.
  - *Why I wrote it this way*: Lazy Prim avoids complicated decrease-key operations while remaining clean and robust.
- **РУССКИЙ**:
  - *Что это делает*: Строит MST или остовный лес с помощью очереди с приоритетом.
  - *Как это работает по шагам*: Внешний цикл обходит все вершины для поддержки несвязных графов. Очередь сортирует ребра по весу. Пропускает устаревшие ребра, оба конца которых уже в остове. Помечает новую вершину посещенной, добавляет ребро в MST и складывает новые инцидентные ребра в очередь.
  - *Зачем так написано*: Ленивая версия Прима не требует сложной операции decrease-key и устойчиво работает со стандартной `PriorityQueue`.
- **WHAT IF**:
  - *Delete `if (visited[u] && visited[v]) continue;`*: Causes cycles to enter the tree, breaking the spanning tree structure.
  - *Delete outer `for (int start = 0; start < n; start++)`*: Only explores one component; crashes on disconnected graphs.

---

## 3.5. `KosarajuSCC.java`

```java
// CODE: KosarajuSCC.java (Pass 1 DFS with Explicit Stack, Lines 27-64)
int[] stackVertex = new int[n];
int[] stackEdgeIdx = new int[n];
int stackPtr = 0;

for (int i = 0; i < n; i++) {
    if (!visited[i]) {
        visited[i] = true;
        stackVertex[stackPtr] = i;
        stackEdgeIdx[stackPtr] = 0;
        stackPtr++;
        metrics.updateMaxDepth(stackPtr);

        while (stackPtr > 0) {
            int topU = stackVertex[stackPtr - 1];
            int edgeIdx = stackEdgeIdx[stackPtr - 1];
            List<Edge> neighbors = graph.getAdj(topU);

            if (edgeIdx < neighbors.size()) {
                stackEdgeIdx[stackPtr - 1]++;
                Edge edge = neighbors.get(edgeIdx);
                metrics.addEdgeScan();

                int v = edge.getV();
                if (!visited[v]) {
                    visited[v] = true;
                    stackVertex[stackPtr] = v;
                    stackEdgeIdx[stackPtr] = 0;
                    stackPtr++;
                    metrics.updateMaxDepth(stackPtr);
                }
            } else {
                stackPtr--;
                finishOrder.add(topU);
            }
        }
    }
}
```

- **ENGLISH (B1)**:
  - *What it does*: Executes Pass 1 of Kosaraju to compute the finish order of vertices using an iterative DFS.
  - *How it works step by step*: Uses two flat int arrays as a stack (`stackVertex` and `stackEdgeIdx`). Pushes unvisited vertices. While stack is not empty, looks at top vertex. If it has unvisited neighbors, pushes next neighbor and updates `maxDepth`. When all outgoing edges are traversed, pops the vertex and appends it to `finishOrder`.
  - *Why I wrote it this way*: Eliminates recursive method calls, preventing `StackOverflowError` on long graph chains of $100\,000$ vertices.
- **РУССКИЙ**:
  - *Что это делает*: Выполняет 1-й проход Косарайю для вычисления порядка завершения вершин через итеративный DFS.
  - *Как это работает по шагам*: Использует два плоских массива как стек (`stackVertex` и `stackEdgeIdx`). Добавляет непосещенную вершину. Пока стек не пуст, смотрит на вершину стека. Если есть необработанные соседи, кладет соседа в стек и обновляет `maxDepth`. Когда все ребра вершины просмотрены, снимает ее со стека и добавляет в `finishOrder`.
  - *Зачем так написано*: Исключает рекурсивные вызовы методов, предотвращая ошибку `StackOverflowError` на цепочках из $100\,000$ вершин.
- **WHAT IF**:
  - *Replace with recursive DFS*: Dies with `java.lang.StackOverflowError` when testing the $100\,000$-vertex path graph.
  - *Forget `stackEdgeIdx[stackPtr - 1]++`*: Infinite loop on the first neighbor of every vertex.

---

## 3.6. `KahnTopologicalSort.java`

```java
// CODE: KahnTopologicalSort.java (Lines 24-59)
int[] inDegree = new int[n];
for (Edge edge : graph.getAllEdges()) {
    inDegree[edge.getV()]++;
}

Queue<Integer> queue = new ArrayDeque<>();
for (int i = 0; i < n; i++) {
    if (inDegree[i] == 0) {
        queue.add(i);
    }
}

List<Integer> order = new ArrayList<>(n);

while (!queue.isEmpty()) {
    int u = queue.poll();
    order.add(u);

    for (Edge edge : graph.getAdj(u)) {
        metrics.addEdgeScan();
        int v = edge.getV();
        inDegree[v]--;
        if (inDegree[v] == 0) {
            queue.add(v);
        }
    }
}

boolean hasCycle = (order.size() < n);
```

- **ENGLISH (B1)**:
  - *What it does*: Produces a topological order or flags directed cycles.
  - *How it works step by step*: Counts incoming edges for each vertex. Vertices with in-degree 0 enter `ArrayDeque`. While queue has elements, pops vertex `u`, adds to order, and decrements in-degrees of neighbors. When neighbor in-degree reaches 0, enqueues it. If order size is less than $n$, a cycle exists.
  - *Why I wrote it this way*: Cycle detection and ordering are performed together in a single linear $O(V + E)$ pass.
- **РУССКИЙ**:
  - *Что это делает*: Строит топологический порядок или сообщает о наличии ориентированного цикла.
  - *Как это работает по шагам*: Считает входящие ребра для каждой вершины. Вершины с in-degree=0 добавляются в `ArrayDeque`. Извлекает вершину `u`, добавляет в результат и уменьшает in-degree соседей. При достижении 0 добавляет соседа в очередь. Если в результат вошло меньше $n$ вершин, обнаружен цикл.
  - *Зачем так написано*: Построение порядка и детекция циклов выполняются одновременно за один линейный проход $O(V + E)$.
- **WHAT IF**:
  - *Delete `boolean hasCycle = (order.size() < n)`*: Code returns an incomplete topological order for cyclic graphs instead of reporting an error.

---

## 3.7. `DagShortestPath.java`

```java
// CODE: DagShortestPath.java (Lines 42-70)
long[] dist = new long[n];
int[] parent = new int[n];
Arrays.fill(dist, Long.MAX_VALUE);
Arrays.fill(parent, -1);

for (int i = 0; i < n; i++) {
    if (inDegree[i] == 0) {
        dist[i] = 0;
    }
}

for (int u : topoOrder) {
    if (dist[u] == Long.MAX_VALUE) continue;

    for (Edge edge : graph.getAdj(u)) {
        metrics.addEdgeScan();
        int v = edge.getV();
        int weight = edge.getWeight();

        metrics.addComparison();
        if (dist[u] + weight < dist[v]) {
            dist[v] = dist[u] + weight;
            parent[v] = u;
        }
    }
}
```

- **ENGLISH (B1)**:
  - *What it does*: Finds shortest paths in a DAG from all in-degree 0 sources.
  - *How it works step by step*: Initializes distances to `Long.MAX_VALUE` and parents to -1. Sources with in-degree 0 start at 0. Iterates over vertices in topological order. Relaxes outgoing edges. Updates `dist[v]` and records predecessor `parent[v] = u`.
  - *Why I wrote it this way*: Uses `long[]` to prevent integer overflow when adding weights to large distances.
- **РУССКИЙ**:
  - *Что это делает*: Находит кратчайшие пути в DAG от всех источников с in-degree=0.
  - *Как это работает по шагам*: Инициализирует расстояния в `Long.MAX_VALUE`, а предков в -1. Источникам с in-degree=0 задает 0. Обходит вершины строго в топологическом порядке. Релаксирует исходящие ребра. Обновляет `dist[v]` и сохраняет предка `parent[v] = u`.
  - *Зачем так написано*: Массив `long[]` исключает переполнение типов при сложении весов ребер с большими расстояниями.
- **WHAT IF**:
  - *Delete `if (dist[u] == Long.MAX_VALUE) continue;`*: Adding edge weights to `Long.MAX_VALUE` causes positive values to wrap around to negative numbers due to overflow.
  - *Use `int[]` instead of `long[]`*: Cumulative path lengths can exceed $2 \cdot 10^9$, causing overflow bugs.

---

## 3.8. Core Java Collections Used in Project

1. **`ArrayList`**:
   - Used for adjacency lists and edge collections.
   - Provides $O(1)$ amortized appending and fast indexed access through contiguous memory arrays.
2. **`PriorityQueue`**:
   - Binary min-heap used in Prim's algorithm.
   - Operations `add()` and `poll()` run in $O(\log k)$ time, ordering edges via custom lambda comparator.
3. **`ArrayDeque`**:
   - Array-backed double-ended queue used in Kahn's algorithm.
   - Outperforms `LinkedList` due to lack of node object allocation and superior CPU cache locality.
4. **`long` vs `int`**:
   - Edge weights are `int`, but cumulative distance sums and operation counts are `long` to prevent 32-bit overflow.
5. **`Integer.compare(a, b)`**:
   - Compares two integers safely without numeric subtraction underflow/overflow.

# PART 4: TIME AND SPACE COMPLEXITY DEEP-DIVE

---

### 4.1. Formal Complexity Table & One-Line Justifications

| Algorithm / Operation | Time Complexity | Extra Space | One-Line English Justification | One-Line Russian Justification |
| :--- | :---: | :---: | :--- | :--- |
| **DSU `makeSet(n)`** | $\Theta(n)$ | $\Theta(n)$ | Allocates parent and rank arrays of size $n$. | Выделяет память под массивы `parent` и `rank` размера $n$. |
| **DSU `find(x)`** | $O(\\alpha(n))$ | $O(\\alpha(n))$ | Path compression flattens trees to near-constant amortized depth. | Сжатие пути делает амортизированную глубину почти константной. |
| **DSU `union(x, y)`** | $O(\\alpha(n))$ | $O(1)$ | Performs two finds plus $O(1)$ root attachment by rank. | Два вызова `find` плюс перепривязка корня меньшего ранга за $O(1)$. |
| **Kruskal MST** | $O(E \log E)$ | $O(V + E)$ | Edge sorting dominates subsequent $E$ near-constant DSU operations. | Сортировка $E$ ребер доминирует над $E$ вызовами DSU. |
| **Prim MST** | $O(E \log V)$ | $O(V + E)$ | Every edge enters PriorityQueue once; each heap operation costs $O(\log V)$. | Каждое ребро входит в кучу; операции с кучей стоят $O(\log V)$. |
| **Kosaraju SCC** | $\Theta(V + E)$ | $\Theta(V + E)$ | Two full iterative DFS passes traverse all vertices and edges twice. | Два итеративных DFS обходят все вершины и ребра ровно дважды. |
| **Kahn TopoSort** | $\Theta(V + E)$ | $\Theta(V + E)$ | Every vertex and edge enters the queue and gets processed exactly once. | Каждая вершина и каждое ребро обрабатываются в очереди ровно один раз. |
| **DAG Shortest Paths**| $\Theta(V + E)$ | $\Theta(V)$ | Single pass over topological order relaxes each outgoing edge once. | Однократный проход по топологическому порядку релаксирует каждое ребро. |

---

### 4.2. Mathematical Derivations from Code Loops

1. **Kruskal MST**:
   - `edges.sort(...)`: Sorts $E$ elements using dual-pivot quicksort / TimSort $\implies O(E \log E)$ comparisons.
   - Loop `for (Edge edge : edges)`: Scans at most $E$ edges $\implies E$ iterations.
   - Inside loop: Calls `dsu.find()` twice and `dsu.union()` once $\implies O(E \cdot \alpha(V))$.
   - Total: $O(E \log E + E \cdot \alpha(V)) = \mathbf{O(E \log E)}$.
2. **Prim MST**:
   - Outer loop over $V$ components: Every vertex marked visited once $\implies O(V)$.
   - While loop: Each edge added to `PriorityQueue` at most twice (undirected) $\implies \le 2E$ insertions and deletions.
   - Each `pq.poll()` and `pq.add()` takes $O(\log |PQ|)$ where $|PQ| \le 2E$. Since $\log(2E) \le \log(2V^2) = O(\log V)$, heap operations take $O(E \log V)$.
   - Total: $\mathbf{O(E \log V)}$.
3. **Kosaraju SCC**:
   - Pass 1: Iterative DFS visits every vertex once and scans every directed edge once $\implies \Theta(V + E)$.
   - Transpose: Reverses $E$ edges into a new graph $\implies \Theta(V + E)$.
   - Pass 2: Iterative DFS on $G^T$ visits every vertex and scans every reversed edge once $\implies \Theta(V + E)$.
   - Total: $\Theta(V + E) + \Theta(V + E) + \Theta(V + E) = \mathbf{\Theta(V + E)}$.
4. **Kahn Topological Sort**:
   - In-degree computation: Scans all $E$ edges once $\implies \Theta(E)$.
   - Queue processing: Each of the $V$ vertices enters and leaves `ArrayDeque` exactly once $\implies \Theta(V)$.
   - For each dequeued vertex $u$, scans its outgoing edges $\implies \sum 	ext{deg}(u) = E$.
   - Total: $\mathbf{\Theta(V + E)}$.
5. **DAG Shortest Paths**:
   - Source initialization: Loops over $V$ vertices $\implies \Theta(V)$.
   - Relaxation loop: Iterates over topological order ($V$ steps); inner loop scans outgoing edges $\implies \sum 	ext{out-deg}(u) = E$.
   - Inside inner loop: $O(1)$ addition and comparison.
   - Total: $\mathbf{\Theta(V + E)}$.

---

### 4.3. Fundamental Theoretical Nuances

- **Why $\log E = \Theta(\log V)$**:
  In any simple graph without parallel edges, $E \le V^2$. Taking logarithms: $\log E \le \log(V^2) = 2 \log V$.  
  Since $2$ is a constant, $O(\log E) = O(\log V)$. Therefore, $O(E \log E) = O(E \log V)$.
- **What $\\alpha(n)$ is and why it is almost constant**:
  $\\alpha(n)$ is the inverse Ackermann function. The Ackermann function grows faster than any tower of powers. For any input $n$ up to the number of atoms in the observable universe ($10^{80}$), $\\alpha(n) \le 4$. In algorithm analysis, it is treated as an effective constant.
- **Why Lazy Prim is $O(E \log E)$**:
  Eager Prim with an indexed binary heap updates vertex keys in place, keeping $|PQ| \le V$ and running in $O(E \log V)$. Our implementation uses lazy Prim (`PriorityQueue<Edge>`), which can hold up to $E$ edges, taking $O(E \log E)$. Because $\log E = \Theta(\log V)$, the asymptotic complexity class remains identical.

---

### 4.4. Operation Counters vs Empirical Formulas

```
+-----------------+-------------------+----------------------+--------------------+
| Algorithm       | Monitored Counter | Theoretical Formula  | Empirical Ratio    |
+-----------------+-------------------+----------------------+--------------------+
| Kosaraju SCC    | edgeScans         | 2m                   | 1.000 (Exact 2.0m) |
| Kosaraju SCC    | edgeScans         | V + E                | 1.600 (on 4n)      |
| Kahn TopoSort   | edgeScans         | m                    | 1.000 (Exact m)    |
| DAG Shortest P. | edgeScans         | V + E                | 0.800 (on 4n)      |
| Prim MST        | edgeScans         | m                    | 2.000 (Exact 2m)   |
| Kruskal MST     | dsuOps            | m                    | ~4.8 (stabilizes)  |
+-----------------+-------------------+----------------------+--------------------+
```

#### Real Data from `results.csv`:
- **Kosaraju on Sparse ($m = 4n$):**
  - $n = 1\,000, m = 4\,000 \implies edgeScans = 8\,000 \implies rac{8\,000}{1\,000 + 4\,000} = \mathbf{1.600}$.
  - $n = 10\,000, m = 40\,000 \implies edgeScans = 80\,000 \implies rac{80\,000}{10\,000 + 40\,000} = \mathbf{1.600}$.
  - $n = 100\,000, m = 400\,000 \implies edgeScans = 800\,000 \implies rac{800\,000}{100\,000 + 400\,000} = \mathbf{1.600}$.
- **Kahn on Sparse ($m = 4n$):**
  - $n = 1\,000, m = 4\,000 \implies edgeScans = 4\,000 \implies rac{4\,000}{4\,000} = \mathbf{1.000}$.
  - $n = 100\,000, m = 400\,000 \implies edgeScans = 400\,000 \implies rac{400\,000}{400\,000} = \mathbf{1.000}$.
- **Why Ratios Must Stay Constant**:
  A constant ratio $rac{	ext{Measured Counter}}{f(V, E)} = C$ as $n 	o \infty$ is empirical proof that the algorithm executes within that complexity class. A rising curve would indicate a higher complexity class.

---

### 4.5. Why Wall-Clock Time is Not the Primary Metric
1. **JVM Warm-up & JIT**: Just-In-Time compiler compiles bytecode to native assembly dynamically after thousands of iterations.
2. **Garbage Collection (GC)**: Unpredictable GC pause times introduce latency noise.
3. **Hardware / OS Noise**: On small graphs ($n = 1\,000, 3\,162$), execution takes 1 to 5 ms, where timer resolution and OS context switching dominate.
4. **Operation counters** (`edgeScans`, `dsuOps`, `comparisons`, `maxDepth`) are hardware-independent and 100% reproducible on any computer.

---

# PART 5: COMPREHENSIVE PLOT ANALYSIS & SLOPE DERIVATION

---

### 5.1. Plot 1: Operations vs $n$ (Log-Log Scale)
- **Axes**: X-axis is $n$ (number of vertices, logarithmic scale); Y-axis is Total Operations (`dsuOps + edgeScans + comparisons`, logarithmic scale).
- **English (B1)**: On log-log axes, a power function $y = c \cdot n^k$ turns into a straight line with slope $k$. A slope of 1.0 means linear complexity $O(n)$.
- **Русский**: На логарифмических осях степенная функция $y = c \cdot n^k$ превращается в прямую линию с наклоном $k$. Наклон 1.0 означает линейную сложность $O(n)$.
- **Why dense lines are higher than sparse**:
  Dense graphs have $m = 20n$, while sparse graphs have $m = 4n$. The edge ratio is $rac{20}{4} = 5$. Since operations scale with $m$, the dense curve is shifted vertically upward by a factor of 5 ($\log 5$).
- **Why Kosaraju is above Kahn**:
  Kosaraju performs two DFS passes ($2m$ scans), whereas Kahn performs one pass ($m$ scans). Thus, Kosaraju executes twice as many scans and sits above Kahn.

---

### 5.2. Plot 2: Scans per Edge ($edgeScans / m$ vs $n$)
- **Axes**: X-axis is $n$; Y-axis is the ratio $rac{edgeScans}{m}$.
- **Observations**:
  - Kosaraju is a flat horizontal line at $y = 2.0$.
  - Kahn is a flat horizontal line at $y = 1.0$.
  - DAG Shortest Paths is a flat horizontal line at $y = 1.0$.
- **What a flat line proves**:
  A flat line proves that work per edge is strictly constant and independent of graph size $n$. If an algorithm were $O(V \cdot E)$, the curve would climb linearly with $n$.

---

### 5.3. Plot 3: Share of Edges Kruskal Examines ($edgeScans / m$)
- **Axes**: X-axis is $n$; Y-axis is the fraction of edges examined before MST completion.
- **Numbers from `results.csv`**:
  - **Sparse graphs ($m = 4n$)**: Kruskal examines $77\% - 99\%$ of edges (e.g., at $n = 100\,000$, scans $399\,668$ out of $400\,000 \implies 99.9\%$).
  - **Dense graphs ($m = 20n$)**: Kruskal examines only $15\% - 37\%$ of edges (e.g., at $n = 100\,000$, scans $743\,572$ out of $2\,000\,000 \implies \mathbf{37.1\%}$).
- **Why the difference exists**:
  To build an MST, Kruskal needs $n - 1$ edges. In dense graphs ($m = 20n$), there is a massive pool of edges, so the tree connects quickly from the lightest edges. In sparse graphs ($m = 4n$), edges are scarce, so Kruskal must inspect nearly every edge to find connections.

---

### 5.4. Slope Fitting & Hand Calculation on the Whiteboard

On log-log axes:
$$	ext{slope } k = rac{\ln(y_2) - \ln(y_1)}{\ln(x_2) - \ln(x_1)}$$

Let us compute the slope for **Kosaraju SCC** using points from `results.csv`:
- Point 1: $n_1 = 1\,000 \implies y_1 = 8\,000$ operations
- Point 2: $n_2 = 100\,000 \implies y_2 = 800\,000$ operations

Calculation:
$$k = rac{\ln(800\,000) - \ln(8\,000)}{\ln(100\,000) - \ln(1\,000)} = rac{\ln(100)}{\ln(100)} = \mathbf{1.000}$$
**A slope of exactly 1.000 proves linear scaling $O(V + E)$!**

For **Kruskal MST**:
- Point 1: $n_1 = 1\,000, m_1 = 4\,000 \implies y_1 = 42\,663$ comparisons
- Point 2: $n_2 = 100\,000, m_2 = 400\,000 \implies y_2 = 5\,543\,379$ comparisons

$$k = rac{\ln(5\,543\,379) - \ln(42\,663)}{\ln(100\,000) - \ln(1\,000)} = rac{\ln(129.93)}{\ln(100)} = rac{4.867}{4.605} = \mathbf{1.057}$$
**The slope is slightly greater than 1.00 ($k \approx 1.057$) because of the logarithmic factor $\log E$ in sorting ($E \log E$)!**

# PART 6: MATHEMATICAL PROOFS & CORRECTNESS ARGUMENTS

---

### 6.1. The Cut Property for Minimum Spanning Trees (MST)
- **ENGLISH (B1)**:
  Let $G = (V, E)$ be a connected undirected weighted graph. A cut $(S, V \setminus S)$ partitions vertices into two disjoint sets. An edge crosses the cut if one endpoint is in $S$ and the other is in $V \setminus S$.  
  **Theorem**: If edge $e$ has strictly minimum weight among all edges crossing a cut $(S, V \setminus S)$, then $e$ belongs to an MST.  
  **Proof**: Assume for contradiction that an MST $T$ does not contain $e$. Adding $e$ to $T$ creates a unique simple cycle. Because the endpoints of $e$ lie on opposite sides of the cut, the cycle must contain at least one other edge $e'$ crossing the same cut. By definition of $e$, $w(e) \le w(e')$. If we remove $e'$ and insert $e$, we obtain a new spanning tree $T' = T \cup \{e\} \setminus \{e'\}$. Its weight is $w(T') = w(T) - w(e') + w(e) \le w(T)$. Thus, $T'$ is also an MST containing $e$.  
  *How algorithms use it*: Prim grows set $S$ and greedily picks the lightest edge across cut $(S, V \setminus S)$. Kruskal implicitly picks the lightest edge connecting two disjoint trees.
- **РУССКИЙ**:
  Пусть $G = (V, E)$ — связный неориентированный взвешенный граф. Разрез $(S, V \setminus S)$ разбивает вершины на два непересекающихся множества.  
  **Теорема**: Если ребро $e$ имеет минимальный вес среди всех ребер, пересекающих разрез, оно принадлежит некоторому MST.  
  **Доказательство**: Предположим от противного, что дерево $T$ не содержит $e$. Добавление $e$ создает цикл. В этом цикле обязано найтись другое ребро $e'$, пересекающее тот же разрез. По условию $w(e) \le w(e')$. Заменяя $e'$ на $e$, получаем остовное дерево $T' = T \cup \{e\} \setminus \{e'\}$ весом $w(T') \le w(T)$. Значит, $T'$ — минимальное остовное дерево, содержащее $e$.  
  *Использование*: Прим явно выбирает минимальное ребро из разреза между остовом и остальным графом. Краскал объединяет компоненты легчайшим доступным ребром.

---

### 6.2. Induction on Topological Order in DAGs
- **ENGLISH (B1)**:
  Let $v_1, v_2, \dots, v_n$ be vertices of a DAG sorted in topological order, meaning all edges point from $v_i$ to $v_j$ where $i < j$.  
  **Claim**: A single pass of edge relaxations in topological order computes shortest paths from sources to all reachable vertices.  
  **Proof by Induction**:  
  - *Base Case*: For the first vertex $v_1$ (and all in-degree 0 vertices), there are no incoming edges. Distance is 0, which is trivially optimal.  
  - *Inductive Step*: Assume optimal distances $d(v_1), \dots, d(v_{k-1})$ are computed for the first $k-1$ vertices. Consider vertex $v_k$. Any path to $v_k$ must enter via an edge $(v_i, v_k)$. Because the graph is a DAG, all predecessors $v_i$ appear before $v_k$ in topological order ($i < k$). Therefore, when $v_k$ is reached, all predecessors $v_i$ have already been relaxed. Hence, $d(v_k) = \min_{(v_i, v_k)} (d(v_i) + w(v_i, v_k))$ is final and will never decrease.  
  *Negative Weights*: Because cycles are impossible in a DAG, negative cycles cannot exist. Therefore, single-pass relaxation works even with negative edge weights.
- **РУССКИЙ**:
  Пусть $v_1, \dots, v_n$ — топологический порядок вершин DAG (все ребра ведут от $v_i$ к $v_j$, где $i < j$).  
  **База**: Вершины с in-degree=0 имеют расстояние 0, что тривиально минимально.  
  **Переход**: Пусть расстояния до первых $k-1$ вершин уже оптимальны. Любой путь в $v_k$ заканчивается входящим ребром $(v_i, v_k)$, где $i < k$. К моменту обработки $v_k$ все предки $v_i$ уже релаксированы. Значит, значение $d(v_k)$ уже окончательное.  
  *Отрицательные веса*: В DAG нет циклов, поэтому отрицательные циклы исключены. Алгоритм корректен при любых весах.

---

### 6.3. Correctness of Kosaraju's Algorithm
- **ENGLISH (B1)**:
  In the condensation DAG of SCCs, Pass 1 DFS exits last from the source SCC. Reversing all edges turns this source SCC into a sink SCC. When Pass 2 explores from this vertex in $G^T$, edges cannot leave the sink SCC. Hence, Pass 2 visits all vertices in that SCC and stops without leaking into others.
- **РУССКИЙ**:
  В графе конденсации первый проход DFS последним покидает компоненту-источник. Разворот всех ребер превращает ее в компоненту-сток. Запуск второго DFS из этой вершины в $G^T$ физически не может выйти за пределы данной SCC. Поэтому DFS обходит ровно одну компоненту.

---

# PART 7: ALGORITHM SELECTION GUIDE & 10 PRACTICAL SCENARIOS

---

### 7.1. Decision Table

| Graph Property | Density / Topology | Cycle Status | Recommended Algorithm | Reason (B1 English & Russian) |
| :--- | :--- | :--- | :--- | :--- |
| **MST** | Sparse ($m = 4n$) | Undirected | **Kruskal** | *Sorts few edges; DSU is cache-friendly and very fast.* / *Сортирует мало ребер; DSU работает очень быстро в памяти.* |
| **MST** | Dense ($m = 20n$) | Undirected | **Prim (or Kruskal with early exit)** | *Prim grows tree directly; Kruskal exits after scanning 15-20% edges.* / *Прим растит дерево без сортировки всех ребер.* |
| **SCC / Deadlocks** | Any directed | Cycles possible | **Kosaraju (iterative)** | *Guaranteed $O(V + E)$ without recursion stack overflow.* / *Линейная сложность $O(V + E)$ без переполнения стека.* |
| **Topological Sort** | Directed graph | Unknown | **Kahn** | *Computes valid order and detects cycles simultaneously.* / *Одновременно строит порядок и надежно находит циклы.* |
| **Shortest Paths** | Directed acyclic | Acyclic (DAG) | **DAG Shortest Paths** | *Linear $O(V + E)$ time; permits negative weights safely.* / *Работает за $O(V + E)$ и безопасен при отрицательных весах.* |

---

### 7.2. 10 Practical Industry Scenarios

1. **City Road Planning (Lowest asphalt cost connecting districts)**:
   - *Algorithm*: Kruskal MST.
   - *English (B1)*: Road networks are planar and sparse ($m \approx 3n$); Kruskal sorts edges quickly with low memory overhead.
   - *Русский*: Дорожные сети планарны и разрежены ($m \approx 3n$); Краскал быстро сортирует ребра с минимальным расходом памяти.
2. **Job Scheduler (Task dependencies with prerequisites)**:
   - *Algorithm*: Kahn's Algorithm.
   - *English (B1)*: Topologically orders tasks and detects circular deadlock dependencies immediately.
   - *Русский*: Строит порядок задач и сразу выявляет взаимные циклические блокировки (deadlocks).
3. **Deadlock Detection in Operating Systems (Wait-For Graph)**:
   - *Algorithm*: Kosaraju SCC.
   - *English (B1)*: Any component with more than 1 vertex or a self-loop represents a set of mutually blocked processes.
   - *Русский*: Любая SCC из более чем 1 вершины указывает на группу взаимно заблокированных процессов.
4. **Fastest Route in Build Pipeline (DAG with task durations)**:
   - *Algorithm*: DAG Shortest Paths (Critical Path).
   - *English (B1)*: Build tasks form a DAG; relaxing topological order computes completion milestones in $O(V + E)$.
   - *Русский*: Граф сборки ацикличен; релаксация в топологическом порядке находит время этапов за $O(V + E)$.
5. **Streaming Edges (Edges arrive one by one over network)**:
   - *Algorithm*: Disjoint Set Union (DSU).
   - *English (B1)*: DSU answers connectivity queries in near $O(1)$ time per arriving edge.
   - *Русский*: DSU позволяет проверять связность за $O(1)$ при поступлении каждого нового ребра.
6. **Water Supply Pipe Network (Connecting city districts with valves)**:
   - *Algorithm*: Prim's MST.
   - *English (B1)*: Grows network outward from central pump stations, avoiding sorting when density is high.
   - *Русский*: Растит трубопровод от центральной насосной станции, не требуя сортировки всех труб.
7. **Social Network Isolated Communities**:
   - *Algorithm*: Kosaraju SCC.
   - *English (B1)*: Partitions directed user followings into mutually interacting circles.
   - *Русский*: Разбивает ориентированный граф подписок на группы взаимно общающихся пользователей.
8. **Compiler Optimization (Instruction reordering)**:
   - *Algorithm*: Kahn's Algorithm.
   - *English (B1)*: Schedules assembly instructions respecting register data dependencies.
   - *Русский*: Планирует порядок машинных инструкций с учетом зависимостей по регистрам.
9. **Financial Arbitrage Detection (Currency exchange multipliers)**:
   - *Algorithm*: Bellman-Ford (General Graph, NOT DAG).
   - *English (B1)*: Currency graphs have cycles; Bellman-Ford is required to detect negative log-cycles.
   - *Русский*: Граф валют содержит циклы, поэтому для поиска арбитража нужен Беллман-Форд, а не DAG.
10. **Game AI Navigation with Negative Elevation Costs**:
    - *Algorithm*: DAG Shortest Paths (if level is strictly forward/downward).
    - *English (B1)*: Handles negative terrain costs in $O(V + E)$ where Dijkstra fails.
    - *Русский*: Находит путь при наличии отрицательных весов высот за $O(V + E)$, где Дейкстра ломается.

---

# PART 8: COMPREHENSIVE THEORY GLOSSARY

- **Graph (Граф)**:
  - *English (B1)*: A mathematical structure of vertices $V$ and edges $E$. Example: `Graph` storing adjacency lists `List<List<Edge>>`.
  - *Русский*: Математическая структура из вершин $V$ и ребер $E$. Пример: наш класс `Graph`.
- **Adjacency List vs Matrix (Списки смежности против матрицы)**:
  - *English (B1)*: Adjacency list stores only existing edges taking $O(V + E)$ memory; matrix always takes $O(V^2)$ memory.
  - *Русский*: Списки смежности хранят только существующие ребра в $O(V + E)$ памяти; матрица всегда требует $O(V^2)$.
- **Minimum Spanning Tree (MST, Минимальное остовное дерево)**:
  - *English (B1)*: A tree of $n - 1$ edges connecting all $n$ vertices with minimum possible total weight.
  - *Русский*: Ацикличный подграф из $n - 1$ ребер, связывающий все $n$ вершин с минимальным суммарным весом.
- **Spanning Forest (Остовный лес)**:
  - *English (B1)*: Collection of spanning trees for a disconnected graph, containing $n - c$ edges for $c$ components.
  - *Русский*: Набор остовных деревьев для несвязного графа из $n - c$ ребер для $c$ компонент связности.
- **Cut (Разрез графа)**:
  - *English (B1)*: A partition of vertex set $V$ into two subsets $S$ and $V \setminus S$.
  - *Русский*: Разбиение множества вершин $V$ на два непересекающихся подмножества $S$ и $V \setminus S$.
- **Path Compression (Сжатие пути)**:
  - *English (B1)*: A DSU optimization where every visited node points directly to the root during `find()`.
  - *Русский*: Эвристика DSU, привязывающая все посещенные вершины напрямую к корню во время `find()`.
- **Union by Rank (Объединение по рангу)**:
  - *English (B1)*: Attaching the root of smaller tree depth under the root of larger tree depth during `union()`.
  - *Русский*: Присоединение корня дерева меньшей глубины к корню большей глубины во время `union()`.
- **$\\alpha(n)$ (Inverse Ackermann Function)**:
  - *English (B1)*: An extremely slow-growing function, bounded by 4 for any practical input size.
  - *Русский*: Сверхмедленно растущая функция, не превышающая 4 для любых практических размеров данных.
- **Strongly Connected Component (SCC)**:
  - *English (B1)*: A maximal subgraph where every vertex can reach every other vertex along directed edges.
  - *Русский*: Максимальный подграф, в котором между любыми двумя вершинами есть ориентированный путь в обе стороны.
- **Topological Order (Топологический порядок)**:
  - *English (B1)*: A linear ordering of vertices such that for every directed edge $u \to v$, $u$ comes before $v$.
  - *Русский*: Линейный порядок вершин, в котором для каждого ребра $u \to v$ вершина $u$ расположена строго раньше $v$.
- **Edge Relaxation (Релаксация ребра)**:
  - *English (B1)*: Checking if $dist[u] + weight < dist[v]$ and updating $dist[v]$ to the smaller value.
  - *Русский*: Проверка условия $dist[u] + weight < dist[v]$ с обновлением расстояния до вершины $v$.

---

# PART 9: 5-MINUTE ORAL DEFENSE SPEECH (BILINGUAL SCRIPT)

### Opening & Project Setup (0:00 - 1:00)
- *English (B1)*:
  "Good day, teacher. Today I present Assignment 3 on Graph Algorithms. In this project, I built a complete graph package in Java with Disjoint Set Union, Kruskal, Prim, Kosaraju, Kahn, and DAG Shortest Paths. All algorithms run on one unified adjacency list graph class. I will now run `mvn clean test` to show that all 13 JUnit tests pass. Then, I run our benchmark to reproduce all reference values across 10 graphs."
- *Русский*:
  «Здравствуйте. Сегодня я защищаю работу №3 по графам. Я реализовал DSU, алгоритмы Краскала, Прима, Косарайю, Кана и кратчайшие пути в DAG на единых списках смежности. Сейчас я запускаю `mvn clean test`, чтобы показать успешное прохождение всех 13 тестов, а затем бенчмарк для сверки эталонных значений на 10 графах».

### Benchmark & Reference Numbers (1:00 - 2:00)
- *English (B1)*:
  "As you can see on the terminal, all 50 benchmark runs finished successfully. On the largest graph of 100 000 vertices and 2 million edges, Kruskal and Prim both produce the exact MST weight of 3 056 681. Kosaraju finds exactly 1 SCC, and DAG shortest paths sum to 23 512 087. Every single number matches the PDF reference table 100%. Furthermore, Kosaraju performs exactly 2m edge scans, while Kahn and DAG relaxation perform exactly m scans."
- *Русский*:
  «Все 50 прогонов завершились успешно. На графе из 100 000 вершин и 2 млн ребер Краскал и Прим выдают точный вес MST 3 056 681. Косарайю находит ровно 1 SCC, а сумма путей равна 23 512 087. Все числа на 100% совпадают с таблицей задания. Косарайю делает ровно 2m просмотров ребер, а Кан и релаксация в DAG — ровно m».

### Plots & Empirical Complexity (2:00 - 3:30)
- *English (B1)*:
  "Now look at our three plots. In Plot 1, operations scale linearly with n on log-log axes. Kosaraju and Kahn show an exact slope of 1.000, while Kruskal has a slope of 1.057 due to sorting. Dense graphs are shifted upward by a factor of 5 because m is 20n versus 4n. In Plot 2, scans per edge are flat horizontal lines at 2.0 and 1.0, proving constant work per edge. In Plot 3, Kruskal examines 99% of edges on sparse graphs, but only 20% on dense graphs because the MST connects quickly."
- *Русский*:
  «Посмотрим на три графика. На первом графике на лог-лог осях Косарайю и Кан имеют наклон ровно 1.000, подтверждая линейность, а Краскал — 1.057 из-за сортировки. Плотные графы лежат выше в 5 раз из-за соотношения ребер 20n к 4n. На втором графике прямые строго горизонтальны на 2.0 и 1.0. На третьем графике Краскал просматривает 99% ребер на разреженных графах и лишь 20% на плотных».

### Algorithm Choice & Conclusion (3:30 - 5:00)
- *English (B1)*:
  "For algorithm selection: on sparse graphs, Kruskal is preferred due to fast sorting and low DSU overhead. On dense graphs, Prim avoids sorting all edges. Kosaraju with an explicit iterative stack is safe for 100 000 vertices without stack overflow. Finally, DAG shortest paths run in linear O(V + E) time from a super-source and handle negative weights safely because DAGs have no cycles. Thank you, I am ready for questions."
- *Русский*:
  «О выборе алгоритмов: на разреженных графах выгоден Краскал из-за быстрой сортировки и легкости DSU. На плотных графах Прим выигрывает, не сортируя все ребра. Косарайю на явном стеке защищен от переполнения стека на 100 000 вершин. Кратчайшие пути в DAG работают за O(V + E) и безопасны для отрицательных весов из-за отсутствия циклов. Спасибо, я готов отвечать на вопросы».

# PART 10: 60+ TEACHER DEFENSE EXAMINATION QUESTIONS & ANSWERS

---

### Group A: How Algorithms Work (20 Questions)

1. **QUESTION**: Show on a small graph how Kruskal checks for cycles.  
   - **SHORT B1 ANSWER**: Kruskal calls `find(u)` and `find(v)`. If both roots are equal, $u$ and $v$ already share a path in the forest. Adding the edge would close a cycle, so we skip it.  
   - **РУССКИЙ**: Краскал вызывает `find(u)` и `find(v)`. Если корни равны, между вершинами уже есть путь, и ребро создаст цикл.

2. **QUESTION**: Why does Kruskal sort edges first?  
   - **SHORT B1 ANSWER**: By the Cut Property, the globally lightest edge that does not form a cycle is always safe to add to an MST. Sorting allows us to greedily consider edges in increasing order of weight.  
   - **РУССКИЙ**: По свойству разреза легчайшее доступное ребро без цикла безопасно добавлять в MST. Сортировка позволяет жадно брать ребра по возрастанию веса.

3. **QUESTION**: Why does Prim skip an edge if both endpoints are visited?  
   - **SHORT B1 ANSWER**: If both endpoints are already in `visited`, adding this edge connects two vertices already in the tree. That would form a cycle and violate the tree definition.  
   - **РУССКИЙ**: Если оба конца ребра уже в дереве, его добавление замкнет цикл, нарушая структуру остова.

4. **QUESTION**: Does the starting vertex change the total weight of Prim's MST?  
   - **SHORT B1 ANSWER**: No, total MST weight is identical regardless of the start vertex. The Cut Property guarantees that the set of edge weights in any MST is unique for distinct weights.  
   - **РУССКИЙ**: Нет, общий вес остова не меняется, так как свойство разреза гарантирует минимальность веса дерева при любом старте.

5. **QUESTION**: What does the array `rank[]` store in DSU?  
   - **SHORT B1 ANSWER**: `rank[x]` stores an upper bound on the height of the tree rooted at $x$. It only increases when two trees of identical rank are merged.  
   - **РУССКИЙ**: `rank[x]` хранит верхнюю границу высоты дерева с корнем в $x$. Он увеличивается на 1 только при объединении деревьев равного ранга.

6. **QUESTION**: What does path compression do in DSU?  
   - **SHORT B1 ANSWER**: During `find(x)`, it flattens the tree by re-pointing every traversed node directly to the root leader. Subsequent finds take $O(1)$ time.  
   - **РУССКИЙ**: Во время `find(x)` сжатие пути перепривязывает все вершины на пути напрямую к корню, ускоряя следующие поиски до $O(1)$.

7. **QUESTION**: Why does Kosaraju use two DFS passes?  
   - **SHORT B1 ANSWER**: Pass 1 computes the finish order, identifying source components in the condensation graph. Pass 2 runs on the reversed graph $G^T$, isolating each sink component so DFS cannot leak into other SCCs.  
   - **РУССКИЙ**: 1-й проход находит компоненты-источники. 2-й проход идет по $G^T$, где источники стали стоками, поэтому DFS изолирован внутри одной SCC.

8. **QUESTION**: Why do we reverse the graph in Kosaraju?  
   - **SHORT B1 ANSWER**: Reversing all edges flips the condensation DAG. The component that finished last in Pass 1 becomes a sink in $G^T$, trapping DFS inside that single SCC.  
   - **РУССКИЙ**: Разворот ребер превращает компоненту с наибольшим временем выхода в сток, блокируя выход DFS наружу.

9. **QUESTION**: How does your iterative Kosaraju know when a vertex is finished?  
   - **SHORT B1 ANSWER**: We maintain an edge index array `stackEdgeIdx`. When `edgeIdx == neighbors.size()`, all outgoing edges have been explored, and the vertex is popped into `finishOrder`.  
   - **РУССКИЙ**: Массив `stackEdgeIdx` отслеживает номер соседа. Когда индекс достигает `neighbors.size()`, вершина завершена и добавляется в `finishOrder`.

10. **QUESTION**: How does Kahn's algorithm detect a directed cycle?  
    - **SHORT B1 ANSWER**: Vertices in a directed cycle always have in-degree $\ge 1$. They never enter the queue, so the output size is strictly less than $n$.  
    - **РУССКИЙ**: Вершины в цикле всегда имеют in-degree $\ge 1$, не попадают в очередь, и итоговый список содержит меньше $n$ вершин.

11. **QUESTION**: What happens in Kahn if the graph has multiple in-degree 0 vertices?  
    - **SHORT B1 ANSWER**: All of them are pushed into the queue at initialization. They can be processed in any arbitrary order, producing one of several valid topological orderings.  
    - **РУССКИЙ**: Все они сразу попадают в очередь и могут обрабатываться в любом порядке, давая один из допустимых топологических порядков.

12. **QUESTION**: Why is topological order sufficient to compute shortest paths in a DAG in one pass?  
    - **SHORT B1 ANSWER**: In topological order, all predecessors of vertex $v$ appear before $v$. When we reach $v$, all incoming paths have been fully relaxed, so $dist[v]$ is finalized.  
    - **РУССКИЙ**: В топологическом порядке все предки вершины $v$ идут раньше нее. К моменту визита $v$ все входящие пути уже релаксированы.

13. **QUESTION**: Why are negative edge weights safe in DAG shortest paths?  
    - **SHORT B1 ANSWER**: Negative cycles cannot exist because DAGs are strictly acyclic. Vertices are relaxed in dependency order without cycles, so negative edges cause no infinite loops.  
    - **РУССКИЙ**: Отрицательные циклы невозможны из-за ацикличности DAG. Релаксация идет в топологическом порядке без риска бесконечного зацикливания.

14. **QUESTION**: What is the purpose of the virtual super-source in DAG shortest paths?  
    - **SHORT B1 ANSWER**: Setting $dist[i] = 0$ for all vertices with in-degree 0 allows single-pass shortest path calculation to reach all disconnected DAG branches uniformly.  
    - **РУССКИЙ**: Назначение $dist[i]=0$ для всех вершин с in-degree=0 позволяет обойти все несвязные ветви DAG за один проход.

15. **QUESTION**: How do you reconstruct the shortest path using `parent[]`?  
    - **SHORT B1 ANSWER**: Start at target vertex $v$ and repeatedly step to $parent[curr]$ until reaching $-1$. Then reverse the collected list to obtain the forward path.  
    - **РУССКИЙ**: Начиная с целевой вершины $v$, переходим по $parent[curr]$ до $-1$, а затем разворачиваем список.

16. **QUESTION**: What does Kruskal do on a disconnected graph?  
    - **SHORT B1 ANSWER**: Kruskal processes all valid edges and builds an MST for each connected component, returning a minimum spanning forest with $n - c$ edges.  
    - **РУССКИЙ**: Краскал обрабатывает ребра и строит MST для каждой компоненты, возвращая остовный лес из $n - c$ ребер.

17. **QUESTION**: What does Prim do on a disconnected graph in your project?  
    - **SHORT B1 ANSWER**: An outer loop iterates from $0$ to $n-1$. Whenever it finds an unvisited vertex, it increments component count and starts a new tree.  
    - **РУССКИЙ**: Внешний цикл от $0$ до $n-1$ находит непосещенные вершины и строит новые деревья, формируя остовный лес.

18. **QUESTION**: Why do we need BOTH path compression and union by rank in DSU?  
    - **SHORT B1 ANSWER**: Rank alone gives $O(\log n)$. Path compression alone gives $O(\log n)$ amortized. Together, they achieve the inverse Ackermann bound $O(\\alpha(n)) \le 4$.  
    - **РУССКИЙ**: Только ранг дает $O(\log n)$, только сжатие — $O(\log n)$. Вместе они достигают границы Аккермана $O(\\alpha(n)) \le 4$.

19. **QUESTION**: How are undirected edges represented in your `Graph` class?  
    - **SHORT B1 ANSWER**: We add two directed edge records: $u \to v$ in list $u$, and $v 	o u$ in list $v$, while storing one copy in `allEdges`.  
    - **РУССКИЙ**: В списках смежности сохраняются два направленных ребра $u \to v$ и $v 	o u$, а в списке `allEdges` — одно ребро.

20. **QUESTION**: What is the difference between a tree and a DAG?  
    - **SHORT B1 ANSWER**: A tree has exactly one parent per node (except root) and no cycles. A DAG has no directed cycles, but a node may have multiple parents.  
    - **РУССКИЙ**: В дереве у каждой вершины (кроме корня) ровно один предок. В DAG нет циклов, но у вершины может быть несколько входящих ребер.

---

### Group B: Plots & Slopes (15 Questions)

21. **QUESTION**: Why are log-log axes used in Plot 1?  
    - **SHORT B1 ANSWER**: Log-log axes transform polynomial curves $y = c \cdot n^k$ into straight lines $\ln y = k \ln n + \ln c$, where the slope directly equals complexity degree $k$.  
    - **РУССКИЙ**: Лог-лог шкала превращает полиномиальную сложность в прямую линию, где наклон равен степени сложности $k$.

22. **QUESTION**: What does a slope of 1.00 mean on a log-log plot?  
    - **SHORT B1 ANSWER**: Slope 1.00 proves that doubling the input size $n$ doubles the operations, which is the definition of linear complexity $O(n)$.  
    - **РУССКИЙ**: Наклон 1.00 доказывает, что при удвоении $n$ число операций удваивается, подтверждая линейную сложность $O(n)$.

23. **QUESTION**: Why is the dense curve above the sparse curve in Plot 1?  
    - **SHORT B1 ANSWER**: Dense graphs have $m = 20n$, which is 5 times more edges than sparse graphs ($m = 4n$). Graph operations scale with edges, shifting the line up.  
    - **РУССКИЙ**: На плотных графах $m = 20n$ (в 5 раз больше, чем $m = 4n$). Число операций зависит от $m$, сдвигая кривую вверх.

24. **QUESTION**: Why are the curves for Kosaraju and Kahn parallel in Plot 1?  
    - **SHORT B1 ANSWER**: Both algorithms have the exact same time complexity $O(V + E)$ and slope 1.00. Parallel lines mean equal asymptotic growth rate.  
    - **РУССКИЙ**: Оба алгоритма имеют одинаковую сложность $O(V + E)$ и наклон 1.00. Параллельность означает одинаковую скорость роста.

25. **QUESTION**: Why is Kosaraju higher than Kahn on the same graph?  
    - **SHORT B1 ANSWER**: Kosaraju performs two DFS passes ($2m$ edge scans), while Kahn performs one pass ($m$ edge scans). Double the operations places it higher.  
    - **РУССКИЙ**: Косарайю делает два прохода DFS ($2m$ сканов), а Кан — один ($m$ сканов), поэтому его кривая лежит выше.

26. **QUESTION**: Why is the slope of Kruskal slightly steeper than 1.00?  
    - **SHORT B1 ANSWER**: Kruskal sorts edges, taking $O(E \log E)$ comparisons. The logarithmic factor $\log E$ makes the slope $k \approx 1.057$ instead of 1.000.  
    - **РУССКИЙ**: Краскал сортирует ребра за $O(E \log E)$. Логарифмический множитель делает наклон $k \approx 1.057$ круче единицы.

27. **QUESTION**: Why are lines in Plot 2 completely flat horizontal?  
    - **SHORT B1 ANSWER**: In Plot 2, Y-axis is $edgeScans / m$. For Kahn and DAG paths, it is strictly 1.0. For Kosaraju, strictly 2.0. Flat lines prove constant work per edge.  
    - **РУССКИЙ**: По оси Y отложено $edgeScans / m$. Значения строго постоянны (1.0 и 2.0), что доказывает константный объем работы на ребро.

28. **QUESTION**: What would Plot 2 look like if Kahn had an $O(V \cdot E)$ bug?  
    - **SHORT B1 ANSWER**: The line would not be flat; it would climb steeply upward as a linear function of $n$.  
    - **РУССКИЙ**: Прямая перестала бы быть горизонтальной и пошла бы вверх пропорционально росту $n$.

29. **QUESTION**: Why do values in Plot 2 not depend on the computer hardware?  
    - **SHORT B1 ANSWER**: Plot 2 plots algorithmic counters (edge visits), not clock milliseconds. Counters depend purely on the graph structure and code logic.  
    - **РУССКИЙ**: На графике 2 отображаются программные счетчики посещения ребер, не зависящие от процессора и ОС.

30. **QUESTION**: Why does Kruskal examine only 20% of edges in dense graphs in Plot 3?  
    - **SHORT B1 ANSWER**: In dense graphs ($m = 20n$), there is an abundance of cheap edges. Kruskal collects $n - 1$ edges rapidly and exits early.  
    - **РУССКИЙ**: В плотных графах много дешевых ребер, поэтому Краскал быстро набирает $n - 1$ ребер и досрочно завершает цикл.

31. **QUESTION**: Why does Kruskal examine almost 100% of edges in sparse graphs in Plot 3?  
    - **SHORT B1 ANSWER**: With $m = 4n$, edges are scarce. Kruskal must inspect almost all sorted edges before finding enough bridges to connect all vertices.  
    - **РУССКИЙ**: При $m = 4n$ ребер мало, поэтому Краскал вынужден просмотреть почти все ребра, чтобы соединить компоненты.

32. **QUESTION**: How does Plot 3 explain where Kruskal wins against Prim?  
    - **SHORT B1 ANSWER**: On dense graphs, Kruskal terminates after inspecting a small fraction of edges. However, it still had to sort all $m$ edges beforehand.  
    - **РУССКИЙ**: На плотных графах Краскал рано останавливается, но вынужден предварительно сортировать весь массив ребер.

33. **QUESTION**: How did you calculate the slope of Kosaraju on the whiteboard?  
    - **SHORT B1 ANSWER**: $	ext{slope} = rac{\ln(y_2) - \ln(y_1)}{\ln(x_2) - \ln(x_1)}$. Using $n=1\,000 	o 8\,000$ and $n=100\,000 	o 800\,000$, $rac{\ln(100)}{\ln(100)} = 1.000$.  
    - **РУССКИЙ**: По формуле логарифмической регрессии между крайними точками отношение дает ровно 1.000.

34. **QUESTION**: What would Plot 1 look like for an $O(n^2)$ algorithm?  
    - **SHORT B1 ANSWER**: It would be a straight line with slope $k = 2.00$, which is twice as steep as our linear algorithms.  
    - **РУССКИЙ**: Это была бы прямая линия с наклоном $k = 2.00$, что вдвое круче наших линейных графиков.

35. **QUESTION**: Why is Prim's total operations higher than Kruskal on dense graphs?  
    - **SHORT B1 ANSWER**: In our lazy Prim, every edge is added to PriorityQueue, executing $O(E \log E)$ heap sift operations with high constant factors.  
    - **РУССКИЙ**: В ленивом Приме каждое ребро попадает в кучу, выполняя множество тяжелых операций просеивания.

---

### Group C: Complexity & Counters (15 Questions)

36. **QUESTION**: Why is Prim's time complexity $O(E \log V)$?  
    - **SHORT B1 ANSWER**: Each edge enters the priority queue. Queue operations take $O(\log |PQ|)$. Since $|PQ| \le V^2$, $\log(V^2) = 2 \log V = O(\log V)$. Total is $O(E \log V)$.  
    - **РУССКИЙ**: Каждое ребро добавляется в кучу размером до $V^2$. Операции с кучей стоят $O(\log V)$, давая суммарно $O(E \log V)$.

37. **QUESTION**: Why can we treat $\\alpha(n)$ as a constant in DSU?  
    - **SHORT B1 ANSWER**: For all practical values of $n$ up to $10^{80}$, $\\alpha(n) \le 4$. Therefore, it never exceeds 4 in any real computer memory.  
    - **РУССКИЙ**: Для любых $n \le 10^{80}$ значение $\\alpha(n) \le 4$, что практически эквивалентно константе.

38. **QUESTION**: What is the worst-case space complexity of Kosaraju?  
    - **SHORT B1 ANSWER**: $\Theta(V + E)$ to store the graph and its transpose, plus $O(V)$ for the explicit stack arrays and visited arrays.  
    - **РУССКИЙ**: $\Theta(V + E)$ для хранения прямого и транспонированного графов, плюс $O(V)$ для явного стека и массивов пометок.

39. **QUESTION**: What is the best-case time complexity of Kruskal?  
    - **SHORT B1 ANSWER**: Still $O(E \log E)$ because all $E$ edges must be sorted upfront before the loop starts.  
    - **РУССКИЙ**: Все равно $O(E \log E)$, так как сортировка всех ребер выполняется до входа в цикл.

40. **QUESTION**: Which exact line increments `dsuOps` in your project?  
    - **SHORT B1 ANSWER**: `findCalls++` at line 40 and `successfulUnions++` at line 71 in `DisjointSetUnion.java`. Added to metrics at line 53 in `KruskalMST.java`.  
    - **РУССКИЙ**: В `DisjointSetUnion.java` на строках 40 и 71, после чего суммируется в метрики в строке 53 `KruskalMST.java`.

41. **QUESTION**: Which exact line increments `edgeScans` in Kosaraju?  
    - **SHORT B1 ANSWER**: Line 48 during Pass 1 and line 101 during Pass 2 in `KosarajuSCC.java`, whenever an edge is fetched from adjacency lists.  
    - **РУССКИЙ**: Строки 48 и 101 в `KosarajuSCC.java` при извлечении каждого ребра из списка смежности.

42. **QUESTION**: Why does the ratio `edgeScans / (V + E)` equal 1.600 for Kosaraju on sparse graphs?  
    - **SHORT B1 ANSWER**: On sparse graphs $m = 4n$. Kosaraju scans $2m = 8n$ edges. $rac{8n}{n + 4n} = rac{8}{5} = \mathbf{1.600}$.  
    - **РУССКИЙ**: При $m = 4n$ Косарайю сканирует $2m = 8n$ ребер. Отношение к $(n+m)$ дает $rac{8n}{5n} = 1.600$.

43. **QUESTION**: Why does the ratio `edgeScans / (V + E)` equal 0.800 for DAG shortest paths on sparse graphs?  
    - **SHORT B1 ANSWER**: DAG shortest paths scan $m = 4n$ edges. $rac{4n}{n + 4n} = rac{4}{5} = \mathbf{0.800}$.  
    - **РУССКИЙ**: Релаксация сканирует $m = 4n$ ребер. Отношение дает $rac{4n}{5n} = 0.800$.

44. **QUESTION**: Why is wall-clock time mostly noise on the two smallest graphs?  
    - **SHORT B1 ANSWER**: Execution times are under 5 milliseconds. JVM JIT compilation, thread scheduling, and timer resolution cause large percentage variations.  
    - **РУССКИЙ**: На малых графах время меньше 5 мс, где доминируют задержки JIT, переключение потоков ОС и погрешность таймера.

45. **QUESTION**: Why did you use the median of 5 runs instead of the average?  
    - **SHORT B1 ANSWER**: The median is robust against statistical outliers caused by background OS tasks or garbage collection pauses.  
    - **РУССКИЙ**: Медиана устойчива к случайным выбросам, вызванным паузами сборщика мусора (GC) и фоновыми процессами.

46. **QUESTION**: What is the extra space complexity of Kahn's algorithm?  
    - **SHORT B1 ANSWER**: $O(V)$ auxiliary space for the `inDegree` array and the `ArrayDeque` queue.  
    - **РУССКИЙ**: $O(V)$ дополнительной памяти для массива `inDegree` и очереди `ArrayDeque`.

47. **QUESTION**: What is the complexity of DAG shortest paths without Kahn?  
    - **SHORT B1 ANSWER**: Finding topological order takes $O(V + E)$ via DFS or Kahn. Relaxation takes $O(V + E)$. Total remains $O(V + E)$.  
    - **РУССКИЙ**: Построение топологического порядка стоит $O(V + E)$, релаксация — $O(V + E)$, итоговая сложность остается $O(V + E)$.

48. **QUESTION**: Why is Prim with indexed min-heap faster than lazy Prim on dense graphs?  
    - **SHORT B1 ANSWER**: An indexed min-heap maintains at most $V$ elements via `decreaseKey`, avoiding $E$ edge insertions and stale edge overhead.  
    - **РУССКИЙ**: Индексированная куча хранит не более $V$ элементов за счет `decreaseKey`, избегая добавления $E$ ребер и мусорных извлечений.

49. **QUESTION**: What is the time complexity of building the graph from file?  
    - **SHORT B1 ANSWER**: Reading $m$ lines and appending edges takes $\Theta(V + E)$ time using `BufferedReader`.  
    - **РУССКИЙ**: Построчное чтение $m$ строк и добавление ребер в списки смежности занимает $\Theta(V + E)$.

50. **QUESTION**: Can Dijkstra run in $O(V + E)$ time on DAGs?  
    - **SHORT B1 ANSWER**: No, Dijkstra relies on a priority queue taking $O(E \log V)$. Processing in topological order eliminates the priority queue, achieving $O(V + E)$.  
    - **РУССКИЙ**: Нет, Дейкстра требует очередь с приоритетом $O(E \log V)$. Топологический порядок исключает кучу и дает $O(V + E)$.

---

### Group D: Code Mechanics & Edge Cases (10 Questions)

51. **QUESTION**: Why did you declare `dist` as `long[]` instead of `int[]` in `DagShortestPath`?  
    - **SHORT B1 ANSWER**: Summing path weights up to 100 000 vertices with edge weights up to 1000 can reach $10^8 - 10^9$, exceeding 32-bit signed integers in distance accumulators.  
    - **РУССКИЙ**: Суммарные расстояния могут накапливаться и вызывать переполнение 32-битного типа `int`.

52. **QUESTION**: Why did you use `ArrayDeque` instead of `LinkedList` in Kahn's algorithm?  
    - **SHORT B1 ANSWER**: `ArrayDeque` stores primitives in contiguous circular arrays without node object overhead, offering superior memory cache locality.  
    - **РУССКИЙ**: `ArrayDeque` основан на массиве, не создает лишних объектов узлов и намного лучше утилизирует процессорный кэш.

53. **QUESTION**: What happens if an input graph text file contains blank lines?  
    - **SHORT B1 ANSWER**: Line 97 in `Graph.java` skips blank lines using `while (line != null && line.trim().isEmpty()) line = reader.readLine();`.  
    - **РУССКИЙ**: Строка 97 в `Graph.java` аккуратно пропускает пустые строки.

54. **QUESTION**: How does your code handle an isolated vertex in Kruskal and Prim?  
    - **SHORT B1 ANSWER**: In Kruskal, it remains its own DSU component. In Prim, the outer loop visits it with no incident edges, counting it as a separate component of size 1.  
    - **РУССКИЙ**: В Краскале она остается отдельной компонентой DSU, а в Приме внешний цикл посещает ее без ребер, считая компонентой из 1 вершины.

55. **QUESTION**: What happens if a graph contains a negative cycle in `DagShortestPath`?  
    - **SHORT B1 ANSWER**: Before running shortest paths, line 24 runs Kahn's sort. If a cycle exists, it throws an `IllegalArgumentException("Граф содержит циклы и не является DAG!")`.  
    - **РУССКИЙ**: В строке 24 алгоритм Кана проверяет ацикличность и выбрасывает исключение при обнаружении цикла.

56. **QUESTION**: Why did you record `maxDepth` in Kosaraju?  
    - **SHORT B1 ANSWER**: The rubric requires measuring explicit stack depth to verify stack safety on $100\,000$ vertices without JVM stack overflow.  
    - **РУССКИЙ**: Рубрика требует фиксировать максимальную глубину явного стека для доказательства безопасности на 100 000 вершин.

57. **QUESTION**: How does `getTranspose()` avoid duplicating memory?  
    - **SHORT B1 ANSWER**: It reads edges from `allEdges` and creates reversed edge objects in a new adjacency list without copying original data structures.  
    - **РУССКИЙ**: Метод читает ребра из `allEdges` и сразу добавляет развернутые ребра в новый список смежности.

58. **QUESTION**: What does `Collections.unmodifiableList` achieve in your result classes?  
    - **SHORT B1 ANSWER**: It protects internal result lists from being modified from outside, preserving immutability and thread safety.  
    - **РУССКИЙ**: Защищает внутренние списки от изменений извне, гарантируя неизменяемость данных.

59. **QUESTION**: Why did you use `new Random(42)` in the benchmark generator?  
    - **SHORT B1 ANSWER**: Fixed seed 42 makes the random sequence deterministic, reproducing identical graphs and reference values on all computers worldwide.  
    - **РУССКИЙ**: Фиксированный сид 42 гарантирует одинаковую псевдослучайную последовательность и точное совпадение чисел на любом ПК.

60. **QUESTION**: How does `isValidOrder` in `TopoResult` validate the topological sort?  
    - **SHORT B1 ANSWER**: It maps every vertex to its output index `pos[u]`. Then for every edge $u \to v$, it asserts that `pos[u] < pos[v]`.  
    - **РУССКИЙ**: Сохраняет позицию каждой вершины `pos[u]` и проверяет выполнение условия `pos[u] < pos[v]` для всех ребер графа.

---

# PART 11: 15 LIVE CODING DEFENSE TASKS WITH SOLUTIONS

---

### Task 1: Change Union by Rank to Union by Size in DSU
- **File**: `src/main/java/daa/dsu/DisjointSetUnion.java`
- **What to say (B1 English)**: *"I replace the rank array with a size array. When merging, the root with fewer elements attaches under the root with more elements, and we add their sizes."*
- **РУССКИЙ**: *«Заменяем массив рангов на массив размеров. Дерево меньшего размера цепляется к корню большего, а их размеры складываются»*.
- **Code Change**:
```java
// In DisjointSetUnion.java: replace int[] rank with int[] size:
public final void makeSet(int n) {
    this.parent = new int[n];
    this.size = new int[n];
    for (int i = 0; i < n; i++) {
        this.parent[i] = i;
        this.size[i] = 1; // initial size is 1
    }
}

// In union(x, y):
if (size[rootX] < size[rootY]) {
    parent[rootX] = rootY;
    size[rootY] += size[rootX];
} else {
    parent[rootY] = rootX;
    size[rootX] += size[rootY];
}
```

---

### Task 2: Remove Path Compression and Demonstrate Counter Changes
- **File**: `src/main/java/daa/dsu/DisjointSetUnion.java`
- **What to say (B1 English)**: *"I remove the assignment `parent[x] = ...` so it only returns the root without flattening the tree. The operation count increases noticeably."*
- **Code Change**:
```java
// Line 41 in DisjointSetUnion.java:
public int find(int x) {
    findCalls++;
    if (parent[x] != x) {
        return find(parent[x]); // NO path compression!
    }
    return parent[x];
}
```

---

### Task 3: Make Prim Print All MST Edges
- **File**: `src/main/java/daa/algorithms/PrimMST.java`
- **What to say (B1 English)**: *"I iterate over `mstEdges` and print each edge endpoints and weight."*
- **Code Snippet**:
```java
for (Edge e : primRes.getEdges()) {
    System.out.println("MST Edge: " + e.getU() + " - " + e.getV() + " (w=" + e.getWeight() + ")");
}
```

---

### Task 4: Print the Reconstructed Shortest Path in DAG
- **File**: `src/main/java/daa/algorithms/PathResult.java`
- **What to say (B1 English)**: *"I call `getPath(target)` and join the vertex sequence with arrows."*
- **Code Snippet**:
```java
List<Integer> path = pathResult.getPath(targetVertex);
System.out.println("Shortest path: " + path.stream().map(String::valueOf).collect(Collectors.joining(" -> ")));
```

---

### Task 5: Add Early Stop to Kruskal on First Component
- **File**: `src/main/java/daa/algorithms/KruskalMST.java`
- **What to say (B1 English)**: *"We break the edge loop as soon as `mstEdges.size() == n - 1` when the graph is connected."*
- **Code**:
```java
if (mstEdges.size() == n - 1 && dsu.components() == 1) {
    break;
}
```

---

### Task 6: Make Kahn Return the Cycle Vertices
- **File**: `src/main/java/daa/algorithms/KahnTopologicalSort.java`
- **What to say (B1 English)**: *"Vertices remaining with `inDegree[v] > 0` after the queue empties belong to cycles."*
- **Code Snippet**:
```java
List<Integer> cycleVertices = new ArrayList<>();
for (int i = 0; i < n; i++) {
    if (inDegree[i] > 0) cycleVertices.add(i);
}
System.out.println("Vertices in cycle: " + cycleVertices);
```

---

### Task 7: Implement Tarjan's SCC Algorithm (Bonus Task A)
- **What to say (B1 English)**: *"Tarjan finds SCCs in a single DFS pass using discovery `index`, `lowlink`, and an active node stack."*
- **Code**:
```java
int timer = 0;
int[] index = new int[n];
int[] lowlink = new int[n];
boolean[] onStack = new boolean[n];
Deque<Integer> stack = new ArrayDeque<>();
Arrays.fill(index, -1);

void dfsTarjan(int u) {
    index[u] = lowlink[u] = timer++;
    stack.push(u);
    onStack[u] = true;

    for (Edge e : graph.getAdj(u)) {
        int v = e.getV();
        if (index[v] == -1) {
            dfsTarjan(v);
            lowlink[u] = Math.min(lowlink[u], lowlink[v]);
        } else if (onStack[v]) {
            lowlink[u] = Math.min(lowlink[u], index[v]);
        }
    }

    if (lowlink[u] == index[u]) {
        List<Integer> scc = new ArrayList<>();
        while (true) {
            int v = stack.pop();
            onStack[v] = false;
            scc.add(v);
            if (u == v) break;
        }
        components.add(scc);
    }
}
```

---

# PART 12: WEAK PLACES, CRITICISMS & DEFENSES

1. **Package Declarations & POM `<sourceDirectory>`**:
   - *Criticism*: Standard Maven projects place source roots at `src/main/java`. Adding `<sourceDirectory>src</sourceDirectory>` in `pom.xml` forced packages to become `main.java.daa`.
   - *Defense (B1)*: *"During IDE workspace setup, the language server required `src` as the base directory. The Maven compiler plugin was configured with explicit inclusion filters, ensuring clean compilation and 100% test success."*
2. **README Run Command Classpath**:
   - *Criticism*: If a student copies `java -Xmx4g -cp target/classes daa.Main`, it fails with `ClassNotFoundException`.
   - *Defense (B1)*: *"The correct command is `java -Xmx4g -cp target/classes main.java.daa.Main`. Both the script and README are updated to reflect the full package name."*
3. **Lazy Prim Memory Usage**:
   - *Criticism*: Lazy Prim inserts all incident edges into `PriorityQueue`, resulting in up to $E$ elements in memory.
   - *Defense (B1)*: *"While indexed min-heap bounds size to $V$, lazy Prim has identical asymptotic time complexity $O(E \log V)$ and is cleaner to maintain without custom indexed heap structures."*
4. **ZIP Archive Packaging**:
   - *Criticism*: Old ZIP file in root was generated before the latest package commit.
   - *Defense (B1)*: *"We re-packed the submission ZIP directly from branch `main` at tag `v1.0`, ensuring total synchronization with GitHub."*

---

# MASTER CHEAT SHEET: 25 GOLDEN DEFENSE RULES

1. **DSU Complexity**: $O(\\alpha(n))$ amortized per operation; $\\alpha(n) \le 4$ for all practical inputs.
2. **Both Heuristics**: Union by rank bounds tree height; path compression flattens the tree. Both are required for $\\alpha(n)$.
3. **Cut Property**: The lightest edge crossing any cut is always safe to include in an MST.
4. **Kruskal vs Prim Equal Weight**: Kruskal and Prim always produce the exact same total MST weight.
5. **Kruskal Early Stop**: On connected graphs, Kruskal stops as soon as $n - 1$ edges are added.
6. **Kruskal Edge Share**: Inspects $\approx 99\%$ of edges on sparse graphs, but only $\approx 20\%$ on dense graphs.
7. **Prim PriorityQueue**: Skips stale edges where both endpoints are already visited.
8. **Prim Undirected Scans**: Scans $2m$ edges because each undirected edge is checked from both endpoints.
9. **Kosaraju Two Passes**: Pass 1 computes finish order; Pass 2 explores $G^T$ in decreasing finish order.
10. **Why Transpose in Kosaraju**: Turns the source SCC into a sink SCC, trapping Pass 2 DFS inside one component.
11. **Iterative DFS**: Uses heap-allocated int arrays (`stackVertex`, `stackEdgeIdx`) to survive 100 000 vertices without stack overflow.
12. **Kahn Cycle Detection**: A directed cycle prevents vertices from reaching in-degree 0; output size $< n$ indicates a cycle.
13. **Kahn Topological Condition**: For every edge $u \to v$, position of $u$ is strictly less than position of $v$ ($pos[u] < pos[v]$).
14. **DAG Shortest Paths**: Relaxing edges in topological order guarantees optimal paths in a single $O(V + E)$ pass.
15. **Virtual Super-Source**: All in-degree 0 vertices start at distance 0, reaching all DAG branches uniformly.
16. **Negative Weights in DAG**: Safe because DAGs contain no cycles, making negative cycles impossible.
17. **Kosaraju Scan Rule**: `edgeScans == 2 * m` strictly holds across all graphs.
18. **Kahn & DAG Scan Rule**: `edgeScans == m` strictly holds across all graphs.
19. **Log-Log Plot Slope**: Slope 1.00 confirms linear scaling $O(n)$; slope 1.057 reflects the $E \log E$ sorting factor.
20. **Parallel Log-Log Curves**: Equal slopes indicate the same asymptotic complexity class.
21. **Dense vs Sparse Shift**: Dense graphs ($m = 20n$) sit 5 times higher than sparse graphs ($m = 4n$).
22. **Flat Scan Curve**: A horizontal line at $edgeScans / m$ proves constant work per edge.
23. **Wall-Clock Time Noise**: Milliseconds on small graphs are dominated by JVM JIT warmup and OS jitter; operation counters are the scientific truth.
24. **When Kruskal Wins**: On sparse graphs ($m \approx n$) and when edges are already sorted or edge lists are provided.
25. **When Prim Wins**: On dense graphs ($m \approx n^2$) where avoiding edge sorting provides superior performance.

---

### Complexity Summary Block

```
+---------------------------------------------------------------------------------+
| Operation / Algorithm  | Time Complexity     | Extra Space  | Status            |
+---------------------------------------------------------------------------------+
| DSU find / union       | O(\alpha(n))         | O(n)         | Near-constant <= 4|
| Kruskal MST            | O(E log E)          | O(V + E)     | Sort + DSU        |
| Prim MST (Lazy)        | O(E log V)          | O(V + E)     | PriorityQueue     |
| Kosaraju SCC           | Theta(V + E)        | Theta(V + E) | 2-Pass Iterative  |
| Kahn TopoSort          | Theta(V + E)        | Theta(V + E) | In-Degree + Queue |
| DAG Shortest Paths     | Theta(V + E)        | Theta(V)     | 1-Pass Relaxation |
+---------------------------------------------------------------------------------+
```
