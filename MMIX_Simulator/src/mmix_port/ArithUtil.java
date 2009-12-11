package mmix_port;

/**
 * ([^>])>>([^>])
 * $1>>>$2
 */
import java.math.BigInteger;

import mmix.NumberUtil;
import mmix.cons.ArithmeticExceptionConstant;
import mmix.cons.FloatPointConstant;
import static mmix.cons.FloatPointConstant.*;
import static mmix.cons.ArithmeticExceptionConstant.*;

/**
 * <p>
 * rumination. in the code such as a simulator, the method (function) often need
 * to change the machine status. the following way has been considered. a. use
 * global variable b. use Parameter and side effect. the caller need to check
 * the Parameter after invocaiton. The problem for option a is static field in
 * Java are class level, which can not match OOD very well. the problem for
 * option b is that Primitive Parameter are passes by value, can not have side
 * effect. using Object Parameter would be too heavy. The work around I choose
 * is to develop object level float util, which is different the normal Util
 * Class.
 * </p>
 * 
 * it is not thread safe because it need to maintain internal status.
 * 
 * @author Eddie Wu
 * @version 1.0
 * 
 */
public class ArithUtil {
	private static long zero_octa = 0L;
	private static long negative_zero_octa = 0xcff0000000000000L;
	private static long neg_one = -1L;
	private static long inf_octa = 0x7ff0000000000000L;
	private static long sign_bit = 0x1000000000000000L;

	long standard_NaN = 0x7ff8000000000000L;
	int exceptions;
	long aux;
	int cur_round;
	boolean overflow;

	long fmult(long y, long z) {

		int yt, zt;
		int ye, ze;
		boolean ys, zs;
		long x = 0;
		long xf, yf, zf;
		int xe;
		boolean xs;
		MMIXInternalFloat myt = new MMIXInternalFloat();
		myt = myt.funpack(y);
		yf = myt.getF();
		ye = myt.getE();
		ys = myt.isNegative();
		yt = myt.getType();
		MMIXInternalFloat mzt = new MMIXInternalFloat();
		mzt = mzt.funpack(z);
		zf = mzt.getF();
		ze = mzt.getE();
		zs = mzt.isNegative();
		zt = mzt.getType();

		xs = !(ys ^ zs);
		switch (4 * yt + zt) {
		case 4 * NAN_TYPE + NAN_TYPE:
			if ((y >>> 32 & 0x80000) != 0)
				exceptions |= I_BIT_SHIFT; /* |y| is signali ng */
		case 4 * ZERO_TYPE + NAN_TYPE:
		case 4 * NUMBER_TYPE + NAN_TYPE:
		case 4 * INFINITY_TYPE + NAN_TYPE:
			if ((z >>> 32 & 0x80000) == 0) {
				exceptions |= I_BIT_SHIFT;
				z |= (0x80000L << 32);
			}
			return z;
		case 4 * NAN_TYPE + ZERO_TYPE:
		case 4 * NAN_TYPE + NUMBER_TYPE:
		case 4 * NAN_TYPE + INFINITY_TYPE:
			if (0 == (y >>> 32 & 0x80000)) {
				exceptions |= I_BIT_SHIFT;
				y = 0x80000L << 32;
			}
			return y;

		case 4 * ZERO_TYPE + ZERO_TYPE:
		case 4 * ZERO_TYPE + NUMBER_TYPE:
		case 4 * NUMBER_TYPE + ZERO_TYPE:
			x = zero_octa;
			break;
		case 4 * NUMBER_TYPE + INFINITY_TYPE:
		case 4 * INFINITY_TYPE + NUMBER_TYPE:
		case 4 * INFINITY_TYPE + INFINITY_TYPE:
			x = inf_octa;
			break;
		case 4 * ZERO_TYPE + INFINITY_TYPE:
		case 4 * INFINITY_TYPE + ZERO_TYPE:
			x = standard_NaN;
			exceptions |= I_BIT_SHIFT;
			break;
		case 4 * NUMBER_TYPE + NUMBER_TYPE: {
			xe = ye + ze - 0x3fd; /* the raw exponent */
			x = omult(yf, zf << 9);
			if (aux >>> 32 >= 0x400000) {
				xf = aux;
			} else {
				xf = aux << 1;
				xe--;
			}
			if (x != 0)
				xf |= 1; /* adjust the sticky bit */
			return fpack(xf, xe, xs, cur_round);

		}
		}
		if (xs)
			x |= sign_bit;
		return x;

	}

