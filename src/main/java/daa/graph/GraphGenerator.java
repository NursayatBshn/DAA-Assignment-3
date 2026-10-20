package daa.graph;

import java.util.Random;

/**
 * Точный генератор графов согласно спецификации задания.
 * Использует фиксированный seed new Random(42).
 */
public class GraphGenerator {

    /**
     * Генерация связного неориентированного графа для алгоритмов MST (Краскал, Прим).
     */
    public static Graph generateUndirected(int n, int m) {
        Random rnd = new Random(42);
        Graph graph = new Graph(n, false);

        int[] perm = new int[n];
        for (int i = 0; i < n; i++) perm[i] = i;
        for (int i = n - 1; i > 0; i--) {
            int j = rnd.nextInt(i + 1);
            int tmp = perm[i];
            perm[i] = perm[j];
            perm[j] = tmp;
        }

        // 1. Случайное остовное дерево (гарантия связности)
        for (int i = 1; i < n; i++) {
            int u = perm[rnd.nextInt(i)];
            int v = perm[i];
            int w = 1 + rnd.nextInt(1000);
            graph.addEdge(u, v, w);
        }

        // 2. Добавление оставшихся случайных ребер (a = rnd, b = rnd c b != a)
        while (graph.getE() < m) {
            int a = rnd.nextInt(n);
            int b = rnd.nextInt(n);
            while (b == a) {
                b = rnd.nextInt(n);
            }
            int w = 1 + rnd.nextInt(1000);
            graph.addEdge(a, b, w);
        }

        return graph;
    }

    /**
     * Генерация случайного ориентированного графа для алгоритма Косарайю.
     */
    public static Graph generateDirected(int n, int m) {
        Random rnd = new Random(42);
        Graph graph = new Graph(n, true);

        for (int i = 0; i < m; i++) {
            int a = rnd.nextInt(n);
            int b = rnd.nextInt(n);
            while (b == a) {
                b = rnd.nextInt(n);
            }
            int w = 1 + rnd.nextInt(1000);
            graph.addEdge(a, b, w);
        }

        return graph;
    }

    /**
     * Генерация бесконтурного ориентированного графа (DAG) для алгоритма Кана и поиска кратчайших путей.
     */
    public static Graph generateDag(int n, int m) {
        Random rnd = new Random(42);
        Graph graph = new Graph(n, true);

        int[] rank = new int[n];
        for (int i = 0; i < n; i++) rank[i] = i;
        for (int i = n - 1; i > 0; i--) {
            int j = rnd.nextInt(i + 1);
            int tmp = rank[i];
            rank[i] = rank[j];
            rank[j] = tmp;
        }

        for (int i = 0; i < m; i++) {
            int a = rnd.nextInt(n);
            int b = rnd.nextInt(n);
            while (b == a) {
                b = rnd.nextInt(n);
            }
            int w = 1 + rnd.nextInt(1000);
            if (rank[a] > rank[b]) {
                int tmp = a;
                a = b;
                b = tmp;
            }
            graph.addEdge(a, b, w);
        }

        return graph;
    }
}
