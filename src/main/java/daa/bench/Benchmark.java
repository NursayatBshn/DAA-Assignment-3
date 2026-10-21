package main.java.daa.bench;

import main.java.daa.algorithms.*;
import main.java.daa.graph.Graph;
import main.java.daa.graph.GraphGenerator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Основной бенчмарк:
 * - Запускает 5 алгоритмов на 10 графах (5 размеров x 2 плотности) = 50 строк в CSV.
 * - Для каждого замера берется медиана из 5 запусков по времени.
 * - Проверяет строгое совпадение контрольных чисел с эталонной таблицей задания.
 */
public class Benchmark {

    private static final int[][] TEST_CASES = {
            {1_000, 4_000},
            {1_000, 20_000},
            {3_162, 12_648},
            {3_162, 63_240},
            {10_000, 40_000},
            {10_000, 200_000},
            {31_623, 126_492},
            {31_623, 632_460},
            {100_000, 400_000},
            {100_000, 2_000_000}
    };

    // Контрольные эталонные значения из PDF
    private static final long[] EXPECTED_MST = {
            147_970L, 30_363L, 468_911L, 97_589L, 1_486_139L,
            309_550L, 4_715_515L, 968_517L, 15_012_094L, 3_056_681L
    };

    private static final long[] EXPECTED_SCC = {
            27L, 1L, 137L, 1L, 373L,
            1L, 1_188L, 1L, 3_923L, 1L
    };

    private static final long[] EXPECTED_DAG_DIST = {
            396_274L, 252_411L, 1_271_911L, 723_157L, 3_941_828L,
            2_381_342L, 12_551_891L, 7_376_612L, 40_096_539L, 23_512_087L
    };

