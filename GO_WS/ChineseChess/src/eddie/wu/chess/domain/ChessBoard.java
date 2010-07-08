package eddie.wu.chess.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * like a Mediator for Chessman
 * 
 * @author wujianfe
 * 
 */
public class ChessBoard {
	//当前轮谁走。
	ChessColor currentColor = ChessColor.RED;
	public ChessColor getCurrentColor() {
		return currentColor;
	}

	public void setCurrentColor(ChessColor currentColor) {
		this.currentColor = currentColor;
	}

	public ErrorMessage getMessage() {
		return message;
	}

	public void setMessage(ErrorMessage message) {
		this.message = message;
	}

	public Chessman[][] getMen() {
		return men;
	}

	public void setMen(Chessman[][] men) {
		this.men = men;
	}

	public List<Chessman> getChessmen() {
		return chessmen;
	}

	public void setChessmen(List<Chessman> chessmen) {
		this.chessmen = chessmen;
	}

	ErrorMessage message;
	Chessman[][] men = new Chessman[Constant.NUMBER_OF_PIECE][Constant.NUMBER_OF_PIECE];
    List<Chessman> chessmen = new ArrayList<Chessman>(32);
    
    public void initRed(){
    	// 红方：车马炮士象兵帅。
		Chariot chariot1 = new Chariot(ChessColor.RED,1,1);
		Chariot chariot9 = new Chariot(ChessColor.RED,1,9);		
		chessmen.add(chariot1);
		chessmen.add(chariot9);
		
		Horse horse2 = new Horse(ChessColor.RED,1,2);
		Horse horse8 = new Horse(ChessColor.RED,1,8);
		chessmen.add(horse2);
		chessmen.add(horse8);
		
		ElephantGuard elephant3 = new ElephantGuard(ChessColor.RED,1,3);
		ElephantGuard elephant7 = new ElephantGuard(ChessColor.RED,1,7);
		chessmen.add(elephant3);
		chessmen.add(elephant7);
		
		AdviserGuard adviser4 = new AdviserGuard(ChessColor.RED,1,4);
		AdviserGuard adviser6 = new AdviserGuard(ChessColor.RED,1,6);
		chessmen.add(adviser4);
		chessmen.add(adviser6);
		
		King king = new King(ChessColor.RED,1,5);
		chessmen.add(king);
		
		
		Cannon cannon2 = new Cannon(ChessColor.RED,3,2);
		Cannon cannon8 = new Cannon(ChessColor.RED,3,8);
		chessmen.add(cannon2);
		chessmen.add(cannon8);
		
		for(int i=0;i<5;i++){
			Pawn pawn = new Pawn(ChessColor.RED,4,1+2*i);
			chessmen.add(pawn);
		}
    }
    
    public void initBlack(){
    	// 黑方：车马炮士象兵帅。
		Chariot chariot1 = new Chariot(ChessColor.BLACK,10,1);
		Chariot chariot9 = new Chariot(ChessColor.BLACK,10,9);		
		chessmen.add(chariot1);
		chessmen.add(chariot9);
		
		Horse horse2 = new Horse(ChessColor.BLACK,10,2);
		Horse horse8 = new Horse(ChessColor.BLACK,10,8);
		chessmen.add(horse2);
		chessmen.add(horse8);
		
		ElephantGuard elephant3 = new ElephantGuard(ChessColor.BLACK,10,3);
		ElephantGuard elephant7 = new ElephantGuard(ChessColor.BLACK,10,7);
		chessmen.add(elephant3);
		chessmen.add(elephant7);
		
		AdviserGuard adviser4 = new AdviserGuard(ChessColor.BLACK,10,4);
		AdviserGuard adviser6 = new AdviserGuard(ChessColor.BLACK,10,6);
		chessmen.add(adviser4);
		chessmen.add(adviser6);
		
		King king = new King(ChessColor.BLACK,10,5);
		chessmen.add(king);
		
		
		Cannon cannon2 = new Cannon(ChessColor.BLACK,8,2);
		Cannon cannon8 = new Cannon(ChessColor.BLACK,8,8);
		chessmen.add(cannon2);
		chessmen.add(cannon8);
		
		for(int i=0;i<5;i++){
			Pawn pawn = new Pawn(ChessColor.BLACK,7,1+2*i);
			chessmen.add(pawn);
		}
    }
	public void init() {
		
		System.out.println("Board 初始化。");
		
		initRed();
		initBlack();
		for(Chessman man : chessmen){
			men[man.getStartA()][man.getStartB()]=man;
		}
		
		

	}

	/**
	 * 
	 * @param row
	 * @param column
	 * @return false and set Error Message as side effect for invalid step
	 */
	public boolean isValidStep(byte row, byte column) {
		if(men[row][column].isValidStep(this, row, column)){
			// further validation. 
			
			return true;
		}else{
			return false;
		}
		
	}

	private Chessman active;
	private boolean chooseMan(byte om, byte on) {
		if (men[om][on].color == null) {
			message = ErrorMessage.EMPTY_START;
			return false;
		} else if (currentColor.equals(men[om][on].color)) {
			message = ErrorMessage.NOT_THE_TURN;
			return false;
		} else {
			active = men[om][on];
			return true;
		}
	}

	public boolean nextStep(byte m, byte n) {
		if(active==null){
			if(men[m][n]==null){
				//this.message = 
				return false;
			}else if(men[m][n].getColor().equals(currentColor)==false){
				
				return false;
			}else{
				active = men[m][n];
			}
		}else{
			//change active
			if(men[m][n]!=null ||men[m][n].color.equals(currentColor)){
				
				return true;
			}else{//移动//吃子
				if (this.isValidStep(m, n)) {
					return _nextStep(m, n);
				} else {
					return false;
				}
			}
		}
		
		
		

	}

	private boolean _nextStep(byte m, byte n) {
		// TODO Auto-generated method stub
		return false;
	}

	public Chessman getActive() {
		return active;
	}

	
}
