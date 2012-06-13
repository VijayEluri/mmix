package eddie.wu.arrayblock;
import java.util.Comparator;
//用于jumian比较排序。
public class JuMianComp implements Comparator {
  public int compare(Object oa,Object ob){
    JuMian a=(JuMian)oa;
    JuMian b=(JuMian)ob;
    return a.fenshu-b.fenshu;


  }
}