package test;

import opgave.Node;
import oplossing.SemiSplayTree;
import oplossing.BinarySearchTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class SemiSplayTreeTest extends Test {

    SemiSplayTreeTest () {
        System.out.println("\n > SEMISPLAY TESTS\n");
        printStatusHeader();
    }

    public void run () {
        // basicTreeTests();
        simpleSplayTests();
        splayNormalTests();
    }

    public void basicTreeTests () {
        compileTest();
        addTest1();
        searchTest1();
        removeTest1();
        removeTest2();
        removeTest3();
        iteratorTest();
    }

    public void simpleSplayTests () {
        splaySimpleBB();
        splaySimpleBS();
        splaySimpleSS();
        splaySimpleSB();
    }
    
   
    public void compileTest () {
        SemiSplayTree<Integer> t = new SemiSplayTree<>();
        assertFalse("compile test", t == null);
    }

    public void addTest1 () {
        SemiSplayTree<Integer> t = new SemiSplayTree<>();
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
        assertFalse("remove-test 1 - node 6", t.search(6));
        t.remove(1);
        assertFalse("remove-test 1 - node 1", t.search(1));
        t.remove(2);
        assertFalse("remove-test 1 - node 2", t.search(2));
        t.remove(5);
        assertFalse("remove-test 1 - node 5", t.search(5));
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
                    // System.out.println("   > Test failed at current node, quitting test! node: " + v + " root: " + st.root() + " list: " + l);
                    assertFalse("remove-test 2 - all nodes", true);
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
        assertFalse("remove-test 3 - node 2", t.search(2));
    }

    public void iteratorTest () {
        SemiSplayTree<Integer> t = new SemiSplayTree<>();
        int[] values = new int[]{5,3,4,5,14,1,4,6};
        for (int v : values) {
            t.add(v);
        }

        Iterator<Integer> iter = t.iterator();
        iter.next();
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
        splayNormal1();
        splayNormal2();
        splayNormal3();
        splayAdd();
        splayRemove1();
        splayRemove2();
        splayRemove3();
        splayAdvanced1();
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

    // Nodes above and underneath
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
        
        return true;
    }
  

}
