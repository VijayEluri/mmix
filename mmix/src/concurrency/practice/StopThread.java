package concurrency.practice;

/**
 * I can not reproduce the issue mentioned in ``Effective Java'' in JDK 1.5 on
 * Sun Sparc, and Xeon.
 * 
 * I have ever thought I may be able to reproduce it in Sun Sparc, so it is a
 * little surprising to find that the fact is opposite.
 */

public class StopThread {
	private static boolean stopRequested;

	public static void main(String[] args) throws InterruptedException {
		Thread backgroundThread = new Thread(new Runnable() {
			public void run() {
				int i = 0;
				while (!stopRequested) {
					i++;
					if (i % 10000 == 0) {
						System.out.println(i);
					}
				}
			}
		});
		backgroundThread.start();
		Thread.sleep(1000);
		stopRequested = true;
	}
}