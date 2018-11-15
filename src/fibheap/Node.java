package fibheap;

public class Node {
    // Degree of the node
    private int degree;
    // Data fields of the node: keyword and frequency
    private String keyword;
    private int frequency;
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

    public Node(String keyword, int frequency) {
        this.keyword = keyword;
        this.frequency = frequency;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
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

    @Override
    public String toString() {
        String childKeyword, parentKeyword, leftSiblingKeyword, rightSiblingKeyword;

        parentKeyword = parent == null ? "None" : parent.getKeyword();
        childKeyword = child == null ? "None" : child.getKeyword();
        leftSiblingKeyword = leftSibling == null ? "None" : leftSibling.getKeyword();
        rightSiblingKeyword = rightSibling == null ? "None" : rightSibling.getKeyword();

        return "Node {" + '\n' +
                "degree=" + degree + '\n' +
                ", keyword='" + keyword + '\'' + '\n' +
                ", frequency=" + frequency + '\n' +
                ", child=" + childKeyword + '\n' +
                ", leftSibling=" + leftSiblingKeyword + '\n' +
                ", rightSibling=" + rightSiblingKeyword + '\n' +
                ", parent=" + parentKeyword + '\n' +
                ", childCut=" + childCut + '\n' +
                '}' + '\n';
    }
}
