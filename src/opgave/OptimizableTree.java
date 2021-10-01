package opgave;

import java.util.Map;

public interface OptimizableTree<E extends Comparable<E>> extends SearchTree<E> {

    /**
     * Rebuild the tree to be optimal for a collection of internal and external
     * values
     *
     * Given a collection of internal and external values mapped to their
     * weights, rebuilds the current tree such that the binary search tree is
     * optimal according to the given weights. The weights represent the
     * probability of that value being searched. The collection of internal
     * values should be included in the tree (resulting in a positive search)
     * and the collection of external values should not be included in the tree
     * (resulting in a negative search).
     *
     * The given weights should be strictly positive, but should not necessarily
     * be normalized (the sum of the weights might be different than 1). Thus
     * actual search counts may be passed as weights.
     */
    void optimize(Map<E, Double> internal, Map<E, Double> external);
}
