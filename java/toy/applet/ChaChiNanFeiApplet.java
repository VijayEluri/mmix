package toy.applet;

import toy.ChaChiNanFei;

public class ChaChiNanFeiApplet extends OldDadApplet {
	@Override
	public void init(){
		super.init();
		board = new ChaChiNanFei();
		
		board.init();
	}
}
