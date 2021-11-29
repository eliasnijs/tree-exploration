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

    public void printPrioriyQueue (PriorityQueue<DataEntry> q) {
        Stack<DataEntry> a = new Stack<>();
        while (0<q.size()) {
            DataEntry e = q.poll();
            System.out.print(e + ", ");
            a.add(e);
        }
        System.out.println();
        while (a.size()>0) {
            q.add(a.pop());
        }
    }

    private class DataEntry {
        public E key;
        public Double weight;
        public String toString() {
            return "{" + key + " : " + weight + "}" ;
        }
    }

    @Override
    public void optimize(List<E> keys, List<Double> weights) {
        if (keys.size() != weights.size()) { System.out.println("invalid keys and weights! quitting `OptimalTree` test...");
        }
        root = null;
        PriorityQueue<DataEntry> data = new PriorityQueue<>((e1,e2) -> {
            int r = e2.weight.compareTo(e1.weight);
            return (r != 0)? r : e1.key.compareTo(e2.key);
        });
        for (int i=0; i < keys.size(); ++i) {
            DataEntry e = new DataEntry();
            e.key = keys.get(i);
            e.weight = weights.get(i);
            data.offer(e);
        }
        System.out.println(data);
        while (data.size() > 0) {
            double weight = data.peek().weight;
            ArrayList<E> tbpKeys = new ArrayList<>();
            while (data.size() > 0) {
                if (data.peek().weight != weight) { break;
                } 
                tbpKeys.add(data.poll().key);
            } 
            placekey(tbpKeys, (tbpKeys.size()-1)/2, tbpKeys.size());
        } 
    }
    
    public void placekey (ArrayList<E> keys, int m, int l) {
        if (l == 0) { return;
        }
        this.add(keys.get(m));
        placekey(keys,m-(l-m)/2,l/2);
        placekey(keys,m+(l-m)/2,l/2);
    }

    @Override
    public void optimize(List<E> keys, List<Double> internalWeights, List<Double> externalWeights) {
        root = null;
        placeKey(makeDataTable(keys, internalWeights, externalWeights), keys, 0, externalWeights.size()-1);
    }

    private static class ProbabilityData {
        public double weight;
        public double cost;
        public int    rooti;
        public String toString () {
            return "{" + weight + ", " + cost + ", " + rooti + "}";
        }
    }

    public void placeKey(ProbabilityData table[][], List<E> keys, int b, int e) {
        if (b >= e) {
            return;
        }
        int i = table[e-b][b].rooti;
        this.add(keys.get(i-1));
        placeKey(table, keys, 0, i-1);
        placeKey(table, keys, i, e);
    }

    public ProbabilityData[][] makeDataTable (List<E> k, List<Double> p, List<Double> q) {
        int twidth = q.size();
        ProbabilityData table[][] = new ProbabilityData[twidth][twidth];
        for (int i=0; i<twidth; ++i) {
            ProbabilityData pd = new ProbabilityData();
            pd.weight = q.get(i); 
            pd.cost   = 0;
            pd.rooti  = 0;
            
            table[0][i] = pd;
        } 
        for (int r=1; r<twidth; ++r) {
            for (int c=0; c<twidth-r; ++c) {
                int i=c; int j=c+r;
                ProbabilityData pd = new ProbabilityData();
                pd.weight = table[r-1][c].weight + p.get(j-1) + q.get(j);
                pd.cost = Double.MAX_VALUE;
                pd.rooti = 0; 
                for (int s=i+1; s<=j; ++s) {
                    double t = table[s-i-1][i].cost + table[j-s][s].cost;
                    if (t <= pd.cost) {
                        pd.cost = t;
                        pd.rooti = s;
                    }
                }
                pd.cost += pd.weight;
                table[r][c] = pd;
            }
        }
        return table;
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
