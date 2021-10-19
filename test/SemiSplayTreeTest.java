import oplossing.SemiSplayTree;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SemiSplayTreeTest {
    @Test
    public void compileTest () {
        SemiSplayTree<Integer> t = new SemiSplayTree<>();
        assertNotEquals(t, null);
    }

    @Test
    public void addTest1 () {
        SemiSplayTree<Integer> t = new SemiSplayTree<>();
        int[] values = new int[]{2,3,1,4};
        for (int v : values) {
            t.add(v);
        }
        assertEquals(t.root().getValue(), 2);
        assertEquals(t.root().getRight().getValue(), 3);
        assertEquals(t.root().getLeft().getValue(), 1);
        assertEquals(t.root().getRight().getRight().getValue(), 4);
    }

    @Test
    public void searchTest1 () {
        SemiSplayTree<Integer> t = new SemiSplayTree<>();
        int[] values = new int[]{2,3,14,1,4,6};
        for (int v : values) {
            t.add(v);
        }
        for (int v : values) {
            assertTrue(t.search(v));
        }
    }

    @Test
    public void removeTest1 () {
        SemiSplayTree<Integer> t = new SemiSplayTree<>();
        int[] values = new int[]{2,3,14,1,4,6};
        for (int v : values) {
            t.add(v);
        }
        t.remove(6);
        assertFalse(t.search(6));
        t.remove(1);
        assertFalse(t.search(1));
        t.remove(2);
        assertFalse(t.search(2));
    }
}
