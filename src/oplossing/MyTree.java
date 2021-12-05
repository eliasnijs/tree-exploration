package oplossing;

import opgave.Node;
import oplossing.SearchTreeB;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ArrayDeque;
import java.util.PriorityQueue;
import java.util.List;
import java.util.NoSuchElementException;

public class MyTree<E extends Comparable<E>> implements SearchTreeB<E> {

    private static final double MAXUNSTABILITY = 1500;

    private Node<E> root;
    private int size;

    public void SemiSplayTree () {
        root = null;
        size = 0;
    }
    
    private class DataEntry {
        public E key;
        public Double weight;
    }

    public void placeKeys (ArrayList<E> keys, int m, int l) {
        if (l == 0) { return;
        }
        this.add(keys.get(m));
        placeKeys(keys,m-(l-m)/2,l/2);
        placeKeys(keys,m+(l-m)/2,l/2);
    }

    // TODO Add weights
    public void optimize(List<E> keys, List<Double> weights) {
        if (keys.size() != weights.size()) { System.out.println("invalid keys and weights! quitting `OptimalTree` test...");
        }
        root = null;
        PriorityQueue<DataEntry> data = new PriorityQueue<>((e1,e2) -> { int r = e2.weight.compareTo(e1.weight); return (r != 0)? r : e1.key.compareTo(e2.key); });
        for (int i=0; i < keys.size(); ++i) {
            DataEntry e = new DataEntry();
            e.key = keys.get(i); 
            e.weight = weights.get(i);
            data.offer(e);
        }
        while (data.size() > 0) {
            double weight = data.peek().weight;
            ArrayList<E> tbpKeys = new ArrayList<>();
            while (data.size() > 0 && data.peek().weight == weight) {
                tbpKeys.add(data.poll().key);
            } 
            placeKeys(tbpKeys, (tbpKeys.size()-1)/2, tbpKeys.size());
        } 
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
    public void clear() {
        root = null;
        size = 0;
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
            return addHelper(o, node.getLeft()); } else if (o.compareTo(node.getValue()) > 0) {
            if (node.getRight() == null) {
                node.setRight(new Node<>(o));
                return true;
            }
            return addHelper(o, node.getRight());
        }
        return false;
    }

    @Override
    public boolean remove(E o) {
        if (root == null) {
            return false; 
        }
       
        Node<E> parent = removeFindParentNode(o, root, root);
        if (parent == null) {
            return false; 
        }

        Node<E> node = (o.compareTo(root.getValue()) == 0)?  root : 
            (o.compareTo(parent.getValue()) < 0)? parent.getLeft() : parent.getRight();
        
        int nodeIsSmaller = o.compareTo(parent.getValue());
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
