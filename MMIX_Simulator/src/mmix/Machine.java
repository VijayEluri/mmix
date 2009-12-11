package mmix;

import static mmix.cons.InstructionConstant.GET;
import static mmix.cons.InstructionConstant.GO;
import static mmix.cons.InstructionConstant.GOI;
import static mmix.cons.InstructionConstant.JMP;
import static mmix.cons.InstructionConstant.JMPB;
import static mmix.cons.InstructionConstant.LDHT;
import static mmix.cons.InstructionConstant.LDHTI;
import static mmix.cons.InstructionConstant.LDSF;
import static mmix.cons.InstructionConstant.LDSFI;
import static mmix.cons.InstructionConstant.POP;
import static mmix.cons.InstructionConstant.PUSHGO;
import static mmix.cons.InstructionConstant.PUSHGOI;
import static mmix.cons.InstructionConstant.PUSHJ;
import static mmix.cons.InstructionConstant.PUSHJB;
import static mmix.cons.InstructionConstant.RESUME;
import static mmix.cons.InstructionConstant.SAVE;
import static mmix.cons.InstructionConstant.STB;
import static mmix.cons.InstructionConstant.STSF;
import static mmix.cons.InstructionConstant.STSFI;
import static mmix.cons.InstructionConstant.STUNCI;
import static mmix.cons.InstructionConstant.SWYM;
import static mmix.cons.InstructionConstant.SYNC;
import static mmix.cons.InstructionConstant.TRAP;
import static mmix.cons.InstructionConstant.TRIP;
import static mmix.cons.InstructionConstant.UNSAVE;
import static mmix.cons.FloatPointConstant.*;
import static mmix.cons.SpecialRegisterConstant.A;
import static mmix.cons.SpecialRegisterConstant.B;
import static mmix.cons.SpecialRegisterConstant.C;
import static mmix.cons.SpecialRegisterConstant.D;
import static mmix.cons.SpecialRegisterConstant.E;
import static mmix.cons.SpecialRegisterConstant.G;
import static mmix.cons.SpecialRegisterConstant.H;
import static mmix.cons.SpecialRegisterConstant.J;
import static mmix.cons.SpecialRegisterConstant.L;
import static mmix.cons.SpecialRegisterConstant.M;
import static mmix.cons.SpecialRegisterConstant.O;
import static mmix.cons.SpecialRegisterConstant.P;
import static mmix.cons.SpecialRegisterConstant.Q;
import static mmix.cons.SpecialRegisterConstant.R;
import static mmix.cons.SpecialRegisterConstant.S;
import static mmix.cons.SpecialRegisterConstant.W;
import static mmix.cons.SpecialRegisterConstant.X;
import static mmix.cons.SpecialRegisterConstant.Y;
import static mmix.cons.SpecialRegisterConstant.Z;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import mmix.cons.InstructionConstant;
import mmix.cons.SpecialRegisterConstant;

import org.apache.log4j.Logger;

/**
 * keep track of the state of the machine
 * 
 * @author wueddie-wym-wrz
 * 
 */
public class Machine {
	public static final long DATA_SEGMENT = 0x2000000000000000L;

	public static final long POOL_SEGMENT = 0x4000000000000000L;// heap?

	public static final long STACK_SEGMENT = 0x6000000000000000L;
	private static final byte[] zero = new byte[8];
	private static final byte[] minusOne = new byte[8];
	static {
		for (int i = 0; i < 8; i++) {
			minusOne[i] = (byte) -1;
		}
	}
	Logger log = Logger.getLogger(getClass());

	private boolean shouldHalt;
	long countBranch = 0;
	long countCorrectPrediction = 0;
	long[] countInstructionType = new long[256];
	long[] countSpecificInstruction;
	HashMap<Long, Long> specificInstruction = new LinkedHashMap<Long, Long>();
	/**
	 * although long is signed in Java, we still can use it to store the 64 bit.
	 * the only problem then is we need a way to treat the bit pattern as a
	 * unsigned number.? e.g. output this number.
	 */
	// first 32 are not used as global register because rG >=32, it is used as
	// special register instead.
	private long[] globalRegister = new long[256];

	private static int NUMBER_OF_LOCAL_REGISTER = 512;

	// could have 256 or 512 or 1024 local register.
	private long[] localRegister = new long[NUMBER_OF_LOCAL_REGISTER];

	// long[] specialRegister = new long[32];

	public long getSpecialRegister(int index) {
		if (index < 0 || index >= 32) {
			throw new RuntimeException(
					"MMIX: special register index is not in [0,32)");
		}
		return globalRegister[index];
	}

	public void setSpecialRegister(int index, long value) {
		if (index < 0 || index >= 32) {
			throw new RuntimeException(
					"MMIX: special register index is not in [0,32)");
		}
		if (log.isInfoEnabled()) {
			log.info("special register["
					+ SpecialRegisterConstant.mapToString(index)
					+ "] is changed from 0x"
					+ Long.toHexString(globalRegister[index]) + " to 0x"
					+ Long.toHexString(value));
		}
		this.globalRegister[index] = value;
	}

	public void increaseSpecialRegister(int index, long value) {
		if (index < 0 || index >= 32) {
			throw new RuntimeException(
					"MMIX: special register index is not in [0,32)");
		}
		if (log.isInfoEnabled()) {
			log.info("special register["
					+ SpecialRegisterConstant.mapToString(index)
					+ "] is changed from 0x"
					+ Long.toHexString(globalRegister[index]) + " to 0x"
					+ Long.toHexString(globalRegister[index] + value));
		}
		this.globalRegister[index] += value;
	}

