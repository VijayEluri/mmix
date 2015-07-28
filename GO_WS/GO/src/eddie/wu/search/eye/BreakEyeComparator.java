package eddie.wu.search.eye;

import java.util.Comparator;

import eddie.wu.search.global.Candidate;

public class BreakEyeComparator implements  Comparator<Candidate>{
	@Override
	public int compare(Candidate o1, Candidate o2) {
		if (o1.isEating() != o2.isEating()) {
			if (o1.isEating())
				return -1;
			else
				return 1;
		}
		
		if(o1.isCapturing()!=o2.isCapturing()){
			if(o1.isCapturing()){
				return -1;
			}else{
				return 1;
			}
		}

		// o1.isEating() == o2.isEating() 气长的优先
		if (o1.getBreaths() != o2.getBreaths()) {
			return o2.getBreaths() - o1.getBreaths();
		}

		// o1.getBreaths()==o2.getBreaths() 高线优先
		return o2.getStep().getPoint().getMinLine()
				- o1.getStep().getPoint().getMinLine();
		// return 0;
	}


}
