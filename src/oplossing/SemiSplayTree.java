package oplossing;

import opgave.Node;
import opgave.SearchTree;
import test.Test;

import java.util.*;

public class SemiSplayTree<E extends Comparable<E>> implements SearchTreeB<E> {

    private Node<E> root;
    private int size;

    // -- NOTE (Elias): A global variable was chosen for the splaypath because it will be accessed frequently
    //                  and since the class doesn't have too many parameters it doesn't become unclear to debug.
    private Stack<Node<E>> splaypath;

    public SemiSplayTree () {
        root = null;
        size = 0;
        splaypath = new Stack<>();
    }

    public void setRoot (Node<E> n) {
        root = n;
    }

    public void splay () {
        if (splaypath.size() < 3) {
            splaypath.clear();
            return;
        }
        while (splaypath.size() > 2) {
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
                break;
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
        }
        splaypath.clear();
    }

    @Override
    public Node<E> root() {
        return root;
    }

    @Override
    public void clear () {
        root = null;
        size = 0;
        splaypath.clear();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean search(E o) {
        if (root == null) {
            return false;
        }
        splaypath.clear();
        boolean b = searchHelper(o, root);
        splay();
        return b;
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

    @Override
    public boolean add(E o) {
        if (root == null) {
            root = new Node<>(o);
            size += 1;
            return true;
        }
        boolean b = addHelper(o, root);
        splay();
        if (b) size += 1;
        return b;
    }

    public boolean addHelper(E o, Node<E> node) {
        splaypath.push(node);
        if (o.compareTo(node.getValue()) < 0) {
            if (node.getLeft() == null) {
                node.setLeft(new Node<>(o));
                splaypath.push(node.getLeft());
                return true;
            }
            return addHelper(o, node.getLeft());
        } else if (o.compareTo(node.getValue()) > 0) {
            if (node.getRight() == null) {
                node.setRight(new Node<>(o));
                splaypath.push(node.getRight());
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
            splay();
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
            splaypath.push(node);
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
        splay();
        size--;
        return true;
    }

    public Node<E> removeFindRightParent (Node<E> parent, Node<E> node) {
        if (node.getRight() != null) {
            splaypath.push(node);
            return removeFindRightParent(node, node.getRight()); 
        }
        return parent;
    }
    
    public Node<E> removeFindParentNode (E o, Node<E> parent, Node<E> node) {
        if (o.compareTo(node.getValue()) < 0) {
            splaypath.push(node);
            return (node.getLeft() != null)? removeFindParentNode(o, node, node.getLeft()) : null;
        } else if (o.compareTo(node.getValue()) > 0) {
            splaypath.push(node);
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