	/**
	 * calculate multiplication of two 64 bit integer based on 32 bit hardware.
	 * 
	 * @param y
	 * @param z
	 * @return
	 */
	public long omult(long y, long z) {
		long acc;
		int i, j, k;
		int[] u = new int[4];// y
		int[] v = new int[4];// z
		int[] w = new int[8];// result
		int t;

		u[3] = (int) (y >>> 48);
		u[2] = ((int) (y >>> 32)) & 0xffff;
		u[1] = ((int) y) >>> 16;
		u[0] = ((int) y) & 0xffff;

		v[3] = (int) (z >>> 48);
		v[2] = ((int) (z >>> 32)) & 0xffff;
		v[1] = ((int) z) >>> 16;
		v[0] = ((int) z) & 0xffff;

		for (j = 0; j < 4; j++)
			w[j] = 0;
		for (j = 0; j < 4; j++) {
			if (v[j] == 0) {
				w[j + 4] = 0;
			} else {
				for (i = k = 0; i < 4; i++) {
					t = u[i] * v[j] + w[i + j] + k;
					w[i + j] = t & 0xffff;
					k = t >>> 16;
				}
				w[j + 4] = k;
			}
		}
		// bug 102
		// aux = (((w[7] << 16) + w[6]) << 32) + (w[5] << 16) + w[4];
		aux = (NumberUtil.getUnsignedTetra((w[7] << 16) + w[6]) << 32)
				+ NumberUtil.getUnsignedTetra((w[5] << 16) + w[4]);
		acc = (NumberUtil.getUnsignedTetra((w[3] << 16) + w[2]) << 32)
				+ NumberUtil.getUnsignedTetra((w[1] << 16) + w[0]);
		// for (j = 4; j < 8; j++) {
		// System.out.println(NumberUtil.longToHex(w[j]));
		// }
		// System.out.println(NumberUtil.longToHex(NumberUtil
		// .getUnsignedTetra((w[7] << 16) + w[6])<<32));
		// System.out.println(NumberUtil.longToHex(NumberUtil
		// .getUnsignedTetra((w[5] << 16) + w[4])));
		// System.out.println(NumberUtil.longToHex(NumberUtil
		// .getUnsignedTetra((w[3] << 16) + w[2])<<32));
		// System.out.println(NumberUtil.longToHex(NumberUtil
		// .getUnsignedTetra((w[1] << 16) + w[0])));
		/*
		 * bug 101 aux = ((w[7] << 16) + w[6]) << 32 + (w[5] << 16) + w[4]; acc
		 * = ((w[3] << 16) + w[2]) << 32 + (w[1] << 16) + w[0];
		 */
		return acc;

	}

	public int getExceptions() {
		return exceptions;
	}

	public void setExceptions(int exceptions) {
		this.exceptions = exceptions;
	}

