package thread;

public class LoopThread extends Thread {
	int count = 0;

	public void run() {
		while (count++ < 1000) {
			System.out.println(this.getName()+": I am fine!" + count);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
