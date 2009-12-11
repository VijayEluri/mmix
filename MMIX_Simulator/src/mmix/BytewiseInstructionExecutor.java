package mmix;

import java.math.BigInteger;

import org.apache.log4j.Logger;

import static mmix.cons.InstructionConstant.*;

/**
 * 
 * BytewiseInstructionExecutor. </p>
 * 
 * @author Eddie Wu
 * @version 1.0
 * 
 */
public class BytewiseInstructionExecutor extends InstructionExecutor {
	Logger log = Logger.getLogger(this.getClass());

	BigInteger bigX;

	BigInteger bigY;

	BigInteger bigZ;

	byte[] yArray;// = new byte[8];
	byte[] zArray;// = new byte[8];
	byte[] xArray;// = new byte[8];

	char[] xChar;

	char[] yChar;

	char[] ZChar;

	boolean isMOR;

	@Override
	public void execute() {

		setOperandYZ();
		byte[] temp;
		bigY = BigInteger.valueOf(valueY);
		bigZ = BigInteger.valueOf(valueZ);

		switch (opCode) {

		case BDIF:
		case BDIFI:
			initBytes();
			for (int i = 0; i < xArray.length; i++) {
				xArray[i] = (byte) Math.max(0, NumberUtil
						.getUnsignedByte(yArray[i])
						- NumberUtil.getUnsignedByte(zArray[i]));
			}
			machine.setGeneralRegister(x, new BigInteger(xArray).longValue());
			break;
		case WDIF:
		case WDIFI:
			initBytes();
			xChar = new char[4];
			for (int i = 0; i < yArray.length; i += 2) {
				xChar[i / 2] = (char) Math.max(0, NumberUtil.getUnsignedWyde(
						yArray[i], yArray[i + 1])
						- NumberUtil.getUnsignedWyde(zArray[i], zArray[i + 1]));
				if (log.isDebugEnabled()) {
					log.debug("xChar[i / 2] = 0x"
							+ Long.toHexString(xChar[i / 2]));
				}
				byte[] ttt = BigInteger.valueOf(xChar[i / 2]).toByteArray();
				System.arraycopy(ttt, 0, xArray, i + (2 - ttt.length),
						ttt.length);
			}

			machine.setGeneralRegister(x, new BigInteger(xArray).longValue());
			break;
		case TDIF:
		case TDIFI:
			initBytes();
			long ltemp;
			for (int i = 0; i < yArray.length; i += 4) {
				ltemp = Math.max(0, NumberUtil.getUnsignedTetra(yArray[i],
						yArray[i + 1], yArray[i + 2], yArray[i + 3])
						- NumberUtil.getUnsignedTetra(zArray[i], zArray[i + 1],
								yArray[i + 2], yArray[i + 3]));
				byte[] ttt = BigInteger.valueOf(ltemp).toByteArray();
				System.arraycopy(ttt, 0, xArray, (2 - ttt.length), ttt.length);
			}
			machine.setGeneralRegister(x, new BigInteger(xArray).longValue());
		case ODIF:
		case ODIFI:
			bigX = bigY.subtract(bigZ);
			if (bigX.compareTo(BigInteger.valueOf(0L)) <= 0) {
				bigX = BigInteger.valueOf(0L);
			}
			machine.setGeneralRegister(x, bigX.longValue());
			break;
		case MOR:
		case MORI:
			isMOR = true;
		case MXOR:
		case MXORI:
			isMOR = false;
			byte[][] tttt = new byte[8][8];

			// byte[] temp;
			temp = BigInteger.valueOf(valueY).toByteArray();
			byte[] yyy = new byte[8];
			System.arraycopy(temp, 0, yyy, (8 - temp.length), temp.length);
			// byte[] zzz = BigInteger.valueOf(valueZ).toByteArray(); bug 123
			temp = BigInteger.valueOf(valueZ).toByteArray();
			byte[] zzz = new byte[8];// ;
			System.arraycopy(temp, 0, zzz, (8 - temp.length), temp.length);

			byte[] xx = new byte[8];
			byte[] yy;
			byte[] zz;
			byte[][] yyyy = new byte[8][8];
			byte[][] zzzz = new byte[8][8];

			// byte tempy,tempz;

			for (int i = 0; i < 8; i++) {
				zz = NumberUtil.bitwiseByte(zzz[i]);
				for (int j = 0; j < 8; j++) {
					zzzz[i][j] = zz[j];
				}
			}
			for (int i = 0; i < 8; i++) {
				yy = NumberUtil.bitwiseByte(yyy[i]);
				for (int j = 0; j < 8; j++) {
					yyyy[i][j] = yy[j];
				}
			}
			if (isMOR == true) {
				for (int i = 0; i < 8; i++) {
					for (int j = 0; j < 8; j++) {
						tttt[i][j] = (byte) (zzzz[i][0] & yyyy[0][j]);
						for (int k = 0; k < 8; k++) {
							tttt[i][j] |= (byte) (zzzz[i][k] & yyyy[k][j]);
						}
					}
				}
			} else {
				for (int i = 0; i < 8; i++) {
					for (int j = 0; j < 8; j++) {
						tttt[i][j] = 0;// bug 9
						for (int k = 0; k < 8; k++) {
							tttt[i][j] ^= (byte) (zzzz[i][k] & yyyy[k][j]);
						}
					}
				}
			}
			for (int i = 0; i < 8; i++) {
				xx[i] = (byte) NumberUtil.reverseBitwiseByte(tttt[i]);
			}
			machine.setGeneralRegister(x, NumberUtil.getSignedOcta(xx));
			break;
		default:
			throw new RuntimeException("MMIX: invalid OpCode (" + opCode
					+ ") for " + this.getClass().getCanonicalName());
		}

	}

	private void initBytes() {
		byte[] temp;
		yArray = new byte[8];
		zArray = new byte[8];
		xArray = new byte[8];
		temp = bigY.toByteArray();
		System.arraycopy(temp, 0, yArray, (8 - temp.length), temp.length);
		temp = bigZ.toByteArray();
		System.arraycopy(temp, 0, zArray, (8 - temp.length), temp.length);
	}

	@Override
	protected void timing() {
		machine.comulativeV += 1;
	}

}
