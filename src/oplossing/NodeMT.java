package oplossing;

import opgave.Node;

public class NodeMT<E extends Comparable<E>> extends Node<E> {

    private double weight;

    public NodeMT (E o) {
        super(o);
        this.weight = 0.0;
    }

}
