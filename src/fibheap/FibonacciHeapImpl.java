package fibheap;

import java.util.HashMap;
import java.util.Map;

public class FibonacciHeapImpl implements FibonacciHeap{

    // The node that contains the max key
    private Node max;

    // Hash table with key -> keyword and value -> node of the keyword
    private HashMap<String, Node> hashTable;

    public FibonacciHeapImpl(HashMap<String, Node> hashTable) {
        this.hashTable = hashTable;
    }

    /**
     * Inserts a keyword into the fibonacci heap.
     * @param keyword the keyword to be inserted
     * @param frequency the frequency of the keyword to be inserted
     * @return the max node of the fibonacci heap
     */
    @Override
    public Node insert(String keyword, int frequency) {
        // check if the keyword already exists
        if (hashTable.containsKey(keyword)) {
            // increase the count of an already existing keyword
            return increaseKey(keyword, frequency);
        } else {
            // Create new node
            Node newNode =  new Node(keyword, frequency);
            // insert the new tree into the fibonacci heap
            addNode(newNode);
        }
        return max;
    }

    /**
     * Increases the frequency of an existing keyword in the fibonacci heap.
     * @param keyword the keyword for which the frequency is to be increased
     * @param frequency the amount by which to increase the frequency of the existing keyword
     * @return the max node of the fibonacci heap
     */
    @Override
    public Node increaseKey(String keyword, int frequency) {
        Node node = hashTable.get(keyword);

        // check if node exists
        if(node == null) {
            return insert(keyword, frequency);
        }

        // update count of node
        node.setFrequency(node.getFrequency() + frequency);
        // if the node is a root node compare it with with max
        if(node.getParent() == null) {
            if(node.getFrequency() > max.getFrequency()) {
                max = node;
            }
        } else {
            // if count of node becomes greater than the parent remove the
            // node (and it's subtree) and re-insert into the heap
            if(node.getFrequency() > node.getParent().getFrequency()) {
                removeAndReinsertNode(node);
            }
        }
        return max;
    }

    /**
     * Returns the keyword with the maximum frequency in the fibonacci heap
     * @return the max node of the fibonacci heap
     */
    @Override
    public Node getMax() {
        return max;
    }

    /**
     * Removes the keyword with maximum frequency from the fibonacci heap.
     * @return the node corresponding to the keyword with the maximum frequency
     */
    @Override
    public Node removeMax() {
        // if heap is empty return null
        if(max == null)
            return null;

        // temporary variable to store max
        Node temp = max;

        // if max node has no children, find the second max node and make it the max node removing the old max node
        // if the node has children do the same but also remove it's children and re-insert into the heap
        if(max.getChild() == null) {
            // check if max is the only node in the heap i.e, there is only one element in the heap
            if(max.getRightSibling() == max && max.getLeftSibling() == max) {
                max = null;
            } else {
                max = getSecondMax();
            }
        } else {
            Node maxChild = max.getChild();
            // remove children of max and re-insert them into the heap
            Node i = maxChild;
            do {
                Node k = i.getRightSibling();
                removeNode(i, true);
                addNode(i);
                i = k;
            } while(i != maxChild);
            // call getSecondMax() only after adding the subtrees of max at the top level of the heap
            max = getSecondMax();
        }
        // remove the old max node from the fibonacci heap
        removeNode(temp, true);
        // perform a meld operation every time removeMax() is called
        meld();
        return temp;
    }

    /**
     * Melds trees of the same degree in the fibonacci heap.
     */
    private void meld() {
        HashMap<Integer, Node> degreeTable = new HashMap<Integer, Node>();

        // if heap is empty return null
        if(max == null)
            return;

        // if there is only one subtree in the heap, no need to meld
        if(max.getRightSibling() == max && max.getLeftSibling() == max)
            return;

        Node i = max;
        do {
            Node k = i.getRightSibling();
            // check if there is a tree with the same degree as i
            if(degreeTable.containsKey(i.getDegree())) {
                // get the tree which has the same degree as i
                Node existingTree = degreeTable.get(i.getDegree());
                // remove the tree from degree table
                degreeTable.remove(i.getDegree());
                // combine trees of the same degree
                combineTrees(degreeTable, i, existingTree);
            } else {
                degreeTable.put(i.getDegree(), i);
            }
            i = k;
        } while (i != max);
    }

    /**
     * Utility function that recursively combines trees of the same degree in the fibonacci heap.
     * @param degreeTable the table(hash map) containing information about the degrees of already checked trees
     * @param a one of the trees to be combined
     * @param b one of the trees to be combined
     */
    private void combineTrees(HashMap<Integer, Node> degreeTable, Node a, Node b) {
        Node newTree;

        // check if either a, b is the max node
        if(a == max)
            newTree = makeChild(a, b);
        else if(b == max)
            newTree = makeChild(b, a);
        else
            // make the node with smaller count as the subtree of the other
            newTree = a.getFrequency() > b.getFrequency() ?  makeChild(a, b) : makeChild(b, a);

        Node existingTree = null;
        // check if there is an existing tree with the same degree as newTree
        if(degreeTable.containsKey(newTree.getDegree())) {
            existingTree = degreeTable.get(newTree.getDegree());
            degreeTable.remove(existingTree.getDegree());
            combineTrees(degreeTable, existingTree, newTree);
        }
        else {
            degreeTable.put(newTree.getDegree(), newTree);
            return;
        }
    }

