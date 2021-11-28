package test;

import oplossing.OptimalTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class MyTreeTest extends Test {

    MyTreeTest (boolean printPriority) {
        super(printPriority);
        System.out.println("\n > MYTREE TESTS\n");
        printStatusHeader();
    }

    public void run () {
    }
    
    public void basicTreeTest () {
        compileTest();
        addTest1();
        searchTest1();
        removeTest1();
        removeTest2();
        removeTest3();
        iteratorTest();
    }
    
    public void compileTest () {
        OptimalTree<Integer> t = new OptimalTree<>();
        assertTrue("compile test", t != null);
    }

    public void addTest1 () {
        OptimalTree<Integer> t = new OptimalTree<>();
        int[] values = new int[]{2,3,1,4};
        for (int v : values) {
            t.add(v);
        }
        assertTrue("add-test 1 - node 2", t.root().getValue() == 2);
        assertTrue("add-test 1 - node 3", t.root().getRight().getValue() == 3);
        assertTrue("add-test 1 - node 1", t.root().getLeft().getValue() == 1);
        assertTrue("add-test 1 - node 4", t.root().getRight().getRight().getValue() == 4);
    }

    public void searchTest1 () {
        OptimalTree<Integer> t = new OptimalTree<>();
        int[] values = new int[]{2,3,14,1,4,6};
        for (int v : values) {
            t.add(v);
        }
        for (int v : values) {
            assertTrue("search-test 1 - node " + v, t.search(v));
        }
    }

    public void removeTest1 () {
        OptimalTree<Integer> t = new OptimalTree<>();
        int[] values = new int[]{5,3,4,5,14,1,4,6};
        for (int v : values) {
            t.add(v);
        }
        t.remove(6);
        assertTrue("remove-test 1 - node 6", !t.search(6));
        t.remove(1);
        assertTrue("remove-test 1 - node 1", !t.search(1));
        t.remove(2);
        assertTrue("remove-test 1 - node 2", !t.search(2));
        t.remove(5);
        assertTrue("remove-test 1 - node 5", !t.search(5));
    }

    public void removeTest2 () {
        List<Integer> arr = new ArrayList<>();
        for (int i = -3; i < 3; i++) {
            arr.add(i);
        }
        List<List<Integer>> perm = generatePerm(arr);
        for (List<Integer> l : perm) {
            OptimalTree<Integer> st = new OptimalTree<>();
            for (int v : l) {
               st.add(v);
            }
            Collections.shuffle(l);
            for (int v : l) {
                // System.out.println((st.root() == null)?null:st.root().getValue() + " " + st.iterator() + " " + v);
                st.remove(v);
                if (st.search(v)) {
                    // System.out.println((st.root() == null)?null:st.root().getValue() + " " + st.iterator() + " " + v);
                    return;
                }
            }
        }
        assertTrue("remove-test 2", true) ;
    }

    public void removeTest3 () {
        OptimalTree<Integer> t = new OptimalTree<>();
        int[] values = new int[]{2,1,3};
        for (int v : values) {
            t.add(v);
        }
        // System.out.println(t.root().getValue() + " " + t.iterator() + " " + 2);
        t.remove(2);
        // System.out.println(t.root().getValue() + " " + t.iterator() + " " + 2);
        assertTrue("remove-test 3 - node 2", t.search(2));
    }

    public void iteratorTest () {
        OptimalTree<Integer> t = new OptimalTree<>();
        int[] values = new int[]{5,3,4,5,14,1,4,6};
        for (int v : values) {
            t.add(v);
        }

        Iterator<Integer> iter = t.iterator();
        iter.next();
        // System.out.println(iter);
    }

}
