package fibheap;

public interface FibonacciHeap {
    void insert(String keyword, int frequency);
    void increaseKey(Node node, int frequency);
    Node removeMax();
}
