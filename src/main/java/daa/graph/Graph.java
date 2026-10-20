package daa.graph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Класс графа на списках смежности.
 * Поддерживает как ориентированные, так и неориентированные графы.
 */
public class Graph {
    private final int v; // количество вершин (0 .. v - 1)
    private int e;       // количество ребер
    private final boolean directed;
    private final List<List<Edge>> adj;
    private final List<Edge> allEdges; // список всех ребер (удобно для Краскала)

    @SuppressWarnings("unchecked")
    public Graph(int v, boolean directed) {
        this.v = v;
        this.directed = directed;
        this.e = 0;
        this.adj = new ArrayList<>(v);
        for (int i = 0; i < v; i++) {
            this.adj.add(new ArrayList<>());
        }
        this.allEdges = new ArrayList<>();
    }

    /**
     * Добавление ребра в граф.
     * Если граф неориентированный, добавляется прямое ребро и обратное ребро в списки смежности.
     */
    public void addEdge(int u, int v, int weight) {
        // Корректная обработка граничных случаев
        if (u < 0 || u >= this.v || v < 0 || v >= this.v) {
            throw new IllegalArgumentException("Номер вершины выходит за границы [0, " + (this.v - 1) + "]");
        }

        Edge edge = new Edge(u, v, weight);
        adj.get(u).add(edge);
        allEdges.add(edge);

        if (!directed) {
            // Для неориентированного графа добавляем ребро v -> u в список смежности
            adj.get(v).add(new Edge(v, u, weight));
        }

        e++;
    }

    public int getV() {
        return v;
    }

    public int getE() {
        return e;
    }

    public boolean isDirected() {
        return directed;
    }

    public List<Edge> getAdj(int u) {
        return Collections.unmodifiableList(adj.get(u));
    }

    public List<Edge> getAllEdges() {
        return Collections.unmodifiableList(allEdges);
    }

    /**
     * Возвращает транспонированный граф (все ребра развернуты в обратную сторону).
     * Нужно для алгоритма Косарайю.
     */
    public Graph getTranspose() {
        Graph transposed = new Graph(v, directed);
        for (Edge edge : allEdges) {
            transposed.addEdge(edge.getV(), edge.getU(), edge.getWeight());
        }
        return transposed;
    }

    /**
     * Чтение графа из текстового файла формата:
     * n m directed|undirected
     * u v w (m строк)
     */
    public static Graph fromFile(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            while (line != null && line.trim().isEmpty()) {
                line = reader.readLine();
            }
            if (line == null) {
                throw new IllegalArgumentException("Файл пуст: " + file.getName());
            }

            String[] header = line.trim().split("\\s+");
            int n = Integer.parseInt(header[0]);
            int m = Integer.parseInt(header[1]);
            boolean isDirected = header[2].equalsIgnoreCase("directed");

            Graph graph = new Graph(n, isDirected);

            for (int i = 0; i < m; i++) {
                line = reader.readLine();
                while (line != null && line.trim().isEmpty()) {
                    line = reader.readLine();
                }
                if (line == null) break;

                String[] parts = line.trim().split("\\s+");
                int u = Integer.parseInt(parts[0]);
                int v = Integer.parseInt(parts[1]);
                int w = Integer.parseInt(parts[2]);
                graph.addEdge(u, v, w);
            }

            return graph;
        }
    }

    public static Graph fromFile(String filePath) throws IOException {
        return fromFile(new File(filePath));
    }
}
