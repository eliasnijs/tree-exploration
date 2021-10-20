package oplossing;

import opgave.Node;
import opgave.SearchTree;

import java.util.*;

public class SemiSplayTree<E extends Comparable<E>> implements SearchTree<E> {

    private Node<E> root;
    private int size;

    public SemiSplayTree () {
        root = null;
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean search(E o) {
        System.out.println(o);
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
       boolean b = removeHelper(comparable);
       if (b) {
           size--;
       }
       return b;
    }

    public boolean removeHelper(E comparable) {
        Node<E> parent = removeFindParentNode(comparable, root, root);
        if (parent == null) {
            return false;
        }
        Node<E> node = (comparable.compareTo(parent.getValue()) < 0)? parent.getLeft() : parent.getRight();
        node = (comparable.compareTo(root.getValue()) == 0)? root : node;
        if (node.getLeft() != null) {
            Node<E> nParent = node.getLeft();
            if (nParent.getRight() == null) {
                node.setValue(nParent.getValue());
                node.setRight(nParent.getRight());
                return true;
            }
            nParent = removeFindRightParent(node, nParent);
            Node<E> replacement = nParent.getRight();
            node.setValue(replacement.getValue());
            nParent.setRight(replacement.getLeft());
        } else if (node.getRight() != null) {
            Node<E> nParent = node.getRight();
            if (nParent.getLeft() == null) {
                node.setValue(nParent.getValue());
                node.setRight(nParent.getRight());
                return true;
            }
            nParent = removeFindLeftParent(node, nParent);
            Node<E> replacement = nParent.getLeft();
            assert replacement != null;
            node.setValue(replacement.getValue());
            nParent.setLeft(replacement.getRight());
        } else {
            if (comparable.compareTo(parent.getValue()) < 0) {
               parent.setLeft(null);
            } else if (comparable.compareTo(parent.getValue()) > 0) {
               parent.setRight(null);
            } else {
                root = null;
            }
        }
        return true;
    }

    public Node<E> removeFindRightParent (Node<E> parent, Node<E> node) {
        return (node.getRight() == null)? parent : removeFindRightParent(node, node.getRight());
    }

    public Node<E> removeFindLeftParent (Node<E> parent, Node<E> node) {
        return (node.getRight() == null)? parent : removeFindRightParent(node, node.getLeft());
    }

    public Node<E> removeFindParentNode (E o, Node<E> parent, Node<E> node) {
        return (o.compareTo(node.getValue()) == 0)? parent : (o.compareTo(node.getValue()) < 0)? ((node.getLeft() == null)? null : removeFindParentNode(o, node, node.getLeft())) : ((node.getRight() == null)? null : removeFindParentNode(o, node, node.getRight()));
    }

    @Override
    public Node<E> root() {
        return root;
    }

    // TODO (elias):
    @Override
    public Iterator<E> iterator() {
        return new Iter(this);
    }

    class Iter implements Iterator<E> {

        private final ArrayDeque<E> fifo;
        private final SemiSplayTree<E> tree;

        private E currentNode;

        public Iter (SemiSplayTree<E> tree) {
            this.fifo = new ArrayDeque<>();
            this.tree = tree;
            currentNode = null;
            root = tree.root();
            if (root != null) {
                tree.add(root.getValue());
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
            return (currentNode = fifo.pop());
        }

        @Override
        public void remove() {
           tree.remove(currentNode);
        }

        @Override
        public String toString() {
            return fifo.toString();
        }
    }

}
