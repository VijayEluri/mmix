package eddie.wu.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import eddie.wu.domain.BlankBlock;
import eddie.wu.domain.Block;
import eddie.wu.domain.ColorUtil;
import eddie.wu.domain.Constant;
import eddie.wu.domain.Delta;
import eddie.wu.domain.GoBoard;
import eddie.wu.domain.Point;
import eddie.wu.domain.survive.RelativeSurviveResult;
import eddie.wu.domain.survive.SmallEye;
import eddie.wu.domain.survive.SurviveResult;

/**
 * 利用悔棋的功能来实现计算,性能应该比复制棋盘状态好,<br/>
 * 因为不需要重复的根据局面生成高级的数据结构,当然以后需要实测检验.<br/>
 * 
 * 先作最基本的功能,限于计算大眼死活,先不考虑打劫.<br/>
 * 
 * 重新实现一个,思路也有所不同,不再复制棋局状态,而是通过悔棋功能和棋谱功能<br/>
 * 在局面之间前进,后退.考虑的测试用例是曲四的死活(眼块术语两块棋).
 * 
 * @author Eddie
 * 
 */
public class SurviveCalculateWithoutCopy extends GoBoard {

	private static final Logger log = Logger
			.getLogger(SurviveCalculateWithoutCopy.class);

	public int survive(Point point) {
		Block block = this.getBlock(point);

		return SurviveResult.LIVE;
	}

	/**
	 * 
	 * @param eyeBlock
	 * @param meFirst 眼块一方先走
	 * @return
	 */
	public int eyeSurvive(BlankBlock eyeBlock, boolean meFirst) {
		if (eyeBlock.isEyeBlock()) {
			throw new RuntimeException("Not a eye block: " + eyeBlock);
		}

		if (eyeBlock.getNeighborBlocks().size() == 1) {
			// 完整的大眼,有捷径(现成的知识)
			SmallEye se = new SmallEye(eyeBlock);
			RelativeSurviveResult result = se.getResult();
			if (result.getHouShou().getSurvive() == RelativeSurviveResult.LIVE) {
				return RelativeSurviveResult.LIVE;
			}
		}

		int minBreath = 256;

		/**
		 * 1. 考虑提吃成眼的块.
		 * 2. 考虑打吃成眼的块,包括扑.
		 * 3. 点眼的选择,点入后气数多为好.
		 */
		List<Point> candidates = new ArrayList<Point>();
		for (Block neighborB : eyeBlock.getNeighborBlocks()) {
			if (neighborB.getBreaths() < minBreath) {
				minBreath = neighborB.getBreaths();
				if (minBreath == 1) {
					candidates.addAll(neighborB.getBreathPoints());
					if (log.isInfoEnabled()) {
						log.info("1. 眼位不完整,对方可以提子 " + neighborB);
					}
				} else if (minBreath == 2) {
					candidates.addAll(neighborB.getBreathPoints());
					if (log.isInfoEnabled()) {
						log.info("2. 眼位不是很完整,对方可以打吃,小心断打 " + neighborB);
					}
				}
			}
		}
		if (minBreath > 2) {
			if (log.isInfoEnabled()) {
				log.info("3. 成眼的块有三气或者以上,安全; 选择气数多的地方点入.");
			}
		}

		// sort the candidate point
		// List<Point> candidates = new ArrayList<Point>();

		// for (Block neighborB : eyeBlock.getAllBlocks()) {
		// allPoints.removeAll(neighborB.getAllBreathPoints());
		// }
		// if (allPoints.isEmpty() == false) {// 中心点容易成眼.
		// candidates.addAll(allPoints);
		// }
		// allPoints.clear();
		// allPoints.addAll(eyeBlock.getAllPoints());
		// allPoints.removeAll(candidates);

		Set<Point> allPoints = new HashSet<Point>();
		allPoints.addAll(eyeBlock.getPoints());

		allPoints.removeAll(candidates);
		/**
		 * 3. 点眼的选择,点入后气数多为好.
		 */
		List<BlankPoint> points = new ArrayList<BlankPoint>();
		for (Point point : allPoints) {
			int breath = 0;
			for (Delta delta : Constant.ADJACENTS) {
				Point nb = point.getNeighbour(delta);
				if (nb == null)
					continue;
				if (this.getColor(nb) == ColorUtil.BLANK) {
					breath++;
				}
			}
			points.add(new BlankPoint(point, breath));

		}
		// sort by breath;
		Collections.sort(points, new BlankPointBreathComparator());
		Collections.reverse(points);
		for (BlankPoint point : points) {
			candidates.add(point.getPoint());
		}
		
		int myColor;
		if(meFirst){
			myColor = eyeBlock.getColor(); 
		}else{
			myColor = eyeBlock.getEnemyColor();
		}
		this.oneStepForward(candidates.get(0), myColor);
		//this.getLastStep()
		
		/**
		 * 对活棋一方来说,就是要杀死点入之子,取得一个眼位,同时尽量围出另一个眼位.
		 * 落子先考虑紧气.<br/>
		 * 
		 * 点眼和紧气这两个回合基本上决定棋的死活.<br/>
		 * 对方杀不掉即是活棋,己方未必要有下一步棋做活的手段.
		 * 
		 * 
		 * 
		 */
		
		
		
		
		return SurviveResult.LIVE;
	}
}
