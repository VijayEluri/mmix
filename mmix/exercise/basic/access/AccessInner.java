package basic.access;
public class AccessInner {
	public static void main(String[] args) {
		MyOuter mo = new MyOuter();
		MyOuter.MyInner inner = mo.new MyInner();
		inner.seeOuter();
	}
}
