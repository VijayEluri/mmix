package toy.applet;

import toy.HenDaoLiMa;

public class HenDaoLiMaApplet extends OldDadApplet{
	@Override
	public void init(){
		super.init();
		board = new HenDaoLiMa();
		
		board.init();
	}

}