    /**
     * Utility function to make a tree the subtree of another tree.
     * @param parent the root of the tree to which the subtree is added as a child
     * @param child the root of the tree that becomes the child of another tree
     * @return the root of the parent tree
     */
    private Node makeChild(Node parent, Node child) {
        Node parentChild = parent.getChild();
        // remove the child (and it's subtree) from the heap first
        removeNode(child, false);
        // check if parent has children
        if (parentChild == null) {
            parent.setChild(child);
        } else {
            insertIntoList(parentChild, child);
        }
        // set the parent field of child node
        child.setParent(parent);
        // update the degree field of parent node
        parent.setDegree(parent.getDegree() + 1);
        return parent;
    }

    /**
     * Utility function to find the second maximum keyword in the fibonacci heap.
     * Since we are adding the subtrees of max to the top level before deleting max,
     * secondMax is guaranteed to be at the top level only.
     * @return the node corresponding to the second maximum keyword in the fibonacci heap
     */
    private Node getSecondMax() {
        // if heap is empty return null
        if(max == null)
            return null;

        // iterator node
        Node i;
        // possible secondMax at the root level
        Node secondMax;

        // search for possible secondMax at the max level
        // check if max has siblings
        if(max.getRightSibling() == max && max.getLeftSibling() == max) {
            secondMax = null;
        } else {
            i = max.getRightSibling();
            secondMax = max.getRightSibling();
            while (i != max) {
                if(i.getFrequency() > secondMax.getFrequency())
                    secondMax = i;
                i = i.getRightSibling();
            }
        }

        return secondMax;
    }

    /**
     * Utility function to add a node (and it's subtree) at the top level in the fibonacci heap.
     * @param node the root of the tree to be added in the fibonacci heap
     */
    private void addNode(Node node) {
        /* if the heap is empty add it directly else
           add it next to the max node
           */
        if(max == null) {
            node.setLeftSibling(node);
            node.setRightSibling(node);
            max = node;
        } else {
            insertIntoList(max, node);
            // childCut is not defined for the root but set it to false
            node.setChildCut(false);
            // if the incoming node's count is greater set it as max
            if(node.getFrequency() > max.getFrequency()) {
                max = node;
            }
        }
        // insert pointer to the new node in the hash table
        hashTable.put(node.getKeyword(), node);
    }

    /**
     * Utility function to insert a node into a doubly linked list of nodes
     * @param head the head node of the doubly linked list of nodes
     * @param node the node to be added into the linked list
     */
    private void insertIntoList(Node head, Node node) {
        // set head as the left sibling of node
        node.setLeftSibling(head);
        // set the right sibling of head as right sibling of mode
        node.setRightSibling(head.getRightSibling());
        // set node as the left sibling of the right sibling of head
        head.getRightSibling().setLeftSibling(node);
        // set node as right sibling of head
        head.setRightSibling(node);
    }

    /**
     * Removes a node (and it's subtree) from the fibonacci heap. Note that this function
     * removes the entire subtree and not an individual node.
     * @param node the root of the subtree to be removed from the fibonacci heap
     * @param removeHashTableEntry specifies whether to remove the node's entry from the hash table
     */
    private void removeNode(Node node, boolean removeHashTableEntry) {
        // parent of the node
        Node parent = node.getParent();
        // set the child pointer of node's parent as nil and update parent's degree
        if(parent != null) {
            parent.setChild(null);
            parent.setDegree(parent.getDegree() - 1);
        }
        // set the parent pointer of the node as nil
        node.setParent(null);
        // if the node has siblings update their pointers before removing the node
        if (node.getLeftSibling() != node || node.getRightSibling() != node) {
            // if the node has a parent then update it's child pointer
            if(parent != null) {
                parent.setChild(node.getLeftSibling());
            }
            Node leftSibling = node.getLeftSibling();
            Node rightSibling = node.getRightSibling();
            leftSibling.setRightSibling(rightSibling);
            rightSibling.setLeftSibling(leftSibling);
        }
        node.setLeftSibling(node);
        node.setRightSibling(node);
        node.setChildCut(false);
        // remove pointer to the node in the hash table
        if(removeHashTableEntry) {
            hashTable.remove(node.getKeyword());
        }
    }

    /**
     * Removes and re-inserts a node (and it's subtree) into the heap, performing cascading cut
     * if necessary. This function is never called on a root node, it's always called on a node with a parent
     * @param node the root of the subtree to be removed and re-inserted into the fibonacci heap
     */
    private void removeAndReinsertNode(Node node) {
        // parent of the node
        Node parent = node.getParent();
        // remove the node (and its's subtree) from the heap
        removeNode(node, true);
        // re-insert node (and its's subtree) into the heap
        addNode(node);
        // perform cascading cut only if the node's parent is not a root
        if(parent.getParent() != null) {
            // if childCut is already true, remove and re-insert the parent into the heap
            // else set childCut of parent to true
            if(parent.isChildCut()) {
                removeAndReinsertNode(parent);
            } else {
                parent.setChildCut(true);
            }
        }
    }
}
