package eddie.wu.domain.survive;

import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.Point;
import eddie.wu.domain.survive.RelativeSurviveResult;
import eddie.wu.domain.survive.SurviveResult;
import eddie.wu.search.SurviveCalculate;
import go.StateAnalysis;
import junit.framework.TestCase;

public class TestSurviveCalculate extends TestCase {
	private String root = "doc/围棋程序数据/大眼基本死活/";
	private String name1 = "直三.wjm";
	private String name2 = "方四.wjm";
	private String name3 = "笠帽四.wjm";
	private String name4 = "盘角曲四.wjm";
	private String name5 = "刀把五.wjm";
	private String name6 = "板六_无外气.wjm";
	private String name7 = "板六_一口外气.wjm";
	private String name8 = "板六_两口外气.wjm";

	public void test() {
		byte[][] state = StateAnalysis.LoadState(root + name1);
		BoardColorState bcs = new BoardColorState(state);
		SurviveCalculate survive = new SurviveCalculate();
		SurviveResult result = survive.surviveCalculate(bcs,
				Point.getPoint(3, 16));
		assertFalse(result.isIndependent());
	}

	/**
	 * 0. 棋型的描述。右下为边。<br/>
	 * WWWWWW<br/>
	 * WBBBBB<br/>
	 * wBB123<br/>
	 * WBB456<br/>
	 * 板六没有外气的情况，从棋手的计算来说是非常容易和快速的，但是对于计算机来说，计算过程要 笨好多。<br/>
	 * 而且还要涉及打劫的知识，比如白方走3位就成为缓一气劫，尽管不是正解。<br/>
	 * 1. 因为没有外气，候选点就只有六个。棋手根据积累的经验，只要计算中间两点，所谓“左右同型走中央”
	 * 在计算机中，我的想法是要中间两点有更高的优先级被搜索，比如说，中间两点做活方先下可以形成两个虎口，
	 * 而其余四点只有一个虎口，甚至没有。所以点杀的一方优先考虑这两点。这个方法至少可以适用于大眼死活的搜索。<br/>
	 * 2. 整体上感觉，棋手征对具体的情况，有很多速算的方法。就像数学上的速算一样，而计算机很难做到这点。<br/>
	 * 3. 2位点后，还有五个候选点，如何得到优先级。根据形成的虎口数。同时紧气优先，非公气优先。
	 * （但是，计算机仍需考虑所有可能性，尽管看起来不太可能。这可能是速度慢的原因，会导致额外的很多计算量，也许可以有个
	 * 限制，比如优先级较低的就不在考虑。）
	 * 4. 5位夹后，四个候选点，有两个点自紧一气，还剩二气。另两个点为扑入。
	 * 5. 为了快速计算，必须将一些计算结果储存起来，比如刀把五。
	 */
	public void test6() {
		byte[][] state = StateAnalysis.LoadState(root + name6);
		BoardColorState bcs = new BoardColorState(state);
		SurviveCalculate survive = new SurviveCalculate();
		SurviveResult result = survive.surviveCalculate(bcs,
				Point.getPoint(3, 17));
		assertFalse(result.isIndependent());
	}
	
	/**
	 * 0. 棋型的描述。右下为边。<br/>
	 * WWWWWW<br/>
	 * WBBBBB<br/>
	 * W B123<br/>
	 * WBB456<br/>
	 * 1. 前两部的候选点相同。
	 * 2. 第四步扑入成劫。当然对方提后才真正开始劫争。  
	 */
	public void test7() {
		byte[][] state = StateAnalysis.LoadState(root + name7);
		BoardColorState bcs = new BoardColorState(state);
		SurviveCalculate survive = new SurviveCalculate();
		SurviveResult result = survive.surviveCalculate(bcs,
				Point.getPoint(3, 16));
		assertFalse(result.isIndependent());
	}
}