    public static List<BenchmarkRow> runAll() {
        List<BenchmarkRow> rows = new ArrayList<>();
        int runs = 5;

        System.out.println("==========================================================================================");
        System.out.println("  STARTING BENCHMARK & METRICS (10 GRAPHS x 5 ALGORITHMS = 50 RUNS)");
        System.out.println("==========================================================================================");

        for (int i = 0; i < TEST_CASES.length; i++) {
            int n = TEST_CASES[i][0];
            int m = TEST_CASES[i][1];
            String density = (m == 4 * n) ? "sparse" : "dense";
            String graphLabel = density + "_" + n;

            System.out.printf("[%d/10] Graph: n=%d, m=%d (%s)...%n", i + 1, n, m, density);

            // 1. Undirected graph for MST
            Graph undirected = GraphGenerator.generateUndirected(n, m);

            // --- Kruskal ---
            double[] kruskalTimes = new double[runs];
            MstResult kruskalRes = null;
            for (int r = 0; r < runs; r++) {
                long t0 = System.nanoTime();
                kruskalRes = KruskalMST.findMst(undirected);
                long t1 = System.nanoTime();
                kruskalTimes[r] = (t1 - t0) / 1_000_000.0;
            }
            Arrays.sort(kruskalTimes);
            double kruskalMedian = kruskalTimes[runs / 2];

            // --- Prim ---
            double[] primTimes = new double[runs];
            MstResult primRes = null;
            for (int r = 0; r < runs; r++) {
                long t0 = System.nanoTime();
                primRes = PrimMST.findMst(undirected);
                long t1 = System.nanoTime();
                primTimes[r] = (t1 - t0) / 1_000_000.0;
            }
            Arrays.sort(primTimes);
            double primMedian = primTimes[runs / 2];

            // Валидация MST
            if (kruskalRes.getTotalWeight() != primRes.getTotalWeight()) {
                throw new IllegalStateException("MST вес Краскала (" + kruskalRes.getTotalWeight()
                        + ") не совпал с Примом (" + primRes.getTotalWeight() + ")");
            }
            if (kruskalRes.getTotalWeight() != EXPECTED_MST[i]) {
                throw new IllegalStateException("MST вес (" + kruskalRes.getTotalWeight()
                        + ") не совпал с эталоном (" + EXPECTED_MST[i] + ")");
            }

            rows.add(new BenchmarkRow("Kruskal", graphLabel, n, m, kruskalMedian,
                    kruskalRes.getMetrics().getDsuOps(),
                    kruskalRes.getMetrics().getEdgeScans(),
                    kruskalRes.getMetrics().getComparisons(),
                    kruskalRes.getMetrics().getMaxDepth(),
                    kruskalRes.getTotalWeight()));

            rows.add(new BenchmarkRow("Prim", graphLabel, n, m, primMedian,
                    primRes.getMetrics().getDsuOps(),
                    primRes.getMetrics().getEdgeScans(),
                    primRes.getMetrics().getComparisons(),
                    primRes.getMetrics().getMaxDepth(),
                    primRes.getTotalWeight()));

            // 2. Directed graph for Kosaraju
            Graph directed = GraphGenerator.generateDirected(n, m);
            double[] kosarajuTimes = new double[runs];
            SccResult sccRes = null;
            for (int r = 0; r < runs; r++) {
                long t0 = System.nanoTime();
                sccRes = KosarajuSCC.findSCC(directed);
                long t1 = System.nanoTime();
                kosarajuTimes[r] = (t1 - t0) / 1_000_000.0;
            }
            Arrays.sort(kosarajuTimes);
            double kosarajuMedian = kosarajuTimes[runs / 2];

            // Валидация Kosaraju
            if (sccRes.getCount() != EXPECTED_SCC[i]) {
                throw new IllegalStateException("Число SCC (" + sccRes.getCount()
                        + ") не совпало с эталоном (" + EXPECTED_SCC[i] + ")");
            }
            if (sccRes.getMetrics().getEdgeScans() != 2L * m) {
                throw new IllegalStateException("Косарайю должен просмотреть ровно 2m ребер, получено: "
                        + sccRes.getMetrics().getEdgeScans());
            }

            rows.add(new BenchmarkRow("Kosaraju", graphLabel, n, m, kosarajuMedian,
                    sccRes.getMetrics().getDsuOps(),
                    sccRes.getMetrics().getEdgeScans(),
                    sccRes.getMetrics().getComparisons(),
                    sccRes.getMetrics().getMaxDepth(),
                    sccRes.getCount()));

            // 3. DAG for Kahn and DagShortestPath
            Graph dag = GraphGenerator.generateDag(n, m);

            // --- Kahn ---
            double[] kahnTimes = new double[runs];
            TopoResult kahnRes = null;
            for (int r = 0; r < runs; r++) {
                long t0 = System.nanoTime();
                kahnRes = KahnTopologicalSort.sort(dag);
                long t1 = System.nanoTime();
                kahnTimes[r] = (t1 - t0) / 1_000_000.0;
            }
            Arrays.sort(kahnTimes);
            double kahnMedian = kahnTimes[runs / 2];

            // Валидация Kahn
            if (kahnRes.hasCycle() || !kahnRes.isValidOrder(dag)) {
                throw new IllegalStateException("Алгоритм Кана вернул некорректный топологический порядок!");
            }
            if (kahnRes.getMetrics().getEdgeScans() != m) {
                throw new IllegalStateException("Кан должен просмотреть ровно m ребер, получено: "
                        + kahnRes.getMetrics().getEdgeScans());
            }

            rows.add(new BenchmarkRow("Kahn", graphLabel, n, m, kahnMedian,
                    kahnRes.getMetrics().getDsuOps(),
                    kahnRes.getMetrics().getEdgeScans(),
                    kahnRes.getMetrics().getComparisons(),
                    kahnRes.getMetrics().getMaxDepth(),
                    kahnRes.getOrder().size()));

            // --- DagShortestPath ---
            double[] spTimes = new double[runs];
            PathResult spRes = null;
            for (int r = 0; r < runs; r++) {
                long t0 = System.nanoTime();
                spRes = DagShortestPath.findShortestPaths(dag, kahnRes.getOrder());
                long t1 = System.nanoTime();
                spTimes[r] = (t1 - t0) / 1_000_000.0;
            }
            Arrays.sort(spTimes);
            double spMedian = spTimes[runs / 2];

            // Валидация DAG Distances
            if (spRes.getSumDistances() != EXPECTED_DAG_DIST[i]) {
                throw new IllegalStateException("Сумма расстояний в DAG (" + spRes.getSumDistances()
                        + ") не совпала с эталоном (" + EXPECTED_DAG_DIST[i] + ")");
            }
            if (spRes.getMetrics().getEdgeScans() != m) {
                throw new IllegalStateException("Релаксация DAG должна просмотреть ровно m ребер, получено: "
                        + spRes.getMetrics().getEdgeScans());
            }

            rows.add(new BenchmarkRow("DagShortestPath", graphLabel, n, m, spMedian,
                    spRes.getMetrics().getDsuOps(),
                    spRes.getMetrics().getEdgeScans(),
                    spRes.getMetrics().getComparisons(),
                    spRes.getMetrics().getMaxDepth(),
                    spRes.getSumDistances()));
        }

        System.out.println("All 50 benchmark runs completed successfully! All reference numbers matched 100%.");
        return rows;
    }

    public static void main(String[] args) throws IOException {
        List<BenchmarkRow> rows = runAll();
        File csvFile = new File("results/results.csv");
        CsvWriter.writeResults(csvFile, rows);
        System.out.println("Results written to " + csvFile.getAbsolutePath());
    }
}
