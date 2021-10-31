import oplossing.SemiSplayTree;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

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
        int[] values = new int[]{5,3,4,5,14,1,4,6};
        for (int v : values) {
            t.add(v);
        }
        t.remove(6);
        assertFalse(t.search(6));
        t.remove(1);
        assertFalse(t.search(1));
        t.remove(2);
        assertFalse(t.search(2));
        t.remove(5);
        assertFalse(t.search(5));
    }

    @Test
    public void removeTest2 () throws Exception {
        List<Integer> arr = new ArrayList<>();
        for (int i = -3; i < 3; i++) {
            arr.add(i);
        }
        List<List<Integer>> perm = generatePerm(arr);
        for (List<Integer> l : perm) {
            SemiSplayTree<Integer> st = new SemiSplayTree<>();
            for (int v : l) {
               st.add(v);
            }
            Collections.shuffle(l);
            for (int v : l) {
                System.out.println((st.root() == null)?null:st.root().getValue() + " " + st.iterator() + " " + v);
                st.remove(v);
                if (st.search(v)) {
                    System.out.println((st.root() == null)?null:st.root().getValue() + " " + st.iterator() + " " + v);
                    throw new Exception();
                }
            }
        }
    }

    @Test
    public void removeTest3 () {
        SemiSplayTree<Integer> t = new SemiSplayTree<>();
        int[] values = new int[]{2,1,3};
        for (int v : values) {
            t.add(v);
        }
        System.out.println(t.root().getValue() + " " + t.iterator() + " " + 2);
        t.remove(2);
        System.out.println(t.root().getValue() + " " + t.iterator() + " " + 2);
        assertFalse(t.search(2));
    }

    @Test
    public void iteratorTest () {
        SemiSplayTree<Integer> t = new SemiSplayTree<>();
        int[] values = new int[]{5,3,4,5,14,1,4,6};
        for (int v : values) {
            t.add(v);
        }

        Iterator<Integer> iter = t.iterator();
        iter.next();
        System.out.println(iter);
    }

    // generatePerm function (c) https://stackoverflow.com/questions/10305153/generating-all-possible-permutations-of-a-list-recursively answer by DaveFar
    public <E> List<List<E>> generatePerm(List<E> original) {
        if (original.isEmpty()) {
            List<List<E>> result = new ArrayList<>();
            result.add(new ArrayList<>());
            return result;
        }
        E firstElement = original.remove(0);
        List<List<E>> returnValue = new ArrayList<>();
        List<List<E>> permutations = generatePerm(original);
        for (List<E> smallerPermutated : permutations) {
            for (int index = 0; index <= smallerPermutated.size(); index++) {
                List<E> temp = new ArrayList<>(smallerPermutated);
                temp.add(index, firstElement);
                returnValue.add(temp);
            }
        }
        return returnValue;
    }
}
