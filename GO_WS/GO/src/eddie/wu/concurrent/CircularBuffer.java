package eddie.wu.concurrent;

/**
 * home grown implementation of queue. Not Thread safe!
 * @author think
 *
 */
public class CircularBuffer {
	private Object[] buffer;
	// start is inclusive (if queue is not empty), and end is exclusive.
	int start = 0, end = 0;
	private int size; // length of container array.
	private int capacity; // efficient place to store data.

	/**
	 * there are (size) elements available to store value in (size+1) slots
	 * 
	 * @param capacity
	 *            >=1
	 */
	public CircularBuffer(int capacity) {
		this.capacity = capacity;
		this.size = capacity + 1;
		buffer = new Object[size];
	}

	public boolean isEmpty() {
		return start == end;
	}

	public boolean isFull() {
		return (end + 1) % (size) == start;
	}

	public int capacity() {
		return this.capacity;
	}

	public int elements() {
		if (start <= end) {
			return end - start;
		} else {
			return end + size - start;
		}
	}

	public void add(Object obj) {
		if (this.isFull())
			throw new RuntimeException("full!");
		buffer[end++] = obj;
		if (end == size)
			end = 0;
	}

	public Object remove() {
		if (this.isEmpty())
			throw new RuntimeException("empty!");
		Object temp = buffer[start++];
		if (start == size)
			start = 0;
		return temp;
	}
}
