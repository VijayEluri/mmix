package effective.lazy.init;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import junit.framework.TestCase;

public class TestLazyInit extends TestCase {
	@Override
	public void setUp() {
		FieldType.setCount(0);
	}

	public void testNormalLazy() {
		final NormalLazy lazy = new NormalLazy();
		int nThreads = 10;
		ExecutorService exec = Executors.newFixedThreadPool(nThreads);
		Runnable command = new Runnable() {
			public void run() {
				FieldType field = lazy.getField();
				System.out.println(field + " count of instance is "
						+ FieldType.countInstance() + " thread id "
						+ Thread.currentThread().getId());
				TestCase.assertEquals(1, FieldType.countInstance());
			}
		};
		for (int i = 0; i < nThreads; i++) {
			exec.execute(command);
		}
		try {
			exec.awaitTermination(1, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void teststaticLazy() {
		System.out.println("teststaticLazy");
		StaticLazy.getField();
	}

	public void testDoubleCheck() {
		final DoubleCheck lazy = new DoubleCheck();
		int nThreads = 10;
		ScheduledExecutorService exec = Executors
				.newScheduledThreadPool(nThreads);
		Runnable command = new Runnable() {
			public void run() {
				FieldType field = lazy.getField();
				System.out.println(field + " count of instance is "
						+ FieldType.countInstance() + " thread id "
						+ Thread.currentThread().getId());
				TestCase.assertEquals(1, FieldType.countInstance());
			}
		};
		for (int i = 0; i < nThreads; i++) {
			exec.schedule(command, 1, TimeUnit.SECONDS);
		}
		try {
			exec.awaitTermination(2, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
