/*
    Fully Generic Binary Search Tree

    NETid: EXR180014
    Author: Erik Rodriguez
 */
public class BSTree <G extends Comparable<G>>{

    private Node<G> root;

    public BSTree(Node<G> root) { this.root = root; } // overloaded
    public BSTree(){ root = null; } // default

    /**
     * Traverses through the tree and inserts value, if its empty insert at root
     * recursive function
     * @param insertionNode generic node
     */

    public void Insert(Node<G> insertionNode) {
        Node <G> current = root;
        if (current == null) // if the tree is empty set the insertion to the root of the tree
            root = insertionNode;
        else
            Insert(current, insertionNode);
    }

    private void Insert(Node<G> parent, Node<G> insertionNode) {
        int comparasionValue = insertionNode.compareTo(parent);
        if (comparasionValue < 0) { // if insertion is less than parent
            if (parent.getLeft() == null)
                parent.setLeft(insertionNode);
            else
                Insert(parent.getLeft(),insertionNode);
        }
        else { // if insertion is more than parent
            if (parent.getRight() == null)
                parent.setRight(insertionNode);
            else
                Insert(parent.getRight(),insertionNode);

        }
    }
    /**
     * Finds a value in the BST
     * recursive function
     * @param searchValue generic node
     * @return the node where the search value was found, else returns null
     */
    public Node<G> Search(Node<G> searchValue){
        Node<G> current = root;
        return SearchRecursive(current, searchValue);
    }
    private Node<G> SearchRecursive(Node<G> CurrentNode,Node<G> SearchValue){

        int comparasionValue = SearchValue.compareTo(CurrentNode);

        if (CurrentNode != null){
            if(comparasionValue == 0)
                return CurrentNode; // if the value is the same return the node
            else if (comparasionValue < 0)
                return SearchRecursive(CurrentNode.getLeft(),SearchValue); // if the key is less move current to the left
            else
                return SearchRecursive(CurrentNode.getRight(),SearchValue); // if the key is more move current to right

        }
        return null;
    }

    /**
     * prints all elements of our Binary tree in a sorted fashion
     * @param root root of the tree
     */
    public void inOrderPrint(Node <G> root){
        if(root != null) {
            inOrderPrint(root.getLeft()); // mov all they way left
            System.out.println(root.getPayload()); // then print
            inOrderPrint(root.getRight()); // move all the way to the right
        }
    }


    /**
     * Recursively finds the child of a node
     * @param child the node you want the parent of
     * @return the parent of the child node, or null if its orphaned
     */
    private Node<G> GetParent (Node<G> child){
        Node<G> current = root;
        return GetParent(current,child);
    }
    private Node<G> GetParent(Node<G> treeSubRoot, Node<G> child) {

        int comparasionValue = child.compareTo(treeSubRoot);

        if (treeSubRoot == null)
            return null;

        if (child.compareTo(treeSubRoot.getLeft()) == 0 || child.compareTo(treeSubRoot.getRight()) == 0) {
            return treeSubRoot;
        }
        if (comparasionValue < 0){
            return GetParent(treeSubRoot.getLeft(),child);
        }
        return GetParent(treeSubRoot.getRight(),child);
    }


    /**
     * Takes a generic node and it deletes it
     * @param deletion
     * @return true if it was deleted, false if it wasn't inside
     */
    public boolean Remove (Node<G> deletion){
        Node<G> node = Search(deletion);
        Node <G> parent = GetParent(deletion);
        return Remove(parent, node);
    }
    private boolean Remove(Node<G> parent, Node<G> node) {
        if (node == null) // if the node isn't present on the tree
            return false;

        // Case 1: Internal node with 2 children
        if (node.getLeft() != null  && node.getRight() != null){
            // find the successor and succesor's parent
            Node<G> succesorNode = node.getRight();
            Node<G> succesorParent = node;
            while(succesorNode.getLeft() != null) {
                succesorParent = succesorNode;
                succesorNode = succesorNode.getLeft();
            }
            // copy the value to our node
            node.setPayload(succesorNode.getPayload());  // make sure it copies
            //node = succesorNode;

            // Remove the succesor
            Remove(succesorParent, succesorNode);
        }
        // Case 2: Root Node (with 1 or 0 children)
        else if (node == getRoot()) {
            if (node.getLeft() != null)
                setRoot(node.getLeft());
            else
                setRoot(node.getRight());

        }
        // Case 3: Internal node with left child only
        else if (node.getLeft() != null) {
            // replace node with node's left child
            if (parent.getLeft() == node)
                parent.setLeft(node.getLeft());
            else
                parent.setRight(node.getLeft());

        }
        // Case 4: Internal with right child or leaf
        else {
            // replace with node's right child
            if (parent.getLeft() == node)
                parent.setLeft(node.getRight());
            else
                parent.setRight(node.getRight());
        }
        return true;

    }

    public Node<G> getRoot() { return root; } // getters and setters
    public void setRoot(Node<G> root) { this.root = root; }
}
