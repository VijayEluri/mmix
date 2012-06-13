package eddie.wu.search.global;

import junit.framework.TestCase;

public class TestGlobalSearch extends TestCase{
	public void test2(){
		GoBoardSearch goS = new GoBoardSearch(2);
		goS.globalSearch();
	}
	
	public void test3(){
		GoBoardSearch goS = new GoBoardSearch(3);
		goS.globalSearch();
	}
	
	
}
