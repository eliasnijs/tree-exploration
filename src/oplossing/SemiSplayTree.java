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

    @Override
    public boolean remove(E comparable) {
        Node<E> parent = removeFindParentNode(comparable, root);
        if (parent == null) {
            return false;
        }
        Node<E> node = (comparable.compareTo(parent.getValue()) < 0)? parent.getLeft() : parent.getRight();
        node = (comparable.compareTo(root.getValue()) == 0)? root : node;
        if (node.getLeft() != null) {
            Node<E> next = node.getLeft();
            if (next.getRight() == null) {
                node.setValue(next.getValue());
                node.setLeft(next.getLeft());
                return true;
            }
            while (next.getRight() != null) {
                if (next.getRight().getRight() == null) {
                    break;
                }
                next = next.getRight();
            }
            Node<E> replacement = next.getRight();
            assert replacement != null;
            node.setValue(replacement.getValue());
            next.setRight(replacement.getLeft());
        } else if (node.getRight() != null) {
            Node<E> next = node.getRight();
            if (next.getLeft() == null) {
                node.setValue(next.getValue());
                node.setLeft(next.getRight());
                return true;
            }
            while (next.getLeft() != null) {
                if (next.getLeft().getLeft() == null) {
                    break;
                }
                next = next.getLeft();
            }
            Node<E> replacement = next.getLeft();
            assert replacement != null;
            node.setValue(replacement.getValue());
            next.setRight(replacement.getRight());
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

    public Node<E> removeFindParentNode (E o, Node<E> node) {
        if (o.compareTo(node.getValue()) == 0) {
            return node;
        }
        Node<E> l = node.getLeft();
        Node<E> r = node.getRight();
        if (l != null) {
            if (l.getValue() == o) {
                return node;
            }
        }
        if (r != null) {
            if (r.getValue() == o) {
                return node;
            }
        }
        if (o.compareTo(node.getValue()) < 0) {
            return (l == null)? null : removeFindParentNode(o,l);
        } else if (o.compareTo(node.getValue()) > 0) {
            return (r == null)? null : removeFindParentNode(o,r);
        }
        return null;
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
