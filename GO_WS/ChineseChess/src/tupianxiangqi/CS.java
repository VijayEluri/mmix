package tupianxiangqi;


public  final class CS {
  //主要是协议中的各种信号定义。
  public static final byte HONGFANG = 0;
  public static final byte HEIFANG = 1;
  public static final int DUANXIANDENGDAISHIJIAN=180;
  //断线等待180秒
  public static final byte CHUCUO = 10;
  public static final byte QQKHXX = 20;//请求客户信息
  public static final byte QIDONG = 22;//客户端启动的信号
  public static final byte JUSHOU = 24; //客户端发出开始命令。
  public static final byte QIANGTUI = 23; //客户端强行关闭程序。
  public static final byte PANGUANTUICHU = 25; // 观战者关闭程序。
  public static final byte GAIBIANJIAODIAN = 31; // 观战者关闭程序。
  public static final byte QIDONGKEHUDUAN = 26;

  //public static final byte GUIZE = 27;
  public static final byte QINGQIUGUIZE = 1;
  public static final byte DANFANGGUIZE = 2;
  public static final byte SHUANGFANGGUIZE = 3;
  public static final byte ZHONGJUTUICHU = 4;
  public static final byte DUANXIAN = 5;
  public static final byte SHIJIAN = 6;
  //通信的定义
  public static final byte GUIZE = 27;
  public static final byte QIPU = 21;
  public static final byte HUIQI = 19;
  public static final byte HUIQIBUTONGYI = 17;
  public static final byte SHUANGFANGHUIQI = 18;
  public static final byte SHENGLI=2;//接收方胜利
   public static final byte SHIBAI=3;//接收方失败
   public static final byte CHAOSHISHENGLI=12;//接收方的对手超时
   public static final byte CHAOSHISHIBAI=13;//接收方超时




  public static final byte BAOGANGSHIJIAN = 10;
  public static final byte BAOGANGBUSHU = 25;
  //命令层的代码
  public static final byte QIDONGYOUXI = 1;
  public static final byte TUICHUYOUXI= 2;
  public static final byte  PANGGUANZHE = 3;
  public static final byte XIUGAIYONGHUXINXI= 4;
  public static final byte BAOLIU = 5;
  public static final byte  YOUXIDINGYI= 6;
  public static final String IP= "127.0.0.1";

}//需要在客户端和服务器端保持一致的常数。
