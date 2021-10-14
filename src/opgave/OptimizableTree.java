package opgave;

import java.util.List;

public interface OptimizableTree<E extends Comparable<E>> extends SearchTree<E> {

    /**
     * Rebuild the tree to be optimal for a list of keys and their weights.
     *
     * Given a list of sorted keys and a list of weights such that for every
     * index <tt>i</tt> between 0 and <tt>keys.size()</tt>, the key at
     * <tt>keys.get(i)</tt> has a weight of <tt>weights.get(i)</tt>,
     * rebuilds the current tree optimized according to the given weights.
     *
     * The weights represent the probability of that value being searched.
     * The given weights should be strictly positive, but should not necessarily
     * be normalized (the sum of the weights might be different than 1). Thus
     * actual search counts may be passed as weights.
     */
    void optimize(List<E> keys, List<Double> weights);
}
