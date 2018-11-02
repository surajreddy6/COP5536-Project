package fibheap;

public class Tree {
    // Root of the tree
    private Node root;
    // Left tree in the fibonacci heap
    private Tree leftTree;
    // Right tree in the fibonacci heap
    private Tree rightTree;

    // Create tree with a single node
    public Tree(Node root) {
        this.root = root;
    }

    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    public Tree getLeftTree() {
        return leftTree;
    }

    public void setLeftTree(Tree leftTree) {
        this.leftTree = leftTree;
    }

    public Tree getRightTree() {
        return rightTree;
    }

    public void setRightTree(Tree rightTree) {
        this.rightTree = rightTree;
    }
}
