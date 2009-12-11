package taocp.v3;

public class TAOCPSample {
	static int[] sample = new int[] { 503, 87, 512, 61, 908, 170, 897, 275,
			653, 426, 154, 509, 612, 677, 765, 703 };
	static int[] sample_1 = new int[] { 0, 503, 87, 512, 61, 908, 170, 897,
			275, 653, 426, 154, 509, 612, 677, 765, 703 };

	public static int[] getSample_ZeroIndexed() {
		return sample;
	}

	public static int[] getSample_OneIndexed() {
		return sample_1;
	}

	// result should be like this
	// 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15 16 17
	// 0 1 3 5 7 10 13 16 19 22 26 29 33 37 41 45 49
	static int[] optimized_comparision_times = new int[] { 0, 0, 1, 3, 5, 7,
			10, 13, 16, 19, 22, 26, 29, 33, 37, 41, 45, 49 };
	public static int getOptimizedComparisionTimes(int index){
		return optimized_comparision_times[index];
	}
}
