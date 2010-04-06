package effective.builder;

import junit.framework.TestCase;

public class TestNutritionFacts extends TestCase{
	public void test() {
		NutritionFacts cocaCola = new NutritionFacts.Builder(240, 8).calories(
				100).sodium(35).carbohydrate(27).build();
		System.out.println("getCalories() "+cocaCola.getCalories());
	}
}
