package daa.algorithms;

import daa.graph.Edge;
import daa.graph.Graph;
import daa.metrics.Metrics;

import java.util.Collections;
import java.util.List;

/**
 * Результат топологической сортировки алгоритмом Кана.
 */
public class TopoResult {
    private final boolean hasCycle;
    private final List<Integer> order;
    private final Metrics metrics;

    public TopoResult(boolean hasCycle, List<Integer> order, Metrics metrics) {
        this.hasCycle = hasCycle;
        this.order = order;
        this.metrics = metrics;
    }

    public boolean hasCycle() {
        return hasCycle;
    }

    public List<Integer> getOrder() {
        return order != null ? Collections.unmodifiableList(order) : Collections.emptyList();
    }

    public Metrics getMetrics() {
        return metrics;
    }

    /**
     * Валидация порядка: для каждого ребра u -> v позиция u должна быть меньше позиции v.
     */
    public boolean isValidOrder(Graph graph) {
        if (hasCycle || order == null || order.size() != graph.getV()) {
            return false;
        }

        int[] pos = new int[graph.getV()];
        for (int i = 0; i < order.size(); i++) {
            pos[order.get(i)] = i;
        }

        for (Edge edge : graph.getAllEdges()) {
            if (pos[edge.getU()] >= pos[edge.getV()]) {
                return false;
            }
        }
        return true;
    }
}
