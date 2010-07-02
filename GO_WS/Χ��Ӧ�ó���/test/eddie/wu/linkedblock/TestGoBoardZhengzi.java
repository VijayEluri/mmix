package eddie.wu.linkedblock;

import junit.framework.TestCase;

public class TestGoBoardZhengzi extends TestCase {
	public void testZhengZi(){
		 //计算死活
        byte m1, n1;
        byte[][] result;
        
        	m1=16;
        	n1=14;
        
        
        BoardColorState state=new LoadExercise().loadZhengZi();
        System.out.println("state"+state);
        GoBoard linkedBlockGoBoard = new GoBoard(state);
        System.out.println("points"+linkedBlockGoBoard.getPoints());
        System.out.println("points"+linkedBlockGoBoard.getPoints()[16	][14]);
        System.out.println("points"+linkedBlockGoBoard.getPoints()[16	][13]);
        System.out.println("points"+linkedBlockGoBoard.getPoints()[16	][15]);
        linkedBlockGoBoard.generateHighLevelState();
        System.out.println("black Block"+linkedBlockGoBoard.getBlackBlocksFromState());
        System.out.println("white Block"+linkedBlockGoBoard.getWhiteBlocksFromState());
        result = linkedBlockGoBoard.jiSuanZhengZi(m1, n1);
        if (result[126][0] == 1) {
            //goapplet.goboard.qiquan();
        }
        for (byte i = 1; i <= result[0][1]; i++) {
            //goapplet.goboard.cgcl(result[i][0], result[i][1]);
            System.out.println("：" + result[i][1]);
            System.out.println("：" + result[i][1]);
        }
                
        System.out.println("征子计算的结果为：" + result[0][0]);
		
	}
}
