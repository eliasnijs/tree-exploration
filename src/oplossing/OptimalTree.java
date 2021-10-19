package oplossing;

import opgave.Node;
import opgave.OptimizableTree;

import java.util.Iterator;
import java.util.List;

public class OptimalTree<E extends Comparable<E>> implements OptimizableTree<E> {
    @Override
    public void optimize(List<E> keys, List<Double> weights) {

    }

    @Override
    public void optimize(List<E> keys, List<Double> internalWeights, List<Double> externalWeights) {

    }

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
    public boolean remove(E e) {
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
