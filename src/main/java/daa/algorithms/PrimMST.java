package daa.algorithms;

import daa.graph.Edge;
import daa.graph.Graph;
import daa.metrics.Metrics;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Алгоритм Прима для поиска минимального остовного дерева (или остовного леса).
 * Использует PriorityQueue и массив посещенных вершин.
 */
public class PrimMST {

    public static MstResult findMst(Graph graph) {
        Metrics metrics = new Metrics();
        int n = graph.getV();
        boolean[] visited = new boolean[n];
        List<Edge> mstEdges = new ArrayList<>();
        long totalWeight = 0;
        int components = 0;

        // Очередь с приоритетом для выбора ребра минимального веса
        PriorityQueue<Edge> pq = new PriorityQueue<>((e1, e2) -> {
            metrics.addComparison();
            return Integer.compare(e1.getWeight(), e2.getWeight());
        });

        // Обработка всех компонент связности (остовный лес для несвязного графа)
        for (int start = 0; start < n; start++) {
            if (visited[start]) {
                continue;
            }

            components++;
            visited[start] = true;

            // Добавляем все инцидентные ребра стартовой вершины
            for (Edge edge : graph.getAdj(start)) {
                metrics.addEdgeScan();
                pq.add(edge);
            }

            while (!pq.isEmpty()) {
                Edge minEdge = pq.poll();
                int u = minEdge.getU();
                int v = minEdge.getV();

                // Пропускаем ребро, если оба конца уже в остовном дереве
                if (visited[u] && visited[v]) {
                    continue;
                }

                // Определяем новую непосещенную вершину
                int nextVertex = !visited[u] ? u : v;
                visited[nextVertex] = true;
                mstEdges.add(minEdge);
                totalWeight += minEdge.getWeight();

                // Добавляем ребра новой вершины в очередь
                for (Edge nextEdge : graph.getAdj(nextVertex)) {
                    metrics.addEdgeScan();
                    int target = (nextEdge.getU() == nextVertex) ? nextEdge.getV() : nextEdge.getU();
                    if (!visited[target]) {
                        pq.add(nextEdge);
                    }
                }
            }
        }

        return new MstResult(totalWeight, mstEdges, components, metrics);
    }
}
