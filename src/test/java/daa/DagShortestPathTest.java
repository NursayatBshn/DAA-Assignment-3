package daa;

import daa.algorithms.DagShortestPath;
import daa.algorithms.PathResult;
import daa.graph.Edge;
import daa.graph.Graph;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class DagShortestPathTest {

    @Test
    void testCompareWithBellmanFordIncludingNegativeWeights() {
        Random rnd = new Random(42);

        for (int iter = 0; iter < 10; iter++) {
            int n = 30 + rnd.nextInt(20);
            int m = n * 3;
            Graph g = new Graph(n, true);

            // Генерируем случайный DAG: ребра только от меньшего индекса к большему
            int[] rank = new int[n];
            for (int i = 0; i < n; i++) rank[i] = i;
            for (int i = n - 1; i > 0; i--) {
                int j = rnd.nextInt(i + 1);
                int tmp = rank[i];
                rank[i] = rank[j];
                rank[j] = tmp;
            }

            for (int i = 0; i < m; i++) {
                int u = rnd.nextInt(n);
                int v = rnd.nextInt(n);
                if (u != v) {
                    if (rank[u] > rank[v]) {
                        int tmp = u;
                        u = v;
                        v = tmp;
                    }
                    // Включаем отрицательные веса (от -50 до +100)
                    int weight = -50 + rnd.nextInt(151);
                    g.addEdge(u, v, weight);
                }
            }

            // Запуск нашего DAG-алгоритма
            PathResult dagResult = DagShortestPath.findShortestPaths(g);

            // Запуск эталонного Беллмана-Форда с тем же супер-источником
            long[] bfDist = runBellmanFordWithSuperSource(g);

            for (int v = 0; v < n; v++) {
                assertEquals(bfDist[v], dagResult.getDist()[v],
                        "Расстояние до вершины " + v + " должно совпадать с Bellman-Ford");
            }
        }
    }

    @Test
    void testPathRestoration() {
        Graph g = new Graph(4, true);
        g.addEdge(0, 1, 5);
        g.addEdge(1, 2, 3);
        g.addEdge(2, 3, 2);
        g.addEdge(0, 2, 10);

        PathResult res = DagShortestPath.findShortestPaths(g);
        assertEquals(10, res.getDist()[3]); // 0 -> 1 -> 2 -> 3 (5 + 3 + 2 = 10)

        List<Integer> path = res.getPath(3);
        assertEquals(List.of(0, 1, 2, 3), path);
    }

    private long[] runBellmanFordWithSuperSource(Graph g) {
        int n = g.getV();
        int[] inDegree = new int[n];
        for (Edge e : g.getAllEdges()) {
            inDegree[e.getV()]++;
        }

        long[] dist = new long[n];
        Arrays.fill(dist, Long.MAX_VALUE);
        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) {
                dist[i] = 0;
            }
        }

        // V - 1 итераций релаксации всех ребер
        for (int i = 0; i < n - 1; i++) {
            for (Edge e : g.getAllEdges()) {
                int u = e.getU();
                int v = e.getV();
                int w = e.getWeight();
                if (dist[u] != Long.MAX_VALUE && dist[u] + w < dist[v]) {
                    dist[v] = dist[u] + w;
                }
            }
        }

        return dist;
    }
}
