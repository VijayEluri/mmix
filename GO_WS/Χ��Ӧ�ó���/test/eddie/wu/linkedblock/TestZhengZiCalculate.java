package eddie.wu.linkedblock;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import eddie.wu.domain.Constant;
import eddie.wu.domain.Point;

import junit.framework.TestCase;

public class TestZhengZiCalculate extends TestCase {
	public void testConvert() {
		ZhengZiCalculate a = new ZhengZiCalculate();
		byte[][] b = new byte[10][2];
		for (int i = 0; i < b.length; i++) {
			b[i][0] =(byte)( i + 5);
			b[i][1] =(byte)(  i + 4);
		}
		Point [] c=a.convertArrayToPoints(b);
		for (int i = 0; i < b.length; i++) {
			assertEquals(c[i].getRow(),(byte)( i + 5));
			assertEquals(c[i].getColumn(),(byte)( i + 4));
		}
	}
    public void testZhengZi1(){
        String fileName="D:\\WorkSpace\\GO_WS\\short链式棋块\\doc\\征子";
        Logger logger = Logger.getLogger(GoBoard.class.getName()+"Zhengzi");
       
        logger.setLevel(Level.DEBUG);
        Point point= new Point(16,14);
        testZhengzi(fileName,point);
        
    }
    
    public void testZhengZi2(){
        String fileName="D:\\WorkSpace\\GO_WS\\short链式棋块\\doc\\征子2";
        Logger logger = Logger.getLogger(GoBoard.class.getName()+"Zhengzi");       
        logger.setLevel(Level.DEBUG);
        Point point= new Point(14,14);
        testZhengzi(fileName,point);
        
    }
    /**
     * @param fileName
     */
    private void testZhengzi(String fileName,Point point) {
        BoardColorState state=new BoardColorState();
        try {
            DataInputStream in = new DataInputStream(
                    new BufferedInputStream(new FileInputStream(fileName)));
            state=zairujumian(in);
            in.close();
        }

        catch (IOException ex) {
            System.out.println("the input meet some trouble!");
            System.out.println("Exception" + ex.toString());
        }
       System.out.println( state.getBlackPoints());
       System.out.println( state.getWhitePoints());
        ZhengZiCalculate d=new ZhengZiCalculate();
        Point[] points=d.jisuanzhengziWithClone( state
                ,point) ;//(16,13)
        for(int j=0;j<points.length;j++){
            System.out.println(points[j]);
        }
    }
    
    /**
     * 从以前制作的征子题目文件中得到ColorBoardState
     * @param jmin
     * @throws IOException
     */
    public BoardColorState zairujumian(DataInputStream jmin) throws IOException {
        BoardColorState an=new BoardColorState();
        byte a, b, color;
        byte i, j;
        while (jmin.available() != 0) {
           a = jmin.readByte();
           b = jmin.readByte();
           color = jmin.readByte();
           an.add(new BoardPoint(new Point(b,a),color));
           
           if (a < 1 | a > 19 | b < 1 | b > 19 | color < 1 | color > 2) {
              if (Constant.DEBUG_CGCL) {
                 System.out.print("载入的数据有误！" + a);
              }
              if (Constant.DEBUG_CGCL) {
                 System.out.print("i=" + a);
              }
              if (Constant.DEBUG_CGCL) {
                 System.out.print("j=" + b);
              }
              if (Constant.DEBUG_CGCL) {
                 System.out.println("color=" + color);
              }
           }

        }

        if (Constant.DEBUG_CGCL) {
           System.out.print("载入局面");

        }
        return an;
     }
}
