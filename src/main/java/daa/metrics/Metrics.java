package main.java.daa.metrics;

/**
 * Класс для подсчета операций в алгоритмах.
 * dsuOps: вызовы find и union в DSU
 * edgeScans: число рассмотренных ребер
 * comparisons: число сравнений весов
 * maxDepth: максимальная глубина явного стека
 */
public class Metrics {
    private long dsuOps = 0;
    private long edgeScans = 0;
    private long comparisons = 0;
    private long maxDepth = 0;

    public void addDsuOps(long count) {
        this.dsuOps += count;
    }

    public void addEdgeScan() {
        this.edgeScans++;
    }

    public void addEdgeScans(long count) {
        this.edgeScans += count;
    }

    public void addComparison() {
        this.comparisons++;
    }

    public void addComparisons(long count) {
        this.comparisons += count;
    }

    public void updateMaxDepth(long depth) {
        if (depth > this.maxDepth) {
            this.maxDepth = depth;
        }
    }

    public long getDsuOps() {
        return dsuOps;
    }

    public long getEdgeScans() {
        return edgeScans;
    }

    public long getComparisons() {
        return comparisons;
    }

    public long getMaxDepth() {
        return maxDepth;
    }

    public void reset() {
        this.dsuOps = 0;
        this.edgeScans = 0;
        this.comparisons = 0;
        this.maxDepth = 0;
    }
}
