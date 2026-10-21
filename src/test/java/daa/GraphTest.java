package test.java.daa;

import main.java.daa.graph.Graph;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class GraphTest {

    @Test
    void testLoadDisconnectedGraph() throws IOException {
        Graph g = Graph.fromFile(new File("data/disconnected.txt"));
        assertEquals(5, g.getV());
        assertFalse(g.isDirected());
        assertEquals(3, g.getE());
        // Проверяем наличие ребер и самопетли
        assertTrue(g.getAdj(0).size() >= 2);
    }

    @Test
    void testLoadCyclicGraph() throws IOException {
        Graph g = Graph.fromFile(new File("data/cyclic.txt"));
        assertEquals(4, g.getV());
        assertTrue(g.isDirected());
        assertEquals(4, g.getE());
    }

    @Test
    void testLoadDagGraph() throws IOException {
        Graph g = Graph.fromFile(new File("data/small_dag.txt"));
        assertEquals(6, g.getV());
        assertTrue(g.isDirected());
        assertEquals(6, g.getE());
    }
}