	/**
	 * encapsulate it because of register stack see page 58
	 * 
	 * @param relativeIndex
	 * @return
	 */
	public long getGeneralRegister(int relativeIndex) {
		int alpha = getAlpha();
		long ll = getSpecialRegister(L);
		long gg = getSpecialRegister(G);
		if (relativeIndex < ll) {
			return localRegister[(alpha + relativeIndex)
					% NUMBER_OF_LOCAL_REGISTER];
		} else if (relativeIndex >= ll && (relativeIndex < gg)) {
			return 0;
		} else {// if (relativeIndex >= gg)
			// bug 1123 return localRegister[relativeIndex];
			return globalRegister[relativeIndex];// in this case, equal to
			// absolute index.
			// bug111, also should print stack when got exception, message is
			// confusing.
		}

	}

	/**
	 * 
	 * @param registerStackIndex
	 * @return
	 */
	public long getGeneralRegister(long registerStackIndex) {
		int index = (int) (registerStackIndex % NUMBER_OF_LOCAL_REGISTER);
		return getGeneralRegister(index);

	}

	// used in machine setup.
	public void setGlobalRegister(int absoluteIndex, long value) {
		if (log.isInfoEnabled()) {
			log.info("globalRegister[" + absoluteIndex + "] is changed from 0x"
					+ Long.toHexString(globalRegister[absoluteIndex])
					+ " to 0x" + Long.toHexString(value));
		}
		this.globalRegister[absoluteIndex] = value;
	}

	// used in debug
	public long getGlobalRegister(int absoluteIndex) {
		return globalRegister[absoluteIndex];
	}

	/**
	 * see page 58
	 * 
	 * @param relativeIndex
	 * @param value
	 */
	public void setGeneralRegister(int relativeIndex, long value) {
		long valueO = getSpecialRegister(O);
		long valueS = getSpecialRegister(S);
		int alpha = getAlpha();
		int garma = getGarma();
		int ss = memoryMap(getSpecialRegister(S));
		int ll = (int) getSpecialRegister(L);
		int gg = (int) getSpecialRegister(G);
		int marginBegin = (int) (alpha + ll);
		// break tie
		// if(alpha == garma && ){
		// localRegister[(alpha + relativeIndex) % NUMBER_OF_LOCAL_REGISTER] =
		// value;
		// this.setSpecialRegister(L,relativeIndex + 1);
		// }else
		if (relativeIndex >= gg) {
			setGlobalRegister(relativeIndex, value);

		} else if (relativeIndex >= ll) {
			for (int i = 0; i <= relativeIndex - ll; i++) {
				// prepare a register by saving it.
				garma = getGarma();
				if ((marginBegin + i) % NUMBER_OF_LOCAL_REGISTER == garma
						&& (valueS + NUMBER_OF_LOCAL_REGISTER == valueO + ll
								+ i)) {
					// stackSave();
					setOcta(getSpecialRegister(S), localRegister[garma]);
					increaseSpecialRegister(S, 8);
				}
				setLocalRegister((marginBegin + i) % NUMBER_OF_LOCAL_REGISTER,
						0);
			}
			// overwrite the value. stack are prepared.
			setLocalRegister(
					(alpha + relativeIndex) % NUMBER_OF_LOCAL_REGISTER, value);
			this.setSpecialRegister(L, relativeIndex + 1);
		} else {
			setLocalRegister(
					(alpha + relativeIndex) % NUMBER_OF_LOCAL_REGISTER, value);

		}
		if (log.isDebugEnabled()) {
			log.debug("machine.getSpecialRegister(L);= "
					+ getSpecialRegister(L));
		}
	}

	private void setLocalRegister(int index, long value) {
		if (log.isInfoEnabled()) {
			log.info("localRegister[" + index + "] is changed from 0x"
					+ Long.toHexString(localRegister[(index)]) + " to 0x"
					+ Long.toHexString(value));
		}
		localRegister[index] = value;
	}

	long comulativeU = 0;// memory access

	long comulativeV = 0; // oops

	/**
	 * SHOULD be virtue address!
	 * 
	 */
	long virtualAt = 0x100;

	Machine setUp() {
		setUpInstructionExecutor();

		this.setSpecialRegister(G, 32);
		// use physical address instead of virtual address for simplicity.
		// change to use virtual address
		this.setSpecialRegister(O, 0x6000000000000000L);
		this.setSpecialRegister(S, 0x6000000000000000L);
		if (log.isDebugEnabled()) {
			log.debug("Page size: " + PAGE_SIZE);
			log.debug("How many pages: " + this.addressLimit / PAGE_SIZE);
		}
		return this;
	}

	// convert to unsigned number when fetch them from byte[]
	int opCode;

	int x;

	int y;

	int z;

	long count = 0;

