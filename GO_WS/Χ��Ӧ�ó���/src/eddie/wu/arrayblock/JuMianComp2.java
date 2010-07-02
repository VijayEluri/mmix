package eddie.wu.arrayblock;
import java.util.*;
//用于jumian比较排序。
public class JuMianComp2 implements Comparator {
  public int compare(Object oa,Object ob){
    JuMian2 a=(JuMian2)oa;
    JuMian2 b=(JuMian2)ob;
    return a.fenshu-b.fenshu;


  }
}