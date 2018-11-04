package fibheap;

public class Node {
    // Degree of the node
    // Default set as 1
    private int degree = 1;
    // Data fields of the node: keyword and count
    private String keyword;
    private int count;
    // Child of the node
    private Node child;
    // Left sibling of the node
    private Node leftSibling;
    // Right sibling of the node
    private Node rightSibling;
    // Parent of the node. Null if the node is root
    private Node parent;
    // ChildCut field of the node
    private boolean childCut;

    public Node(String keyword, int count) {
        this.keyword = keyword;
        this.count = count;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Node getChild() {
        return child;
    }

    public void setChild(Node child) {
        this.child = child;
    }

    public Node getLeftSibling() {
        return leftSibling;
    }

    public void setLeftSibling(Node leftSibling) {
        this.leftSibling = leftSibling;
    }

    public Node getRightSibling() {
        return rightSibling;
    }

    public void setRightSibling(Node rightSibling) {
        this.rightSibling = rightSibling;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public boolean isChildCut() {
        return childCut;
    }

    public void setChildCut(boolean childCut) {
        this.childCut = childCut;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
