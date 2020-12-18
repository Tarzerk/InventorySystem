/*
    Generic Node Class

    NetID: EXR180014
    Author: Erik Rodriguez
 */
public class Node <G extends Comparable<G>> implements Comparable{
    private Node<G> left;
    private Node<G> right;
    private G payload;

    public Node(G payload) { // overloaded
        this.payload = payload;
    }

    public Node() { } // default constructor

    public void setPayload(G payload) { this.payload = payload; }
    public void setLeft(Node<G> left) { this.left = left; }
    public void setRight(Node<G> right) { this.right = right; }

    public Node<G> getLeft() { return left; }  // Accessors and mutators
    public Node<G> getRight() { return right; }
    public G getPayload() { return payload; }


    @Override
    public int compareTo(Object o){
        int val = 0;

        if (o instanceof Node){
            val = payload.compareTo(((Node<G>)o).payload);
        }

        return val;

    }
}
