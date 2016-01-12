package eddie.wu.concurrent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import junit.framework.TestCase;
import eddie.wu.search.two.VerifyAll_IT;

public class DemoMultiProgramming extends TestCase {
	long start, end;
	//6.1
	public void testAMultipleThread() {
		start = System.nanoTime();
		_testMultipleThread1();
		end = System.nanoTime();
		System.out.println("It takes " + (end - start)
				+ " nano sec in multiple thread mode.");
	}
	
	public void testMultipleThread_overhead() {
		start = System.nanoTime();
		_testMultipleThread_overhead2();
		end = System.nanoTime();
		System.out.println("It takes " + (end - start)
				+ " nano sec in multiple thread mode (pure overhead).");
	}

	private void _testMultipleThread1() {
		ExecutorService service = Executors.newFixedThreadPool(8);
		Collection<Runnable> tasks = new ArrayList<Runnable>();
		Runnable task;
		task = new Runnable() {
			public void run() {
				new VerifyAll_IT().testState3011();
			}
		};
		Future<?> future1 = service.submit(task);

		task = new Runnable() {
			public void run() {
				new VerifyAll_IT().testState1011();
			}
		};
		Future<?> future2 = service.submit(task);

		task = new Runnable() {
			public void run() {
				new VerifyAll_IT().testState1012();
			}
		};
		Future<?> future3 = service.submit(task);

		task = new Runnable() {
			public void run() {
				new VerifyAll_IT().testState2011();
			}
		};
		Future<?> future4 = service.submit(task);

		task = new Runnable() {
			public void run() {
				new VerifyAll_IT().testState2012();
			}
		};
		Future<?> future5 = service.submit(task);

		try {
			future1.get();
			future2.get();
			future3.get();
			future4.get();
			future5.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		System.out.println("future1.isDone? " + future1.isDone());
		System.out.println("future2.isDone? " + future2.isDone());
		System.out.println("future3.isDone? " + future3.isDone());
		System.out.println("future4.isDone? " + future4.isDone());
		System.out.println("future5.isDone? " + future5.isDone());
		service.shutdown();

	}
	
	private void _testMultipleThread_overhead2() {
		ExecutorService service = Executors.newFixedThreadPool(8);
		Collection<Runnable> tasks = new ArrayList<Runnable>();
		Runnable task;
		task = new Runnable() {
			public void run() {
				System.out.println("Fake task 1.");
			}
		};
		Future<?> future1 = service.submit(task);

		task = new Runnable() {
			public void run() {
				System.out.println("Fake task 2.");
			}
		};
		Future<?> future2 = service.submit(task);

		task = new Runnable() {
			public void run() {
				System.out.println("Fake task 3.");
			}
		};
		Future<?> future3 = service.submit(task);

		task = new Runnable() {
			public void run() {
				System.out.println("Fake task 4.");
			}
		};
		Future<?> future4 = service.submit(task);

		task = new Runnable() {
			public void run() {
				System.out.println("Fake task 5.");
			}
		};
		Future<?> future5 = service.submit(task);

		try {
			future1.get();
			future2.get();
			future3.get();
			future4.get();
			future5.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		System.out.println("future1.isDone? " + future1.isDone());
		System.out.println("future2.isDone? " + future2.isDone());
		System.out.println("future3.isDone? " + future3.isDone());
		System.out.println("future4.isDone? " + future4.isDone());
		System.out.println("future5.isDone? " + future5.isDone());
		service.shutdown();

	}

	//8.0
	public void testSingleThread() {
		start = System.nanoTime();
		_testSingleThread();
		end = System.nanoTime();
		System.out.println("It takes " + (end - start)
				+ " nano sec in single thread mode.");
	}

	private void _testSingleThread() {
		VerifyAll_IT it = new VerifyAll_IT();
		it.testState3011();
		it.testState1011();
		it.testState1012();
		it.testState2011();
		it.testState2012();
	}

	public static void main(String[] args) {
		//improvement is big!
		compare2();
	}
	//6.1 v.s. 5.7
	public static void compare1(){
		DemoMultiProgramming demo = new DemoMultiProgramming();
		long start = System.nanoTime();
		demo.testAMultipleThread();
		long end = System.nanoTime();
		
		demo.testSingleThread();
		System.out.println("It takes " + (end - start)
				+ " nano sec in multiple thread mode.");
	}
	
	//7.5 v.s. 4.1
	public static  void compare2(){
		DemoMultiProgramming demo = new DemoMultiProgramming();
		long start = System.nanoTime();
		demo.testSingleThread();		
		long end = System.nanoTime();		
		demo.testAMultipleThread();
		System.out.println("It takes " + (end - start)
				+ " nano sec in single thread mode.");
	}
}
