package main.java.daa.algorithms;

import main.java.daa.metrics.Metrics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Результат поиска кратчайших путей в DAG.
 */
public class PathResult {
    private final long[] dist;
    private final int[] parent;
    private final long sumDistances;
    private final Metrics metrics;

    public PathResult(long[] dist, int[] parent, long sumDistances, Metrics metrics) {
        this.dist = dist;
        this.parent = parent;
        this.sumDistances = sumDistances;
        this.metrics = metrics;
    }

    public long[] getDist() {
        return dist;
    }

    public int[] getParent() {
        return parent;
    }

    public long getSumDistances() {
        return sumDistances;
    }

    public Metrics getMetrics() {
        return metrics;
    }

    /**
     * Восстановление пути от источника (вершины с in-degree = 0) до целевой вершины v.
     */
    public List<Integer> getPath(int v) {
        if (v < 0 || v >= parent.length || dist[v] == Long.MAX_VALUE) {
            return Collections.emptyList();
        }

        List<Integer> path = new ArrayList<>();
        int curr = v;
        while (curr != -1) {
            path.add(curr);
            curr = parent[curr];
        }
        Collections.reverse(path);
        return path;
    }
}
