package fibheap;

import java.util.HashMap;

public class FibonacciHeap {
    // The node that contains the max key
    private Node max;
    // The tree that contains the max key
    private Tree maxTree;
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
            // Create a new tree with a single node
            Tree newTree = new Tree(newNode);
            // insert pointer to the new node in the hash table
            hashTable.put(keyword, newNode);
            // insert the new tree into the fibonacci heap
            addTree(newTree);
        }
    }

    public void addTree(Tree tree) {
        /* if the heap is empty add it directly else
           add it next to the maxTree
           */
        if(maxTree == null) {
            tree.setLeftTree(tree);
            tree.setRightTree(tree);
            maxTree = tree;
            max = tree.getRoot();
        } else {
            // set maxTree as the left tree of the new tree
            tree.setLeftTree(maxTree);
            // set the right tree of maxTree as
            tree.setRightTree(maxTree.getRightTree());
            // set the left tree of the right tree of maxTree as tree
            maxTree.getRightTree().setLeftTree(tree);
            // set right tree of maxTree as tree
            maxTree.setRightTree(tree);
            /* if the count at root at root of new tree is greater than
               the max, set it as the new max
               */
            if(maxTree.getRoot().getCount() < tree.getRoot().getCount()) {
                max = tree.getRoot();
                maxTree = tree;
            }
        }
    }

    public void increaseKey(Node node, int frequency) {
        // if the node is a root node just increase the count and check with max
        if(node.getParent() == null) {
            node.setCount(node.getCount() + frequency);
            if(node.getCount() > max.getCount()) {

            }
        }
    }
}
