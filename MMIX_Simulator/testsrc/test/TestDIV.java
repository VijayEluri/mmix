package test;

import static mmix.cons.SpecialRegisterConstant.R;

import java.math.BigInteger;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import junit.framework.TestCase;

public class TestDIV extends TestCase {
	Logger log = Logger.getLogger(this.getClass());

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		log.setLevel(Level.DEBUG);
	}

	public void showtestDiv() {
		log.debug("3/2 = " + 3 / 2 + "; remainder = " + 3 % 2);
		log.debug("3/-2 = " + 3 / -2 + "; remainder = " + 3 % -2);
		log.debug("-3/2 = " + -3 / 2 + "; remainder = " + -3 % 2);
		log.debug("-3/-2 = " + -3 / -2 + "; remainder = " + -3 % -2);
	}

	// not match the definition in MMIX.
	public void testDiv_converted() {
		showtestDiv();
		log.debug("");
		divide(3, 2);
		divide(3, -2);
		divide(-3, 2);
		divide(-3, -2);
		log.debug("divide_mmix ");
		divide_mmix(3, 2);
		divide_mmix(3, -2);
		divide_mmix(-3, 2);
		divide_mmix(-3, -2);
	}

	private void divide(int a, int b) {
		BigInteger bigY = BigInteger.valueOf(a);
		BigInteger bigZ = BigInteger.valueOf(b);
		BigInteger[] result = bigY.divideAndRemainder(bigZ);
		log.debug(a + "/" + b + "; qutient = " + result[0].intValue()
				+ "; remainder = " + result[1].intValue());
	}

	private void divide_mmix(int a, int b) {
		int valueY = a;
		int valueZ = b;
		BigInteger bigY = BigInteger.valueOf(a);
		BigInteger bigZ = BigInteger.valueOf(b);
		BigInteger[] result = bigY.divideAndRemainder(bigZ);
		long [] results = new long[2];
		if((valueY>0 && valueZ>0)||(valueY<0 && valueZ<0)){
			results[1]= result[1].longValue();
			results[0]= result[0].longValue();
		}else if(valueY>0 && valueZ<0){
			results[1]= result[1].longValue()+valueZ;
			results[0]= result[0].longValue()-1;
		}else if(valueY<0 && valueZ>0){
			results[1] = result[1].longValue()+valueZ;
			results[0]= result[0].longValue()-1;
		}
		log.debug(a + "/" + b + "; qutient = " + results[0] + "; remainder = " + results[1]);
	}
}
