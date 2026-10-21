package main.java.daa.algorithms;

import main.java.daa.graph.Edge;
import main.java.daa.graph.Graph;
import main.java.daa.metrics.Metrics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Алгоритм Косарайю для поиска компонент сильной связности (SCC).
 * Реализован полностью итеративный DFS с явным стеком,
 * чтобы избежать переполнения системного стека вызовов (StackOverflowError)
 * на больших графах (до 100 000 вершин).
 */
public class KosarajuSCC {

    public static SccResult findSCC(Graph graph) {
        Metrics metrics = new Metrics();
        int n = graph.getV();

        // 1-й проход DFS: вычисление порядка завершения вершин (finish order)
        boolean[] visited = new boolean[n];
        List<Integer> finishOrder = new ArrayList<>(n);

        // Явный стек для DFS (вершина и индекс текущего исходящего ребра)
        int[] stackVertex = new int[n];
        int[] stackEdgeIdx = new int[n];
        int stackPtr = 0;

        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                visited[i] = true;
                stackVertex[stackPtr] = i;
                stackEdgeIdx[stackPtr] = 0;
                stackPtr++;
                metrics.updateMaxDepth(stackPtr);

                while (stackPtr > 0) {
                    int topU = stackVertex[stackPtr - 1];
                    int edgeIdx = stackEdgeIdx[stackPtr - 1];
                    List<Edge> neighbors = graph.getAdj(topU);

                    if (edgeIdx < neighbors.size()) {
                        stackEdgeIdx[stackPtr - 1]++;
                        Edge edge = neighbors.get(edgeIdx);
                        metrics.addEdgeScan();

                        int v = edge.getV();
                        if (!visited[v]) {
                            visited[v] = true;
                            stackVertex[stackPtr] = v;
                            stackEdgeIdx[stackPtr] = 0;
                            stackPtr++;
                            metrics.updateMaxDepth(stackPtr);
                        }
                    } else {
                        // Все соседи вершины обработаны - снимаем со стека
                        stackPtr--;
                        finishOrder.add(topU);
                    }
                }
            }
        }

        // 2. Строим транспонированный граф (все ребра развернуты)
        Graph transposed = graph.getTranspose();

        // 3. Второй проход DFS по транспонированному графу в порядке убывания времени выхода
        Arrays.fill(visited, false);
        int[] componentId = new int[n];
        Arrays.fill(componentId, -1);
        List<List<Integer>> components = new ArrayList<>();

        stackPtr = 0;

        for (int i = finishOrder.size() - 1; i >= 0; i--) {
            int root = finishOrder.get(i);
            if (!visited[root]) {
                List<Integer> currentComponent = new ArrayList<>();
                int currentSccIndex = components.size();

                visited[root] = true;
                componentId[root] = currentSccIndex;
                currentComponent.add(root);

                stackVertex[stackPtr] = root;
                stackEdgeIdx[stackPtr] = 0;
                stackPtr++;
                metrics.updateMaxDepth(stackPtr);

                while (stackPtr > 0) {
                    int topU = stackVertex[stackPtr - 1];
                    int edgeIdx = stackEdgeIdx[stackPtr - 1];
                    List<Edge> neighbors = transposed.getAdj(topU);

                    if (edgeIdx < neighbors.size()) {
                        stackEdgeIdx[stackPtr - 1]++;
                        Edge edge = neighbors.get(edgeIdx);
                        metrics.addEdgeScan();

                        int v = edge.getV();
                        if (!visited[v]) {
                            visited[v] = true;
                            componentId[v] = currentSccIndex;
                            currentComponent.add(v);

                            stackVertex[stackPtr] = v;
                            stackEdgeIdx[stackPtr] = 0;
                            stackPtr++;
                            metrics.updateMaxDepth(stackPtr);
                        }
                    } else {
                        stackPtr--;
                    }
                }

                components.add(currentComponent);
            }
        }

        return new SccResult(components.size(), components, componentId, metrics);
    }
}
