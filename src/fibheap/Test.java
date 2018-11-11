package fibheap;

import java.util.HashMap;

public class Test {

    // cascading cut test
    public static void test1() {
        // The node that contains the max key
        Node max;
        // Hash table with key -> keyword and value -> node of the keyword
        HashMap<String, Node> hashTable = new HashMap<String, Node>();

        Node a = new Node("a", 300);
        hashTable.put("a", a);
        Node b = new Node("b", 290);
        hashTable.put("b", b);
        Node c = new Node("c", 280);
        hashTable.put("c", c);
        Node d = new Node("d", 270);
        hashTable.put("d", d);
        Node e = new Node("e", 260);
        hashTable.put("e", e);
        Node f = new Node("f", 250);
        hashTable.put("f", f);
        Node g = new Node("g", 240);
        hashTable.put("g", g);

        max = a;
        update(a, 3, b, a, a, null, false);
        update(b, 2, e, d, c, a, true);
        update(c, 0, null, b, d, a, false);
        update(d, 0, null, c, b, a, false);
        update(e, 1, g, f, f, b, true);
        update(f, 0, null, e, e, b, false);
        update(g, 0, null, g, g, e, false);

        FibonacciHeap fibHeap = new FibonacciHeapImpl(max, hashTable);
        fibHeap.print();
        fibHeap.increaseKey("g", 500);
        fibHeap.print();
    }

    public static void test2() {
        // The node that contains the max key
        Node max;
        // Hash table with key -> keyword and value -> node of the keyword
        HashMap<String, Node> hashTable = new HashMap<String, Node>();

        Node a = new Node("a", 300);
        hashTable.put("a", a);
        Node b = new Node("b", 290);
        hashTable.put("b", b);
        Node c = new Node("c", 280);
        hashTable.put("c", c);
        Node d = new Node("d", 270);
        hashTable.put("d", d);
        Node e = new Node("e", 300);
        hashTable.put("e", e);
        Node f = new Node("f", 250);
        hashTable.put("f", f);
        Node g = new Node("g", 300);
        hashTable.put("g", g);

        max = a;
        update(a, 2, b, g, e, null, false);
        update(b, 1, d, c, c, a, false);
        update(c, 0, null, b, b, a, false);
        update(d, 0, null, d, d, b, false);
        update(e, 1, f, a, g, null, true);
        update(f, 0, null, f, f, e, false);
        update(g, 0, null, e, a, null, false);

        FibonacciHeap fibHeap = new FibonacciHeapImpl(max, hashTable);
        fibHeap.print();
        fibHeap.removeMax();
        fibHeap.removeMax();
        fibHeap.removeMax();
        fibHeap.removeMax();
        fibHeap.removeMax();
        fibHeap.print();
        fibHeap.removeMax();
        fibHeap.print();
        fibHeap.removeMax();
        fibHeap.removeMax();
        fibHeap.removeMax();
        fibHeap.removeMax();
        fibHeap.removeMax();
        fibHeap.removeMax();
        fibHeap.removeMax();
        fibHeap.print();
    }

    public static void test3() {
        // The node that contains the max key
        Node max;
        // Hash table with key -> keyword and value -> node of the keyword
        HashMap<String, Node> hashTable = new HashMap<String, Node>();

        Node a = new Node("a", 300);
        hashTable.put("a", a);
        Node b = new Node("b", 300);
        hashTable.put("b", b);
        Node c = new Node("c", 300);
        hashTable.put("c", c);
        Node d = new Node("d", 300);
        hashTable.put("d", d);
        Node e = new Node("e", 300);
        hashTable.put("e", e);
        Node f = new Node("f", 300);
        hashTable.put("f", f);
        Node g = new Node("g", 300);
        hashTable.put("g", g);

        max = a;
        update(a, 2, b, g, e, null, false);
        update(b, 1, d, c, c, a, false);
        update(c, 0, null, b, b, a, false);
        update(d, 0, null, d, d, b, false);
        update(e, 1, f, a, g, null, true);
        update(f, 0, null, f, f, e, false);
        update(g, 0, null, e, a, null, false);

        FibonacciHeap fibHeap = new FibonacciHeapImpl(max, hashTable);
        fibHeap.print();
        fibHeap.removeMax();
        fibHeap.removeMax();
        fibHeap.removeMax();
        fibHeap.removeMax();
        fibHeap.removeMax();
        fibHeap.removeMax();
        fibHeap.removeMax();
        fibHeap.removeMax();
        fibHeap.removeMax();
        fibHeap.removeMax();
        fibHeap.removeMax();
        fibHeap.print();
    }

    public static void update(Node node, int degree, Node child, Node leftSibling, Node rightSibling, Node parent, boolean childCut) {
        node.setDegree(degree);
        node.setChild(child);
        node.setLeftSibling(leftSibling);
        node.setRightSibling(rightSibling);
        node.setParent(parent);
        node.setChildCut(childCut);
    }
}
