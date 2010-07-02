package sushu;

public class Untitled1 {
  Untitled1(){
    super();
  }

  //在数字中剔除2.3.5因子的和数后，在没有2.3.5因子的
  //数中间有没有连续可分解的孪生数.是否有规律.
  public static void main(String[] args){
    int[] base=new int[12];

    base[0]=29;
    base[1]=31;

    base[2]=41;
    base[3]=43;

    base[4]=47;
    base[5]=49;

    base[6]=59;
    base[7]=61;

    base[8]=71;
    base[9]=73;

    base[10]=77;
    base[11]=79;

    int temp1=0;
    int temp2=0;
    int i=0,j=0;
    int k=0;
    int[] fenjie1=new int[10];
    int[] fenjie2=new int[10];
    for( i=0;i<100000;i++){//
      //System.out.println("i="+i);
      for( j=0;j<12;j+=2){
        //System.out.println("j="+j);
        temp1 =i*60+base[j];
        //System.out.println("temp1="+temp1);
        fenjie1=isHeshu(temp1);
        //System.out.println("fenjie1[0]="+fenjie1[0]);
        if(fenjie1[0]==5){
          temp2 =i*60+base[j+1];
          //System.out.println("temp2="+temp2);
          fenjie2=isHeshu(temp2);
          //System.out.println("fenjie2[0]="+fenjie2[0]);
          if(fenjie2[0]==3){
            System.out.println("j="+j);
            System.out.print("temp1="+temp1+"=");
            for(k=1;fenjie1[k]!=0&&k<=fenjie1[0];k++ ){
              System.out.print(fenjie1[k]+"*");
            }
              System.out.println("");
            System.out.print("temp2="+temp2+"=");
            for(k=1;fenjie2[k]!=0&&k<=fenjie2[0];k++ ){
              System.out.print(fenjie2[k]+"*");
            }
            System.out.println("");
          }
        }else{

        }

      }
    }

  }


  public static int[] isHeshu(int num){
    int [] fa=new int[100];
    int jie=0;
    //int numori=num;
   // System.out.println("num="+num);

    for (int i=7;i*i<=num;i+=2){
      if(num%i==0){
        num=num/i;
        jie++;
        //System.out.println("fa["+jie+"]="+i);
        fa[jie]=i;
        while(num%i==0){//
          num=num/i;
          jie++;
          fa[jie]=i;
          //System.out.println("fa["+jie+"]="+i);
        }
      }
    }
    if(num!=1){
      jie++;
      fa[jie] = num;
      //System.out.println("fa[" + jie + "]=" + num);
    }
    fa[0]=jie;
    return fa;
  }
}