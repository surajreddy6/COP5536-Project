package fibheap;

import java.util.HashMap;

public class FibonacciHeap {
    // The node that contains the max key
    private Node max;
    // Hash table with key -> keyword and value -> node of the keyword
    private HashMap<String, Node> hashTable = new HashMap<String, Node>();

    public void insert(String keyword, int frequency) {
        // check if the keyword already exists
        if (hashTable.containsKey(keyword)) {
            // increase the count of an already existing keyword
            increaseKey(hashTable.get(keyword), frequency);
        } else {
            // Create new node
            Node newNode =  new Node(frequency);
            // insert pointer to the new node in the hash table
            hashTable.put(keyword, newNode);
            // insert the new tree into the fibonacci heap
            addNode(newNode);
        }
    }

    public void addNode(Node node) {
        /* if the heap is empty add it directly else
           add it next to the max node
           */
        if(max == null) {
            node.setLeftSibling(node);
            node.setRightSibling(node);
            max = node;
        } else {
            // set max as the left sibling of node
            node.setLeftSibling(max);
            // set the right sibling of max as right sibling of mode
            node.setRightSibling(max.getRightSibling());
            // set node as the left sibling of the right sibling of max
            max.getRightSibling().setLeftSibling(node);
            // set node as right sibling of max
            max.setRightSibling(node);
            // childCut is not defined for the root but set it to false
            node.setChildCut(false);
            // if the incoming node's count is greater set is as max
            if(node.getCount() > max.getCount()) {
                max = node;
            }
        }
    }

    public void increaseKey(Node node, int frequency) {
        // update count of node
        node.setCount(node.getCount() + frequency);
        // if the node is a root node check with max
        if(node.getParent() == null) {
            if(node.getCount() > max.getCount()) {
                max = node;
            }
        } else {
            // if count of node becomes greater than the parent remove the
            // node (and it's subtree) and re-insert into the heap
            if(node.getCount() > node.getParent().getCount()) {
                removeAndReinsertNode(node);
            }
        }
    }

    // Never called on a root node. Always called on a node with a parent
    public void removeAndReinsertNode(Node node) {
        // parent of the node
        Node parent = node.getParent();
        // set the child pointer of node's parent as nil
        parent.setChild(null);
        // set the parent pointer of the node as nil
        node.setParent(null);
        // if the node has siblings update their pointers before removing the node
        if (node.getLeftSibling() != node || node.getRightSibling() != node) {
            Node leftSibling = node.getLeftSibling();
            Node rightSibling = node.getRightSibling();
            leftSibling.setRightSibling(rightSibling);
            rightSibling.setLeftSibling(leftSibling);
        }
        node.setLeftSibling(node);
        node.setRightSibling(node);
        node.setChildCut(false);
        // re-insert node (and its's subtree) into the heap
        addNode(node);
        // Check to perform cascading cut only if the node's parent is not a root
        if(parent.getParent() != null) {
            // if childCut if already true, remove and re-insert the parent into the heap
            // else set childCut of parent to true
            if(parent.isChildCut()) {
                removeAndReinsertNode(parent);
            } else {
                parent.setChildCut(true);
            }
        }
    }
}
