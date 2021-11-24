package oplossing;

import opgave.Node;
import opgave.SearchTree;

import java.util.*;

public class SemiSplayTree<E extends Comparable<E>> implements SearchTree<E> {

    private Node<E> root;
    private int size;

    private Stack<Node<E>> splaypath;

    public SemiSplayTree () {
        root = null;
        size = 0;
        splaypath = new Stack<>();
    }

    public void splay () {
        if (splaypath.size() < 3) {
            return;
        }
        while (true) {
            
            Node<E> next = splaypath.pop();
            Node<E> mid = splaypath.pop();
            Node<E> prev = splaypath.pop();

            if (prev.getValue().compareTo(mid.getValue()) < 0 &&
                prev.getValue().compareTo(next.getValue()) < 0 )
            {
                if (mid.getValue().compareTo(next.getValue()) < 0) {
                    prev.setRight(mid.getLeft());
                    mid.setLeft(prev);
                    splaypath.push(mid);
                } else {
                    prev.setRight(next.getLeft());
                    next.setLeft(prev);
                    mid.setLeft(next.getRight());
                    next.setRight(mid);
                    splaypath.push(next);
                } 
            } else {
                if (mid.getValue().compareTo(next.getValue()) > 0) {
                    prev.setLeft(mid.getRight());
                    mid.setRight(prev);
                    splaypath.push(mid);
                } else {
                    prev.setLeft(next.getRight());
                    next.setRight(prev);
                    mid.setRight(next.getLeft());
                    next.setLeft(mid);
                    splaypath.push(next);
                } 
            }
            
            if (splaypath.size() == 1) {
                root = splaypath.pop();        
                return;
            }
            
            Node<E> child = splaypath.pop();
            Node<E> parent = splaypath.pop();
            if (child.getValue().compareTo(parent.getValue()) < 0) {
                parent.setLeft(child);    
            } else {
                parent.setRight(child);    
            }
            splaypath.push(parent);
            splaypath.push(child);
            if (splaypath.size() == 2) {
                return;
            }
            break;
        }
    }

    // Get root of tree
    @Override
    public Node<E> root() {
        return root;
    }

    // Get size of tree
    @Override
    public int size() {
        return size;
    }

    // Search node
    @Override
    public boolean search(E o) {
        if (root == null) 
            return false;
        boolean found = searchHelper(o, root);
        if (found)
            splay();
        splaypath.clear();
        return found;
    }

    public boolean searchHelper (E o, Node<E> node) {
        splaypath.push(node);
        if (o.compareTo(node.getValue()) < 0) {
            return node.getLeft() != null && searchHelper(o, node.getLeft());
        } else if (o.compareTo(node.getValue()) > 0) {
            return node.getRight() != null && searchHelper(o, node.getRight());
        }
        return true;
    }

    // Add node
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

    // Remove node
    @Override
    public boolean remove(E comparable) {
        if (root == null) 
            return false; 
        
        Node<E> parent = removeFindParentNode(comparable, root, root);
        if (parent == null)
            return false; 

        Node<E> node = (comparable.compareTo(parent.getValue()) < 0)? parent.getLeft() : parent.getRight();
        node = (comparable.compareTo(root.getValue()) == 0)? root : node;

        if (node.getLeft() != null) {
            Node<E> nParent = node.getLeft();
            if (nParent.getRight() != null) {
                nParent = removeFindRightParent(node, nParent);
                node.setValue(nParent.getRight().getValue());
                nParent.setRight(nParent.getRight().getLeft());
            }
            node.setValue(nParent.getValue());
            node.setLeft(nParent.getLeft());
        } else if (node.getRight() != null) {
            Node<E> nParent = node.getRight();
            if (nParent.getLeft() != null) {
                nParent = removeFindLeftParent(node, nParent);
                node.setValue(nParent.getLeft().getValue());
                nParent.setLeft(nParent.getLeft().getRight());
            }
            node.setValue(nParent.getValue());
            node.setRight(nParent.getRight());
        } else if (comparable.compareTo(parent.getValue()) < 0) {
            parent.setLeft(null);
        } else if (comparable.compareTo(parent.getValue()) > 0) {
            parent.setRight(null);
        } else {
            root = null;
        }

       size--;
       return true;
    }

    public Node<E> removeFindRightParent (Node<E> parent, Node<E> node) {
        return (node.getRight() == null)? parent : removeFindRightParent(node, node.getRight());
    }

    public Node<E> removeFindLeftParent (Node<E> parent, Node<E> node) {
        return (node.getLeft() == null)? parent : removeFindLeftParent(node, node.getLeft());
    }

    public Node<E> removeFindParentNode (E o, Node<E> parent, Node<E> node) {
        if (o.compareTo(node.getValue()) < 0) {
            return (node.getLeft() != null)? removeFindParentNode(o, node, node.getLeft()) : null;
        } else if (o.compareTo(node.getValue()) > 0) {
            return (node.getRight() != null)? removeFindParentNode(o, node, node.getRight()) : null;
        }
        return parent;
    }

    // Iterator
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