	void execute() {
		// count = 1;
		long atCopy;// = at;
		boolean resume_flag = false;
		long resume_inst = 0;

		int at;
		try {
			// at = 0;
			// bug 6:int atCopy = at;
			do {
				count += 1;
				atCopy = virtualAt;
				at = memoryMap(virtualAt);
				opCode = NumberUtil.getUnsignedByte(memory[at]);
				x = NumberUtil.getUnsignedByte(memory[at + 1]);
				y = NumberUtil.getUnsignedByte(memory[at + 2]);
				z = NumberUtil.getUnsignedByte(memory[at + 3]);
				if (log.isInfoEnabled()) {
					log.info("");
					log.info("Instruction = " + count);
					log.info("virtualAt = 0x" + Long.toHexString(virtualAt)
							+ "; at = 0x" + Integer.toHexString(at));
					log.info("opCode = " + opCode + " (0x"
							+ Integer.toHexString(opCode) + ")"
							+ "; mnemonic: "
							+ mmix.cons.InstructionConstant.mapToString(opCode));
					log.info("x = " + x + " (0x" + NumberUtil.byteToHex(x)
							+ "); " + "y = " + y + " (0x"
							+ NumberUtil.byteToHex(y) + "); " + "z = " + z
							+ " (0x" + NumberUtil.byteToHex(z) + ")");
					log
							.info("executors[" + opCode + "] = "
									+ executors[opCode]);
					showInternalStatus(log);
				}

				executors[opCode].setInstruction(opCode, x, y, z);
				this.countInstructionType[opCode]++;
				if (this.specificInstruction.get(virtualAt) != null) {
					this.specificInstruction.put(virtualAt,
							this.specificInstruction.get(virtualAt) + 1);
				} else {
					this.specificInstruction.put(virtualAt, 1L);
				}
				executors[opCode].execute();
				if (log.isDebugEnabled()) {
					showInternalStatus(log);
				}
				executors[opCode].timing();
				checkException();
				if (resume_flag == true) {
					// virtualAT should be same
					if (virtualAt != atCopy) {
						log.debug("virtualAT should be same, virtualAt = 0x"
								+ virtualAt + "; atCopy = 0x" + atCopy);
					}
				} else if (virtualAt == atCopy) {// at not changed in the
					// execution
					// of
					// instruction.
					virtualAt += 4;
				}

			} while (this.shouldHalt == false);

			this.showStatistics();

		} catch (RuntimeException e) {
			// if (log.isInfoEnabled()) {
			// log.info(e.getMessage());
			// if (e.getMessage() == null
			// || e.getMessage().indexOf("MMIX") == -1) {
			//					
			// }
			// }
			e.printStackTrace();
		}
	}

	private void showStatistics() {
		this.setSpecialRegister(C, comulativeU << 32 + comulativeV);
		if (log.isInfoEnabled()) {
			log.info("memory accesss count = " + comulativeU);
			log.info("instruction count = " + comulativeV);
			log.info("count of Branchs = " + countBranch);
			log
					.info("count of correct Predictions = "
							+ countCorrectPrediction);
			// mmix ware compatiable
			log.info(count + " instructions, " + comulativeU + " mems, "
					+ comulativeV + " oops; " + (countCorrectPrediction)
					+ " good guesses, "
					+ (countBranch - countCorrectPrediction) + " bad");
			if (countBranch != 0) {
				log.debug("count of correct Predict/count of Branchs = "
						+ countCorrectPrediction / countBranch);
			}

			for (int i = 0; i < 256; i++) {
				if (countInstructionType[i] != 0)
					log.info(InstructionConstant.mapToString(i) + " "
							+ this.countInstructionType[i] + " times;");
			}
			// Iterator iter = this.specificInstruction.entrySet().iterator();
			// while(iter.hasNext()){
			// Entry<Long, Long> entry = iter.next();
			// }
			for (Entry<Long, Long> entry : specificInstruction.entrySet()) {
				log.info("virtual addr = 0x"
						+ NumberUtil.longToHex(entry.getKey())
						+ " : "
						+ NumberUtil.intToHex((int) getUnsignedTetra(entry
								.getKey())) + "; " + entry.getValue()
						+ " times;");
			}
		}

	}

	private void checkException() {
		// TODO Auto-generated method stub
		// log.debug(Long.toHexString(getGeneralRegister(0)));
	}

	public boolean isRoundDown() {
		int roundMode = getRoundMode();
		return roundMode == ROUND_DOWN;
	}
	
	public boolean isRoundUp() {
		int roundMode = getRoundMode();
		return roundMode == ROUND_UP;
	}
	
	public boolean isRoundOff() {
		int roundMode = getRoundMode();
		return roundMode == ROUND_OFF;
	}
	public boolean isRoundNear() {
		int roundMode = getRoundMode();
		return roundMode == ROUND_NEAR;
	}

	public int getRoundMode() {
		return (int) (getSpecialRegister(A) >> 16);
	}

	/**
	 * arithmetic exception check
	 * 
	 * @param exception
	 */
	public void setArithmeticException(int exception) {
		if ((getSpecialRegister(A) & (0x1 << (exception + 8))) == (0x1 << (exception + 8))) {
			// trip to #10 ...#80
			virtualAt = (exception + 1) * 0x10;
			// TODO: be careful, O and X may happen at the same time.
		} else {
			// getSpecialRegister(A) &= (0x1 << exception);//BUG1
			this.setSpecialRegister(A, getSpecialRegister(A)
					| (0x1 << exception));
			if (log.isInfoEnabled()) {

			}
		}
		if (log.isDebugEnabled()) {
			log.debug("this.getSpecialRegister(A) = 0x"
					+ Long.toBinaryString(getSpecialRegister(A)));
		}
	}

	public boolean isArithmeticException(int exception) {
		// BitSet b =new BitSet();
		return (getSpecialRegister(A) & (0x1 << exception)) == (0x1 << exception);
	}

	/**
	 * used in case of explicit trip instruction.
	 * 
	 * @return
	 */
	public boolean isPreviousStoreInstruction() {
		int temp = this.getUnsignedByte(virtualAt - 4);
		return this.isStoreInstruction(temp);
	}

	/**
	 * used in case of implicit trip, such as Arithmetic exception
	 * 
	 * @return
	 */
	public boolean isCurrentStoreInstruction() {
		int temp = this.getUnsignedByte(virtualAt);
		return this.isStoreInstruction(temp);
	}

