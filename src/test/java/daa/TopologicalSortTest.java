package daa;

import daa.algorithms.KahnTopologicalSort;
import daa.algorithms.TopoResult;
import daa.graph.Graph;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class TopologicalSortTest {

    @Test
    void testValidDagOrdering() throws IOException {
        Graph dag = Graph.fromFile(new File("data/small_dag.txt"));
        TopoResult result = KahnTopologicalSort.sort(dag);

        assertFalse(result.hasCycle(), "В DAG не должно быть циклов");
        assertNotNull(result.getOrder());
        assertEquals(dag.getV(), result.getOrder().size());
        assertTrue(result.isValidOrder(dag), "Порядок должен удовлетворять pos[u] < pos[v] для каждого ребра");
    }

    @Test
    void testCycleDetection() throws IOException {
        Graph cyclic = Graph.fromFile(new File("data/cyclic.txt"));
        TopoResult result = KahnTopologicalSort.sort(cyclic);

        assertTrue(result.hasCycle(), "В cyclic.txt должен быть обнаружен цикл");
        assertTrue(result.getOrder().isEmpty(), "При наличии цикла порядок не возвращается");
    }
}
