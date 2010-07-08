package untitled4;
/*
 *快速排序的代码并不容易写,记得去予善天信息技术有限公司面试的时候
 *就没有能够做出来,尽管对算法的思想早就掌握了.所以细节的编码也是需
 *要很高的技巧和想像力的.
 */
class QuickSort {
   int len=10;
    int[] a = new int[10];
   QuickSort(int b) {
      System.out.println("b=" + b);
      a = new int[b];
      len = b;
      int i = 0;
      for (; i < b; i++) {
         a[i] = (int) (Math.random() * 10000);
         System.out.println("a=" +a[i]);
      }
   }
   public void swap(int i, int j) {
     int t = a[i];
     a[i] = a[j];
     a[j] = t;
  }

   public void quickSort(int ll,int uu){
      if(uu-ll<2)return;
      int t=a[ll];//取第一位作为比较值,中间值,或者叫划分值.

      int pt=ll;
      System.out.println("zuo=" + ll);
      System.out.println("bijoiaozhi=" + t);
      for(int i=ll+1;i<=uu;i++){
         if(a[i]<t){
            a[pt]=a[i];
            pt++;
            System.out.println("yidong=" + i);
            System.out.println("yidong=" + pt);
            for(int j=i;j>pt;j--){
               a[j]=a[j-1];
            }

         }

      }
      a[pt]=t;
      for(int k=0;k<10;k++){

            System.out.println("\t"+a[k]);
         }
         quickSort(ll,pt-1);
         quickSort(pt+1,uu);
   }

   public static void main(String args[]){
      new QuickSort(10).quickSort(0,9);
   }


}