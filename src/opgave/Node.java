package opgave;
import java.util.*;

public class Node<E extends Comparable<E>> {
    protected E value;
    protected Node<E> left;
    protected Node<E> right;

    public Node(E value) {
        if (value == null) {
            throw new IllegalArgumentException("value cannot not be null"); 
        }
        this.value = value;
        this.left = null;
        this.right = null;
    }

    public E getValue() {
        return value;
    }

    public void setValue(E value) {
        this.value = value;
    }

    public void setLeft(Node<E> left) {
        this.left = left;
    }

    public void setRight(Node<E> right) {
        this.right = right;
    }

    public Node<E> getLeft() {
        return left;
    }

    public Node<E> getRight() {
        return right;
    }
    
    private class NodeData {
        public int x;
        public int y;
        public Node node;
    }

    public void printTree () {
        NodeData rootData = new NodeData();  
        rootData.x = 0;
        rootData.y = 0;
        rootData.node = this;

        ArrayList<NodeData> collection = new ArrayList<>();

        printTreeHelper(rootData, collection);
        
        int width = collection.size()*2; 
        int height = 0; 
        for (int i=0; i<collection.size(); ++i) {
            height = (height < collection.get(i).y + 1)? collection.get(i).y + 1: height;
        }
        int offset = collection.get(0).x * -1;
        String[][] grid = new String[height][width];
        
        for (int i=0; i<collection.size(); ++i) {
            NodeData nd = collection.get(i);
            int move = 0;
            for (int j=i+1; j<collection.size(); ++j) {
                int t = (collection.get(j).x > nd.x)? 0 : (Math.abs(collection.get(j).x - nd.x) + 1);
                move = (t > move)? t : move;
                break;
            }
            for (int j=i+1; j<collection.size(); ++j) {
                collection.get(j).x += move;
            }
            grid[nd.y][nd.x + offset] = nd.node.toString();
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

    public void printTreeHelper (NodeData nd, ArrayList<NodeData> collection) {
        Node l = nd.node.getLeft();
        if (l != null) {
            NodeData lnd = new NodeData();
            lnd.x = nd.x - 1;
            lnd.y = nd.y + 1;
            lnd.node = l;
            printTreeHelper(lnd, collection);
        }
        collection.add(nd);
        Node r = nd.node.getRight();
        if (r != null) {
            NodeData rnd = new NodeData();
            rnd.x = nd.x + 1;
            rnd.y = nd.y + 1;
            rnd.node = r;
            printTreeHelper(rnd, collection);
        }
    }

    public String treeToString() {
        return 
            "{" + 
            value + 
            ((left == null)? "" : "<" + left.treeToString()) + 
            ((right == null)? "" : ">" + right.treeToString()) + 
            "}";
    }
    
    @Override
    public String toString() {
        return "" + value;
    }

}
