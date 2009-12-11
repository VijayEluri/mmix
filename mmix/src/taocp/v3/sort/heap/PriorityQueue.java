package taocp.v3.sort.heap;

public class PriorityQueue {
	int size;

	int count = 0;

	Heap heap;

	public PriorityQueue(int size) {
		this.size = size;
		heap = new Heap(size);
	}

	public void insert(int value) {
		if (++count > size) {
			throw new IllegalStateException("the queue is full");
		}
		heap.add(value);
	}

	public int extractMin() {
		return heap.removeRoot();
	}

}
