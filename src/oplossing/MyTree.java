package oplossing;

import opgave.Node;
import opgave.SearchTree;

import java.util.Iterator;

public class MyTree<E extends Comparable<E>> implements SearchTree<E> {
    @Override
    public int size() {
        return 0;
    }

    @Override
    public boolean search(E o) {
        return false;
    }

    @Override
    public boolean add(E o) {
        return false;
    }

    @Override
    public boolean remove(E comparable) {
        return false;
    }

    @Override
    public Node<E> root() {
        return null;
    }

    @Override
    public Iterator<E> iterator() {
        return null;
    }
}
