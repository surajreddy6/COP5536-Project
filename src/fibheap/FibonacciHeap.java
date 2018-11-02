package fibheap;

import java.util.HashMap;

public class FibonacciHeap {
    // The node that contains the max key
    private Node max;
    // Hash table with key-keyword and value-node of the keyword
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
            // if the incoming node's count is greater set is as max
            if(max.getCount() < node.getCount()) {
                max = node;
            }
        }
    }

    public void increaseKey(Node node, int frequency) {
        // if the node is a root node just increase the count and check with max
        if(node.getParent() == null) {
            node.setCount(node.getCount() + frequency);
            if(node.getCount() > max.getCount()) {
                max = node;
            }
        } else {
            // if count of node becomes greater than the parent remove the
            // subtree rooted and node and re-insert into the heap
            if(node.getCount() + frequency > node.getParent().getCount()) {

            } else {
                node.setCount(node.getCount() + frequency);
            }
        }
    }
}
