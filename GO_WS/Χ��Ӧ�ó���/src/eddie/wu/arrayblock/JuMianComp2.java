package eddie.wu.arrayblock;
import java.util.*;
//����jumian�Ƚ�����
public class JuMianComp2 implements Comparator {
  public int compare(Object oa,Object ob){
    JuMian2 a=(JuMian2)oa;
    JuMian2 b=(JuMian2)ob;
    return a.fenshu-b.fenshu;


  }
}