package eddie.wu.linkedblock;

import java.awt.Button;
import java.awt.Event;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.GoBoard;
import eddie.wu.domain.Point;
import eddie.wu.search.ZhengZiCalculate;
/**
 * UI for GoBoard
 * 
 * 新的征子算法似乎仍然没有调试成功。
 * @author eddie
 *
 */
public class ZhengZiJiSuanFrame extends Frame {

    //private int id;
    //GoBoard的主要缺陷是棋块数有限制，只有128块。但是用于计算死活题足够了。
    //数组棋块的好处是易于复制数据；
    ShortLianApplet1 goapplet = new ShortLianApplet1();

    Button shuangfanglunxia = new Button("双方轮下");

    Button zhengzijisuan = new Button("征子计算");

    Button zhengzijisuan2 = new Button("征子计算for linked block");

    //Button baocunqipu = new Button("保存棋谱");
    Button renjiduixia = new Button("人机对下");

    Button baichujumian = new Button("摆出局面");

    Button baocunjumian = new Button("保存局面");

    Button zairujumian = new Button("载入局面");

    Label zuobiao = new Label("输入被征子棋块的坐标点(X,Y屏幕坐标)");

    TextField zuobiaoa = new TextField();

    TextField zuobiaob = new TextField();

    Label rowColumn = new Label("输入被征子棋块的坐标点(row,column屏幕坐标)");

//    TextField row = new TextField();
//
//    TextField column = new TextField();

    boolean SHUANGFANGLUNXIA = true;

    boolean RENJIDUIXIA;

    boolean BAICHUJUMIAN;

    public ZhengZiJiSuanFrame() throws HeadlessException {
        shuangfanglunxia.addActionListener(new LunxiaActionListener());
        renjiduixia.addActionListener(new DuixiaActionListener());
        baichujumian.addActionListener(new JumianActionListener());
        zhengzijisuan.addActionListener(new ZhengziActionListener());
        baocunjumian.addActionListener(new BaocunjumianActionListener(this));
        zairujumian.addActionListener(new ZairujumianActionListener(this));
        zhengzijisuan2.addActionListener(new ZhengziActionListener2());

        add(goapplet);
        add(shuangfanglunxia);
        add(zhengzijisuan);
        add(zhengzijisuan2);
        add(renjiduixia);
        add(baichujumian);
        add(baocunjumian);
        add(zairujumian);
        add(zuobiao);
        add(zuobiaoa);
        add(zuobiaob);
        //add
        add(rowColumn);
//        add(row);
//        add(column);

        goapplet.setVisible(true);
        shuangfanglunxia.setVisible(true);
        zhengzijisuan.setVisible(true);
        renjiduixia.setVisible(true);
        baichujumian.setVisible(true);
        zairujumian.setVisible(true);
        setLayout(null);
        goapplet.setBounds(30, 30, 560, 560);
        shuangfanglunxia.setBounds(600, 100, 100, 30);
        renjiduixia.setBounds(600, 130, 100, 30);

        baichujumian.setBounds(600, 160, 100, 30);
        zairujumian.setBounds(700, 100, 100, 30);
        zhengzijisuan.setBounds(700, 130, 100, 30);
        zhengzijisuan2.setBounds(600, 320, 200, 30);
        baocunjumian.setBounds(700, 160, 100, 30);
        zuobiao.setBounds(600, 190, 200, 30);
        zuobiaoa.setBounds(600, 220, 100, 30);
        zuobiaob.setBounds(700, 220, 100, 30);
        rowColumn.setBounds(600, 290, 200, 30);
//        row.setBounds(600, 260, 100, 30);
//        column.setBounds(700, 260, 100, 30);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent event) {
                dispose();
                System.exit(0);
            }

        });
    }

    public boolean mouseDown(Event e, int x, int y) { //接受鼠标输入
        System.out.print("chuanbodaorongqi");
        if (SHUANGFANGLUNXIA == true) {
            byte a = (byte) ((x - 4) / 28 + 1); //完成数气提子等.
            byte b = (byte) ((y - 4) / 28 + 1);
            goapplet.KEXIA = true;
        }
        if (RENJIDUIXIA == true) {
            //zuochuyingdui
        }
        if (BAICHUJUMIAN == true) {
            goapplet.goboard.qiquan();
            goapplet.KEXIA = true;
        }
        return true;
    }

    public static void main(String[] args) {
        ZhengZiJiSuanFrame weiqi = new ZhengZiJiSuanFrame();
        weiqi.setBounds(0, 0, 800, 600);
        weiqi.setVisible(true);
    }

    class LunxiaActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            //点击后重新开局。
            goapplet.goboard = new BoardLianShort();
            goapplet.KEXIA = true;

            SHUANGFANGLUNXIA = true;
            BAICHUJUMIAN = false;
            RENJIDUIXIA = false;
            System.out.println("SHUANGFANGLUNXIA=" + SHUANGFANGLUNXIA);
        }

    }

    class ZairujumianActionListener implements ActionListener { //载入局面。
        Frame parent;

        public ZairujumianActionListener(Frame par) {
            parent = par;
        }

        public void actionPerformed(ActionEvent e) {
            //载入局面
            FileDialog fd = new FileDialog(parent, "载入局面的位置", FileDialog.LOAD);
            fd.setFile("1.wjm");
            fd.setDirectory("doc/征子局面");
            fd.show();

            String inname = fd.getFile();
            String dir = fd.getDirectory();

            try {
                DataInputStream in = new DataInputStream(
                        new BufferedInputStream(new FileInputStream(dir
                                + inname)));

                goapplet.goboard.zairujumian(in);

                in.close();
            }

            catch (IOException ex) {
                System.out.println("the input meet some trouble!");
                System.out.println("Exception" + ex.toString());
            }
            goapplet.goboard.shengchengjumian();
            goapplet.goboard.output();
            goapplet.repaint();

            System.out.println("载入局面");

        }
    }

    class ZhengziActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            //计算死活
            byte m1, n1;
            byte[][] result;
           try{
            m1 = (byte) Integer.parseInt(zuobiaoa.getText());
            n1 = (byte) Integer.parseInt(zuobiaob.getText());
           }catch(Exception ee){
               return;
           }
            result = goapplet.goboard.jiSuanZhengZi(m1, n1);
            if (result[126][0] == 1) {
                goapplet.goboard.qiquan();
            }
            for (byte i = 1; i <= result[0][1]; i++) {
                goapplet.goboard.cgcl(result[i][0], result[i][1]);

            }
            goapplet.DONGHUA = true;
            goapplet.repaint();
           

            //每步10种变化计算，最多算10步(主要是内存限制)。
            //假设其中一方每次都能先计算好点。
            //做活方能做活的话，只需能找到最佳步骤即可。
            //做活方不能做活，必须验证所有点，包括弃权才能下结论
            //攻击方能杀棋，只需正解即可
            //攻击方不能成功，需要全部验证。
            System.out.println("征子计算的结果为：" + result[0][0]);
        }

    }

    /**
     * Action for linked block to calculate zhengzi.
     * @author eddie
     *
     */
    class ZhengziActionListener2 implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            //计算死活
            byte m1, n1;
            byte[][] result;
            if(zuobiaoa.getText().length()==0||
            		zuobiaob.getText().length()==0){
            	m1=16;
            	n1=14;
            }else{
	            m1 = (byte) Integer.parseInt(zuobiaoa.getText());
	            //m1= Integer.valueOf(zuobiaoa.getText()).byteValue();
	            n1 = (byte) Integer.parseInt(zuobiaob.getText());
            }
            BoardPoint point=new BoardPoint(Point.getPoint(m1,n1));
            if(!point.isValidCoordinate()){
            	return;
            	
            }
            else{
            	m1=16;
            	n1=14;
            }
            BoardColorState state=new BoardColorState(goapplet.goboard
                    .getStateArray());
            System.out.print("state"+state);
            GoBoard linkedBlockGoBoard = new GoBoard(state);
           
            System.out.println("points"+Point.getPoint(16,14));
            System.out.println("points"+Point.getPoint(16,13));
            System.out.println("points"+Point.getPoint(16,15));
            
            
            linkedBlockGoBoard.generateHighLevelState();
            System.out.println("black Block"+linkedBlockGoBoard.getBlackBlocks());
            System.out.println("white Block"+linkedBlockGoBoard.getWhiteBlocks());
            //result = linkedBlockGoBoard.jiSuanZhengZi(m1, n1);
            //result = linkedBlockGoBoard.jisuanzhengziWithClone(m1, n1);
            
            ZhengZiCalculate d=new ZhengZiCalculate();
            Point[] points=d.jisuanzhengziWithClone( state
                    ,Point.getPoint(m1,n1)) ;//(16,13)
            goapplet.goboard.qiquan();
            for(int j=0;j<points.length;j++){
            	goapplet.goboard.cgcl(point.getRow(), point.getColumn());
                System.out.println(points[j]);
            }
            goapplet.goboard.zhengzijieguo = ZhengZiCalculate.convertPointsToArray(points);
