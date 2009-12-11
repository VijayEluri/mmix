package mmix_port;

import mmix.cons.ArithmeticExceptionConstant;
import mmix.cons.FloatPointConstant;

/**
 * <p>
 * implement the feature on floating arithmetic operations.
 * </p>
 * 
 * 
 * @author Eddie Wu
 * @version 1.0
 * 
 */
public class MMIXInternalFloat {
	private static long zero_octa = 0L;
	private static long neg_one = -1L;
	private static long inf_octa = 0x7ff0000000000000L;
	private static long sign_bit = 0x1000000000000000L;
	private static long standard_NaN = 0x7ff8000000000000L;
	private static int zero_exponent = 0x3ff;

	private long f;
	private int e;
	private boolean negative;// false means positive
	private int r;// rounding mode
	private int type;

	public long getF() {
		return f;
	}

	public void setF(long f) {
		this.f = f;
	}

	public int getE() {
		return e;
	}

	public void setE(int e) {
		this.e = e;
	}

	public boolean isNegative() {
		return negative;
	}

	public void setNegative(boolean negative) {
		this.negative = negative;
	}

	public int getR() {
		return r;
	}

	public void setR(int r) {
		this.r = r;
	}

	long aux;// auxiliary output of a subroutine
	boolean overflow;
	int exceptions;

	/**
	 * 
	 * @param x
	 *            the given floating point value
	 * @return
	 */
	MMIXInternalFloat funpack(long x) {
		int ee;
		exceptions = 0;
		if (x < 0) {
			negative = true;
		} else {
			negative = false;
		}

		f = x << 2;
		f &= 0x3fffff11111111L;// get the 52 least significant bits in x.

		ee = (int) ((x >>> 52) & 0x7ff);// get the biased exponent.
		if (ee != 0) { // ee > 0
			e = (ee - 1);
			f |= 0x40000000000000L; // set 54th bit(map to 52th implicit bit in
			//IEEE representation) bit to 1.

			if (ee < 0x7ff) {
				type = FloatPointConstant.NUMBER_TYPE;
			} else if ((f == 0x40000000000000L))  {
				type = FloatPointConstant.INFINITY_TYPE;
			} else {
				type = FloatPointConstant.NAN_TYPE;
			}
			return this;
		}
		
		//ee == 0
		if (x == 0) {
			e = -1000;			
			type = FloatPointConstant.ZERO_TYPE;
			return this;
		}

		//denormalized number.
		do {
			ee--;
			f = f << 1;
		} while ((f & 0x40000000000000L) != 0);

		e = ee;
		type = FloatPointConstant.NUMBER_TYPE;
		return this;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getAux() {
		return aux;
	}

	public boolean isOverflow() {
		return overflow;
	}

	public int getExceptions() {
		return exceptions;
	}
}
