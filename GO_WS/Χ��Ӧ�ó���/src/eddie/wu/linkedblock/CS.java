package eddie.wu.linkedblock;

import eddie.wu.domain.Constant;

//含义为ConStant。
public class CS {

   public static final byte BoardSize = 19;

   public static final byte ZBSX = BoardSize + 1; //棋盘坐标上限（人造边界）;
   public static final byte ZBXX = 0; //棋盘坐标下限（人造边界）;

   public static final byte[][] szld = {
      //遍历四周（横轴和纵轴）邻子点,顺序可调.右-下-左-上
      {
      1, 0}
      , {
      0, -1}
      , {
      -1, 0}
      , {
      0, 1}
   };
   public static final byte[][] szdjd = {
      //遍历四周对角点,顺序可调.右-下-左-上
      {
      1, 1}
      , {
      1, -1}
      , {
      -1, -1}
      , {
      -1, 1}
   };
   public static final byte ZTXB = 0; //下标0存储状态值（无子、黑子、白子）;
   public static final byte BLANK = Constant.BLANK;
   public static final byte BLACK = Constant.BLACK; //1表示黑子;
   public static final byte WHITE = Constant.WHITE; //2表示白子;

   public static final byte SQBZXB = 1; //下标1存储算气标志;
   public static final byte QSXB = 2; //下标2存储气数（单子或者棋块的气数）;
   public static final byte QKSYXB = 3; //下标3存储气块索引（空白点而言）

   // 眼位相关的参数
   //1.眼的位置。
   public static final byte BIANYAN = 2;
   public static final byte JIAOYAN = 1;
   public static final byte ZHONGFUYAN = 3;
   //2.眼的性质。
   public static final byte JIAYAN = 1;
   public static final byte WEIDINGYAN = 2;
   public static final byte ZHENYAN = 3;
   //3.连接性。
  public static final byte WEILIANJIE = 1;
  public static final byte KELIANJIE = 2;
  public static final byte YILIANJIE = 3;
  public static final byte WEIDINGLIANJIE = 4;

  public static final boolean DEBUG_CGCL =true; //cgcl()方法调试
  //public static final boolean DEBUG_JISUANZHENGZI =false;
  public static final boolean DEBUG_JISUANSIHUO =false;
  public static final boolean DEBUG_KDQ =false;
  public static final boolean DEBUG_JSKQ =false;
  public static final boolean DEBUG_JISUANZHENGZI =true;
}