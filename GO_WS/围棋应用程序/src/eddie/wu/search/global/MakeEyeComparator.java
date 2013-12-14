package eddie.wu.search.global;

import java.util.Comparator;

/**
 * 做眼方成的虎口数越多越好
 * 
 * @author Eddie
 * 
 */
public class MakeEyeComparator implements Comparator<Candidate> {
	/**
	 * TODO: eating some time is not efficient. ##01,02,03,04,05,06,07 <br/>
	 * 01[_, _, B, _, _, _, _]01<br/>
	 * 02[_, _, B, _, _, _, _]02<br/>
	 * 03[_, _, B, _, _, B, W]03<br/>
	 * 04[B, B, B, B, B, _, _]04<br/>
	 * 05[B, W, B, _, B, B, W]05<br/>
	 * 06[B, W, W, B, W, W, W]06<br/>
	 * 07[B, W, _, B, W, _, _]07<br/>
	 * ##01,02,03,04,05,06,07 <br/>
	 * whoseTurn=Black, point=[7,3]<br/>
	 */
	@Override
	public int compare(Candidate o1, Candidate o2) {
		if (o1.isEatingDead() != o2.isEatingDead()) {
			if (o1.isEatingDead())
				return 1;
			else
				return -1;
		}

		if (o1.isEating() != o2.isEating()) {
			if (o1.isEating())
				return -1;
			else
				return 1;
		}

		if (o1.getEyes() != o2.getEyes()) {
			return o2.getEyes() - o1.getEyes();
		}

		if (o1.getCountEyePoint() != o2.getCountEyePoint()) {
			return o2.getCountEyePoint() - o1.getCountEyePoint();
		}

		if (o1.getTigerMouths() != o2.getTigerMouths()) {
			return o2.getTigerMouths() - o1.getTigerMouths();
		}

		if (o1.isCapturing() != o2.isCapturing()) {
			if (o1.isCapturing()) {
				return -1;
			} else {
				return 1;
			}
		}

		// o1.isEating() == o2.isEating() 气长的优先
		if (o1.getBreaths() != o2.getBreaths()) {
			return o2.getBreaths() - o1.getBreaths();
		}

		return 0;
	}

}
