package untitled4;

import org.apache.log4j.Logger;

import untitled10.QiKuai1;

/*
 *快速排序的代码并不容易写,记得去予善天信息技术有限公司面试的时候
 *就没有能够做出来,尽管对算法的思想早就掌握了.所以细节的编码也是需
 *要很高的技巧和想像力的.
 */
class QuickSort {
	private static final Logger log = Logger.getLogger(QiKuai1.class);
   int len=10;
    int[] a = new int[10];
   QuickSort(int b) {
      if(log.isDebugEnabled()) log.debug("b=" + b);
      a = new int[b];
      len = b;
      int i = 0;
      for (; i < b; i++) {
         a[i] = (int) (Math.random() * 10000);
         if(log.isDebugEnabled()) log.debug("a=" +a[i]);
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
      if(log.isDebugEnabled()) log.debug("zuo=" + ll);
      if(log.isDebugEnabled()) log.debug("bijoiaozhi=" + t);
      for(int i=ll+1;i<=uu;i++){
         if(a[i]<t){
            a[pt]=a[i];
            pt++;
            if(log.isDebugEnabled()) log.debug("yidong=" + i);
            if(log.isDebugEnabled()) log.debug("yidong=" + pt);
            for(int j=i;j>pt;j--){
               a[j]=a[j-1];
            }

         }

      }
      a[pt]=t;
      for(int k=0;k<10;k++){

            if(log.isDebugEnabled()) log.debug("\t"+a[k]);
         }
         quickSort(ll,pt-1);
         quickSort(pt+1,uu);
   }

   public static void main(String args[]){
      new QuickSort(10).quickSort(0,9);
   }


}