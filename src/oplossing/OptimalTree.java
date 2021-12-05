package oplossing;

import opgave.Node;
import opgave.OptimizableTree;

import java.util.*;

public class OptimalTree<E extends Comparable<E>> implements OptimizableTree<E> {
    
    private Node<E> root;
    private int size;

    public OptimalTree () {
        root = null;
        size = 0;
    }

    @Override
    public void optimize(List<E> keys, List<Double> weights) {
        List<Double> external = new ArrayList<>();
        for (int i=0; i<keys.size()+1; ++i) {
            external.add(0.0);
        }
        optimize(keys, weights, external);
    }

    private static class WeightDataExternal {
        public double weight;
        public double cost;
        public int rootindex;
    }
    
    public void placeKeys (WeightDataExternal table[][], List<E> keys, int b, int e) {
        if (b >= e) { 
            return; }
        int i = table[e-b][b].rootindex;
        this.add(keys.get(i-1));
        placeKeys(table, keys, b, i-1);
        placeKeys(table, keys, i, e);
    }

    // -- NOTE (Elias): The formulas used work with ranges from index i to j. To get the corresponding cell in the table,
    //                  we need to convert range {i,j} to location {j-i, i} in the table (location in the table is denoted as {row, column}). 
    //                  The reverse is also used multiple times, location {r,c} in the table becomes range {c, r+c}.
    // -- NOTE (Elias): For a detailed explanation of the ideas behind the algorithm see: 
    //                  https://www.youtube.com/watch?v=wAy6nDMPYAE&t=3071s 
    @Override
    public void optimize(List<E> keys, List<Double> internalWeights, List<Double> externalWeights) {
        root = null;
        int twidth = externalWeights.size();
        WeightDataExternal table[][] = new WeightDataExternal[twidth][twidth];
        for (int i=0; i<twidth; ++i) { // initialise the table by filling in the first row
            WeightDataExternal pd = new WeightDataExternal();
            pd.weight = externalWeights.get(i); 
            pd.cost = 0;
            pd.rootindex = 0;
            table[0][i] = pd;
        } 
        for (int r=1; r<twidth; ++r) { // Complete the full table
            for (int c=0; c<twidth-r; ++c) {
                int j=c+r;
                WeightDataExternal pd = new WeightDataExternal();
                pd.weight = table[r-1][c].weight + internalWeights.get(j-1) + externalWeights.get(j);
                pd.cost = Double.MAX_VALUE;
                pd.rootindex = 0;
                for (int s = c +1; s<=j; ++s) {
                    double t = table[s-c-1][c].cost + table[j-s][s].cost;
                    if (t < pd.cost) {
                        pd.cost = t;
                        pd.rootindex = s;
                    }
                }
                pd.cost += pd.weight;
                table[r][c] = pd;
            }
        }
        placeKeys(table, keys, 0, externalWeights.size()-1);
    }

    @Override
    public Node<E> root() {
        return root;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean search(E o) {
        return root != null && searchHelper(o, root);
    }

    public boolean searchHelper (E o, Node<E> node) {
        if (o.compareTo(node.getValue()) < 0) {
            return node.getLeft() != null && searchHelper(o, node.getLeft());
        } else if (o.compareTo(node.getValue()) > 0) {
            return node.getRight() != null && searchHelper(o, node.getRight());
        }
        return true;
    }

    @Override
    public boolean add(E o) {
        if (root == null) {
            root = new Node<>(o);
            size += 1;
            return true;
        }
        boolean b = addHelper(o, root);
        size += b? 1 : 0;
        return b;
    }

    public boolean addHelper(E o, Node<E> node) {
        if (o.compareTo(node.getValue()) < 0) {
            if (node.getLeft() == null) {
                node.setLeft(new Node<>(o));
                return true;
            }
            return addHelper(o, node.getLeft());
        } else if (o.compareTo(node.getValue()) > 0) {
            if (node.getRight() == null) {
                node.setRight(new Node<>(o));
                return true;
            }
            return addHelper(o, node.getRight());
        }
        return false;
    }
    
    @Override
    public boolean remove(E comparable) {
        if (root == null) {
            return false; 
        }
       
        Node<E> parent = removeFindParentNode(comparable, root, root);
        if (parent == null) {
            return false; 
        }

        Node<E> node = (comparable.compareTo(root.getValue()) == 0)?  root : 
            (comparable.compareTo(parent.getValue()) < 0)? parent.getLeft() : parent.getRight();
        
        int nodeIsSmaller = comparable.compareTo(parent.getValue());
        if (node.getLeft() != null && node.getRight() == null) {
            if (nodeIsSmaller < 0) {
                parent.setLeft(node.getLeft());
            } else if (nodeIsSmaller > 0) {
                parent.setRight(node.getLeft());
            } else {
                root = node.getLeft();
            }
        } else if (node.getLeft() == null && node.getRight() != null) {
            if (nodeIsSmaller < 0) {
                parent.setLeft(node.getRight());
            } else if (nodeIsSmaller > 0) {
                parent.setRight(node.getRight());
            } else {
                root = node.getRight();
            }
        } else if (node.getLeft() != null) {
            Node<E> nParent = node.getLeft();
            if (nParent.getRight() != null) {
                nParent = removeFindRightParent(node, nParent);
                node.setValue(nParent.getRight().getValue());
                nParent.setRight(nParent.getRight().getLeft());
            } else {
                node.setValue(nParent.getValue());
                node.setLeft(nParent.getLeft()); 
            }
        } else if (nodeIsSmaller < 0) {
            parent.setLeft(null);
        } else if (nodeIsSmaller > 0) {
            parent.setRight(null);
        } else {
            root = null;
        }

        size--;
        return true;
    }

    public Node<E> removeFindRightParent (Node<E> parent, Node<E> node) {
        return (node.getRight() != null)? removeFindRightParent(node, node.getRight()) : parent;
    }
    
    public Node<E> removeFindParentNode (E o, Node<E> parent, Node<E> node) {
        if (o.compareTo(node.getValue()) < 0) {
            return (node.getLeft() != null)? removeFindParentNode(o, node, node.getLeft()) : null;
        } else if (o.compareTo(node.getValue()) > 0) {
            return (node.getRight() != null)? removeFindParentNode(o, node, node.getRight()) : null;
        }
        return parent;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iter(root);
    }
    
    class Iter implements Iterator<E> {

        private final ArrayDeque<E> fifo;

        public Iter (Node<E> root) {
            this.fifo = new ArrayDeque<>();
            if (root != null) {
                fifoMaker(root);
            }
        }

        public void fifoMaker (Node<E> node) {
            Node<E> l = node.getLeft();
            Node<E> r = node.getRight();
            if (l != null) {
                fifoMaker(l);
            }
            fifo.add(node.getValue());
            if (r != null) {
                fifoMaker(r);
            }
        }

        @Override
        public boolean hasNext() {
            return !fifo.isEmpty();
        }

        @Override
        public E next() {
            if (!hasNext()) {
               throw new NoSuchElementException();
            }
            return fifo.pop();
        }

        @Override
        public String toString() {
            return fifo.toString();
        }
    }

}