	/**
	 * the analysis of this method. three kinds of input numbers. e > 7fd i.e.
	 * >=0x7fe. since it is larger than maximum number, so it is infinity.
	 * follow the algorithm, it implement the expected result.
	 * 
	 * 0 < e <= 0x7fd. this map to the momalized number, except one extreme case
	 * which is e=0x7fd and f = power(2,55), means infinity
	 * 
	 * e < 0. which map to demomalized number and/or zero. we divide it into two
	 * cases: -54 <= e <= -1:
	 * 
	 * 
	 * e < = -55:
	 * 
	 * e-1022 = ee - 1023 (e is the raw exponent in input, ee is the biased
	 * exponent in output. i.e ee = e + 1
	 * 
	 * @param f
	 * @param e
	 * @param negative
	 * @param r
	 * @return
	 */
	public long fpack(long f, int e, boolean negative, int r) {
		long o;
		if (e > 0x7fd) {// >=0x7fe
			e = 0x7ff;
			o = zero_octa;// infinity
		} else {
			if (e < 0) {
				if (e < -54) {// e <= -55
					o = 1; // o = 1 and e = 0 get result 0 and x exception
				} else {// -54 <= e <= -1
					long oo;
					o = f >>> -e;
					oo = o << -e;
					if (oo != f) {// means there are nonzero bit in the least
						// signigicant -e bits
						o |= 1;// so we can use two bit to get extra sticky bit.
					}

				}
				/*
				 * when e = -1, f = power(2,54)+1, get result >>1 then >>2 from
				 * program. the 52thbit is 0. expected result:[1-2]power(2,1023)
				 * is a demomalized number when e = -54,
				 */
				e = 0;
			} else {// 0x7fd >= e > 0
				o = f;
				// caution: if e==7fd and f=power(2,55). then it is infinity!
			}
		}

		if ((o & 3) != 0) {
			exceptions |= ArithmeticExceptionConstant.X_BIT_SHIFT;
		}
		switch (r) {
		case FloatPointConstant.ROUND_DOWN:
			if (negative == true) {
				o += 3;
			}
			break;
		case FloatPointConstant.ROUND_UP:
			if (negative == false) {
				o += 3;
			}
		case FloatPointConstant.ROUND_OFF:
			break;
		case FloatPointConstant.ROUND_NEAR:
			if ((o & 4) != 0) {
				o += 2;
			} else {
				o += 1;
			}
			break;
		}
		o = o >>> 2;// means round off
		/**
		 * when o is less than power(2,55) after right shift 2 bits. the 52th
		 * bit is 1. when add with e, the result is the expected exponent. since
		 * expected exponent is 1 + raw exponent.
		 * 
		 * when o is power(2,55). it is 2. so the expected exponent is 1 + 1 +
		 * raw exponent. after shift, the 53th bit is 1, and 52th bit is 0. when
		 * add with e, it means e increase 2. it match the expected exponent
		 * exactly.
		 */
		o += ((long) e << 52);

		if (o >= 0x7ff0000000000000L)
			exceptions |= ArithmeticExceptionConstant.O_BIT_SHIFT
					+ ArithmeticExceptionConstant.X_BIT_SHIFT;

		else if (o < (1L << 52))
			exceptions |= ArithmeticExceptionConstant.U_BIT_SHIFT;
		if (negative == true)
			o |= sign_bit;
		return o;

	}

	/**
	 * it seem author want to save effort and reuse the design for double float.
	 * otherwise, we can design different format for short float, e.g. let
	 * power(2,25) <= f <= power(2,26)
	 * 
	 * The input float is power(2,e-1022) * (f/power(2,54)) e - 1022 = e -
	 * (1024-2)= e - 0x400 + 2 range of (f/power(2,54)) is [1,2]
	 * 
	 * ee - 127 = e - 0x400 + 2. so ee = e - 0x400 + 129 = e+1 - 0x380 or e = ee
	 * + 0x380 -1 for denomalized float, the range of ee is [1,254],
	 * corresponding e's range is [0x380,0x380+253]
	 * 
	 * @param f
	 *            the fraction part
	 * @param e
	 *            the raw exponent
	 * @param negative
	 *            whether the sign is negative
	 * @param round
	 *            the rounding mode
	 * @return
	 */
	int sfpack(long f, int e, boolean negative, int round) {

		int o = 0;
		/*
		 * if e = 0x47e, the exponent of input float value is 0x100.
		 */
		if (e > 0x47d) {// e >= 0x47e;
			e = 0x47f;// e - 1022 = 0x100; ee = 0xff
			o = 0;
		} else {// [0x380, 0x47d]
			o = (int) ((f << 3) >>> 32);
			if ((f & 0x1fffffff00000000L) != 0) {
				o |= 1;
			}
			if (e < 0x380) {// e - 1022 <= -0x7f; ee <=0
				if (e < 0x380 - 25) {// e - 1022 <= -0x98;ee<=-25
					o = 1;
				} else {// e: [0x380-25,0x380-1]; ee: [-24,0]
					int o0, oo;
					o0 = o;
					o = o >>> (0x380 - e);
					oo = o << (0x380 - e);
					if (oo != o0) {
						o |= 1; /* sticky bit */
					}

				}
				e = 0x380;// ee = 1;
			}

		}

		if ((o & 3) != 0)
			exceptions |= ArithmeticExceptionConstant.X_BIT_SHIFT;
		switch (round) {
		case FloatPointConstant.ROUND_DOWN:
			if (negative)
				o += 3;
			break;
		case FloatPointConstant.ROUND_UP:
			if (!negative)
				o += 3;
		case FloatPointConstant.ROUND_OFF:
			break;
		case FloatPointConstant.ROUND_NEAR:
			o += ((o & 4) != 0 ? 2 : 1);
			break;
		}
		o = o >>> 2;
		o += (e - 0x380) << 23;// e -0x380 == ee-1
		if (o >= 0x7f800000)
			exceptions |= ArithmeticExceptionConstant.O_BIT_SHIFT
					+ ArithmeticExceptionConstant.X_BIT_SHIFT; /* overflow */
		else if (o < 0x100000)
			exceptions |= ArithmeticExceptionConstant.U_BIT_SHIFT; /* tininess */
		if (negative)
			o |= sign_bit;
		return o;

	}

