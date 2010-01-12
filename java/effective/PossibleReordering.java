package effective;


public class PossibleReordering {
	static int x = 0, y = 0;
	static int a = 0, b = 0;
	static int[][] count = new int[2][2];

	public static void main(String[] args) throws InterruptedException {

		for (int i = 0; i < 100000; i++) {
			x = 0;
			y = 0;
			a = 0;
			b = 0;
			Thread one = new Thread(new Runnable() {
				public void run() {
					a = 1;
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					x = b;
				}
			});
			Thread other = new Thread(new Runnable() {
				public void run() {
					b = 1;
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					y = a;
				}
			});
			one.start();
			other.start();
			one.join();
			other.join();
			count[x][y] += 1;

		}
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				System.out.println("count=" + count[i][j] + "(x=" + i + ",y="
						+ j + ")");
			}
		}

	}
}
