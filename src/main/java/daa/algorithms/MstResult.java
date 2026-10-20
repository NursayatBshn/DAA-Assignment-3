package daa.algorithms;

import daa.graph.Edge;
import daa.metrics.Metrics;

import java.util.Collections;
import java.util.List;

/**
 * Результат работы алгоритма построения минимального остовного дерева (леса).
 */
public class MstResult {
    private final long totalWeight;
    private final List<Edge> edges;
    private final int components;
    private final Metrics metrics;

    public MstResult(long totalWeight, List<Edge> edges, int components, Metrics metrics) {
        this.totalWeight = totalWeight;
        this.edges = edges;
        this.components = components;
        this.metrics = metrics;
    }

    public long getTotalWeight() {
        return totalWeight;
    }

    public List<Edge> getEdges() {
        return Collections.unmodifiableList(edges);
    }

    public int getComponents() {
        return components;
    }

    public Metrics getMetrics() {
        return metrics;
    }
}
