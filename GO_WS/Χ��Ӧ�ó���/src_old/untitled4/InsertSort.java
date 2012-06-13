package untitled4;

import java.util.Date;

import org.apache.log4j.Logger;

import untitled10.QiKuai1;

//import java.util.T
class InsertSort {
	private static final Logger log = Logger.getLogger(QiKuai1.class);
   int len = 10;
   int[] a = new int[10];
   InsertSort() {
      int i = 0;
      for (; i < 10; i++) {
         a[i] = (int) (Math.random() * 10000);
         //if(log.isDebugEnabled()) log.debug("a=" + a[i]);
      }
   }

   InsertSort(int b) {
      if(log.isDebugEnabled()) log.debug("b=" + b);
      a = new int[b];
      len = b;
      int i = 0;
      for (; i < b; i++) {
         a[i] = (int) (Math.random() * 10000);
      }
   }

   public void swap(int i, int j) {
      int t = a[i];
      a[i] = a[j];
      a[j] = t;
   }

   public void insertSort() {
      int i = 0;
      int j = 0;
      int len = a.length;
      Date begin = new Date();
      for (; i < len; i++) {
         for (j = i; j > 0 && a[j - 1] > a[j]; j--) {
            swap(j - 1, j);
         }
      }
      Date end = new Date();
      long diff = end.getTime() - begin.getTime();
      if(log.isDebugEnabled()) log.debug("time diff=" + (diff) + "ms");
      for (i = 0; i < 10; i++) {
         // if(log.isDebugEnabled()) log.debug("a"+i+"="+a[i]);
      }

   }

   public void insertSort1() {
      int i = 0;
      int j = 0;
      int t = 0;
      int len = a.length;
      Date begin = new Date();
      for (; i < len; i++) {
         for (j = i; j > 0 && a[j - 1] > a[j]; j--) {
            t = a[j - 1];
            a[j - 1] = a[j];
            a[j] = t;

         }
      }
      Date end = new Date();
      long diff = end.getTime() - begin.getTime();
      if(log.isDebugEnabled()) log.debug("time diff=" + (diff) + "ms");
      for (i = 0; i < 10; i++) {
         //if(log.isDebugEnabled()) log.debug("a" + i + "=" + a[i]);
      }

   }

   public void insertSort2() {
      int i = 0;
      int j = 0;
      int t = 0;
      int len = a.length;
      Date begin = new Date();
      for (; i < len; i++) {
         t = a[i];
         for (j = i; j > 0 && a[j - 1] > t; j--) {
            a[j] = a[j - 1];
            a[j - 1] = t;

         }
      }
      Date end = new Date();
      long diff = end.getTime() - begin.getTime();
      if(log.isDebugEnabled()) log.debug("time diff=" + (diff) + "ms");
      for (i = 0; i < 10; i++) {
         //if(log.isDebugEnabled()) log.debug("a" + i + "=" + a[i]);
      }

   }

   public static void main(String[] args) {
      new InsertSort(10).insertSort();
      new InsertSort(10).insertSort1();
      new InsertSort(10).insertSort2();

      new InsertSort(100).insertSort();
      new InsertSort(100).insertSort1();
      new InsertSort(100).insertSort2();

      new InsertSort(1000).insertSort();
      new InsertSort(1000).insertSort1();
      new InsertSort(1000).insertSort2();

      new InsertSort(10000).insertSort();
      new InsertSort(10000).insertSort1();
      new InsertSort(10000).insertSort2();

      new InsertSort(100000).insertSort();
     new InsertSort(100000).insertSort1();
     new InsertSort(100000).insertSort2();

   }

}
