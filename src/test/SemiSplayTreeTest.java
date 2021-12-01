package test;

import opgave.Node;
import opgave.SearchTree;
import oplossing.SemiSplayTree;
import oplossing.BinarySearchTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.SortedSet;
import java.util.Random;
import java.util.HashMap;
import java.util.Map;

public class SemiSplayTreeTest extends Test {
    
    public SemiSplayTreeTest (boolean printPriority) {
        super(printPriority, "SEMISPLAY TESTS");
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
            // splay base case
            put("splay simple BB",  () -> splaySimpleBB());
            put("splay simple BS",  () -> splaySimpleBS());
            put("splay simple SS",  () -> splaySimpleSS());
            put("splay simple SB",  () -> splaySimpleSB());
            // splay normal tests
            put("splay normal 1",   () -> splayNormal1());
            put("splay normal 2",   () -> splayNormal2());
            put("splay normal 3",   () -> splayNormal3());
            put("splay add",        () -> splayAdd());
            put("splay remove 1",   () -> splayRemove1());
            put("splay remove 2",   () -> splayRemove2());
            put("splay remove 3",   () -> splayRemove3());
            put("splay advanced 1", () -> splayAdvanced1());
            put("splay advanced 2", () -> removeShouldSplayFromReplacement());
            put("splay advanced 3", () -> advancedIteratorTest());
    }};

    public void compileTest () {
        SemiSplayTree<Integer> t = new SemiSplayTree<>();
        assertTrue("compile test", t != null);
    }

    public void addTest1 () {
        SemiSplayTree<Integer> t = new SemiSplayTree<>();
        int[] values = new int[]{2,3,1,4};
        for (int v : values) {
            t.add(v);
        }
        BinarySearchTree<Integer> t2 = new BinarySearchTree<>();
        int[] values2 = new int[]{3,2,1,4};
        for (int v : values2) {
            t2.add(v);
        }
        assertTrue("simple add test 1", compareTree(t.root(),t2.root()));
    }

    public void searchTest1 () {
        SemiSplayTree<Integer> t = new SemiSplayTree<>();
        int[] values = new int[]{2,3,14,1,4,6};
        for (int v : values) {
            t.add(v);
        }
        for (int v : values) {
            assertTrue("search-test 1 - node " + v, t.search(v));
        }
    }

    public void removeTest1 () {
        SemiSplayTree<Integer> t = new SemiSplayTree<>();
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
            SemiSplayTree<Integer> st = new SemiSplayTree<>();
            for (int v : l) {
               st.add(v);
            }
            Collections.shuffle(l);
            for (int v : l) {
                st.remove(v);
                if (st.search(v)) {
                    System.out.println("   > Test failed at current node, quitting test! node: " + v + " root: " + st.root() + " list: " + l);
                    assertTrue("remove-test 2 - all nodes", false);
                    return;
                }
            }
        }
        assertTrue("remove-test 2 - all nodes", true);
    }

    public void removeTest3 () {
        SemiSplayTree<Integer> t = new SemiSplayTree<>();
        int[] values = new int[]{2,1,3};
        for (int v : values) {
            t.add(v);
        }
        t.remove(2);
        assertTrue("remove-test 3 - node 2", !t.search(2));
    }

    public void iteratorTest () {
        SemiSplayTree<Integer> t = new SemiSplayTree<>();
        int[] values = new int[]{5,3,4,5,14,1,4,6};
        for (int v : values) {
            t.add(v);
        }
        Iterator<Integer> iter = t.iterator();
    }
    
    public boolean splaySimpleBB () {
        BinarySearchTree<Integer> reference = new BinarySearchTree<>();
        reference.add(5); 
        reference.add(8); 
        reference.add(2);

        SemiSplayTree<Integer> tree = new SemiSplayTree<>();
        tree.add(2); 
        tree.add(5); 
        tree.add(8);

        tree.search(8);
       
        Node treeRoot = tree.root(); 
        Node refRoot = reference.root(); 
        assertTrue("Splay simple BB - root", compareNodes(treeRoot, refRoot));
        assertTrue("Splay simple BB - node left", compareNodes(treeRoot.getLeft(), refRoot.getLeft()));
        assertTrue("Splay simple BB - node right", compareNodes(treeRoot.getRight(), refRoot.getRight()));

        return true;
    } 
    
    public boolean splaySimpleBS () {
        BinarySearchTree<Integer> reference = new BinarySearchTree<>();
        reference.add(5); 
        reference.add(8); 
        reference.add(2);

        SemiSplayTree<Integer> tree = new SemiSplayTree<>();
        tree.add(2); 
        tree.add(8); 
        tree.add(5);

        tree.search(5);
       
        Node treeRoot = tree.root(); 
        Node refRoot = reference.root(); 
        assertTrue("Splay simple BS - root", compareNodes(treeRoot, refRoot));
        assertTrue("Splay simple BS - node left", compareNodes(treeRoot.getLeft(), refRoot.getLeft()));
        assertTrue("Splay simple BS - node right", compareNodes(treeRoot.getRight(), refRoot.getRight()));

        return true;
    } 

    public boolean splaySimpleSS () {
        BinarySearchTree<Integer> reference = new BinarySearchTree<>();
        reference.add(5); 
        reference.add(8); 
        reference.add(2);

        SemiSplayTree<Integer> tree = new SemiSplayTree<>();
        tree.add(8); 
        tree.add(5); 
        tree.add(2);

        tree.search(2);
       
        Node treeRoot = tree.root(); 
        Node refRoot = reference.root(); 
        assertTrue("Splay simple SS - root", compareNodes(treeRoot, refRoot));
        assertTrue("Splay simple SS - node left", compareNodes(treeRoot.getLeft(), refRoot.getLeft()));
        assertTrue("Splay simple SS - node right", compareNodes(treeRoot.getRight(), refRoot.getRight()));

        return true;
    } 
    
    public boolean splaySimpleSB () {
        BinarySearchTree<Integer> reference = new BinarySearchTree<>();
        reference.add(5); 
        reference.add(8); 
        reference.add(2);

        SemiSplayTree<Integer> tree = new SemiSplayTree<>();
        tree.add(8); 
        tree.add(2); 
        tree.add(5);

        tree.search(5);
       
        Node treeRoot = tree.root(); 
        Node refRoot = reference.root(); 
        assertTrue("Splay simple SS - root", compareNodes(treeRoot, refRoot));
        assertTrue("Splay simple SS - node left", compareNodes(treeRoot.getLeft(), refRoot.getLeft()));
        assertTrue("Splay simple SS - node right", compareNodes(treeRoot.getRight(), refRoot.getRight()));

        return true;
    } 

    public void splayNormalTests () {
    }

    // Nodes underneath
    public boolean splayNormal1 () {
        BinarySearchTree<Integer> reference = new BinarySearchTree<>();
        int[] bstValues = {5, 8, 11, 7, 2, 1, 3};
        for (int i : bstValues) {
            reference.add(i);
        }

        BinarySearchTree<Integer> construction = new BinarySearchTree<>();
        int[] tValues = {8, 2, 5, 11, 7, 1, 3};
        for (int i : tValues) {
            construction.add(i);
        }

        SemiSplayTree<Integer> tree = new SemiSplayTree<>();
        tree.setRoot(construction.root());
        tree.search(5);
       
        Node treeRoot = tree.root(); 
        Node refRoot = reference.root(); 

        return assertTrue("splay normal 1 - compare tree with reference", compareTree(treeRoot, refRoot));
    }
    
    // Nodes above
    public boolean splayNormal2 () {
        BinarySearchTree<Integer> reference = new BinarySearchTree<>();
        int[] bstValues = {2, 1, 8, 5, 7, 11, 3};
        for (int i : bstValues) {
            reference.add(i);
        }

        BinarySearchTree<Integer> construction = new BinarySearchTree<>();
        int[] tValues = {2, 5, 8, 11, 7, 1, 3};
        for (int i : tValues) {
            construction.add(i);
        }

        SemiSplayTree<Integer> tree = new SemiSplayTree<>();
        tree.setRoot(construction.root());
        tree.search(11);
       
        Node treeRoot = tree.root(); 
        Node refRoot = reference.root(); 

        return assertTrue("splay normal 2 - compare tree with reference", compareTree(treeRoot, refRoot));
    }

    public boolean splayNormal3 () {
        BinarySearchTree<Integer> reference = new BinarySearchTree<>();
        int[] bstValues = {2, 7, 10, 9, 5, 6, 3};
        for (int i : bstValues) {
            reference.add(i);
        }

        BinarySearchTree<Integer> construction = new BinarySearchTree<>();
        int[] tValues = {2, 5, 10, 3, 7, 6, 9};
        for (int i : tValues) {
            construction.add(i);
        }

        SemiSplayTree<Integer> tree = new SemiSplayTree<>();
        tree.setRoot(construction.root());
        tree.search(7);
       
        Node treeRoot = tree.root(); 
        Node refRoot = reference.root(); 

        return assertTrue("splay normal 3 - compare tree with reference", compareTree(treeRoot, refRoot));
    }


    public boolean splayAdd () {
        BinarySearchTree<Integer> reference = new BinarySearchTree<>();
        int[] bstValues = {5, 2, 1, 3, 7};
        for (int i : bstValues) {
            reference.add(i);
        }

        BinarySearchTree<Integer> construction = new BinarySearchTree<>();
        int[] tValues = {2, 5, 1, 3};
        for (int i : tValues) {
            construction.add(i);
        }
        SemiSplayTree<Integer> tree = new SemiSplayTree<>();
        tree.setRoot(construction.root());
        tree.add(7);
        
        Node refRoot = reference.root(); 
        Node treeRoot = tree.root(); 

        return assertTrue("splay normal add - compare tree with reference", compareTree(treeRoot, refRoot));
    }

    public boolean splayRemove1 () {
        BinarySearchTree<Integer> reference = new BinarySearchTree<>();
        int[] bstValues = {2, 0, 5, 3, 1, 6};
        for (int i : bstValues) {
            reference.add(i);
        }

        BinarySearchTree<Integer> construction = new BinarySearchTree<>();
        int[] tValues = {0, 2, 5, 1, 3, 7, 6};
        for (int i : tValues) {
            construction.add(i);
        }
        SemiSplayTree<Integer> tree = new SemiSplayTree<>();
        tree.setRoot(construction.root());
        tree.remove(7);
       
        Node refRoot = reference.root(); 
        Node treeRoot = tree.root(); 

        return assertTrue("splay normal remove 1 - compare tree with reference", compareTree(treeRoot, refRoot));
    }

    public boolean splayRemove2 () {
        BinarySearchTree<Integer> reference = new BinarySearchTree<>();
        int[] bstValues = {5, 2, 1, 3, 6, 9};
        for (int i : bstValues) {
            reference.add(i);
        }

        BinarySearchTree<Integer> construction = new BinarySearchTree<>();
        int[] tValues = {2, 5, 1, 3, 7, 6, 9};
        for (int i : tValues) {
            construction.add(i);
        }
        SemiSplayTree<Integer> tree = new SemiSplayTree<>();
        tree.setRoot(construction.root());
        tree.remove(7);
       
        Node refRoot = reference.root(); 
        Node treeRoot = tree.root(); 

        return assertTrue("splay normal remove 2 - compare tree with reference", compareTree(treeRoot, refRoot));
    }

    public boolean splayRemove3 () {
        BinarySearchTree<Integer> construction = new BinarySearchTree<>();
        int[] tValues = {8, 7, 4, 1, 6, 2, 3};
        for (int i : tValues) {
            construction.add(i);
        }

        SemiSplayTree<Integer> tree = new SemiSplayTree<>();
        tree.setRoot(construction.root());
        
        tree.remove(4);
        
        BinarySearchTree<Integer> reference = new BinarySearchTree<>();
        int[] bstValues = {7,2,1,3,6,8};
        for (int i : bstValues) {
            reference.add(i);
        }
       
        Node refRoot = reference.root(); 
        Node treeRoot = tree.root(); 
        return assertTrue("splay normal remove 3 - compare tree with reference", compareTree(treeRoot, refRoot));
    }
    
    public boolean splayAdvanced1 () {
        BinarySearchTree<Integer> construction = new BinarySearchTree<>();
        int[] tValues = {50,1,30,25,22,20,14,13,15,17,23,32,55,61};
        for (int i : tValues) {
            construction.add(i);
        }

        SemiSplayTree<Integer> tree = new SemiSplayTree<>();
        tree.setRoot(construction.root());
        
        tree.add(18);
       
        int[] bstValues = {50,22,1,17,14,13,15,20,18,30,25,23,32,55,61};
        BinarySearchTree<Integer> reference = new BinarySearchTree<>();
        for (int i : bstValues) {
            reference.add(i);
        }
        Node refRoot = reference.root(); 
        Node treeRoot = tree.root();
        if (!assertTrue("splay advanced 1 - add 17", compareTree(treeRoot, refRoot))) {
            return false;
        }
        // printTree(treeRoot);
       
        return true;
    }

    public void removeShouldSplayFromReplacement() {
        SearchTree<Integer> tree = new SemiSplayTree<>();
        SortedSet<Integer> inserted = new TreeSet<>();
        Random rng = new Random(2021);
        int numbers[] = new int[30];
        for (int i = 0; i < 30; i++) {
            int key = rng.nextInt(100);
            tree.add(key);
            inserted.add(key);
            numbers[i] = key;
        }

        assertTrue("Remove should splay from replacement",58 == tree.root().getValue());
        assertTrue("Remove should splay from replacement",91 == tree.root().getRight().getValue());
        assertTrue("Remove should splay from replacement",69 == tree.root().getRight().getLeft().getValue());
        assertTrue("Advancedddd removeeee - starting tree integrity check",85 == tree.root().getRight().getLeft().getRight().getValue());
        assertTrue("Advancedddd removeeee - starting tree integrity check",78 == tree.root().getRight().getLeft().getRight().getLeft().getValue());
        assertTrue("Advancedddd removeeee - starting tree integrity check",84 == tree.root().getRight().getLeft().getRight().getLeft().getRight().getValue());
        assertTrue("Advancedddd removeeee - starting tree integrity check",82 == tree.root().getRight().getLeft().getRight().getLeft().getRight().getLeft().getValue());
        assertTrue("Advancedddd removeeee - starting tree integrity check",tree.root().getRight().getLeft().getRight().getLeft().getRight().getLeft().getLeft() == null);
        assertTrue("Advancedddd removeeee - starting tree integrity check",tree.root().getRight().getLeft().getRight().getLeft().getRight().getLeft().getRight() == null);
        assertTrue("Advancedddd removeeee - remove 85", tree.remove(85));
        assertTrue("Advancedddd removeeee - integrity check after remove", 78 == tree.root().getValue());
        assertTrue("Advancedddd removeeee - integrity check after remove", 91 == tree.root().getRight().getValue());
        assertTrue("Advancedddd removeeee - integrity check after remove", 58 == tree.root().getLeft().getValue());
        assertTrue("Advancedddd removeeee - integrity check after remove", 51 == tree.root().getLeft().getLeft().getValue());
        assertTrue("Advancedddd removeeee - integrity check after remove", 69 == tree.root().getLeft().getRight().getValue());
        assertTrue("Advancedddd removeeee - integrity check after remove", 84 == tree.root().getRight().getLeft().getValue());
        assertTrue("Advancedddd removeeee - integrity check after remove", 96 == tree.root().getRight().getRight().getValue());
  
        Iterator iter = tree.iterator();
        int size = 0;
        while (iter.hasNext()) {
            iter.next();
            ++size;
        }
        assertTrue("Advancedddd removeeee - iterator size", size == tree.size());

    }
    
    public void advancedIteratorTest () {
        boolean rtrn = true;
        for (int seed = 1; seed < 10000; seed *= 10) {
            TreeSet<Integer> oracle = new TreeSet<>();
            Random random = new Random(seed);
            SemiSplayTree<Integer> tree = new SemiSplayTree<>();
            for (int i = 0; i < 1000; i++) {
                int x = random.nextInt(100);
                boolean add = random.nextFloat() < .9;
                if (add) {
                    rtrn = rtrn && oracle.add(x) == tree.add(x);
                } 
                else {
                    rtrn = rtrn && oracle.remove(x) == tree.remove(x);
                }
                for (int j = 0; j < 100; j++) {
                    rtrn = rtrn && oracle.contains(j) == tree.search(j);
                }
                rtrn = rtrn || toList(oracle) == toList(tree);
            }
        }
        assertTrue("advanced iterator test", rtrn);
    }

}
