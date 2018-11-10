package fibheap;

public interface FibonacciHeap {
    void insert(String keyword, int frequency);
    void increaseKey(String keyword, int frequency);
    Node removeMax();
    public void print();
}
