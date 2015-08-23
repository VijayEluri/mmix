package eddie.wu.search.two;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.Constant;
import eddie.wu.domain.GoBoardForward;
import eddie.wu.domain.analy.SurviveAnalysis;
import eddie.wu.search.global.GoBoardSearch;

/**
 * 需要不同的log策略：单元测试log需要尽量详细，便于定位问题。集成测试log需要尽量简单，提升性能。 <br/>
 * 目前的解决办法是普通测试类用于单元测试，同时建立以_IT为后缀的子类。后者改变log级别。 <br/>
 * 如果更为全局的测试，比如suite则应当包含_IT子类。
 * 
 * @author think
 *
 */
public class TestAllState2_IT extends TestAllState2 {
	/**
	 * 貌似子类的static块后执行。
	 */
	static {
		Constant.INTERNAL_CHECK = false;
		Logger.getLogger(SurviveAnalysis.class).setLevel(Level.ERROR);
		Logger.getLogger(GoBoardSearch.class).setLevel(Level.ERROR);
		Logger.getLogger(GoBoardForward.class).setLevel(Level.ERROR);
		Logger.getLogger(TestAllState2.class).setLevel(Level.ERROR);
	}
}
