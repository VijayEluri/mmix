package eddie.wu.linkedblock;

import junit.framework.TestCase;

public class TestLoadExercise extends TestCase {
	public void testLoadExercise(){
		BoardColorState state=new LoadExercise().loadZhengZi();
		System.out.println(state.toString());
		//System.out.println(state.getDisplayString());
	}
}
