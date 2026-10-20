package daa.bench;

/**
 * Строка результата бенчмарка для вывода в CSV.
 */
public class BenchmarkRow {
    public final String algorithm;
    public final String graphKind;
    public final int n;
    public final int m;
    public final double timeMs;
    public final long dsuOps;
    public final long edgeScans;
    public final long comparisons;
    public final long maxDepth;
    public final long result;

    public BenchmarkRow(String algorithm, String graphKind, int n, int m,
                        double timeMs, long dsuOps, long edgeScans,
                        long comparisons, long maxDepth, long result) {
        this.algorithm = algorithm;
        this.graphKind = graphKind;
        this.n = n;
        this.m = m;
        this.timeMs = timeMs;
        this.dsuOps = dsuOps;
        this.edgeScans = edgeScans;
        this.comparisons = comparisons;
        this.maxDepth = maxDepth;
        this.result = result;
    }
}
