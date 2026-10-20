package daa.algorithms;

import daa.graph.Edge;
import daa.graph.Graph;
import daa.metrics.Metrics;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Алгоритм Кана (Kahn's Algorithm) для топологической сортировки.
 * Использует полустепени захода (in-degree) и очередь.
 * Если из очереди вышло меньше n вершин, обнаруживает цикл.
 */
public class KahnTopologicalSort {

    public static TopoResult sort(Graph graph) {
        Metrics metrics = new Metrics();
        int n = graph.getV();

        // Вычисляем полустепени захода вершин
        int[] inDegree = new int[n];
        for (Edge edge : graph.getAllEdges()) {
            inDegree[edge.getV()]++;
        }

        // Очередь для вершин с нулевой полустепенью захода
        Queue<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) {
                queue.add(i);
            }
        }

        List<Integer> order = new ArrayList<>(n);

        // Обработка вершин из очереди
        while (!queue.isEmpty()) {
            int u = queue.poll();
            order.add(u);

            // Просматриваем исходящие ребра
            for (Edge edge : graph.getAdj(u)) {
                metrics.addEdgeScan();
                int v = edge.getV();
                inDegree[v]--;
                if (inDegree[v] == 0) {
                    queue.add(v);
                }
            }
        }

        // Если обработали меньше n вершин, значит в графе есть ориентированный цикл
        boolean hasCycle = (order.size() < n);
        if (hasCycle) {
            return new TopoResult(true, null, metrics);
        }

        return new TopoResult(false, order, metrics);
    }
}
