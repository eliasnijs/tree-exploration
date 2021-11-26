package opgave;

public class Node<E extends Comparable<E>> {
    protected E value;
    protected Node<E> left;
    protected Node<E> right;

    public Node(E value) {
        if (value == null) {
            throw new IllegalArgumentException("value cannot not be null"); 
        }
        this.value = value;
        this.left = null;
        this.right = null;
    }

    public E getValue() {
        return value;
    }

    public void setValue(E value) {
        this.value = value;
    }

    public void setLeft(Node<E> left) {
        this.left = left;
    }

    public void setRight(Node<E> right) {
        this.right = right;
    }

    public Node<E> getLeft() {
        return left;
    }

    public Node<E> getRight() {
        return right;
    }

    public String treeToString() {
        return 
            "{" + 
            value + 
            ", <- " + 
            ((left == null)? "null" : left.treeToString()) + 
            ", -> " + 
            ((right == null)? "null" : right.treeToString()) + 
            "}";
    }
    
    @Override
    public String toString() {
        return "" + value;
    }

}
