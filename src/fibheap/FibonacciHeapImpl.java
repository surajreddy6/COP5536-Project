package fibheap;

import java.util.HashMap;
import java.util.Map;

public class FibonacciHeapImpl implements FibonacciHeap{
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
        System.out.println("*****************************************************");
        if(max == null) {
            System.out.println("Heap is empty!!!!!!!!!!!!!!!!!");
        } else {
            System.out.println("Max is " + max.getKeyword());
            for (Map.Entry<String, Node> entry : hashTable.entrySet()) {
                System.out.println(entry.getValue().toString());
            }
        }
    }

    @Override
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

    @Override
    public void increaseKey(Node node, int frequency) {
        // update count of node
        node.setCount(node.getCount() + frequency);
        // if the node is a root node compare it with with max
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

    @Override
    public Node removeMax() {
        // if heap is empty return null
        if(max == null)
            return null;

        // temporary variable to store max
        Node temp = max;

        System.out.println("Removing max node " + max.getKeyword());

        // if max node has no children, find the second max node and make it the max node removing the old max node
        // if the node has children do the same but also remove it's children and re-insert into the heap
        if(max.getChild() == null) {
            // check if max is the only node in the heap i.e, there is only one element in the heap
            if(max.getRightSibling() == max && max.getLeftSibling() == max) {
                max = null;
            } else {
                max = getSecondMax();
                System.out.println("New max is");
                System.out.println(max.toString());
            }
        } else {
            Node maxChild = max.getChild();
            System.out.println("New max is");
            System.out.println(max.toString());
            // remove children of max and re-insert them into the heap
            Node i = maxChild;
            do {
                Node k = i.getRightSibling();
                removeNode(i);
                addNode(i);
                i = k;
            } while(i != maxChild);
            // call getSecondMax() only after adding the subtrees of max at the top level of the heap
            max = getSecondMax();
        }
        // remove the old max node from the fib heap
        removeNode(temp);
        // perform a meld operation every time removeMax is called
        meld();
        return temp;
    }

    private void meld() {
        HashMap<Integer, Node> degreeTable = new HashMap<Integer, Node>();

        // if heap is empty return null
        if(max == null)
            return;

        // if there is only one subtree in the heap, no need to meld
        // possibly not needed
        if(max.getRightSibling() == max && max.getLeftSibling() == max)
            return;
        Node i = max;
        System.out.println("Melding...");
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
                System.out.println("Putting node " + i.getKeyword() + " in degree table");
                degreeTable.put(i.getDegree(), i);
            }
            i = k;
        } while (i != max);
    }

    // recursively combines trees of the same degree
    private void combineTrees(HashMap<Integer, Node> degreeTable, Node a, Node b) {
        System.out.println("Combining trees with keywords: " + a.getKeyword() + " and " + b.getKeyword());
        Node newTree;
        // make the node with smaller count as the subtree of the other
        newTree = a.getCount() > b.getCount() ?  makeChild(a, b) : makeChild(b, a);

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
        System.out.println("Making " + child.getKeyword() + " as child of " + parent.getKeyword());
        Node parentChild = parent.getChild();
        // remove the child (and it's subtree) from the heap first
        System.out.println("Removing " + child.getKeyword() + " from heap");
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

    // since we are adding the subtrees of max to the top level before deleting max,
    // secondMax is guaranteed to be at the top level only
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
                if(i.getCount() > secondMax.getCount())
                    secondMax = i;
                i = i.getRightSibling();
            }
        }

        return secondMax;
    }

    private void addNode(Node node) {
        System.out.println("Adding node " + node.getKeyword());
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

    // TODO: Support arbitrary remove operation. ATM this function deletes the node and it's subtree too.
    // TODO: use a flag to select if the entry from hashTable should also be deleted
    private void removeNode(Node node) {
        System.out.println("Removing node " + node.getKeyword());
        // set the child pointer of node's parent as nil
        if(node.getParent() != null) {
            node.getParent().setChild(null);
        }
        // set the parent pointer of the node as nil
        node.setParent(null);
        // if the node has siblings update their pointers before removing the node
        if (node.getLeftSibling() != node || node.getRightSibling() != node) {
            System.out.println("Updating sibling pointers of " + node.getKeyword());
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