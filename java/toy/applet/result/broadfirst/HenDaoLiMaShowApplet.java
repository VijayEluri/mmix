package toy.applet.result.broadfirst;

import toy.HenDaoLiMa;
import toy.TestHenDaoLiMa;
import toy.Util;

public class HenDaoLiMaShowApplet extends BroadFirstManualApplet {
	@Override
	public void init() {
		super.init();
		board = new HenDaoLiMa();

		board.init();
		solution = Util.getList(TestHenDaoLiMa.fileName);

	}
}
