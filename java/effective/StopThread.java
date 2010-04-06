package effective;

import java.util.concurrent.TimeUnit;

public class StopThread {

	public static class ExecuteRunnable implements Runnable {
		private DataFlag flag;

		public ExecuteRunnable() {

		}

		public ExecuteRunnable(DataFlag flag) {
			this.flag = flag;
		}

		public static synchronized void start() {
			ExecuteRunnable.class.notifyAll();
		}

		public void run() {
			while (true) {
				if (this.flag.isStopRequested()) {
					System.out.println("stopped");
				}
				synchronized (ExecuteRunnable.class) {
					try {
						this.wait();
					} catch (Exception e) {

					}
				}
			}

		}
	}

	public static void main(String[] args) throws InterruptedException {
		final DataFlag flag = new DataFlag();
		final Thread[] threads = new Thread[1];
		final ExecuteRunnable[] rbs = new ExecuteRunnable[1];
		for (int i = 0; i < rbs.length; i++) {
			rbs[i] = new ExecuteRunnable(flag);
		}

		for (int i = 0; i < rbs.length; i++) {
			threads[i] = new Thread(rbs[i]);
			threads[i].start();
		}

		Thread backgroundThread = new Thread(new Runnable() {
			public void run() {
				while (true) {
					for (int i = 0; i < rbs.length; i++) {
						rbs[i].start();
					}
					try {
						Thread.sleep(1L);
					} catch (Exception e) {

					}
				}
			}
		});
		backgroundThread.start();
		TimeUnit.SECONDS.sleep(1);
		flag.setStopRequested(true);
	}
}

class DataFlag {
	private boolean stopRequested;

	public boolean isStopRequested() {
		return stopRequested;
	}

	public void setStopRequested(boolean stopRequested) {
		this.stopRequested = stopRequested;
	}
}
