package challenge;


public class Cantor {

	public static Position fun(int temp) {
		if (temp > 100000 || temp < 1)
			throw new RuntimeException();
		int oldsum = 0;
		int newsum = 0;
		for (int i = 1; oldsum < temp; i++) {
			newsum = cn2(i);
			if (newsum >= temp) {
				int variance = (temp - oldsum);

				return getPosition(i, variance);
			} else {
				oldsum = newsum;
			}
		}
		return null;

	}

	private static Position getPosition(int n, int variance) {
		System.out.println("middle: n=" + n + "; variance=" + variance);
		Position p = new Position(1, n);
		for (int i = 1; i < variance; i++) {
			p.increment();
		}
		return p;
	}

	private static int cn2(int n) {
		return n * (n + 1) / 2;
	}

}
