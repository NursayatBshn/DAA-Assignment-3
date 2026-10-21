package main.java.daa.dsu;

/**
 * Система непересекающихся множеств (Disjoint Set Union / DSU).
 * Использует обе эвристики: эвристику ранга (union by rank)
 * и сжатие путей (path compression).
 */
public class DisjointSetUnion {
    private int[] parent;
    private int[] rank;
    private int numComponents;

    // Счетчики операций
    private long findCalls = 0;
    private long successfulUnions = 0;

    public DisjointSetUnion(int n) {
        makeSet(n);
    }

    /**
     * Инициализация n непересекающихся элементов [0 .. n - 1].
     */
    public final void makeSet(int n) {
        this.parent = new int[n];
        this.rank = new int[n];
        this.numComponents = n;
        for (int i = 0; i < n; i++) {
            this.parent[i] = i;
            this.rank[i] = 0;
        }
        this.findCalls = 0;
        this.successfulUnions = 0;
    }

    /**
     * Поиск представителя множества с эвристикой сжатия пути (path compression).
     */
    public int find(int x) {
        findCalls++;
        if (parent[x] != x) {
            parent[x] = find(parent[x]); // рекурсивное сжатие пути
        }
        return parent[x];
    }

    /**
     * Объединение множеств с эвристикой по рангу (union by rank).
     * Возвращает true, если множества были разными и успешно объединились,
     * false если элементы уже находились в одном множестве.
     */
    public boolean union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);

        if (rootX == rootY) {
            return false;
        }

        // Присоединяем дерево меньшего ранга к корню дерева большего ранга
        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else {
            parent[rootY] = rootX;
            rank[rootX]++;
        }

        numComponents--;
        successfulUnions++;
        return true;
    }

    /**
     * Текущее количество компонент (непересекающихся множеств).
     */
    public int components() {
        return numComponents;
    }

    public long getFindCalls() {
        return findCalls;
    }

    public long getSuccessfulUnions() {
        return successfulUnions;
    }

    /**
     * Общее число DSU-операций (find + union) согласно метрикам задания.
     */
    public long getTotalDsuOps() {
        return findCalls + successfulUnions;
    }
}
