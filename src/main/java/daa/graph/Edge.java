package main.java.daa.graph;

/**
 * Класс ребра графа, соединяющего вершину u и вершину v с весом weight.
 */
public class Edge {
    private final int u;
    private final int v;
    private final int weight;

    public Edge(int u, int v, int weight) {
        this.u = u;
        this.v = v;
        this.weight = weight;
    }

    public int getU() {
        return u;
    }

    public int getV() {
        return v;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "(" + u + " -> " + v + ", w=" + weight + ")";
    }
}
