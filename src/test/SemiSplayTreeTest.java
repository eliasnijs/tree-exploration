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
        splaySimpleBB();
        splaySimpleBS();
        splaySimpleSS();
        splaySimpleSB();
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
        SemiSplayTree<Integer> t = new SemiSplayTree<>();
        int[] values = new int[]{2,1,3};
        for (int v : values) {
            t.add(v);
        }
        // System.out.println(t.root().getValue() + " " + t.iterator() + " " + 2);
        t.remove(2);
        // System.out.println(t.root().getValue() + " " + t.iterator() + " " + 2);
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
        // System.out.println(iter);
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

    // Nodes underneath
    public boolean splayNormal1 () {
        return true;
    }
    
    // Nodes above
    public boolean splayNormal2 () {
        return true;
    }

    // Nodes above and underneath
    public boolean splayNormal3 () {
        return true;
    }













}