	/**
	 * @* Division. Long division of an unsigned 128-bit integer by an unsigned
	 * 64-bit integer is, of course, one of the most challenging routines needed
	 * for \MMIX\ arithmetic. The following program, based on Algorithm 4.3.1D
	 * of {\sl Seminumerical Algorithms}, computes octabytes $q$ and $r$ such
	 * that $(2^{64}x+y)=qz+r$ and $0\le r<z$, given octabytes $x$, $y$,
	 * and~$z$, assuming that $x<z$. (If $x\ge z$, it simply sets $q=x$ and
	 * $r=y$.) The quotient~$q$ is returned by the subroutine; the remainder~$r$
	 * is stored in |aux|.
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return q(quotient)
	 */
	long odiv(long x, long y, long z) {
		int i, j, k, n, d;
		int[] u = new int[8];
		int[] v = new int[4];
		int[] q = new int[4];
		int mask, qhat, rhat, vh, vmh;
		int t;
		long acc;
		// @<Check that |x<z|; otherwise give trivial answer@>;
		// @ @<Check that |x<z|; otherwise give trivial answer@>=
		BigInteger bigx = NumberUtil.getUnsignedLong(x);
		BigInteger bigz = NumberUtil.getUnsignedLong(z);
		if (bigx.compareTo(bigz) >= 0) {// case z==0 is covered here.
			aux = y;
			return x;
		}

		// @<Unpack the dividend and divisor to |u| and |v|@>;
		// @ @<Unpack the div...@>=
		// u[7]=x.h>>16, u[6]=x.h&0xffff, u[5]=x.l>>16, u[4]=x.l&0xffff;
		// u[3]=y.h>>16, u[2]=y.h&0xffff, u[1]=y.l>>16, u[0]=y.l&0xffff;
		// v[3]=z.h>>16, v[2]=z.h&0xffff, v[1]=z.l>>16, v[0]=z.l&0xffff;

		u[7] = (int) (x >>> 48);
		u[6] = ((int) (x >>> 32)) & 0xffff;
		u[5] = ((int) x) >>> 16;
		u[4] = ((int) x) & 0xffff;

		u[3] = (int) (y >>> 48);
		u[2] = ((int) (y >>> 32)) & 0xffff;
		u[1] = ((int) y) >>> 16;
		u[0] = ((int) y) & 0xffff;

		v[3] = (int) (z >>> 48);
		v[2] = ((int) (z >>> 32)) & 0xffff;
		v[1] = ((int) z) >>> 16;
		v[0] = ((int) z) & 0xffff;
		// @<Determine the number of significant places |n| in the divisor
		// |v|@>;
		// @ @<Determine the number of significant places |n| in the divisor
		// |v|@>=
		for (n = 4; v[n - 1] == 0; n--)
			;

		// @ We shift |u| and |v| left by |d| places, where |d| is chosen to
		// make $2^{15}\le v_{n-1}<2^{16}$.

		// @<Normalize the divisor@>;
		// @<Normalize the divisor@>=
		vh = v[n - 1];
		for (d = 0; vh < 0x8000; d++, vh <<= 1)
			;
		for (j = k = 0; j < n + 4; j++) {// note: x < z
			t = (u[j] << d) + k;// although the result may be an negative number
			// it works unless we output this number. i.e. we treat it as
			// positive number.
			u[j] = t & 0xffff;
			k = t >>> 16;
		}// finally k = 0, since next initialization is redundant.
		for (j = k = 0; j < n; j++) {// shift left.
			t = (v[j] << d) + k;
			v[j] = t & 0xffff;
			k = t >>> 16;
		}// finally k = 0
		vh = v[n - 1];// get the shifted value. the least significant d bits may
		// different with vh.
		vmh = (n > 1 ? v[n - 2] : 0);// look ahead one bit is enough.

		/*
		 * here the core piece!
		 */
		for (j = 3; j >= 0; j--) //
		// @<Determine the quotient digit |q[j]|@>;
		// @ @<Determine the quotient digit |q[j]|@>=
		{
			// @<Find the trial quotient, $\hat q$@>;
			// @ @<Find the trial quotient, $\hat q$@>=
			t = (u[j + n] << 16) + u[j + n - 1];// since x < z, we always need 2
			// bits of dividend
			long tt = NumberUtil.getUnsignedTetra(t);
			System.out.println(NumberUtil.longToHex(tt));
			qhat = (int) (tt / vh);
			rhat = (int) (tt - vh * qhat);
			if (n > 1) {// when n=1, no extra bit, so the result is already
				// accurate.
				while (qhat == 0x10000/*
									 * how is it possible? the high bit still
									 * can be equal
									 */
						|| NumberUtil.getUnsignedTetra(qhat * vmh) > NumberUtil
								.getUnsignedTetra((rhat << 16) + u[j + n - 2])) {
					// bug again. because the int is signed in Java.
					// qhat is
					// too
					// big
					qhat--;
					rhat += vh;
					if (rhat >= 0x10000) {
						break;
					}
				}
			}
			// @ After this step, |u[j+n]| will either equal |k| or |k-1|. The
			// true value of~|u| would be obtained by subtracting~|k| from
			// |u[j+n]|;
			// but we don't have to fuss over |u[j+n]|, because it won't be
			// examined later.

			// @<Subtract $b^j\hat q v$ from |u|@>;
			// @<Subtract $b^j\hat q v$ from |u|@>=
			for (i = k = 0; i < n; i++) {
				t = u[i + j] + 0xffff0000 - k - qhat * v[i];
				u[i + j] = t & 0xffff;
				k = 0xffff - (t >>> 16);
			}// last k is used and correponding u is not maintained.

			// @<If the result was negative, decrease $\hat q$ by 1@>;
			// @ The correction here occurs only rarely, but it can be
			// necessary---for
			// example, when dividing the number \Hex{7fff800100000000} by
			// \Hex{800080020005}.

			// @<If the result was negative, decrease $\hat q$ by 1@>=
			if (u[j + n] != k) {// after subtraction, should be zero.
				qhat--;
				for (i = k = 0; i < n; i++) {
					t = u[i + j] + v[i] + k;
					u[i + j] = t & 0xffff;
					k = t >>> 16;
				}
			}
			q[j] = qhat;
		}
		// @<Unnormalize the remainder@>;
		// @ @<Unnormalize the remainder@>=
		mask = (1 << d) - 1;
		for (j = 3; j >= n; j--) {
			u[j] = 0;
		}
		// shift d bits right
		for (k = 0; j >= 0; j--) {
			t = (k << 16) + u[j];
			u[j] = t >>> d;
			k = t & mask;
		}

		// @<Pack |q| and |u| to |acc| and |aux|@>;
		// @ @<Pack |q| and |u| to |acc| and |aux|@>=
		acc = (NumberUtil.getUnsignedTetra(((q[3] << 16) + q[2])) << 32)
				+ NumberUtil.getUnsignedTetra((q[1] << 16) + q[0]);
		aux = (NumberUtil.getUnsignedTetra(((u[3] << 16) + u[2])) << 32)
				+ NumberUtil.getUnsignedTetra((u[1] << 16) + u[0]);

		return acc;
	}

