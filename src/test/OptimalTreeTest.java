package test;

import opgave.*;
import oplossing.OptimalTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class OptimalTreeTest extends Test {

    OptimalTreeTest (boolean printPriority) {
        super(printPriority);
        System.out.println("\n > OPTIMALTREE TESTS\n");
        printStatusHeader();
    }

    public void run () {
        givenTests();
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
                st.remove(v);
                if (st.search(v)) {
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
        
        t.remove(2);
        assertTrue("remove-test 3 - node 2", !t.search(2));
    }

    public void iteratorTest () {
        OptimalTree<Integer> t = new OptimalTree<>();
        int[] values = new int[]{5,3,4,5,14,1,4,6};
        for (int v : values) {
            t.add(v);
        }

        Iterator<Integer> iter = t.iterator();
        iter.next();
    }
   
    public void givenTests () {
        shouldThrowAwayExisting();
        singleItem();
        // singleItemWithExternal();
        twoDescending();
        twoAscending();
    }

    public void shouldThrowAwayExisting() {
        OptimizableTree<Integer> tree = new OptimalTree<>();
        tree.add(1);
        tree.add(2);
        tree.add(3);
        tree.optimize(List.of(4), List.of(1d));
        
        Iterator<Integer> it = tree.iterator();
        assertTrue("optimise throws away existing", 4 == it.next());
        assertTrue("optimise throws away existing", !it.hasNext());
    }

    public void singleItem() {
        OptimizableTree<Integer> tree = new OptimalTree<>();
        List<Integer> keys = List.of(1);
        List<Double> weights = List.of(1d);
        tree.optimize(keys, weights);
        assertTrue("Single Item", 1 == tree.root().getValue());
        assertTrue("Single Item", null == tree.root().getLeft());
        assertTrue("Single Item", null == tree.root().getRight());

        assertTrue("Single Item", 1d == treeWeight(tree, keys, weights, null));
    }

    public void singleItemWithExternal() {
        OptimizableTree<Integer> tree = new OptimalTree<>();
        List<Integer> keys = List.of(1);
        List<Double> weights = List.of(1d);
        List<Double> external = List.of(1d, 1d);
        tree.optimize(keys, weights, external);
        assertTrue("single item with external", 1 == tree.root().getValue());
        assertTrue("single item with external", null == tree.root().getLeft());
        assertTrue("single item with external", null == tree.root().getRight());

        assertTrue("single item with external", 5d == treeWeight(tree, keys, weights, external));
    }

    void twoDescending() {
        OptimizableTree<Integer> tree = new OptimalTree<>();

        List<Integer> keys = List.of(1, 2);
        List<Double> weights = List.of(1d, 2d);

        tree.optimize(keys, weights);
        printTree(tree.root());
        assertTrue("2 descending", 2 == tree.root().getValue());
        assertTrue("2 descending", 1 == tree.root().getLeft().getValue());
        assertTrue("2 descending", 4d == treeWeight(tree, keys, weights, null));
    }

    void twoAscending() {
        OptimizableTree<Integer> tree = new OptimalTree<>();

        List<Integer> keys = List.of(1, 2);
        List<Double> weights = List.of(2d, 1d);

        tree.optimize(keys, weights);
        assertTrue("2 ascending", 1 == tree.root().getValue());
        assertTrue("2 ascending", 2 == tree.root().getRight().getValue());

        assertTrue("2 ascending", 4d == treeWeight(tree, keys, weights, null));
    }
    
    /**
     * Calculates the weight of a tree given the list of keys, internal and external weights.
     */
    public double treeWeight(SearchTree<Integer> tree, List<Integer> keys, List<Double> internal, List<Double> external) {
        if (external == null) {
            external = new ArrayList<>();
            for (int i = 0; i < keys.size() + 1; i++) {
                external.add(0d);
            }
        }
        return nodeWeight(tree.root(), keys, internal, external, 1);
    }

    public double nodeWeight(Node<Integer> node, List<Integer> keys, List<Double> internal, List<Double> external, int depth) {

        int v = node.getValue();
        int i = keys.indexOf(v);
        assert i >= 0;

        // multiply the weight of the key with the current depth
        double w = depth * internal.get(i);

        if (node.getLeft() == null) {
            // we count the visited "nodes" as weight, including the NULL pointer
            w += (depth + 1) * external.get(i);
        } else {
            w += nodeWeight(node.getLeft(), keys, internal, external, depth + 1);
        }
        if (node.getRight() == null) {
            w += (depth + 1) * external.get(i + 1);
        } else {
            w += nodeWeight(node.getRight(), keys, internal, external, depth + 1);
        }
        return w;
    }

}
