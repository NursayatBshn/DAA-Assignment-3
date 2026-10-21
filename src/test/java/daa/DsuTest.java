package test.java.daa;

import main.java.daa.dsu.DisjointSetUnion;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class DsuTest {

    @Test
    void testBasicOperations() {
        DisjointSetUnion dsu = new DisjointSetUnion(5);
        assertEquals(5, dsu.components());

        assertTrue(dsu.union(0, 1));
        assertEquals(4, dsu.components());
        assertEquals(dsu.find(0), dsu.find(1));

        // Повторное объединение тех же элементов
        assertFalse(dsu.union(0, 1));
        assertEquals(4, dsu.components());

        assertTrue(dsu.union(2, 3));
        assertEquals(3, dsu.components());

        assertTrue(dsu.union(1, 2));
        assertEquals(2, dsu.components());
        assertEquals(dsu.find(0), dsu.find(3));

        // Проверяем счетчики
        assertTrue(dsu.getFindCalls() > 0);
        assertEquals(3, dsu.getSuccessfulUnions());
    }

    @Test
    void testRandomUnionsAgainstReference() {
        int n = 500;
        int operations = 2000;
        Random rnd = new Random(12345);

        DisjointSetUnion dsu = new DisjointSetUnion(n);

        // Наивная эталонная реализация на списках множеств
        List<Set<Integer>> sets = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Set<Integer> set = new HashSet<>();
            set.add(i);
            sets.add(set);
        }

        for (int step = 0; step < operations; step++) {
            int u = rnd.nextInt(n);
            int v = rnd.nextInt(n);

            boolean dsuMerged = dsu.union(u, v);

            // Ищем множества в эталонной структуре
            Set<Integer> setU = null;
            Set<Integer> setV = null;
            for (Set<Integer> s : sets) {
                if (s.contains(u)) setU = s;
                if (s.contains(v)) setV = s;
            }

            if (setU != setV) {
                assertTrue(dsuMerged, "DSU должен был объединить разные множества");
                setU.addAll(setV);
                sets.remove(setV);
            } else {
                assertFalse(dsuMerged, "DSU не должен был объединять уже соединенные вершины");
            }

            assertEquals(sets.size(), dsu.components(), "Число компонент должно совпадать с эталоном");
        }

        // Проверяем, что find согласуется для всех пар
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 50; j++) {
                int other = rnd.nextInt(n);
                boolean sameInDsu = (dsu.find(i) == dsu.find(other));

                boolean sameInRef = false;
                for (Set<Integer> s : sets) {
                    if (s.contains(i) && s.contains(other)) {
                        sameInRef = true;
                        break;
                    }
                }
                assertEquals(sameInRef, sameInDsu);
            }
        }
    }
}
