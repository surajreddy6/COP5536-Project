package fibheap;

import java.util.HashMap;

public class FibonacciHeap {
    // The node that contains the max key
    private Node max;
    // Hash table with key -> keyword and value -> node of the keyword
    private HashMap<String, Node> hashTable = new HashMap<String, Node>();

    // test function
    public void getMax() {
        System.out.println(max.getKeyword());
        System.out.println(max.getCount());
    }

    public void insert(String keyword, int frequency) {
        // check if the keyword already exists
        if (hashTable.containsKey(keyword)) {
            // increase the count of an already existing keyword
            increaseKey(hashTable.get(keyword), frequency);
        } else {
            // Create new node
            Node newNode =  new Node(keyword, frequency);
            // insert the new tree into the fibonacci heap
            addNode(newNode);
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

    public Node removeMax() {
        // if heap is empty return null
        if(max == null)
            return null;

        // temporary variable to store max
        Node temp = max;

        // if max node has no children, find the second max node and make it the max node removing the old max node
        // if the node has children do the same but also remove it's children and re-insert into the heap
        if(max.getChild() == null) {
            // if max is the only node in the heap
            if(max.getRightSibling() == max && max.getLeftSibling() == max) {
                max = null;
            } else {
                max = getSecondMax();
            }
        } else {
            Node maxChild = max.getChild();
            // second max is guaranteed to be at the same level as max
            max = getSecondMax();
            // if max has only one child
            if(maxChild.getLeftSibling() == maxChild && maxChild.getRightSibling() == maxChild) {
                // remove and re-insert children into the heap
                removeNode(maxChild);
                addNode(maxChild);
            } else {
                Node i = maxChild;
                do {
                    Node k = i.getRightSibling();
                    // remove and re-insert children into the heap
                    removeNode(i);
                    addNode(i);
                    i = k;
                } while(i != maxChild);
            }
        }
        // remove the old max node from the fib heap
        removeNode(temp);
        // perform a meld operation every time removeMax is called
        meld();
        return temp;
    }

    private void meld() {
        
    }

    // function to find the second max node at the max node level
    private Node getSecondMax() {
        // if heap is empty return null
        if(max == null)
            return null;

        // if there are no siblings at the level of max return null
        if(max.getRightSibling() == max && max.getLeftSibling() == max)
            return null;

        Node i = max.getRightSibling();
        Node secondMax = max.getRightSibling();
        while (i != max) {
            if(i.getCount() > secondMax.getCount())
                secondMax = i;
            i = i.getRightSibling();
        }
        return secondMax;
    }

    private void addNode(Node node) {
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
        // insert pointer to the new node in the hash table
        hashTable.put(node.getKeyword(), node);
    }

    private void removeNode(Node node) {
        // set the child pointer of node's parent as nil
        if(node.getParent() != null) {
            node.getParent().setChild(null);
        }
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
        // remove pointer to the node in the hash table
        hashTable.remove(node.getKeyword());
    }

    // Never called on a root node. Always called on a node with a parent
    private void removeAndReinsertNode(Node node) {
        // parent of the node
        Node parent = node.getParent();
        // remove the node (and its's subtree) from the heap
        removeNode(node);
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
