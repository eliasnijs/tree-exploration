package oplossing;

import opgave.Node;
import opgave.SearchTree;

import java.util.*;

public class MyTree<E extends Comparable<E>> implements SearchTree<E> {

    private Node<E> root;
    private int size;

    public void SemiSplayTree () {
        root = null;
        size = 0;
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

    // Search Node
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
