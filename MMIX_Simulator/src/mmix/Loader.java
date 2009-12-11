package mmix;

import static mmix.cons.SpecialRegisterConstant.G;
import mmix.util.IOUtil;

import org.apache.log4j.Logger;

/**
 * <p>
 * Loader.java
 * </p>
 * 
 * 
 * @author Eddie Wu
 * @version 1.0
 * 
 */
public class Loader {
	private Logger log = Logger.getLogger(this.getClass());

	// TODO put into MMOConstant since it will be shared with mmix assembler.
	private static int ESCAPE_MAGIC = 0x98;
	/**
	 * LOP means loader opcode (Loader OPeration code)
	 */
	private static final int LOP_QUOTE = 0x0;
	private static final int LOP_LOC = 0x1;
	private static final int LOP_SKIP = 0x2;// line of code
	private static final int LOP_FIXO = 0x3;
	private static final int LOP_FIXR = 0x4;
	private static final int LOP_FIXRX = 0x5;
	private static final int LOP_FILE = 0x6;

	private static final int LOP_LINE = 0x7;
	private static final int LOP_SPEC = 0x8;
	private static final int LOP_PRE = 0x9;
	private static final int LOP_POST = 0xa;
	private static final int LOP_STAB = 0xb;
	private static final int LOP_END = 0xc;

	private static String[] LOP_CODE = new String[0xc + 1];
	static {
		LOP_CODE[0x0] = "LOP_QUOTE";
		LOP_CODE[0x1] = "LOP_LOC";
		LOP_CODE[0x2] = "LOP_SKIP";
		LOP_CODE[0x3] = "LOP_FIXO";
		LOP_CODE[0x4] = "LOP_FIXR";
		LOP_CODE[0x5] = "LOP_FIXRX";
		LOP_CODE[0x6] = "LOP_FILE";
		LOP_CODE[0x7] = "LOP_LINE";
		LOP_CODE[0x8] = "LOP_SPEC";
		LOP_CODE[0x9] = "LOP_PRE";
		LOP_CODE[0xa] = "LOP_POST";
		LOP_CODE[0xb] = "LOP_STAB";
		LOP_CODE[0xc] = "LOP_END";
	}

	private int countOfSymbol;
	private long currentLocation;
	int fileNumber = 0;
	int lineNumber = 0;

	/**
	 * just for share between method. not instance field semantically.
	 */
	int opCode;
	int x;
	int y;
	int z;
	int yz;

	int opCode2;
	int x2;
	int y2;
	int z2;

	int i;
	long virtualAddress = 0;

