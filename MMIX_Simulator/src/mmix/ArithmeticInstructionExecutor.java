package mmix;

import static mmix.cons.ArithmeticExceptionConstant.D_BIT_SHIFT;
import static mmix.cons.ArithmeticExceptionConstant.V_BIT_SHIFT;
import static mmix.cons.InstructionConstant.*;
import static mmix.cons.SpecialRegisterConstant.*;

import java.math.BigInteger;

import org.apache.log4j.Logger;


/**
 * for arithmetic operations it is troublesome to handle overflow, at least it
 * complicated the whole task.
 * 
 * @author Eddie Wu
 * @version 1.0
 * 
 */
public class ArithmeticInstructionExecutor extends InstructionExecutor {
	Logger log = Logger.getLogger(this.getClass());

	public static int u = 0; // memory access measure

	public static int v = 1; // oops

	long valueX;

	BigInteger bigY;// = BigInteger.valueOf(valueY);

	BigInteger bigZ;// = BigInteger.valueOf(valueZ);

	BigInteger bigX;// = bigY.multiply(bigZ);

	@Override
	public void execute() {
		this.setOperandYZ();

		switch (opCode) {
		case MUL:
		case MULI:
			multiply();
			if (bigX.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0
					|| bigX.compareTo(BigInteger.valueOf(Long.MIN_VALUE)) < 0) {
				machine.setArithmeticException(V_BIT_SHIFT);
			}
			break;
		case MULU:
		case MULUI:
			multiply();
			break;
		/**
		 * TODO:java's implementation may not match MMIX
		 */

		case DIV:
		case DIVI:
			if (valueZ == 0) {
				machine.setGeneralRegister(x, 0);
				machine.setSpecialRegister(R, valueY);
				machine.setArithmeticException(D_BIT_SHIFT);
			} else {
				divideSigned();
			}
			break;
		case DIVU:
		case DIVUI:
			if (valueZ == 0
					|| machine.getGeneralRegister(x) <= machine
							.getSpecialRegister(D)) {
				machine.setGeneralRegister(x, machine.getSpecialRegister(D));
				machine.setSpecialRegister(R, valueY);

			} else {
				long dividend = machine.getSpecialRegister(D);
				;
				// valueY
				divideUnSigned();
			}

			// divide();
			break;
		/**
		 * we just use the signed long to save the
		 */
		case ADD:
		case ADDI:
			valueX = valueY + valueZ;
			machine.setGeneralRegister(x, valueX);
			if (addOverflow(valueY, valueZ)) {
				machine.setArithmeticException(V_BIT_SHIFT);
			}
			if (log.isDebugEnabled()) {
				log.debug("valueX = " + valueX);
				log.debug("valueY = " + valueY);
				log.debug("valueZ = " + valueZ);
			}
			break;
		/**
		 * we just use the signed long to save the unsigned long data take care
		 * when interpret those data
		 */
		case ADDU:
		case ADDUI:
			machine.setGeneralRegister(x, valueY + valueZ);
			break;

		case SUB:
		case SUBI:
			valueX = valueY - valueZ;
			machine.setGeneralRegister(x, valueX);
			if (subOverflow(valueY, valueZ)) {
				machine.setArithmeticException(V_BIT_SHIFT);
			}
			break;

		case SUBU:
		case SUBUI:
			machine.setGeneralRegister(x, valueY - valueZ);
			break;

		/**
		 * only care about unsigned number (so no overflow), since it is about
		 * address. though machineState.genericRegister[x] is signed, we need to
		 * treat it as unsigned when output it.
		 */
		case _2ADDU:
		case _2ADDUI:
			// valueY = valueY << 1;
			int multi = 2;
			bigX = this.xADDU(multi);
			machine.setGeneralRegister(x, bigX.remainder(
					NumberUtil.get2Power64()).longValue());
			break;
		case _4ADDU:
		case _4ADDUI:
			// valueY = valueY << 2;
			multi = 4;
			bigX = this.xADDU(multi);
			machine.setGeneralRegister(x, bigX.remainder(
					NumberUtil.get2Power64()).longValue());
			break;
		case _8ADDU:
		case _8ADDUI:
			// valueY = valueY * 8;
			// valueY = valueY << 3;
			multi = 8;
			bigX = this.xADDU(multi);
			machine.setGeneralRegister(x, bigX.remainder(
					NumberUtil.get2Power64()).longValue());
			break;
		case _16ADDU:
		case _16ADDUI:
			// valueY = valueY * 16;
			// valueY = valueY << 4;
			multi = 16;
			bigX = this.xADDU(multi);
			machine.setGeneralRegister(x, bigX.remainder(
					NumberUtil.get2Power64()).longValue());
			break;

		case CMP:
		case CMPI:
			if (valueY > valueZ) {
				machine.setGeneralRegister(x, 1);
			} else if (valueY < valueZ) {
				machine.setGeneralRegister(x, -1);
			} else {
				machine.setGeneralRegister(x, 0);
			}
			break;
		case CMPU:
		case CMPUI:
			if (valueY < 0 && valueZ < 0) {
				if (valueY > valueZ) {
					machine.setGeneralRegister(x, -1);
				} else if (valueY < valueZ) {
					machine.setGeneralRegister(x, 1);
				} else {
					machine.setGeneralRegister(x, 0);
				}
			} else if (valueY < 0 && valueZ > 0) {
				machine.setGeneralRegister(x, 1);
			} else if (valueY > 0 && valueZ < 0) {
				machine.setGeneralRegister(x, -1);
			} else {
				if (valueY > valueZ) {
					machine.setGeneralRegister(x, 1);
				} else if (valueY < valueZ) {
					machine.setGeneralRegister(x, -1);
				} else {
					machine.setGeneralRegister(x, 0);
				}
			}
			break;
		case NEG:
		case NEGI:
			valueY = y;// a little ugly!
			machine.setGeneralRegister(x, valueY - valueZ);
			if (valueY == 0) {
				if (valueZ == 0x8000000000000000L) {
					machine.setArithmeticException(V_BIT_SHIFT);
				}
			} else if ((valueY > 0 && valueZ < 0 && valueX <= 0)
					|| (valueY < 0 && valueZ > 0 && valueX >= 0)) {
				machine.setArithmeticException(V_BIT_SHIFT);
			}
			break;
		case NEGU:
		case NEGUI:
			valueY = y;// a little ugly!
			// TODO: if(valueZ )
			machine.setGeneralRegister(x, valueY - valueZ);
			break;

		/**
		 * the diff between SL and SLU are overflow.
		 */
		case SL:
		case SLI:
			if (valueY == 0) {
				machine.setGeneralRegister(x, 0);
				break;
			}
			// valueY != 0
			if (valueZ == 0) {
				machine.setGeneralRegister(x, valueY);
			} else if (valueZ < 0 || valueZ >= 64) {
				machine.setGeneralRegister(x, 0);
				machine.setArithmeticException(V_BIT_SHIFT);
			} else {
				machine.setGeneralRegister(x, valueY << valueZ);
				BigInteger bigg = BigInteger.valueOf(valueY);
				bigg.shiftLeft((int) valueZ);
				if (bigg.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) == 1
						|| bigg.compareTo(BigInteger.valueOf(Long.MIN_VALUE)) == -1) {
					machine.setArithmeticException(V_BIT_SHIFT);
				}
			}
			break;
		case SLU:
		case SLUI:
			if (valueY == 0) {
				machine.setGeneralRegister(x, 0);
				break;
			}
			// valueY != 0
			if (valueZ == 0) {
				machine.setGeneralRegister(x, valueY);
			} else if (valueZ < 0 || valueZ >= 64) {
				machine.setGeneralRegister(x, 0);
			} else {
				machine.setGeneralRegister(x, valueY << valueZ);
			}
			break;

		case SR:
		case SRI:
			if (valueY == 0) {
				machine.setGeneralRegister(x, 0);
				break;
			}
			// valueY != 0
			if (valueZ == 0) {
				machine.setGeneralRegister(x, valueY);
			} else if (valueZ < 0 || valueZ >= 64) {
				if (valueY > 0) {
					machine.setGeneralRegister(x, 0);
				} else {
					machine.setGeneralRegister(x, -(1L));
				}
			} else {
				machine.setGeneralRegister(x, valueY >> valueZ);
			}
			break;
		case SRU:
		case SRUI:
			if (valueY == 0) {
				machine.setGeneralRegister(x, 0);
				break;
			}
			// valueY != 0
			if (valueZ == 0) {
				machine.setGeneralRegister(x, valueY);
			} else if (valueZ < 0 || valueZ >= 64) {
				machine.setGeneralRegister(x, 0);
			} else {
				machine.setGeneralRegister(x, valueY >>> valueZ);
			}
			break;
		default:
			throw new RuntimeException("MMIX: invalid OpCode (" + opCode
					+ ") for " + this.getClass().getCanonicalName());
		}

	}

	private void multiply() {
		bigY = BigInteger.valueOf(valueY);
		bigZ = BigInteger.valueOf(valueZ);
		bigX = bigY.multiply(bigZ);
		machine.setGeneralRegister(x, bigX.longValue());
		if (bigX.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) == 1
				|| bigX.compareTo(BigInteger.valueOf(Long.MIN_VALUE)) == -1) {
			machine.setArithmeticException(V_BIT_SHIFT);
		}
	}

	BigInteger[] result;

	private void divideSigned() {
		bigY = BigInteger.valueOf(valueY);
		bigZ = BigInteger.valueOf(valueZ);
		result = bigY.divideAndRemainder(bigZ);
		bigX = result[0];
		// since the JVM definition is not consistent with MMIX definition about
		// signed division.
		// we need to adjust the result.
		if ((valueY > 0 && valueZ > 0) || (valueY < 0 && valueZ < 0)) {
			machine.setSpecialRegister(R, result[1].longValue());
			machine.setGeneralRegister(x, bigX.longValue());
		} else if ((valueY > 0 && valueZ < 0) || (valueY < 0 && valueZ > 0)) {
			machine.setSpecialRegister(R, result[1].longValue() + valueZ);
			machine.setGeneralRegister(x, bigX.longValue() - 1);
		}

	}

	private void divideUnSigned() {
		bigY = NumberUtil.getUnsignedLong(valueY);
		bigZ = NumberUtil.getUnsignedLong(valueZ);
		BigInteger rD = NumberUtil.getUnsignedLong(machine.getSpecialRegister(D));
		bigY = rD.multiply(NumberUtil.get2Power64()).add(bigY);
		
		result = bigY.divideAndRemainder(bigZ);
		bigX = result[0];
		machine.setSpecialRegister(R, result[1].longValue());
		machine.setGeneralRegister(x, bigX.longValue() );
	}

	// byte[] getByteArray(long value) {
	// byte[] temp = new byte[8];
	// Long a = new Long(value);
	// a.
	// for (int i = 0; i < size; i++) {
	// temp[i] = machineState.memory[address + i];
	//
	// }
	// return temp;
	// }

	@Override
	protected void timing() {
		if (opCode >= ADD && opCode <= SRUI) {
			machine.comulativeV++;
		} else if (opCode >= MUL && opCode <= MULUI) {
			machine.comulativeV += 10;
		} else if (opCode >= DIV && opCode <= DIVUI) {
			machine.comulativeV += 60;
		}

	}

	/**
	 * @deprecated
	 */
	protected void timing2() {
		// profiling
		if (opCode >= MUL && opCode <= MULUI) {
			machine.comulativeV += 10;
		} else if (opCode >= DIV && opCode <= DIVUI) {
			machine.comulativeV += 60;
		} else if (opCode >= ADD && opCode <= _16ADDUI) {
			machine.comulativeV += 1;
		}
	}

	public static boolean addOverflow(long valueY, long valueZ) {
		long valueX = valueY + valueZ;
		return (valueY > 0 && valueZ > 0 && valueX <= 0)
				|| (valueY < 0 && valueZ < 0 && valueX >= 0);
	}

	public static boolean addOverflow(byte valueY, byte valueZ) {
		byte valueX = (byte) (valueY + valueZ);
		return (valueY > 0 && valueZ > 0 && valueX <= 0)
				|| (valueY < 0 && valueZ < 0 && valueX >= 0);
	}

	public static boolean subOverflow(long valueY, long valueZ) {
		long valueX = valueY + valueZ;
		if (valueY == 0) {
			if (valueZ == 0x8000000000000000L) {
				return true;
			} else {
				return false;
			}
		} else if ((valueY > 0 && valueZ < 0 && valueX <= 0)
				|| (valueY < 0 && valueZ > 0 && valueX >= 0)) {
			return true;
		} else {
			return false;
		}
	}

	private BigInteger xADDU(int x) {
		bigY = NumberUtil.getUnsignedLong(valueY);
		bigZ = NumberUtil.getUnsignedLong(valueZ);
		return bigY.multiply(BigInteger.valueOf(x)).add(bigZ);

	}
}
