package daa;

import daa.algorithms.KosarajuSCC;
import daa.algorithms.SccResult;
import daa.graph.Edge;
import daa.graph.Graph;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class SccTest {

    @Test
    void testCompareWithBruteForce() {
        Random rnd = new Random(42);

        for (int iter = 0; iter < 10; iter++) {
            int n = 50 + rnd.nextInt(51); // 50 .. 100 вершин
            int m = n * 3;
            Graph g = new Graph(n, true);

            for (int i = 0; i < m; i++) {
                int u = rnd.nextInt(n);
                int v = rnd.nextInt(n);
                g.addEdge(u, v, 1);
            }

            // Запускаем Косарайю
            SccResult scc = KosarajuSCC.findSCC(g);

            // Эталонный Brute Force через BFS: достижимость в обе стороны
            boolean[][] reach = computeReachability(g);

            // Проверяем каждую пару вершин:
            // u и v в одной компоненте тогда и только тогда, когда u достигает v и v достигает u
            for (int u = 0; u < n; u++) {
                for (int v = 0; v < n; v++) {
                    boolean sameComponentKosaraju = (scc.getComponentId()[u] == scc.getComponentId()[v]);
                    boolean mutuallyReachable = reach[u][v] && reach[v][u];
                    assertEquals(mutuallyReachable, sameComponentKosaraju,
                            "Ошибка взаимной достижимости для пары (" + u + ", " + v + ") в тесте " + iter);
                }
            }
        }
    }

    @Test
    void testStackSafetyOn100000VerticesPath() {
        int n = 100_000;
        Graph pathGraph = new Graph(n, true);

        // Цепочка 0 -> 1 -> 2 -> ... -> n-1
        for (int i = 0; i < n - 1; i++) {
            pathGraph.addEdge(i, i + 1, 1);
        }

        // Проверяем, что не падает с StackOverflowError
        assertDoesNotThrow(() -> {
            SccResult result = KosarajuSCC.findSCC(pathGraph);
            // В ацикличной цепочке каждая вершина образует свою компоненту (всего n компонент)
            assertEquals(n, result.getCount());
            // Максимальная глубина стека должна быть n
            assertEquals(n, result.getMetrics().getMaxDepth());
        });
    }

    private boolean[][] computeReachability(Graph g) {
        int n = g.getV();
        boolean[][] reach = new boolean[n][n];

        for (int start = 0; start < n; start++) {
            reach[start][start] = true;
            Queue<Integer> q = new ArrayDeque<>();
            q.add(start);

            while (!q.isEmpty()) {
                int u = q.poll();
                for (Edge edge : g.getAdj(u)) {
                    int v = edge.getV();
                    if (!reach[start][v]) {
                        reach[start][v] = true;
                        q.add(v);
                    }
                }
            }
        }
        return reach;
    }
}
