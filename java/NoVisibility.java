import java.util.concurrent.TimeUnit;
public class NoVisibility {

private static volatile boolean ready;
private static int number;
private static class ReaderThread2 extends Thread {
public void run() {
while (!ready)
Thread.yield();
System.out.println(number);
}
}
public static void main(String[] args) throws InterruptedException{
new ReaderThread().start();
TimeUnit.SECONDS.sleep(1);
number = 42;
ready = true;
}
}
