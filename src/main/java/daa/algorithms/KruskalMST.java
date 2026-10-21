package main.java.daa.algorithms;

import main.java.daa.dsu.DisjointSetUnion;
import main.java.daa.graph.Edge;
import main.java.daa.graph.Graph;
import main.java.daa.metrics.Metrics;

import java.util.ArrayList;
import java.util.List;

/**
 * Алгоритм Краскала для поиска минимального остовного дерева (или остовного леса).
 * Использует DSU для обнаружения циклов.
 */
public class KruskalMST {

    public static MstResult findMst(Graph graph) {
        Metrics metrics = new Metrics();
        int n = graph.getV();
        DisjointSetUnion dsu = new DisjointSetUnion(n);

        // Копируем все ребра графа для сортировки
        List<Edge> edges = new ArrayList<>(graph.getAllEdges());

        // Сортируем ребра по возрастанию веса, подсчитывая сравнения
        edges.sort((e1, e2) -> {
            metrics.addComparison();
            return Integer.compare(e1.getWeight(), e2.getWeight());
        });

        long totalWeight = 0;
        List<Edge> mstEdges = new ArrayList<>();

        // Проходим по отсортированным ребрам
        for (Edge edge : edges) {
            metrics.addEdgeScan();
            int u = edge.getU();
            int v = edge.getV();

            if (dsu.find(u) != dsu.find(v)) {
                dsu.union(u, v);
                mstEdges.add(edge);
                totalWeight += edge.getWeight();

                // Если уже набрали n - 1 ребер и граф изначально связный, можно прервать
                if (mstEdges.size() == n - 1 && dsu.components() == 1) {
                    break;
                }
            }
        }

        // Сохраняем суммарное число операций DSU (find + union)
        metrics.addDsuOps(dsu.getTotalDsuOps());

        return new MstResult(totalWeight, mstEdges, dsu.components(), metrics);
    }
}