	/**
	 * leave the file operation into other dirty class.
	 * 
	 * @param buffer
	 */
	public void load(Machine machine, byte[] buffer) {
		if (NumberUtil.getUnsignedByte(buffer[0]) != this.ESCAPE_MAGIC) {
			throw new RuntimeException("MMIX: Invalide MMO file.");
		}

		for (i = 0; i < buffer.length;) {
			initParam(buffer);

			if (opCode == ESCAPE_MAGIC) {
				if (log.isDebugEnabled()) {
					log.debug("LOP_Code = " + LOP_CODE[x]);
				}
				switch (x) {
				case LOP_QUOTE:
					lopQuote(machine, buffer);
					break;
				case LOP_LOC:
					lopLoc(buffer);
					break;
				case LOP_SKIP:
					lopSkip();
					break;
				case LOP_FIXO:
					lopFixO(machine, buffer);
					break;
				case LOP_FIXR:
					lopFixR(machine);
					break;
				case LOP_FIXRX:
					lopFixRx(machine, buffer);
					break;
				case LOP_FILE:
					fileNumber = y;
					lineNumber = 0;
					byte[] tt = new byte[4 * z];
					for (int j = 0; j < 4 * z; j++) {
						tt[j] = buffer[i + 4 + j];
					}
					if (log.isDebugEnabled()) {
						log.debug("source file is: " + new String(tt));
					}
					i += (4 + 4 * z);
					break;
				case LOP_LINE:
					lineNumber = NumberUtil.getUnsignedWyde(y, z);
					i += 4;
					break;
				case LOP_SPEC:
					int j = lopSpec(buffer);
					break;
				case LOP_PRE:
					lopPre(buffer);
					break;
				case LOP_POST:
					j = lopPost(machine, buffer);
					break;
				case LOP_STAB:
					if (log.isDebugEnabled())
						log.debug("lop_start");
					if (NumberUtil.getUnsignedWyde(y, z) != 0) {
						throw new RuntimeException("MMIX: yz != 0 in lop_stab.");
					}

					IOUtil.showByteArrayInHex(buffer, i, buffer.length - i);
					this.countOfSymbol = (buffer.length - 4 - (4 + i)) / 4;
					i = buffer.length - 4;// break out
					if (log.isDebugEnabled())
						log.debug("lop_end");
					break;
				case LOP_END:
					if (log.isDebugEnabled())
						log.debug("lop_end");
					if (NumberUtil.getUnsignedWyde(y, z) != countOfSymbol) {
						throw new RuntimeException(
								"MMIX: yz != countOfSymbol. yz="
										+ NumberUtil.getUnsignedWyde(y, z)
										+ "; countofSymbol=" + countOfSymbol);
					}
					i += 4;
					break;
				default:
					throw new RuntimeException(
							"MMIX: invalid loader operation code");
					// break;
				}
			} else {
				if (log.isDebugEnabled()) {
					log.debug("instruction was loded");
					IOUtil.showByteArrayInHex(buffer, i, 4);
				}
				machine.setPartialTetra(currentLocation, buffer, i);
				// IOUtil.showByteArrayInHex(machine.memory(), machine
				// .memoryMap(currentLocation), 4);
				this.currentLocation += 4;
				i += 4;
			}
			// if()
		}
	}

	private int lopPost(Machine machine, byte[] buffer) {
		int j;
		machine.setSpecialRegister(G,z);
		byte[] ttt = new byte[8];
		for (j = 0; j < 256 - z; j++) {
			for (int k = 0; k < 8; k++) {
				ttt[k] = buffer[i + 4 + j * 8 + k];
			}
			machine.setGlobalRegister(z + j, NumberUtil.getSignedOcta(ttt));
			// machine.setOcta(loc, value);
		}
		i += (4 + 4 * (2 * (256 - z)));
		if (log.isDebugEnabled())
			log.debug("getGlobalRegister(254)"
					+ Long.toHexString(machine.getGlobalRegister(254)));
		if (log.isDebugEnabled())
			log.debug("getGlobalRegister(255)"
					+ Long.toHexString(machine.getGlobalRegister(255)));
		return j;
	}

	private void lopPre(byte[] buffer) {
		if (log.isDebugEnabled()) {
			log.debug("MMIX version: " + y);
			if (z > 0) {
				opCode2 = NumberUtil.getUnsignedByte(buffer[i + 4]);
				x2 = NumberUtil.getUnsignedByte(buffer[i + 4 + 1]);
				y2 = NumberUtil.getUnsignedByte(buffer[i + 4 + 2]);
				z2 = NumberUtil.getUnsignedByte(buffer[i + 4 + 3]);
				log.debug("MMO is generated in: "
						+ NumberUtil.getUnsignedTetra(opCode2, x2, y2, z2)
						+ "seconds since 1970");
			}
		}
		i += (4 + 4 * z);
	}

