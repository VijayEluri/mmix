package eddie.wu.search.global;

import java.util.Comparator;

/**
 *  做眼方成的虎口数越多越好
 * @author Eddie
 *
 */
public class MakeEyeComparator implements  Comparator<Candidate>{
	@Override
	public int compare(Candidate o1, Candidate o2) {
		if (o1.isEating() != o2.isEating()) {
			if (o1.isEating())
				return -1;
			else
				return 1;
		}
		
		if(o1.getEyes()!=o2.getEyes()){
			return o2.getEyes()-o1.getEyes();
		}
		
		if(o1.getTigerMouths()!=o2.getTigerMouths()){
			return o2.getTigerMouths()-o1.getTigerMouths();
		}

		// o1.isEating() == o2.isEating() 气长的优先
		if (o1.getBreaths() != o2.getBreaths()) {
			return o2.getBreaths() - o1.getBreaths();
		}

		
		 return 0;
	}

}
