package untitled5;

import org.apache.log4j.Logger;

import untitled10.QiKuai1;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class DsNode { //定式节点。
	private static final Logger log = Logger.getLogger(QiKuai1.class);
   public byte xianhoushou; //-1为后手，1为先手；比较时先手大于后手；
   //有时先后手比较难说清楚，用0表示。
   public int ju;//该节点后继变化所在的文件数字，
   //如果是定式变招，前面为“dingshibianzhao”
   //也可能该步是骗着，从此可以调入具体变化，文件名前面为“pianzhao”。
   //或者该步定式告一段落，指向定式之后的变化。“dingshizhihou”
   public byte zba;//该步的横坐标
   public byte zbb;//该步的竖坐标
   public byte xingshi1;//征子成立时的形势；
   public byte xingshi2;//征子不成立时的形势；
   public byte blbz; //遍历标志（走该步的计数）
   //相对于该步颜色而言；100以上特殊对待，与征子有关；
   //征子有关不用有利与否的概念，只看征子能否成立。
   public DsNode left;
   public DsNode right;
   public boolean ZZYG=false;//征子有关。
   //并非每一项都写入文件。
   public DsNode(byte a, byte b, byte pj1, byte pj2, byte xh) {
      left = null;
      right = null;
      xingshi1 = pj1;
      xingshi2 = pj2;
      xianhoushou = xh;
      zba = a;
      zbb = b;
   }
   //定式的坐标也采用标准形式，与实际坐标匹配时确定转化方式。
   public DsNode(byte a, byte b, byte pj1, byte pj2) {
      left = null;
      right = null;
      xingshi1 = pj1;
      xingshi2 = pj2;
      if(a<1||a>19){
         if(log.isDebugEnabled()) log.debug("a<1||a>19");
      }

      if(b<1||b>19){
         if(log.isDebugEnabled()) log.debug("b<1||b>19");
      }
      zba = a;
      zbb = b;
   }

   public DsNode(byte a, byte b) {
      left = null;
      right = null;
      xingshi1 = 0;//默认为定式的两分
      xingshi2 = 0;
      if(a<1||a>19){
         if(log.isDebugEnabled()) log.debug("a<1||a>19");
      }
      if(b<1||b>19){
         if(log.isDebugEnabled()) log.debug("b<1||b>19");
      }

      zba = a;
      zbb = b;
   }

}
