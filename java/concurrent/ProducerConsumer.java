package concurrent;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

public class ProducerConsumer {
	private static Logger log = Logger.getLogger(ProducerConsumer.class);
	CircularBuffer queue;
	int capacity = 4;
	int total = 100;
	int threads = 4;
	Random sleep = new Random(0);
	Random product = new Random(0);
	AtomicInteger count1 = new AtomicInteger(0);
	AtomicInteger count2 = new AtomicInteger(0);

	public static void main(String[] args) {
		new ProducerConsumer().demo();
	}

	public void demo() {
		// this.capacity = 10;
		queue = new CircularBuffer(capacity);
		for (int i = 0; i < threads; i++) {
			new Thread("[Producer Thread " + (i + 1) + "]") {
				public void run() {
					produce();
				}
			}.start();
			new Thread("[Consumer Thread " + (i + 1) + "]") {
				public void run() {
					consume();
				}
			}.start();
		}

		// try {
		// Thread.currentThread().join();
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// if (count2.get() == total + threads) {
		// System.out.println("finished");
		// } else {
		// System.out.println("finished" + count2.get());
		// }
	}

	public void produce() {
		int count = 0;
		String threadName = Thread.currentThread().getName();
		while ((count = count1.incrementAndGet()) <= total) {

			String a = "product " + count + "[" + product.nextInt(total) + "]";
			log.debug(threadName + " produce " + a);
			synchronized (queue) {
				boolean notify = false;
				if (queue.isEmpty()) {
					notify = true;
				}
				while (queue.isFull() == true) {
					try {
						log.debug(threadName + " producer wait " + queue.start
								+ "," + queue.end);
						queue.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				queue.add(a);
				log.debug(threadName + " add " + a);
				if (notify) {
					log.debug(threadName
							+ "Produce notify Consumer when adding into empty queue");
					queue.notifyAll();
				}
			}

			int millis = 0;
			if (count1.get() * 2 <= total) {
				millis = sleep.nextInt(100);
			} else {
				millis = sleep.nextInt(50);
			}

			try {
				Thread.sleep(millis);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		log.debug(threadName + " finished at " + count1.get());
	}

	public void consume() {
		int count = 0;
		Object removed;
		String threadName = Thread.currentThread().getName();
		while ((count = count2.incrementAndGet()) <= total) {
			synchronized (queue) {
				boolean notify = false;
				if (queue.isFull()) {
					notify = true;
				}
				while (queue.isEmpty() == true) {
					try {
						log.debug(threadName + " consumer wait");
						queue.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				removed = queue.remove();
				log.debug(threadName + " consume/remove " + removed.toString()
						+ " as " + count);
				if (notify) {
					log.debug(threadName
							+ " Consumer notify producer when removing from full queue");
					queue.notifyAll();
				}
			}
			int millis = 0;
			if (count2.get() * 2 <= total) {
				millis = sleep.nextInt(50);
			} else {
				millis = sleep.nextInt(100);
			}
			try {
				Thread.sleep(millis);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}

		log.debug(threadName + " finished at " + count2.get());
	}

}
