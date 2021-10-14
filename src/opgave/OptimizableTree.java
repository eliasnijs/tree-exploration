package opgave;

import java.util.List;

public interface OptimizableTree<E extends Comparable<E>> extends SearchTree<E> {

    /**
     * Rebuild the tree to be optimal for a list of keys and their weights.
     *
     * Rebuilds the current tree optimized according to the given weights:
     * Given a list of sorted keys and a list of weights such that for every
     * index <tt>i</tt> between 0 and <tt>keys.size()</tt>, the key at
     * <tt>keys.get(i)</tt> has a weight of <tt>weights.get(i)</tt>.
     *
     * The weights represent the probability of that value being searched.
     * The given weights should be strictly positive, but should not necessarily
     * be normalized (the sum of the weights might be different than 1). Thus
     * actual search counts may be passed as weights.
     */
    void optimize(List<E> keys, List<Double> weights);

    /**
     * Rebuild the tree to be optimal for a list of keys, their weights, and
     * weights of keys not in the tree.
     *
     * This extends the method above with weights of unsuccessful searches.
     *
     * Rebuilds the current tree optimized according to the given weights:
     * Given a list of sorted keys and a list of internal weights such that for
     * every index <tt>i</tt> between 0 and <tt>keys.size()</tt>, the key at
     * <tt>keys.get(i)</tt> has a weight of <tt>internalWeights.get(i)</tt>, and
     * a list of external weights such that <tt>externalWeights.get(i)</tt> is
     * the weight of all unsuccessful searches for keys between
     * <tt>keys.get(i - 1)</tt> and <tt>keys.get(i)</tt>. The weight at
     * <tt>externalWeights.get(0)</tt> is the weight of all searches for keys
     * smaller than those in <tt>keys</tt>, the weight at
     * <tt>externalWeights.get(keys.size())</tt> is the weight of all searches
     * for keys greater then those in keys.
     *
     * The weights represent the probability of that value being searched.
     * The given weights should be strictly positive, but should not necessarily
     * be normalized (the sum of the weights might be different than 1). Thus
     * actual search counts may be passed as weights.
     */
    void optimize(List<E> keys, List<Double> internalWeights, List<Double> externalWeights);
}