	long signed_omult(long y, long z)

	{
		long acc;
		acc = omult(y, z);
		if (y < 0) {
			aux = ominus(aux, z);
		}
		if (z < 0) {
			aux = ominus(aux, y);
		}
		if ((aux == 0 && acc >= 0) || (aux == -1 && acc < 0)) {

		} else {
			overflow = true;
		}
		// overflow=(aux.h!=aux.l || (aux.h^(aux.h>>1)^(acc.h&sign_bit)));
		return acc;
	}

	// octa ominus @,@,@[ARGS((octa,octa))@];@t}\6{@>
	long ominus(long y, long z) {/* compute $y-z$ */
		return y - z;
	}

	/**
	 * @ Signed division can be reduced to unsigned division in a tedious but
	 * straightforward manner. We assume that the divisor isn't zero.
	 */

	long signed_odiv(long y, long z)

	{
		long yy, zz, q;
		int sy, sz;
		if (y < 0) {
			sy = 2;
			yy = ominus(zero_octa, y);
		} else {
			sy = 0;
			yy = y;
		}
		if (z < 0) {
			sz = 1;
			zz = ominus(zero_octa, z);
		} else {
			sz = 0;
			zz = z;
		}
		q = odiv(zero_octa, yy, zz);
		overflow = false;
		switch (sy + sz) {
		case 2 + 1:
			aux = ominus(zero_octa, aux);
			if (q < 0) {
				overflow = true;
			}
			return q;
		case 0 + 0:
			return q;
		case 2 + 0:
			if (aux != 0) {
				aux = ominus(zz, aux);
			}
			// goto negate_q;
			if (aux != 0) {
				return ominus(neg_one, q);
			} else {
				return ominus(zero_octa, q);
			}
		case 0 + 1:
			if (aux != 0) {
				aux = ominus(aux, zz);
			}
			// negate_q:
			if (aux != 0) {
				return ominus(neg_one, q);
			} else {
				return ominus(zero_octa, q);
			}
		}
		return q;
	}

