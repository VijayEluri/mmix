package core.random;

public class AdditiveNG {
	int[] y = new int[56];
	int j = 24, k = 55;

	public int next() {
		y[k] = (y[j] + y[k]);
		int temp = y[k];
		j--;
		k--;
		if (j == 0)
			j = 55;
		if (k == 0)
			k = 55;
		return temp;
	}
}
