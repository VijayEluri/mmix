package sushu;
class Untitled4 extends Untitled3{
  int pri =0;
  int b=9;
  //int c=90;
  public static void main (String args[]){
    //System.out.println("protect="+d);
    new Untitled4().test1();
     new Untitled4().test2();
  }
  public void test1(){
    System.out.println("protect="+d);
    //System.out.println("pri="+pri);
    System.out.println("c="+c);
    System.out.println("b="+b);
  }
  private void test2(){
   System.out.println("protect="+d);
   //System.out.println("pri="+pri);
   System.out.println("c="+c);
   System.out.println("b="+b);

 }

}