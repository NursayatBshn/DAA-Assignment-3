package main.java.daa.algorithms;

import main.java.daa.metrics.Metrics;

import java.util.Collections;
import java.util.List;

/**
 * Результат поиска компонент сильной связности (SCC).
 */
public class SccResult {
    private final int count;
    private final List<List<Integer>> components;
    private final int[] componentId;
    private final Metrics metrics;

    public SccResult(int count, List<List<Integer>> components, int[] componentId, Metrics metrics) {
        this.count = count;
        this.components = components;
        this.componentId = componentId;
        this.metrics = metrics;
    }

    public int getCount() {
        return count;
    }

    public List<List<Integer>> getComponents() {
        return Collections.unmodifiableList(components);
    }

    public int[] getComponentId() {
        return componentId;
    }

    public Metrics getMetrics() {
        return metrics;
    }
}
