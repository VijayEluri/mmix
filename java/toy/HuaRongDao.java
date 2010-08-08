package toy;

import static toy.Constant.BIGBOSS;

public abstract class HuaRongDao extends Board {

	@Override
	public boolean achieveGoal() {
		if (state[4][2] == BIGBOSS && state[4][3] == BIGBOSS
				&& state[5][2] == BIGBOSS && state[5][3] == BIGBOSS)
			return true;
		else
			return false;
	}

	@Override
	public void checkInternal() {
		if (blockList.size() != 9) {
			System.out.println("there are " + blockList.size() + " blocks");

		}

	}

}
