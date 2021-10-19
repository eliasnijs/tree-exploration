package oplossing;

import opgave.Node;
import opgave.SearchTree;

import java.util.Iterator;

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

    // TODO (elias):
    // go one left -> keep going right -> remove Node
    @Override
    public boolean remove(E comparable) {
        Node<E> node = removeFindNode(comparable, root);
        if (node == null) {
            return false;
        }
        if (node.getLeft() != null) {

        } else if (node.getRight() != null) {

        } else {
            node.setValue(comparable);
        }
        return true;
    }

    public Node<E> removeFindReplacement (Node<E> node) {

        return null;
    }

    public Node<E> removeFindNode (E o, Node<E> node) {
        if (node == null) {
           return null ;
        }
        if (o.compareTo(node.getValue()) < 0) {
            return removeFindNode(o, node.getLeft());
        } else if (o.compareTo(node.getValue()) > 0) {
            return removeFindNode(o, node.getRight());
        }
        return node;
    }


    @Override
    public Node<E> root() {
        return root;
    }

    // TODO (elias):
    @Override
    public Iterator<E> iterator() {
        return null;
    }
}