	private boolean isStoreInstruction(int opCode) {
		if (opCode >= STB && opCode <= STUNCI) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * we can not really simulate 2 power 64 bytes memory, we simulate only 16M.
	 * at the same time, the index is limited to the range of int in Java.
	 */
	// int addressLimit = 1024 * 1024 * 16;
	// use less memory in development phase.
	int addressLimit = 1024 * 1024 * 1;

	// only consider the user space segment.
	// int segmentSize = this.addressLimit / 4;
	private byte[] memory = new byte[addressLimit];

	byte[] memoryFlag = new byte[addressLimit / PAGE_SIZE];

	public static final int PAGE_SIZE = 1024 * 4;

	public static final int PAGE_SIZE_SHIFT = 12;

	Map<Long, Integer> tlb = new HashMap<Long, Integer>(this.addressLimit
			/ PAGE_SIZE);

	/**
	 * 
	 * @param virtualAddr
	 * @return
	 */
	private int memoryMap(long virtualAddr) {
		// log.debug("virtualAddr=" + virtualAddr);
		long pageFrame = virtualAddr >>> 12;
		if (tlb.size() != 0 && tlb.containsKey(Long.valueOf(pageFrame))) {
			Integer a = (Integer) tlb.get(Long.valueOf(pageFrame));
			// log.debug(a.intValue() + (int) (virtualAddr %
			// PAGE_SIZE));
			return a.intValue() + (int) (virtualAddr % PAGE_SIZE);
		} else {
			boolean match = false;
			int i;
			for (i = 0; i < memoryFlag.length; i++) {
				if (memoryFlag[i] == 0) {
					memoryFlag[i] = 1;
					tlb.put(Long.valueOf(pageFrame), Integer
							.valueOf(i << PAGE_SIZE_SHIFT));
					match = true;
					break;
				}
			}
			if (match) {
				if (log.isDebugEnabled()) {
					log.debug("virtualAddr=" + Long.toHexString(virtualAddr));
					log
							.debug("i= "
									+ i
									+ " ; physical address is 0x"
									+ Long
											.toHexString(((i << PAGE_SIZE_SHIFT) + (int) (virtualAddr % PAGE_SIZE))));
				}
				return (i << PAGE_SIZE_SHIFT) + (int) (virtualAddr % PAGE_SIZE);
				// bug 101: << is lower priority than +
			} else {
				throw new RuntimeException("MMIX: out of memory");
			}
		}

	}

	public void setByte(long virtualAddr, int value) {
		int realAddr = memoryMap(virtualAddr);
		if (log.isInfoEnabled()) {
			log.info("memory virtualAddr[1] = 0x"
					+ Long.toHexString(virtualAddr) + "; realAddr = 0x"
					+ Integer.toHexString(realAddr) + " was changed from 0x"
					+ Integer.toHexString(memory[realAddr]) + " to 0x"
					+ Integer.toHexString((byte) value));
		}
		memory[realAddr] = (byte) value;
	}

	public void setByte(long virtualAddr, byte value) {
		int realAddr = memoryMap(virtualAddr);
		if (log.isInfoEnabled()) {
			log.info("memory virtualAddr[1] = 0x"
					+ Long.toHexString(virtualAddr) + "; realAddr = 0x"
					+ Integer.toHexString(realAddr) + " was changed from 0x"
					+ Integer.toHexString(memory[realAddr]) + " to 0x"
					+ Integer.toHexString(value));
		}
		memory[realAddr] = value;
	}

	public void setWyde(long virtualAddr, int value) {
		int realAddr = memoryMap(virtualAddr);
		int i = realAddr % 2;
		if (log.isInfoEnabled()) {
			log.info("memory virtualAddr[2] = 0x"
					+ Long.toHexString(virtualAddr) + "; realAddr = 0x"
					+ Integer.toHexString(realAddr - i)
					+ " was changed from 0x"
					+ Integer.toHexString(this.getSignedWyde(virtualAddr))
					+ " to 0x" + Integer.toHexString(value));
		}
		memory[realAddr - i] = (byte) (value >>> 8 & 0x000000ff);
		memory[realAddr + 1 - i] = (byte) (value & 0x000000ff);

	}

	/**
	 * lessens learned: the library is powerful. it is also easy to make mistake
	 * if you are not familiar with library.
	 * 
	 * @param virtualAddr
	 * @param value
	 */
	public void setTetra(long virtualAddr, int value) {
		int realAddr = memoryMap(virtualAddr);
		int i = realAddr % 4;
		if (log.isInfoEnabled()) {
			log.info("memory virtualAddr[4] = 0x"
					+ Long.toHexString(virtualAddr) + "; realAddr = 0x"
					+ Integer.toHexString(realAddr - i)
					+ " was changed from 0x"
					+ Long.toHexString(this.getSignedTetra(virtualAddr))
					+ " to 0x" + Integer.toHexString(value));
		}
		BigInteger bigI = BigInteger.valueOf(value);
		byte[] temp = bigI.toByteArray();

		if (temp[0] == 0) {
			System.arraycopy(zero, 0, memory, realAddr - i,
					+(4 - (temp.length - 1)));
			System.arraycopy(temp, 0, memory, realAddr - i
					+ (4 - (temp.length - 1)), temp.length - 1);
		} else if (temp[0] == -1) {
			System.arraycopy(minusOne, 0, memory, realAddr - i,
					+(4 - (temp.length)));
			System.arraycopy(temp, 0, memory, realAddr - i + (4 - temp.length),
					temp.length);
		} else {
			System.arraycopy(zero, 0, memory, realAddr - i, +(4 - temp.length));
			System.arraycopy(temp, 0, memory, realAddr - i + (4 - temp.length),
					temp.length);
		}
	}

	public void setTetra(long virtualAddr, byte[] value, int offset) {
		int realAddr = memoryMap(virtualAddr);
		int i = realAddr % 4;
		if (log.isInfoEnabled()) {
			log.info("memory virtualAddr[4] = 0x"
					+ Long.toHexString(virtualAddr)
					+ "; realAddr = 0x"
					+ Long.toHexString(realAddr - i)
					+ " was changed from 0x"
					+ Long.toHexString(this.getSignedTetra(virtualAddr))
					+ " to 0x"
					+ Long
							.toHexString(NumberUtil.getSignedTetra(value,
									offset)));
		}
		System.arraycopy(value, offset, memory, realAddr - i, 4);

	}

	public void setPartialTetra(long virtualAddr, byte[] value, int offset) {
		int realAddr = memoryMap(virtualAddr);
		int i = realAddr % 4;
		memory[realAddr - i] ^= value[offset];
		memory[realAddr + 1 - i] ^= value[offset + 1];
		memory[realAddr + 2 - i] ^= value[offset + 2];
		memory[realAddr + 3 - i] ^= value[offset + 3];

	}

	public void setOcta(long virtualAddr, long value) {
		int realAddr = memoryMap(virtualAddr);
		int i = realAddr % 8;
		BigInteger bigI = BigInteger.valueOf(value);
		byte[] temp = bigI.toByteArray();
		// byte[] from = new byte[8];
		if (log.isDebugEnabled()) {
			log.debug("virAddr=0x" + Long.toHexString(virtualAddr)
					+ "; realAddr=0x" + Long.toHexString(realAddr)
					+ "; temp.length=" + temp.length);
		}
		if (log.isInfoEnabled()) {
			log.info("memory virtualAddr[8] = 0x"
					+ Long.toHexString(virtualAddr) + "; realAddr = 0x"
					+ Long.toHexString(realAddr - i) + " was changed from 0x"
					+ Long.toHexString(this.getSignedOcta(virtualAddr))
					+ " to 0x" + Long.toHexString(value));
		}
		// bug 1035 temp.length is not always 8.
		if (temp[0] == 0) {
			System.arraycopy(zero, 0, memory, realAddr - i,
					+(8 - (temp.length - 1)));
			System.arraycopy(temp, 0, memory, realAddr - i
					+ (8 - (temp.length - 1)), temp.length - 1);
		} else if (temp[0] == -1) {// bug 1234. the format ot byte[] from
			// Biginteger!
			System.arraycopy(minusOne, 0, memory, realAddr - i,
					+(8 - (temp.length)));
			System.arraycopy(temp, 0, memory, realAddr - i + (8 - temp.length),
					temp.length);
		} else {
			System.arraycopy(zero, 0, memory, realAddr - i, +(8 - temp.length));
			System.arraycopy(temp, 0, memory, realAddr - i + (8 - temp.length),
					temp.length);
		}
	}

	public int getUnsignedByte(long virtualAddr) {
		int realAddr = memoryMap(virtualAddr);
		return NumberUtil.getUnsignedByte(memory[realAddr]);
	}

	public int getSignedByte(long virtualAddr) {
		int realAddr = memoryMap(virtualAddr);
		return memory[realAddr];
	}

	public int getUnsignedWyde(long virtualAddr) {
		int address = memoryMap(virtualAddr);
		int i = address % 2;
		return NumberUtil.getUnsignedWyde(memory[address - i], memory[address
				- i + 1]);
	}

	public int getSignedWyde(long virtualAddr) {
		int address = memoryMap(virtualAddr);
		int i = address % 2;
		return NumberUtil.getSignedWyde(memory[address - i], memory[address - i
				+ 1]);
	}

	public long getUnsignedTetra(long virtualAddr) {
		int address = memoryMap(virtualAddr);
		int i = address % 4;
		return NumberUtil.getUnsignedTetra(memory[address - i], memory[address
				- i + 1], memory[address - i + 2], memory[address - i + 3]);
	}

	public int getSignedTetra(long virtualAddr) {
		int address = memoryMap(virtualAddr);
		int i = address % 4;
		return NumberUtil.getSignedTetra(memory, address - i);
	}

	public long getSignedOcta(long virtualAddr) {
		int realAddr = memoryMap(virtualAddr);
		int i = realAddr % 8;
		return NumberUtil.getSignedOcta(memory, realAddr - i);
	}

	// public long getSignedOcta(long virtualAddr) {
	// int address = memoryMap(virtualAddr);
	// int i = address % 8;
	// return NumberUtil.getSignedOcta(memory,address - i);
	// }

	/**
	 * can not use real address because of it may cross pages.
	 * 
	 * @param virtualAddress
	 * @param value
	 * @param offset
	 * @param length
	 */
	public void setByteArray(long virtualAddress, byte[] value, int offset,
			int length) {
		for (int i = 0; i < length; i++) {
			memory[memoryMap(virtualAddress + i)] = value[offset + i];
		}
	}

	/**
	 * the string ended in first null byte.
	 * 
	 * @param virtualAddress
	 * @return
	 */
	protected String getStringByNull(long virtualAddress) {
		StringBuilder builder = new StringBuilder();
		byte[] tt = new byte[1];
		for (long i = virtualAddress; (tt[0] = memory[memoryMap(i)]) != 0; i++) {

			builder.append(new String(tt));
		}
		if (log.isDebugEnabled()) {
			log.debug(builder.toString());
		}
		return builder.toString();
	}

	protected String getString(long virtualAddress, int size) {
		StringBuilder builder = new StringBuilder();
		byte[] tt = new byte[1];
		for (long i = 0; i < size; i++) {
			tt[0] = memory[this.memoryMap(virtualAddress + i)];
			builder.append(new String(tt));
		}
		if (log.isDebugEnabled()) {
			log.debug(builder.toString());
		}
		if (!builder.toString().equals(
				new String(this.getByteArray(virtualAddress, size)))) {
			throw new RuntimeException(
					"MMIX: getStringByNull no consistent with getString");
		}
		return builder.toString();
	}

	protected byte[] getByteArray(long virtualAddress, int size) {
		byte[] tt = new byte[size];
		for (int i = 0; i < size; i++) {
			tt[i] = memory[memoryMap(virtualAddress + i)];
		}
		return tt;
	}

	/**
	 * 
	 * @param x
	 *            how many results are returned.
	 */
	void pop(int x) {
		// every push will store the value in relative $x, which is just ahead
		// of the current relative $0.
		int alpha = getAlpha();
		int oldX = (int) localRegister[alpha - 1];

		// may be need to load the register from memory
		int garma;

		for (int i = 0; i < oldX + 1; i++) {
			garma = getGarma();
			if (((alpha - 1 - i) % NUMBER_OF_LOCAL_REGISTER) == ((garma - 1) % NUMBER_OF_LOCAL_REGISTER)) {
				localRegister[alpha - 1 - i] = getSignedOcta(getSpecialRegister(S));
				increaseSpecialRegister(S, -8);
			}
		}

		// set up return value - original impl.
		// if (x == 0) {
		//
		// } else if (x == 1) {
		// localRegister[alpha - 1] = localRegister[alpha];
		// } else if (x > 1) {
		// localRegister[alpha - 1] = localRegister[alpha + x - 1];
		// }

		// general one
		// localRegister[alpha - 1] = localRegister[alpha + x - 1];
		setLocalRegister(alpha - 1, localRegister[alpha + x - 1]);
		// shrink register stack from top
		this.setSpecialRegister(L, x);
		// extends register from bottom
		increaseSpecialRegister(L, (oldX + 1));

		increaseSpecialRegister(O, -(oldX + 1) * 8);
	}

	// access the general resister directly because it will handle it directly
	/**
	 * actually this is the most difficult and confusing part of MMIX. after I
	 * got the MMIXware I finally know the whole idea behind it. 1. register
	 * stack is first a memory stack to backup the local registers. 2. second,
	 * to avoid the memory access, in fact the MMIX hardware will have from 256
	 * to 1024 local registers, so actually not every subroutine calls will
	 * cause the register was stored into memory. this will dramatically
	 * increase the efficiency of subroutine calls.
	 * 
	 * refer to MMIXware, please. Knuth can can describe it way better than me,
	 * those comments just help me record my learning curve.
	 */
	void push(int x) {
		int alpha = getAlpha();
		int ll = (int) getSpecialRegister(L);
		int gg = (int) getSpecialRegister(G);
		// TODO change to x <=ll?
		if (x < ll && x >= 0) {
			// localRegister[alpha + x] = x;// to track the number of
			setLocalRegister(alpha + x, x);// 
			// register
			// pushed.
			increaseSpecialRegister(O, 8 * (x + 1));
			increaseSpecialRegister(L, -(x + 1));
		} else if (x >= gg) {
			setLocalRegister(ll, ll);
			this.setSpecialRegister(L, 0);
		} else {
			throw new RuntimeException("MMIX: invalud X in push (X)" + x);
		}

	}

	public void trip() {

	}

	/**
	 * consider both memory backup and resister stack used to avoid register
	 * movement. getSpecialRegister(S) store the address of first availabe
	 * memory(not used yet) used to store the regist pushed down register stack.
	 * getSpecialRegister(O) store the address of memory used to store r[alpha],
	 * just a mapping, actually not stored yet.
	 * 
	 * @param x
	 */
	void push2(int x) {
		// int alpha = getSpecialRegister(O)/8

		int oldOffset = (int) getSpecialRegister(O); // inclusive

		if (x < getSpecialRegister(L)) {
			localRegister[oldOffset + x] = x;// to track the number of
			// register
			// pushed.
			increaseSpecialRegister(O, x + 1);
			increaseSpecialRegister(L, -(x + 1));
		} else if (x >= getSpecialRegister(G)) {
			// ?localRegister[(int)
			// this.setSpecialRegister(L],getSpecialRegister(L));
			this.setSpecialRegister(L, 0);
		} else {
			throw new RuntimeException("MMIX: invalud X in push (X)");
		}

	}

	private int getAlpha() {
		return (memoryMap(getSpecialRegister(O)) / 8)
				% NUMBER_OF_LOCAL_REGISTER;
	}

	private int getGarma() {
		return (memoryMap(getSpecialRegister(S)) / 8)
				% NUMBER_OF_LOCAL_REGISTER;
	}

	InstructionExecutor[] executors = new InstructionExecutor[256];

	InstructionExecutor floatPointInstructionExecutor = new FloatPointInstructionExecutor();

	InstructionExecutor arithmeticInstructionExecutor = new ArithmeticInstructionExecutor();

	BranchInstructionExecutor branchInstructionExecutor = new BranchInstructionExecutor();

	ConditionalInstructionExecutor conditionalInstructionExecutor = new ConditionalInstructionExecutor();

	LoadingInstructionExecutor loadingInstructionExecutor = new LoadingInstructionExecutor();

	SystemInstructionExecutor systemInstructionExecutor = new SystemInstructionExecutor();

	JumpInstructionExecutor jumpInstructionExecutor = new JumpInstructionExecutor();

	StoringInstructionExecutor storingInstructionExecutor = new StoringInstructionExecutor();

	SubroutineInstructionExecutor subroutineInstructionExecutor = new SubroutineInstructionExecutor();

	BitwiseInstructionExecutor bitwiseInstructionExecutor = new BitwiseInstructionExecutor();

	BytewiseInstructionExecutor bytewiseInstructionExecutor = new BytewiseInstructionExecutor();

	WydeImmediateInstructionExecutor wydeImmediateInstructionExecutor = new WydeImmediateInstructionExecutor();

	OtherInstructionExecutor otherInstructionExecutor = new OtherInstructionExecutor();

	InterruptInstructionExecutor interruptInstructionExecutor = new InterruptInstructionExecutor();

	private void setUpInstructionExecutor() {
		executors[TRAP] = new InterruptInstructionExecutor();

		for (int i = 0x1; i < 0x18; i++) {
			executors[i] = floatPointInstructionExecutor;
		}

		for (int i = 0X18; i < 0x40; i += 1) {
			executors[i] = arithmeticInstructionExecutor;
		}

		for (int i = 0X40; i < 0x60; i += 1) {
			executors[i] = branchInstructionExecutor;
		}

		for (int i = 0x60; i < 0x80; i++) {
			executors[i] = conditionalInstructionExecutor;
		}

		for (int i = 0x80; i < 0x90; i++) {
			executors[i] = loadingInstructionExecutor;
		}
		executors[LDSF] = floatPointInstructionExecutor;// 90
		executors[LDSFI] = floatPointInstructionExecutor;// 91
		executors[LDHT] = loadingInstructionExecutor;// 92
		executors[LDHTI] = loadingInstructionExecutor;// 93

		for (int i = 0x94; i < 0x9e; i++) {
			executors[i] = systemInstructionExecutor;
		}

		executors[GO] = jumpInstructionExecutor;// 9e
		executors[GOI] = jumpInstructionExecutor;// 9f
		// 9x

		for (int i = 0xa0; i < 0xb0; i += 1) {
			executors[i] = storingInstructionExecutor;

		}
		executors[STSF] = floatPointInstructionExecutor;// b0
		executors[STSFI] = floatPointInstructionExecutor;// b1

		for (int i = 0xb2; i < 0xb6; i += 1) {
			// bug 7: executors[STHT] = new StoringInstructionExecutor();
			executors[i] = storingInstructionExecutor;
		}
		// executors[STHT] = new StoringInstructionExecutor();//b2
		// executors[STHTI] = new StoringInstructionExecutor();//b3
		// executors[STCO] = new StoringInstructionExecutor();//b4
		// executors[STCOI] = new StoringInstructionExecutor();//b5

		for (int i = 0xb6; i < 0xbe; i += 1) {
			executors[i] = systemInstructionExecutor;

		}

		executors[PUSHGO] = subroutineInstructionExecutor;// be
		executors[PUSHGOI] = subroutineInstructionExecutor;// bf

		// for (int i = 0xb8; i < 0xc0; i += 1) {
		// executors[i] = new StoringInstructionExecutor();
		//
		// }

		for (int i = 0xc0; i < 0xd0; i += 1) {
			executors[i] = bitwiseInstructionExecutor;

		}

		for (int i = 0xd0; i < 0xd8; i += 1) {
			executors[i] = bytewiseInstructionExecutor;

		}

		for (int i = 0xd8; i < 0xdc; i += 1) {
			executors[i] = bitwiseInstructionExecutor;

		}
		for (int i = 0xdc; i < 0xe0; i += 1) {
			executors[i] = bytewiseInstructionExecutor;
		}

		for (int i = 0xe0; i < 0xf0; i += 1) {
			executors[i] = wydeImmediateInstructionExecutor;
		}
		executors[JMP] = jumpInstructionExecutor;// f0
		executors[JMPB] = jumpInstructionExecutor;// f1
		executors[PUSHJ] = subroutineInstructionExecutor;// f2
		executors[PUSHJB] = subroutineInstructionExecutor;// f3

		for (int i = 0xf4; i < 0xf8; i += 1) {
			executors[i] = otherInstructionExecutor;
		}

		executors[POP] = subroutineInstructionExecutor;// f8
		executors[RESUME] = interruptInstructionExecutor;// f9
		executors[UNSAVE] = subroutineInstructionExecutor;// fa
		executors[SAVE] = subroutineInstructionExecutor;// fb
		executors[SYNC] = systemInstructionExecutor;// fc
		executors[SWYM] = otherInstructionExecutor;// fd
		executors[GET] = otherInstructionExecutor;// fe
		executors[TRIP] = interruptInstructionExecutor;// ff

		for (int i = 0; i < 256; i++) {
			if (executors[i] != null) {// temp solution.{
				executors[i].setMachineState(this);
				// log.debug("executors[" + i + "] = " + executors[i]);
			} else {
				throw new RuntimeException("MMIX: executors[i] != null; i = 0x"
						+ Integer.toHexString(i));
			}
		}
	}

	public void showInternalStatus(Logger log) {
		if (log.isDebugEnabled()) {
			log.debug("getGeneralRegister(0)= 0x"
					+ Long.toHexString(getGeneralRegister(0)));
			log.debug("getGeneralRegister(1)= 0x"
					+ Long.toHexString(getGeneralRegister(1)));
			log.debug("getGeneralRegister(2)= 0x"
					+ Long.toHexString(getGeneralRegister(2)));

			log.debug("getSpecialRegister(L)= " + getSpecialRegister(L));
			log.debug("getSpecialRegister(G)= " + getSpecialRegister(G));
			log.debug("getSpecialRegister(O)= 0x"
					+ Long.toHexString(getSpecialRegister(O)));
			log.debug("getSpecialRegister(S)= 0x"
					+ Long.toHexString(getSpecialRegister(S)));

			log.debug("getGeneralRegister(253)= 0x"
					+ Long.toHexString(getGeneralRegister(253)));
			log.debug("getGeneralRegister(254)= 0x"
					+ Long.toHexString(getGeneralRegister(254)));
			log.debug("getGeneralRegister(255)= 0x"
					+ Long.toHexString(getGeneralRegister(255)));
		}
	}

	/**
	 * TODO: can be optimized to reduce the memory mapping.
	 * @param log
	 * @param virtualAddress
	 * @param length
	 */
	public void showMemory(Logger log, long virtualAddress, int length) {
		if (log.isDebugEnabled()) {
			log.debug("in virtual address Byte array:  from 0x" + Long.toHexString(virtualAddress)
					+ " to 0x" + Long.toHexString(virtualAddress + length)
					+ " of menory");
			StringBuffer buf = new StringBuffer();
			for (long i = virtualAddress; i < virtualAddress + length; i++) {
				if (i % 8 == 7) {
					buf.append("" + NumberUtil.byteToHex(memory[memoryMap(i)])
							+ "\n");
				} else if (i % 8 == 3) {
					buf.append("" + NumberUtil.byteToHex(memory[memoryMap(i)])
							+ "\t");
				} else {
					buf.append("" + NumberUtil.byteToHex(memory[memoryMap(i)]));
				}
			}
			log.debug("\n" + buf.toString());
		}
	}

	public void showMemory(Logger log) {
		showMemory(log, virtualAt, 128);
	}

	public void showMemory(Logger log, long virtualAddress) {
		showMemory(log, virtualAddress, 128);
	}

	byte[] memory() {
		return memory;
	}

	public void save(int x) {

		long oo = getSpecialRegister(O);
		long ss = getSpecialRegister(S);
		int i = 0;
		for (i = 0; i < oo - ss; i++) {
			this.setOcta(ss + i, this.getGeneralRegister(ss + i));
		}
		int gg = (int) getSpecialRegister(G);
		for (i = 0; i < 256 - gg; i++) {
			this.setOcta(oo + i, this.getGlobalRegister(gg + i));
		}
		this.setOcta(oo + (i++), getSpecialRegister(B));
		this.setOcta(oo + (i++), getSpecialRegister(D));
		this.setOcta(oo + (i++), getSpecialRegister(E));
		this.setOcta(oo + (i++), getSpecialRegister(H));
		this.setOcta(oo + (i++), getSpecialRegister(J));
		this.setOcta(oo + (i++), getSpecialRegister(M));

		this.setOcta(oo + (i++), getSpecialRegister(R));
		this.setOcta(oo + (i++), getSpecialRegister(P));
		this.setOcta(oo + (i++), getSpecialRegister(W));
		this.setOcta(oo + (i++), getSpecialRegister(X));
		this.setOcta(oo + (i++), getSpecialRegister(Y));
		this.setOcta(oo + (i++), getSpecialRegister(Z));

		this.setOcta(oo + (i++),
				getSpecialRegister(G) << 56 + getSpecialRegister(A));
		setGeneralRegister(x, oo + (256 - gg) + 13 - 1);
	}

	public void unsave(long valueZ) {

		this.setSpecialRegister(B, this.getSignedOcta(valueZ - 12 + B));
		this.setSpecialRegister(D, this.getSignedOcta(valueZ - 12 + D));
		this.setSpecialRegister(E, this.getSignedOcta(valueZ - 12 + E));
		this.setSpecialRegister(H, this.getSignedOcta(valueZ - 12 + H));
		this.setSpecialRegister(J, this.getSignedOcta(valueZ - 12 + J));
		this.setSpecialRegister(M, this.getSignedOcta(valueZ - 12 + M));
		this.setSpecialRegister(R, this.getSignedOcta(valueZ - 12 + R));

		this
				.setSpecialRegister(P, this.getSignedOcta(valueZ - 12 + P - 23
						+ 7));
		this
				.setSpecialRegister(W, this.getSignedOcta(valueZ - 12 + W - 23
						+ 7));
		this
				.setSpecialRegister(X, this.getSignedOcta(valueZ - 12 + X - 23
						+ 7));
		this
				.setSpecialRegister(Y, this.getSignedOcta(valueZ - 12 + Y - 23
						+ 7));
		this
				.setSpecialRegister(Z, this.getSignedOcta(valueZ - 12 + Z - 23
						+ 7));

		int gg = (int) getSpecialRegister(G);
		int i = 0;
		for (i = 0; i < 256 - gg; i++) {
			this.setGlobalRegister(gg + i, this.getSignedOcta(valueZ - 12
					- (256 - gg) + i));
		}
		// TODO pop up the local register from the stack and load them fathom
	}

	static void arraycopy(byte[] src, int srcPos, byte[] dest, long destPos,
			int length) {

	}

	public void setMachineException(int exception) {
		setSpecialRegister(Q, getSpecialRegister(Q) | (0x1 << exception));
	}

	public void setShouldHalt(boolean shouldHalt) {
		this.shouldHalt = shouldHalt;
	}

}