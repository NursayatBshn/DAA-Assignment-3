package test.java.daa;

import main.java.daa.algorithms.KruskalMST;
import main.java.daa.algorithms.MstResult;
import main.java.daa.algorithms.PrimMST;
import main.java.daa.graph.Graph;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class MstTest {

    @Test
    void test100RandomConnectedGraphs() {
        Random rnd = new Random(42);
        for (int iter = 0; iter < 100; iter++) {
            int n = 20 + rnd.nextInt(31); // от 20 до 50 вершин
            Graph g = new Graph(n, false);

            // Генерируем связный граф: сначала остовное дерево
            int[] perm = new int[n];
            for (int i = 0; i < n; i++) perm[i] = i;
            for (int i = n - 1; i > 0; i--) {
                int j = rnd.nextInt(i + 1);
                int tmp = perm[i];
                perm[i] = perm[j];
                perm[j] = tmp;
            }
            for (int i = 1; i < n; i++) {
                int u = perm[rnd.nextInt(i)];
                int v = perm[i];
                int w = 1 + rnd.nextInt(100);
                g.addEdge(u, v, w);
            }

            // Добавляем дополнительные случайные ребра
            int extraEdges = n * 2;
            for (int i = 0; i < extraEdges; i++) {
                int u = rnd.nextInt(n);
                int v = rnd.nextInt(n);
                if (u != v) {
                    g.addEdge(u, v, 1 + rnd.nextInt(100));
                }
            }

            MstResult kruskal = KruskalMST.findMst(g);
            MstResult prim = PrimMST.findMst(g);

            assertEquals(kruskal.getTotalWeight(), prim.getTotalWeight(),
                    "Вес остовного дерева Краскала и Прима должен совпадать на итерации " + iter);
            assertEquals(1, kruskal.getComponents());
            assertEquals(1, prim.getComponents());
            assertEquals(n - 1, kruskal.getEdges().size());
            assertEquals(n - 1, prim.getEdges().size());
        }
    }

    @Test
    void testDisconnectedGraph() throws IOException {
        Graph g = Graph.fromFile(new File("data/disconnected.txt"));
        MstResult kruskal = KruskalMST.findMst(g);
        MstResult prim = PrimMST.findMst(g);

        assertEquals(kruskal.getTotalWeight(), prim.getTotalWeight());
        assertEquals(kruskal.getComponents(), prim.getComponents());
        assertTrue(kruskal.getComponents() > 1, "Граф должен иметь несколько компонент");
    }
}
