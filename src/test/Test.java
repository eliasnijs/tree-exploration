package test;

import opgave.Node;
import java.util.*;
import java.util.Map;

public class Test {

    private boolean printPriority;

    public Test (boolean printPriority) {
        this.printPriority = printPriority;
    }

    public void runTests (Map<String, Runnable> tests) {
        HashMap<String, Long> benchmarks = new HashMap<>();
        for (Map.Entry<String, Runnable> entry : tests.entrySet()) {
            System.out.println(" > " + entry.getKey());
            long stt = System.nanoTime();
            entry.getValue().run();
            long ett = System.nanoTime();
            benchmarks.put(entry.getKey(), (ett-stt));
        }
        // Print table
        long totaltime = 0;
        System.out.println();
        System.out.println(" > Benchmarks");
        System.out.println("    Time(ns)           |   Name");
        System.out.println(" --------------------- | -----------------------------------------");
        for (Map.Entry<String, Long> entry : benchmarks.entrySet()) {
            System.out.println(String.format("    %-18d |   %s ", entry.getValue(), entry.getKey()));
            totaltime += entry.getValue();
        }
        System.out.println("  Total time:  " + totaltime + "ns\n");
    }
    
    public boolean assertTrue (String name, boolean p1) {
        printStatus(name, p1);
        return p1;
    }

    public void printStatusHeader () {
        System.out.println("   Status   |    Name");
        System.out.println(" ---------- | -----------------------------------------");
    }

    public void printStatus (String name, boolean b) {
        if (b && printPriority) {
            System.out.println("\033[0;32m   SUCCES\033[0m   |    " + name);
        } else if (!b) {
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

    public List<Integer> toList(Iterable<Integer> iterable) {
        List<Integer> list = new ArrayList<>();
        for (Integer integer : iterable) {
            list.add(integer);
        }
        return list;
    }
    
    public boolean compareTree(Node r1, Node r2) {
        return r1 != null && r2 != null && treeToString(r1).equals(treeToString(r2));
    }
    
    public String treeToString(Node n) {
        return 
            "{" + 
            n.getValue() + 
            ((n.getLeft() == null)? "" : " < " + treeToString(n.getLeft())) + 
            ((n.getRight() == null)? "" : " > " + treeToString(n.getRight())) + 
            "}";
    }

    private static class NodeContext {
        public int x; // x coordinate
        public int y; // y coordinate
        public Node node;
    }

    // OPTTODO (Elias): display `/` and `\`
    public void printTree (Node n) {
        NodeContext rootData = new NodeContext();
        rootData.x = 0;
        rootData.y = 0;
        rootData.node = n;

        ArrayList<NodeContext> collection = new ArrayList<>();

        printTreeHelper(rootData, collection);
        
        int width = collection.size()*2; 
        int height = 0;
        for (NodeContext nodeData : collection) {
            height = Math.max(height, nodeData.y + 1);
        }
        int offset = collection.get(0).x * -1;
        String[][] grid = new String[height][width];
        
        for (int i=0; i<collection.size(); ++i) {
            NodeContext nd = collection.get(i);
            int move = 0;
            for (int j=i+1; j<collection.size(); ++j) {
                int t = (collection.get(j).x > nd.x)? 0 : (Math.abs(collection.get(j).x - nd.x) + 1);
                move = Math.max(t, move);
            }
            for (int j=i+1; j<collection.size(); ++j) {
                collection.get(j).x += move;
            }
            grid[nd.y][nd.x + offset] = nd.node.getValue().toString();
            while (grid[nd.y][nd.x + offset].length() < 3) {
                grid[nd.y][nd.x + offset] = " " + grid[nd.y][nd.x + offset];
            }
        }
        System.out.print("\n");
        for (String[] row : grid) { for (String cell : row) {
                System.out.print((cell == null)? "    ": " " + cell + " ");
            } System.out.print("\n"); 
        }
    }

    // NOTE (Elias): Order of operation will ensure all NodeContexts are collected in the right order (i.e. left -> middle -> right)
    public void printTreeHelper (NodeContext nd, ArrayList<NodeContext> collection) {
        Node l = nd.node.getLeft();
        if (l != null) {
            NodeContext lnd = new NodeContext();
            lnd.x = nd.x - 1;
            lnd.y = nd.y + 1;
            lnd.node = l;
            printTreeHelper(lnd, collection);
        }
        collection.add(nd);
        Node r = nd.node.getRight();
        if (r != null) {
            NodeContext rnd = new NodeContext();
            rnd.x = nd.x + 1;
            rnd.y = nd.y + 1;
            rnd.node = r;
            printTreeHelper(rnd, collection);
        }
    }
   
    public <T> List<List<T>> generatePerm(List<T> original) {
    // generatePerm function (c) https://stackoverflow.com/questions/10305153/generating-all-possible-permutations-of-a-list-recursively answer by DaveFar
        if (original.isEmpty()) {
            List<List<T>> result = new ArrayList<>();
            result.add(new ArrayList<>());
            return result;
        }
        T firstElement = original.remove(0);
        List<List<T>> returnValue = new ArrayList<>();
        List<List<T>> permutations = generatePerm(original);
        for (List<T> smallerPermutated : permutations) {
            for (int index = 0; index <= smallerPermutated.size(); index++) {
                List<T> temp = new ArrayList<>(smallerPermutated);
                temp.add(index, firstElement);
                returnValue.add(temp);
            }
        }
        return returnValue;
    }

}
