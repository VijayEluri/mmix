package eddie.wu.search.global;

import java.util.Comparator;

/**
 * 打吃优先<br/>
 * 气长的优先<br/>
 * 高线优先<br/>
 * 
 * @author Eddie
 * 
 */
public class CandidateComparator implements Comparator<Candidate> {

	@Override
	public int compare(Candidate o1, Candidate o2) {
		if (o1.isBecomeLive() != o2.isBecomeLive()) {
			if (o1.isBecomeLive())
				return -1;
			else
				return 1;
		}
		/**
		 * ##01,02,03 <br/>
		 * 01[_, _, _]01<br/>
		 * 02[B, W, _]02<br/>
		 * 03[_, B, _]03<br/>
		 * ##01,02,03 <br/>
		 * whoseTurn=White
		 */
		if (o1.isGifting()) {
			o1.setEating(false);
			o1.setCapturing(false);
		}

		if (o2.isGifting()) {
			o2.setEating(false);
			o2.setCapturing(false);
		}

		if (o1.isEating() != o2.isEating()) {
			if (o1.isEating())
				return -1;
			else
				return 1;
		} else if (o1.isEating() && o2.isEating()) {
			// same eating, then the one remove the capturing win
			if (o1.isRemoveCapturing() != o2.isRemoveCapturing()) {
				if (o1.isRemoveCapturing()) {
					return -1;
				} else
					return 1;
			}
		}

		/**
		 * the more eyes, the better.
		 */
		int temp = o2.getEyes() - o1.getEyes();
		if (temp != 0)
			return temp;

		if (o1.isCapturing() != o2.isCapturing()) {
			if (o1.isCapturing())
				return -1;
			else
				return 1;
		}
		//
		if (o1.isGifting() != o2.isGifting()) {
			if (o1.isGifting())
				return 1;
			else
				return -1;
		}

		temp = o2.getCountEyePoint() - o1.getCountEyePoint();
		if (temp != 0)
			return temp;

//		if (o1.isIncreaseBreath() != o2.isIncreaseBreath()) {
//			if (o1.isIncreaseBreath())
//				return -1;
//			else
//				return 1;
//		}

		if (o1.getIncreasedBreath() != o2.getIncreasedBreath()) {
			return o2.getIncreasedBreath() - o1.getIncreasedBreath();
		}

		// o1.isEating() == o2.isEating() 气长的优先
		if (o1.getBreaths() != o2.getBreaths()) {
			return o2.getBreaths() - o1.getBreaths();
		}

		// o1.getBreaths()==o2.getBreaths() 高线优先
		temp = o2.getStep().getPoint().getMinLine()
				- o1.getStep().getPoint().getMinLine();
		if (temp != 0)
			return temp;

		return o2.getStep().getPoint().getMaxLine()
				- o1.getStep().getPoint().getMaxLine();
		// return 0;
	}

}
