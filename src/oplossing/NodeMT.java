package oplossing;

import opgave.Node;

public class NodeMT<E extends Comparable<E>> extends Node<E> {

    private double weight;
    protected NodeMT<E> left;
    protected NodeMT<E> right;

    public NodeMT (E o, double w) {
        super(o);
        this.weight = w;
    }


    public void setLeft(NodeMT<E> left) {
        this.left = left;
    }
    
    public void setRight(NodeMT<E> right) {
        this.right = right;
    }
  
    @Override
    public NodeMT<E> getLeft() {
        return left;
    }
    
    @Override
    public NodeMT<E> getRight() {
        return right;
    }
    
    public void setWeight (double w) {
       this.weight = w;
    }

    public double getWeight () {
        return weight;
    }

    public void multWeight (double w) {
        weight *= w;
    }
    
    public void plusWeight (double w) {
        weight += w;
    }

}