//            if (result[126][0] == 1) {
//                goapplet.goboard.qiquan();
//            }
//            for (byte i = 1; i <= result[0][1]; i++) {
//                goapplet.goboard.cgcl(result[i][0], result[i][1]);
//                System.out.println("：" + result[i][1]);
//                System.out.println("：" + result[i][1]);
//            }
            goapplet.DONGHUA = true;
            goapplet.repaint();            
            System.out.println("征子计算的结果为：" + points[0]);
        }

    }

    class DuixiaActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            goapplet.goboard = new BoardLianShort();
            goapplet.KEXIA = true;
            SHUANGFANGLUNXIA = false;
            BAICHUJUMIAN = false;
            RENJIDUIXIA = true;
            System.out.println("RENJIDUIXIA=" + RENJIDUIXIA);

        }

    }

    class JumianActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (BAICHUJUMIAN == true) {
                goapplet.goboard.qiquan();
            } else {
                goapplet.goboard = new BoardLianShort();
                goapplet.KEXIA = true;

                SHUANGFANGLUNXIA = false;
                BAICHUJUMIAN = true;
                RENJIDUIXIA = false;
            }

            System.out.println("BAICHUJUMIAN=" + BAICHUJUMIAN);
        }
    }

    class BaocunjumianActionListener implements ActionListener {
        Frame parent;

        public BaocunjumianActionListener(Frame par) {
            parent = par;
        }

        public void actionPerformed(ActionEvent e) {
            if (true) { //保存局面
                FileDialog fd = new FileDialog(parent, "保存局面的位置",
                        FileDialog.SAVE);
                fd.setFile("1.wjm");
                fd.setDirectory(".");
                fd.show();

                String outname = fd.getFile();
                String dir = fd.getDirectory();
                System.out.print(outname);
                try {
                    DataOutputStream out = new DataOutputStream(
                            new BufferedOutputStream(new FileOutputStream(dir
                                    + outname)));
                    System.out.print(out);

                    goapplet.goboard.shuchujumian(out);

                }

                catch (IOException ex) {
                    System.out.println("the output meet some trouble!");
                    System.out.println("Exception" + ex.toString());
                }

                System.out.println("保存局面");

            } else { //保存棋谱

                System.out.println("保存棋谱");

            }

        }
    }

}