	private int lopSpec(byte[] buffer) {
		log.debug("LOP_SPEC strat");
		int j = 0;
		while (true) {
			opCode2 = NumberUtil.getUnsignedByte(buffer[i + 4 + j]);
			x2 = NumberUtil.getUnsignedByte(buffer[i + 4 + j + 1]);

			if (opCode2 != ESCAPE_MAGIC) {
				IOUtil.showByteArrayInHex(buffer, i + 4 + j, 4);
				j += 4;
				// countOfSymbol++;
			} else if (x2 == LOP_QUOTE) {
				j += 4;
				IOUtil.showByteArrayInHex(buffer, i + 4 + j + 4, 4);
				// countOfSymbol++;
				j += 4;
			} else {
				break;
			}
		}
		i += (4 + j);
		log.debug("LOP_SPEC end: j= " + j);
		return j;
	}

	private void lopFixRx(Machine machine, byte[] buffer) {
		opCode2 = NumberUtil.getUnsignedByte(buffer[i + 4]);
		x2 = NumberUtil.getUnsignedByte(buffer[i + 4 + 1]);
		y2 = NumberUtil.getUnsignedByte(buffer[i + 4 + 2]);
		z2 = NumberUtil.getUnsignedByte(buffer[i + 4 + 3]);
		if (log.isDebugEnabled()) {

			log.debug("opCode2 = " + opCode2 + " (0x"
					+ Integer.toHexString(opCode2) + ")");
			log.debug("x2 = " + x2 + " (0x" + NumberUtil.byteToHex(x2) + ")");
			log.debug("y2 = " + y2 + " (0x" + NumberUtil.byteToHex(y2) + ")");
			log.debug("z2 = " + z2 + " (0x" + NumberUtil.byteToHex(z2) + ")");
			IOUtil.showByteArrayInHex(buffer, i + 4, 4);
		}
		int temp = NumberUtil.getSignedTetra(buffer, i + 4);
		if (log.isDebugEnabled())
			log.debug("temp=" + Integer.toHexString(temp));
		virtualAddress = 0;
		if (buffer[i + 4] == 0) {
			virtualAddress = currentLocation - 4 * temp;
		} else if (buffer[i + 4] == 1) {
			if (z == 16) {
				// bug 102
				// virtualAddress = currentLocation - 4
				// * ((temp & 0xffff) - (2 << 16));
				virtualAddress = currentLocation - 4
						* ((temp & 0xffff) - (1 << 16));
			} else if (z == 24) {
				virtualAddress = currentLocation - 4
						* ((temp & 0xffffff) - (1 << 24));
			} else {
				throw new RuntimeException("invalid loader operation code");
			}
		} else {
			throw new RuntimeException("invalid loader operation code");
		}

		// int value = NumberUtil.getSignedTetra(machine.memory(),
		// machine.memoryMap(virtualAddress));
		int value = machine.getSignedTetra(virtualAddress);
		machine.setTetra(virtualAddress, value ^ temp);

		if (log.isDebugEnabled()) {
			log.debug("value = 0x" + Long.toHexString(value)
					+ " was set to address= 0x "
					+ Long.toHexString(virtualAddress));
		}

		i += 8;
	}

	private void lopFixR(Machine machine) {
		yz = NumberUtil.getUnsignedWyde(y, z);
		machine.setWyde(currentLocation - 4 * yz + 2, yz);
		if (log.isDebugEnabled()) {
			log.debug("value = 0x" + Long.toHexString(yz)
					+ " was set to address: 0x"
					+ Long.valueOf(currentLocation - 4 * yz + 2));
		}
		i += 4;
	}

	private void lopFixO(Machine machine, byte[] buffer) {
		if (z == 1) {
			virtualAddress =  ((long)y << 56)
					+ NumberUtil.getSignedTetra(buffer, i + 4);
		} else if (z == 2) {
			virtualAddress =  ((long)y << 56)
					+ NumberUtil.getSignedOcta(buffer, i + 4);
		}
		machine.setOcta(virtualAddress, currentLocation);
		if (log.isDebugEnabled()) {

			log.debug("y <<56 = 0x" + Long.toHexString((long)y << 56));
			log.debug("NumberUtil.getSignedTetra(buffer, i + 4) = 0x"
					+ Long
							.toHexString(NumberUtil.getSignedTetra(buffer,
									i + 4)));
			log.debug("value = 0x" + Long.toHexString(currentLocation)
					+ " was set to address: 0x"
					+ Long.toHexString(virtualAddress));
		}
		i += (4 + 4 * z);
	}

