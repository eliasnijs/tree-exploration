package opgave;

public interface SearchTree<E extends Comparable<E>> extends Iterable<E> {
    int size();

    boolean search(E o);

    boolean add(E o);

    boolean remove(E e);
    
    Node<E> root();
}
