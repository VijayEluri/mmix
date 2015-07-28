package eddie.wu.util;

import java.util.Random;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class MathExercise {
	private static final Logger log = Logger.getLogger(MathExercise.class);
	private Random random = new Random(100);

	/**
	 * 生成100以内的加减法，连加，连减，混合加减。带点巧算。
	 */
	public void generateExercise() {
		for (int i = 0; i < 5; i++) {
			generateExercise_lianjia();
			generateExercise_lianjian();
			generateExercise_hunhejiajian();
		}
	}

	public void generateExercise_lianjia() {
		int a1 = random.nextInt(64);
		if (log.isEnabledFor(Level.WARN))
			log.warn("a1=" + a1);
		int a2 = random.nextInt(64);
		if (log.isEnabledFor(Level.WARN))
			log.warn("a2=" + a2);
		int a1_last = a1 % 10;
		int a3 = a1_last + random.nextInt(a1 / 10) * 10;
		if (log.isEnabledFor(Level.WARN))
			log.warn(a1 + " + " + a2 + " - " + a3 + " = ");
	}

	public void generateExercise_lianjian() {

	}

	public void generateExercise_hunhejiajian() {

	}

	public static void main(String[] args) {
		new MathExercise().generateExercise();
	}
}