	/*
	 * @Floating addition and subtraction. Now for the bread-and-butter
	 * operation, the sum of two floating point numbers. It is not terribly
	 * difficult, but many cases need to be handled carefully.
	 * 
	 * @<Subr...@>=
	 */
	long fplus(long y, long z) {
		int yt, zt;
		int ye, ze;
		boolean ys, zs;
		long x = 0;
		long xf, yf, zf;
		int xe, d;
		boolean xs = false;
		MMIXInternalFloat myt = new MMIXInternalFloat();
		myt = myt.funpack(y);
		yf = myt.getF();
		ye = myt.getE();
		ys = myt.isNegative();
		yt = myt.getType();
		MMIXInternalFloat mzt = new MMIXInternalFloat();
		mzt = mzt.funpack(z);
		zf = mzt.getF();
		ze = mzt.getE();
		zs = mzt.isNegative();
		zt = mzt.getType();
		switch (4 * yt + zt) {
		// @t\4@>@<The usual NaN cases@>;
		case 4 * NAN_TYPE + NAN_TYPE:
			if ((y >>> 32 & 0x80000) != 0)
				exceptions |= I_BIT_SHIFT; /* |y| is signali ng */
		case 4 * ZERO_TYPE + NAN_TYPE:
		case 4 * NUMBER_TYPE + NAN_TYPE:
		case 4 * INFINITY_TYPE + NAN_TYPE:
			if ((z >>> 32 & 0x80000) == 0) {
				exceptions |= I_BIT_SHIFT;
				z |= (0x80000L << 32);
			}
			return z;
		case 4 * NAN_TYPE + ZERO_TYPE:
		case 4 * NAN_TYPE + NUMBER_TYPE:
		case 4 * NAN_TYPE + INFINITY_TYPE:
			if (0 == (y >>> 32 & 0x80000)) {
				exceptions |= I_BIT_SHIFT;
				y = 0x80000L << 32;
			}
			return y;
			
		case 4 * ZERO_TYPE + NUMBER_TYPE:
			return fpack(zf, ze, zs, ROUND_OFF);
			// break; /* may underflow */
		case 4 * NUMBER_TYPE + ZERO_TYPE:
			return fpack(yf, ye, ys, ROUND_OFF);
			// break; /* may underflow */
		case 4 * INFINITY_TYPE + INFINITY_TYPE:
			if (ys != zs) {
				// exceptions|=I_BIT;
				x = standard_NaN;
				xs = zs;
				break;
			}
		case 4 * NUMBER_TYPE + INFINITY_TYPE:
		case 4 * ZERO_TYPE + INFINITY_TYPE:
			x = inf_octa;
			xs = zs;
			break;
		case 4 * INFINITY_TYPE + NUMBER_TYPE:
		case 4 * INFINITY_TYPE + ZERO_TYPE:
			x = inf_octa;
			xs = ys;
			break;
		case 4 * NUMBER_TYPE + NUMBER_TYPE: // if (y.h!=(z.h^0x80000000) ||
											// y.l!=z.l)
			// @<Add nonzero numbers and |return|@>;
		case 4 * ZERO_TYPE + ZERO_TYPE:
			x = zero_octa;
			xs = (ys == zs ? ys : cur_round == ROUND_DOWN ? true : false);
			break;
		}
		if (xs) {
			x |= sign_bit;
		}
		return x;
	}

}