	private void lopSkip() {
		if (log.isDebugEnabled()) {
			log.debug("currentLocation = 0x"
					+ Long.toHexString(currentLocation));
		}
		currentLocation += NumberUtil.getUnsignedWyde(y, z);
		if (log.isDebugEnabled()) {
			log.debug(" and it's changes to= 0x"
					+ Long.toHexString(currentLocation));
		}
		i += 4;
	}

	private void lopLoc(byte[] buffer) {
		if (log.isDebugEnabled())
			log.debug("currentLocation = 0x"
					+ Long.toHexString(currentLocation));
		if (z == 1) {
			currentLocation = (((long) y) << 56)
					+ NumberUtil.getSignedTetra(buffer, i + 4);
		} else if (z == 2) {
			currentLocation = (((long) y) << 56)
					+ NumberUtil.getSignedOcta(buffer, i + 4);
		}
		// currentLocation =
		// NumberUtil.alignTetraLeft(currentLocation);

		// if(log.isDebugEnabled()) log.debug("currentLocation = 0x"
		// + Long.toHexString(currentLocation));
		// TODO set the at value according to symbol table.
		// machine.at = machine.memoryMap(currentLocation);
		if (log.isDebugEnabled()) {
			log.debug("currentLocation is set to = 0x"
					+ Long.toHexString(currentLocation));
		}
		i += (4 + 4 * z);// bug i += (4 + z);
	}

	private void lopQuote(Machine machine, byte[] buffer) {
		if (log.isDebugEnabled()) {
			opCode2 = NumberUtil.getUnsignedByte(buffer[i + 4]);
			x2 = NumberUtil.getUnsignedByte(buffer[i + 4 + 1]);
			y2 = NumberUtil.getUnsignedByte(buffer[i + 4 + 2]);
			z2 = NumberUtil.getUnsignedByte(buffer[i + 4 + 3]);
			log.debug("opCode2 = " + opCode2 + " (0x"
					+ Integer.toHexString(opCode2) + ")");
			log.debug("x2 = " + x2 + " (0x" + NumberUtil.byteToHex(x2) + ")");
			log.debug("y2 = " + y2 + " (0x" + NumberUtil.byteToHex(y2) + ")");
			log.debug("z2 = " + z2 + " (0x" + NumberUtil.byteToHex(z2) + ")");
			IOUtil.showByteArrayInHex(buffer, i + 4, 4);
		}
		machine.setTetra(currentLocation, buffer, i + 4);
		this.currentLocation += 4;
		i += 8;
	}

	private void initParam(byte[] buffer) {
		opCode = NumberUtil.getUnsignedByte(buffer[i]);
		x = NumberUtil.getUnsignedByte(buffer[i + 1]);
		y = NumberUtil.getUnsignedByte(buffer[i + 2]);
		z = NumberUtil.getUnsignedByte(buffer[i + 3]);
		if (log.isDebugEnabled()) {
			log.debug("");
			log.debug("input bytes i = " + i + "; input tetras = "
					+ (i / 4 + 1));
			log.debug("currentLocation = 0x"
					+ Long.toHexString(currentLocation));
			log.debug("opCode = " + opCode + " (0x"
					+ Integer.toHexString(opCode) + ")");
			log.debug("x = " + x + " (0x" + NumberUtil.byteToHex(x) + "); "
					+ "y = " + y + " (0x" + NumberUtil.byteToHex(y) + "); "
					+ "z = " + z + " (0x" + NumberUtil.byteToHex(z) + ")");
		}
	}
}
