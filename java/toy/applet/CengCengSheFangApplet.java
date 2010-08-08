package toy.applet;

import toy.CengCengSheFang;

public class CengCengSheFangApplet extends OldDadApplet {
	@Override
	public void init(){
		super.init();
		board = new CengCengSheFang();
		
		board.init();
	}
}
