package test;

import opgave.*;
import opgave.samplers.Sampler;
import oplossing.OptimalTree;

import java.util.*;
import java.util.stream.Collectors;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class OptimalTreeTest extends Test {
    
    public OptimalTreeTest (boolean printPriority) {
        super(printPriority, "OPTIMALTREE TESTS");
        super.tests = tests;
    }

    private final HashMap<String,Runnable> tests = new HashMap<String, Runnable> () {{
                 // basic tests 
            put("compile test",     () -> compileTest());
            put("add test 1",       () -> addTest1());
            put("search test 1",    () -> searchTest1());
            put("remove test 1",    () -> removeTest1());
            put("remove test 2",    () -> removeTest2());
            put("remove test 3",    () -> removeTest3());
            put("iterator test",    () -> iteratorTest());
                // No external 
            put("throw away",       () -> shouldThrowAwayExisting());
            put("single item",      () -> singleItem());
            put("two descending",   () -> twoDescending());
            put("two ascending",    () -> twoAscending());
            put("seven equal",      () -> sevenEqual());
               // External
            put("seven equal external",     () -> sevenEqualWithExternal());
            put("single item external",     () -> singleItemWithExternal());
            put("descending external",      () -> descendingExternal());
            put("ascending external",       () -> ascendingExternal());
            put("simple external",          () -> simpleWithExternal());
            put("example with external",    () -> exampleWithExternal());
            put("zigzag external",          () -> zigzagExternal());
            put("large", () -> big());
            put("largOptimize", () -> bigOptimize());
    }};

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

    void twoDescending() {
        OptimizableTree<Integer> tree = new OptimalTree<>();
        List<Integer> keys = List.of(1, 2);
        List<Double> weights = List.of(1d, 2d);
        tree.optimize(keys, weights);
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
    
    void sevenEqual() {
        OptimizableTree<Integer> tree = new OptimalTree<>();

        List<Integer> keys = List.of(1, 2, 3, 4, 5, 6, 7);
        List<Double> weights = List.of(1d, 1d, 1d, 1d, 1d, 1d, 1d);

        tree.optimize(keys, weights);
        assertTrue("seven equal", 4 == tree.root().getValue());

        assertTrue("seven equal", 2 == tree.root().getLeft().getValue());
        assertTrue("seven equal", 1 == tree.root().getLeft().getLeft().getValue());
        assertTrue("seven equal", 3 == tree.root().getLeft().getRight().getValue());

        assertTrue("seven equal", 6 == tree.root().getRight().getValue());
        assertTrue("seven equal", 5 == tree.root().getRight().getLeft().getValue());
        assertTrue("seven equal", 7 == tree.root().getRight().getRight().getValue());

        assertTrue("seven equal", 17d == treeWeight(tree, keys, weights, null));
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
    
    public void sevenEqualWithExternal() {
        OptimizableTree<Integer> tree = new OptimalTree<>();

        List<Integer> keys = List.of(1, 2, 3, 4, 5, 6, 7);
        List<Double> weights = List.of(1d, 1d, 1d, 1d, 1d, 1d, 1d);
        List<Double> external = List.of(1d, 1d, 1d, 1d, 1d, 1d, 1d, 1d);

        tree.optimize(keys, weights, external);
        assertTrue("seven equal external", 4 == tree.root().getValue());

        assertTrue("seven equal external", 2 == tree.root().getLeft().getValue());
        assertTrue("seven equal external", 1 == tree.root().getLeft().getLeft().getValue());
        assertTrue("seven equal external", 3 == tree.root().getLeft().getRight().getValue());

        assertTrue("seven equal external", 6 == tree.root().getRight().getValue());
        assertTrue("seven equal external", 5 == tree.root().getRight().getLeft().getValue());
        assertTrue("seven equal external", 7 == tree.root().getRight().getRight().getValue());
       
        assertTrue("seven equal external", 49d == treeWeight(tree, keys, weights, external));
    }

    public void descendingExternal() {
        OptimizableTree<Integer> tree = new OptimalTree<>();

        List<Integer> keys = List.of(1, 2, 3);
        List<Double> weights = List.of(3d, 2d, 1d);
        List<Double> external = List.of(4d, 3d, 2d, 2d);

        tree.optimize(keys, weights, external);
        assertTrue("descending external", 1 == tree.root().getValue());
        assertTrue("descending external", null == tree.root().getLeft());
        assertTrue("descending external", 2 == tree.root().getRight().getValue());
        assertTrue("descending external", null == tree.root().getRight().getLeft());
        assertTrue("descending external", 3 == tree.root().getRight().getRight().getValue());

        assertTrue("descending external", 43d == treeWeight(tree, keys, weights, external));
    }
    
    public void ascendingExternal() {
        OptimizableTree<Integer> tree = new OptimalTree<>();

        List<Integer> keys = List.of(1, 2, 3);
        List<Double> weights = List.of(2d, 8d, 50d);
        List<Double> external = List.of(1d, 1d, 4d, 21d);

        tree.optimize(keys, weights, external);
        assertTrue("ascending external", 134 == treeWeight(tree, keys, weights, external));

        assertTrue("ascending external", 3 == tree.root().getValue());
        assertTrue("ascending external", null == tree.root().getRight());
        assertTrue("ascending external", 2 == tree.root().getLeft().getValue());
        assertTrue("ascending external", null == tree.root().getLeft().getRight());
        assertTrue("ascending external", 1 == tree.root().getLeft().getLeft().getValue());

        assertTrue("ascending external", 134d == treeWeight(tree, keys, weights, external));
    }
    
    public void zigzagExternal() {
        OptimizableTree<Integer> tree = new OptimalTree<>();

        List<Integer> keys = List.of(1, 2, 3);
        List<Double> weights = List.of(1d, 1d, 1d);
        List<Double> external = List.of(5d, 1d, 1d, 4d);

        tree.optimize(keys, weights, external);
        assertTrue("zigzag external", 36 == treeWeight(tree, keys, weights, external));

        assertTrue("zigzag external", 1 == tree.root().getValue());
        assertTrue("zigzag external", null == tree.root().getLeft());
        assertTrue("zigzag external", 3 == tree.root().getRight().getValue());
        assertTrue("zigzag external", null == tree.root().getRight().getRight());
        assertTrue("zigzag external", 2 == tree.root().getRight().getLeft().getValue());

        assertTrue("zigzag external", 36d == treeWeight(tree, keys, weights, external));
    }

    public void exampleWithExternal() {
        OptimizableTree<Integer> tree = new OptimalTree<>();

        List<Integer> keys = List.of(0, 1, 2, 3, 4, 5);
        List<Double> internal = List.of(77.0, 90.0, 88.0, 85.0, 13.0, 78.0);
        List<Double> external = List.of(38.0, 78.0, 55.0, 56.0, 75.0, 59.0, 17.0);
        
        tree.optimize(keys, internal, external);
        assertTrue("example with external", 2450.0 == treeWeight(tree, keys, internal, external));
    }
    
    public void simpleWithExternal() {
        OptimizableTree<Integer> tree = new OptimalTree<>();

        List<Integer> keys = List.of(10,20,30,40);
        List<Double> internal = List.of(3.0,3.0,1.0,1.0);
        List<Double> external = List.of(2.0,3.0,1.0,1.0,1.0);
        tree.optimize(keys, internal, external);
        
        assertTrue("simple with external", true);
    }

    public void big () {
        // OptimizableTree<Integer> tree = new OptimalTree<>();
        // int amount = 10000;
        // Sampler sampler = new Sampler(new Random(), amount);
        // List<Double> internal = sampler.sample(amount).stream().map(x -> (double) x).collect(Collectors.toList());
        // // List<Double> external = sampler.sample(amount).stream().map(x -> (double) x).collect(Collectors.toList());
        // external.add(0.0);
        // List<Integer> keys = sampler.sample(amount);
        //
        // tree.optimize(keys, internal);
    }
    
    void bigOptimize() {
        OptimizableTree<Integer> tree = new OptimalTree<>();
        List<Integer> keys = new ArrayList<>();
        List<Double> weights = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            keys.add(i);
            weights.add((double) new Random().nextInt(100));
        }
        tree.optimize(keys, weights);
    }
}
