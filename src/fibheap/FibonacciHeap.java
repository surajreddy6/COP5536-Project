package fibheap;

import java.util.HashMap;
import java.util.Map;

public class FibonacciHeap {
    // The node that contains the max key
    private Node max;
    // Hash table with key -> keyword and value -> node of the keyword
    private HashMap<String, Node> hashTable = new HashMap<String, Node>();

    // test function
    public void getMax() {
        System.out.println(max.toString());
    }

    // test function
    public void print() {
        for(Map.Entry<String, Node> entry : hashTable.entrySet()) {
            System.out.println(entry.getValue().toString());
        }
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
                System.out.println("New max");
                System.out.println(max.toString());
            }
        } else {
            Node maxChild = max.getChild();
            max = getSecondMax();
            System.out.println("New max");
            System.out.println(max.toString());
            // remove children of max and re-insert them into the heap
            Node i = maxChild;
            do {
                Node k = i.getRightSibling();
                removeNode(i);
                addNode(i);
                i = k;
            } while(i != maxChild);
        }
        // remove the old max node from the fib heap
        removeNode(temp);
        // perform a meld operation every time removeMax is called
        meld();
        return temp;
    }

    private void meld() {
        HashMap<Integer, Node> degreeTable = new HashMap<Integer, Node>();
        // if there is only one subtree in the heap, no need to meld
        // possibly not needed
        if(max.getRightSibling() == max && max.getLeftSibling() == max)
            return;
        Node i = max;
        System.out.println("melding...");
        do {
            Node k = i.getRightSibling();
            System.out.println("Current node in meld: " + i.getKeyword());
            // check if there is a tree with the same degree as i
            if(degreeTable.containsKey(i.getDegree())) {
                // get the tree which has the same degree as i
                Node existingTree = degreeTable.get(i.getDegree());
                System.out.println("Same degree found with key: " + existingTree.getKeyword());
                // remove the tree from degree table
                degreeTable.remove(i.getDegree());
                // combine trees of the same degree
                combineTrees(degreeTable, i, existingTree);
            } else {
                System.out.println("Putting node in degree table. keyword: " + i.getKeyword());
                degreeTable.put(i.getDegree(), i);
            }
            i = k;
        } while (i != max);
    }

    // recursively combines trees of the same degree
    private void combineTrees(HashMap<Integer, Node> degreeTable, Node a, Node b) {
        System.out.println("Combining trees with keywords: " + a.getKeyword() + " and " + b.getKeyword());
        Node newTree;
        if(a.getCount() > b.getCount()) {
            newTree = makeChild(a, b);
        }
        else {
            newTree = makeChild(b, a);
        }
        Node existingTree = null;
        // check if there is an existing tree with the same degree as newTree
        if(degreeTable.containsKey(newTree.getDegree())) {
            existingTree = degreeTable.get(newTree.getDegree());
            System.out.println("Same degree found with key(in combine): " + existingTree.getKeyword());
            degreeTable.remove(existingTree.getDegree());
            combineTrees(degreeTable, existingTree, newTree);
        }
        else {
            degreeTable.put(newTree.getDegree(), newTree);
            return;
        }
    }

    private Node makeChild(Node parent, Node child) {
        System.out.println("making " + child.getKeyword() + " as child of " + parent.getKeyword());
        Node parentChild = parent.getChild();
        // remove the child (and it's subtree) from the heap first
        System.out.println("removing " + child.getKeyword() + " from heap");
        removeNode(child);
        // add back only the reference to the child node
        hashTable.put(child.getKeyword(), child);
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

    // function to find the second max node in the heap
    // check the top level also one level below max for the secondMax
    private Node getSecondMax() {
        // if heap is empty return null
        if(max == null)
            return null;

        // iterator node
        Node i;
        // possible secondMax at the root level
        Node secondMax;
        // possible secondMax one level below max
        Node childSecondMax;

        // search for possible secondMax at the max level first
        // check if max has siblings
        if(max.getRightSibling() == max && max.getLeftSibling() == max) {
            secondMax = null;
        } else {
            i = max.getRightSibling();
            secondMax = max.getRightSibling();
            while (i != max) {
                if(i.getCount() > secondMax.getCount())
                    secondMax = i;
                i = i.getRightSibling();
            }
        }

        // search for possible secondMax one level below max
        Node maxChild = max.getChild();
        // check if max has no children
        if(maxChild == null) {
            childSecondMax = null;
        } else {
            i = maxChild;
            childSecondMax = maxChild;
            do {
                if(i.getCount() > childSecondMax.getCount())
                    childSecondMax = i;
                i = i.getRightSibling();
            } while (i != maxChild);
        }


        if(secondMax == null) {
            return childSecondMax;
        } else if(childSecondMax == null) {
            return secondMax;
        } else {
            return secondMax.getCount() > childSecondMax.getCount() ? secondMax : childSecondMax;
        }
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
            insertIntoList(max, node);
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

    // function to insert a node into a doubly linked list of nodes
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

    private void removeNode(Node node) {
        // set the child pointer of node's parent as nil
        if(node.getParent() != null) {
            node.getParent().setChild(null);
        }
        // set the parent pointer of the node as nil
        node.setParent(null);
        // if the node has siblings update their pointers before removing the node
        if (node.getLeftSibling() != node || node.getRightSibling() != node) {
            System.out.println("updating sibling pointers of " + node.getKeyword());
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
