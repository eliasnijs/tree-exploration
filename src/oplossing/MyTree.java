package oplossing;

import opgave.Node;
import oplossing.NodeMT;
import oplossing.SearchTreeB;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.ArrayDeque;
import java.util.PriorityQueue;
import java.util.List;
import java.util.HashSet;
import java.util.NoSuchElementException;

public class MyTree<E extends Comparable<E>> implements SearchTreeB<E> {

    private static final double MAXUNSTABLE = 0.2;
    private static final double SMLT        = 1.25;

    private NodeMT<E> root;
    private HashSet<E> unstables;
    private int size;

    public MyTree () {
        root = null;
        size = 0;
        unstables = new HashSet<>();
    }

    public void placeKeys (ArrayList<E> keys, int b, int e) {
        if (b >= e) {
            return;
        }
        int mid = b+(e-b)/2;
        this.addnorm(keys.get(mid));
        keys.set(mid, null);
        placeKeys(keys, b, mid);
        placeKeys(keys, mid+1, e);
    }

    public void balance() {
        if (unstables.size()/size < MAXUNSTABLE) {
            return; 
        }
        PriorityQueue<NodeMT<E>> queue = new PriorityQueue<>((e1,e2) -> { 
            int r = Double.valueOf(e2.getWeight()).compareTo(Double.valueOf(e1.getWeight())); 
            return (r != 0)? r : e1.getValue().compareTo(e2.getValue()); 
        });
        makeQueue(root, queue);
        root = null;
        while (queue.size() > 0) {
            double weight = queue.peek().getWeight();
            ArrayList<E> tbpKeys = new ArrayList<>();
            while (queue.size() > 0 && queue.peek().getWeight() == weight) {
                tbpKeys.add(queue.poll().getValue());
            } 
            placeKeys(tbpKeys, 0, tbpKeys.size());
        } 
        unstables.clear();
    }

    public void makeQueue (NodeMT<E> node, PriorityQueue<NodeMT<E>> q) {
        NodeMT<E> l = node.getLeft();
        NodeMT<E> r = node.getRight();
        if (l != null) makeQueue(l, q);
        q.add(node);
        if (r != null) makeQueue(r, q);
    }
    
    public void addnorm(E o) {
        if (root == null) { 
            root = new NodeMT<>(o, 1.0);
            return;
        }
        addHelper(o, root);
    }

    @Override
    public NodeMT<E> root() {
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
        if (root == null) {
            return false;
        }
        double w = searchHelper(o, root, -1);
        unstables.add(o);
        balance();
        return w != -1.0;
    }

    public double searchHelper (E o, NodeMT<E> node, int dist) {
        dist += 1;
        if (o.compareTo(node.getValue()) < 0) {
            return (node.getLeft() != null)? searchHelper(o, node.getLeft(), dist) : -1.0;
        } else if (o.compareTo(node.getValue()) > 0) {
            return (node.getRight() != null)? searchHelper(o, node.getRight(), dist) : -1.0;
        } 
        node.multWeight(SMLT); 
        return node.getWeight(); 
    }

    @Override
    public boolean add(E o) {
        if (root == null) { 
            root = new NodeMT<>(o, 1.0);
            size += 1;
            return true;
        }
        boolean b = addHelper(o, root); size += b? 1 : 0;
        return b;
    }

    public boolean addHelper(E o, NodeMT<E> node) {
        if (o.compareTo(node.getValue()) < 0) {
            if (node.getLeft() == null) {
                node.setLeft(new NodeMT<>(o, 1.0));
                return true;
            }
            return addHelper(o, node.getLeft()); 
        } else if (o.compareTo(node.getValue()) > 0) {
            if (node.getRight() == null) {
                node.setRight(new NodeMT<>(o, 1.0));
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
       
        NodeMT<E> parent = removeFindParentNodeMT(o, root, root);
        if (parent == null) {
            return false; 
        }

        NodeMT<E> node = (o.compareTo(root.getValue()) == 0)?  root : 
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
            NodeMT<E> nParent = node.getLeft();
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

    public NodeMT<E> removeFindRightParent (NodeMT<E> parent, NodeMT<E> node) {
        return (node.getRight() != null)? removeFindRightParent(node, node.getRight()) : parent;
    }
    
    public NodeMT<E> removeFindParentNodeMT (E o, NodeMT<E> parent, NodeMT<E> node) {
        if (o.compareTo(node.getValue()) < 0) {
            return (node.getLeft() != null)? removeFindParentNodeMT(o, node, node.getLeft()) : null;
        } else if (o.compareTo(node.getValue()) > 0) {
            return (node.getRight() != null)? removeFindParentNodeMT(o, node, node.getRight()) : null;
        }
        return parent;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iter(root);
    }

    class Iter implements Iterator<E> {

        private final ArrayDeque<E> fifo;

        public Iter (NodeMT<E> root) {
            this.fifo = new ArrayDeque<>();
            if (root != null) {
                fifoMaker(root);
            }
        }

        public void fifoMaker (NodeMT<E> node) {
            NodeMT<E> l = node.getLeft();
            NodeMT<E> r = node.getRight();
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
