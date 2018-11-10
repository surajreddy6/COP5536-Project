package fibheap;

public interface FibonacciHeap {
    Node insert(String keyword, int frequency);
    Node increaseKey(String keyword, int frequency);
    Node removeMax();
    Node getMax();
    public void print();
}
