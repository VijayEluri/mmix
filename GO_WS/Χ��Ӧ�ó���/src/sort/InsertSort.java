package sort;

public class InsertSort {
  int[] a = new int[1000000];
  public void test() {
    for (int i = 0; i < 1000000; i++) {
      double ro=Math.random();
      //System.out.println("ir="+ro);
      a[i] = (int)(Math.pow(2,32)*ro);
      System.out.println("="+a[i]);
    }
    for (int i = 0; i < 10; i++) {
     System.out.println("="+a[i]);
    }

    insertSort(a);
    for (int i = 0; i < 10; i++) {
    System.out.println("="+a[i]);
   }


  }

  public static void main(String args[]) {
    new InsertSort().test();
  }

  public void insertSort(int[] a) {
    int len = a.length;
    int j = 0;
    for (int i = 0; i < len; i++) {
      for (j = i; j > 0 && a[j - 1] > a[j]; j--) {
        swap(j - 1, j);

      }
    }

  }

  public void swap(int i, int j) {
    int t = a[i];
    a[i] = a[j];
    a[j] = t;
  }
}
