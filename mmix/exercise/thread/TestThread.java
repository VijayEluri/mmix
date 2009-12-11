package thread;


public class TestThread  {
	public static void main(String [] args) {
		System.out.println("before start thread");
		new LoopThread().start();
		new LoopThread().start();
		new LoopThread().start();
		System.out.println("after start thread");
	}
}
