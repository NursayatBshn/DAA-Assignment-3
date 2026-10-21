package main.java.daa.algorithms;

import main.java.daa.graph.Edge;
import main.java.daa.graph.Graph;
import main.java.daa.metrics.Metrics;

import java.util.Arrays;
import java.util.List;

/**
 * Алгоритм поиска кратчайших путей в бесконтурном ориентированном графе (DAG).
 * Время работы: O(V + E) за один проход релаксации по топологическому порядку.
 * Допускает отрицательные веса ребер (так как циклов в DAG нет).
 */
public class DagShortestPath {

    /**
     * Поиск кратчайших путей от виртуального супер-источника
     * (все вершины с in-degree = 0 имеют начальное расстояние 0).
     */
    public static PathResult findShortestPaths(Graph graph) {
        TopoResult topoResult = KahnTopologicalSort.sort(graph);
        if (topoResult.hasCycle()) {
            throw new IllegalArgumentException("Граф содержит циклы и не является DAG!");
        }
        return findShortestPaths(graph, topoResult.getOrder());
    }

    /**
     * Поиск кратчайших путей с уже готовым топологическим порядком вершин.
     */
    public static PathResult findShortestPaths(Graph graph, List<Integer> topoOrder) {
        Metrics metrics = new Metrics();
        int n = graph.getV();

        // Находим полустепени захода для определения источников
        int[] inDegree = new int[n];
        for (Edge edge : graph.getAllEdges()) {
            inDegree[edge.getV()]++;
        }

        long[] dist = new long[n];
        int[] parent = new int[n];
        Arrays.fill(dist, Long.MAX_VALUE);
        Arrays.fill(parent, -1);

        // Все вершины с in-degree == 0 стартуют с расстояния 0 (виртуальный супер-источник)
        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) {
                dist[i] = 0;
            }
        }

        // Проход по топологическому порядку и релаксация исходящих ребер
        for (int u : topoOrder) {
            if (dist[u] == Long.MAX_VALUE) {
                continue;
            }

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

        // Считаем сумму всех расстояний
        long sumDistances = 0;
        for (int i = 0; i < n; i++) {
            if (dist[i] != Long.MAX_VALUE) {
                sumDistances += dist[i];
            }
        }

        return new PathResult(dist, parent, sumDistances, metrics);
    }
}
