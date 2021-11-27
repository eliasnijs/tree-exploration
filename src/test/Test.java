package test;

import opgave.SearchTree;
import opgave.Node;
import java.util.*;

public class Test {

    private final static boolean PRINT_PRIORITY_ALL = true;

    public boolean assertFalse (String name, boolean p1) {
        boolean t = !p1; 
        printStatus(name, t);
        return t;
    }
    
    public boolean assertTrue (String name, boolean p1) {
        boolean t = p1; 
        printStatus(name, t);
        return t;
    }

    public void printStatusHeader () {
        System.out.println("   Status   |    Name");
        System.out.println(" ---------- | -----------------------------------------");
    }

    public void printStatus (String name, boolean b) {
        if (b && PRINT_PRIORITY_ALL) {
            System.out.println("\033[0;32m   SUCCES\033[0m   |    " + name);
        } else if (!b || !PRINT_PRIORITY_ALL) {
            System.out.println("\033[0;31m   FAILED\033[0m   |    " + "\033[0;31m" + name +  "\033[0m");
        }
    }
    
    public boolean compareNodes (Node n1, Node n2) {
        boolean current = n1 == null && n2 == null;
        if (!current && n1 != null && n2 != null) {
           current = n1.getValue() == n2.getValue();
        } 

        if (n1 == null || n2 == null) {
            return current;
        }
        
        boolean left = n1.getLeft() == null && n2.getLeft() == null;
        if (!left && n1.getLeft() != null && n2.getLeft() != null) {
           left = n1.getLeft().getValue() == n2.getLeft().getValue();
        } 
        
        boolean right = n1.getRight() == null && n2.getRight() == null;
        if (!right && n1.getRight() != null && n2.getRight() != null) {
            right = n1.getRight().getValue() == n2.getRight().getValue();
        }  
        
        return current && left && right;
    }

    public boolean compareTree(Node r1, Node r2) {
        return (r1 != null && r2 != null)? r1.treeToString().equals(r2.treeToString()) : false;
    }
   
    public <E> List<List<E>> generatePerm(List<E> original) {
    // generatePerm function (c) https://stackoverflow.com/questions/10305153/generating-all-possible-permutations-of-a-list-recursively answer by DaveFar
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
