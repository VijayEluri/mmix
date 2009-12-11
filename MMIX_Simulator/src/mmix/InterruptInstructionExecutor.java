package mmix;

import static mmix.cons.InstructionConstant.RESUME;
import static mmix.cons.InstructionConstant.TRAP;
import static mmix.cons.InstructionConstant.TRIP;
import static mmix.cons.InterruptMaskConstant.INTERRUPT_PROGRAM_B;
import static mmix.cons.SpecialRegisterConstant.BB;
import static mmix.cons.SpecialRegisterConstant.J;
import static mmix.cons.SpecialRegisterConstant.K;
import static mmix.cons.SpecialRegisterConstant.W;
import static mmix.cons.SpecialRegisterConstant.WW;
import static mmix.cons.SpecialRegisterConstant.X;
import static mmix.cons.SpecialRegisterConstant.XX;
import static mmix.cons.SpecialRegisterConstant.Y;
import static mmix.cons.SpecialRegisterConstant.YY;
import static mmix.cons.SpecialRegisterConstant.Z;
import static mmix.cons.SpecialRegisterConstant.ZZ;
import static mmix.cons.TrapConstant.Fclose;
import static mmix.cons.TrapConstant.Fgets;
import static mmix.cons.TrapConstant.Fopen;
import static mmix.cons.TrapConstant.Fputs;
import static mmix.cons.TrapConstant.Fread;
import static mmix.cons.TrapConstant.Fseek;
import static mmix.cons.TrapConstant.Ftell;
import static mmix.cons.TrapConstant.Fwrite;
import static mmix.cons.TrapConstant.HALT;
import static mmix.cons.TrapConstant.StdErr;
import static mmix.cons.TrapConstant.StdIn;
import static mmix.cons.TrapConstant.StdOut;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;

import mmix.cons.TrapConstant;

import org.apache.log4j.Logger;

/**
 * 
 * @author Eddie Wu
 * @version 1.0
 * 
 */
public class InterruptInstructionExecutor extends InstructionExecutor {
	Logger log = Logger.getLogger(this.getClass());

	String[] fileName = new String[256];

	Object[] inStream = new Object[256];
	Object[] outStream = new Object[256];

	InterruptInstructionExecutor() {
		super();
	}

	long high;

	long low;

	@Override
	public void execute() {

		switch (opCode) {
		/**
		 * see page 39 of MMIXware TRAP is handled by Kernel. XYZ = 0, conclude
		 * a program. XYZ = 1, provide default action in case user did not
		 * provide handler for certain kind of interrupt.
		 */
		case TRAP:
			if (log.isDebugEnabled()) {
				log.debug("original machine.getGeneralRegister(255)=0x"
						+ Long.toHexString(machine.getGeneralRegister(255)));
			}
			machine.setSpecialRegister(BB, machine.getGeneralRegister(255));
			machine.setGeneralRegister(255, machine.getSpecialRegister(J));
			machine.setSpecialRegister(WW, machine.virtualAt + 4);
			high = 0x80000000;
			low = NumberUtil.getSignedTetra((byte) opCode, (byte) x, (byte) y,
					(byte) z);
			// opCode << 24 + x << 16 + y << 8 + z;
			machine.setSpecialRegister(XX, high << 32 + low);
			if (!machine.isPreviousStoreInstruction()) {
				machine.setSpecialRegister(YY, machine.getGeneralRegister(y));
				if (isImmediateInstruction()) {
					machine.setSpecialRegister(ZZ, z);
				} else {
					machine.setSpecialRegister(ZZ, machine
							.getGeneralRegister(z));
				}
			} else {
				machine.setSpecialRegister(YY, machine.getGeneralRegister(y)
						+ machine.getGeneralRegister(z));
				machine.setSpecialRegister(ZZ, machine.getGeneralRegister(x));
			}
			// temp work around
			// machine.at = (int) machine.getSpecialRegister(T);
			if (x == 0) {
				if (y == HALT) {
					halt();
				} else if (y >= TrapConstant.Fopen && y <= TrapConstant.Ftell) {
					switch (y) {
					case Fopen:
						fOpen();
						break;
					case Fclose:
						fClose();						
						break;
					case Fread:
						fRead();

						break;
					case Fgets:
						fGets();

						break;
					case Fwrite:
						fWrite();
						break;
					case Fputs:
						fPuts();
						break;
					case Ftell:
						fTell();
						break;
					case Fseek:
						fSeek();
						break;
					}
				} else {
					throw new RuntimeException("MMIX: invalid trap function");
				}
			} else {
				// for future extension.
			}
			break;

		// TODO encapsulate the access to special register.
		/**
		 * see page 38 of MMIXware trip is handled by user program
		 */
		case TRIP:
			trip();
			machine.virtualAt = 0x0;
			break;
		case RESUME:
			if (x != 0 || y != 0) {
				throw new RuntimeException("MMIX: invalid Resume: x=0x"
						+ Long.toHexString(x) + "; y=0x" + Long.toHexString(y));
			}
			if (z == 0) {
				if (machine.getSpecialRegister(X) < 0) {
					// ignore current instruction, continue to execute next
					// instruction.
					machine.virtualAt = machine.getSpecialRegister(W);
				} else {


					// dresume_inst = machine.getSpecialRegister(X);
					int ropCode = (int) (machine.getSpecialRegister(X) >> 58);
					int temp = (int) (machine.getSpecialRegister(X) & 0x00000000ffffffffL);
					BigInteger big = BigInteger.valueOf(temp);
					byte[] tt = big.toByteArray();
					// tt.length should be 4, since trip and trap will not be
					// re-executed.
					opCode = NumberUtil.getUnsignedByte(tt[0]);
					x = NumberUtil.getUnsignedByte(tt[1]);
					y = NumberUtil.getUnsignedByte(tt[2]);
					z = NumberUtil.getUnsignedByte(tt[3]);

					// re-execute the instruction in rX.
					machine.executors[opCode].resume = true;
					machine.executors[opCode].ropCode = ropCode;
					machine.executors[opCode].resumeZ = z;

					machine.executors[opCode].setInstruction(opCode, x, y, z);
					machine.executors[opCode].execute();

					machine.executors[opCode].resume = false;
					machine.executors[opCode].ropCode = 0;
					machine.executors[opCode].resumeZ = 0;

					machine.virtualAt = (int) machine.getSpecialRegister(W);// -4?
				}
			} else if (z == 1) {
				if (machine.virtualAt < 0) {
					machine.setSpecialRegister(K, machine
							.getGeneralRegister(255));
					machine.setGeneralRegister(255, machine
							.getSpecialRegister(BB));
				}
			} else {
				//TODO set bit b of rQ
				throw new RuntimeException("MMIX: invalid z in Resume; z = 0x"
						+ z);
			}

			break;
		default:
			// TODO set interruption in this case instead of throw exception.
			machine.setMachineException(INTERRUPT_PROGRAM_B);

			// TODO: comment out
			throw new RuntimeException("MMIX: invalid OpCode (" + opCode
					+ ") for " + this.getClass().getCanonicalName());
		}
	}

	private void fClose() {
		if(log.isInfoEnabled()){
			log.info("closing file: " + fileName[z]);
		}
		fileName[z] = null;
		inStream[z] = null;
		outStream[z] = null;
		systemCallOk();
	}

	public void trip() {
		machine.setSpecialRegister(BB, machine.getGeneralRegister(255));
		// should we set it to 0. see page 18 of fascicle one.
		machine.setGeneralRegister(255, machine.getSpecialRegister(J));
		machine.setSpecialRegister(W, machine.virtualAt + 4);
		high = 0x80000000;
		low = opCode << 24 + x << 16 + y << 8 + z;
		if (low != NumberUtil.getSignedTetra((byte) opCode, (byte) x, (byte) y,
				(byte) Z)) {
			throw new RuntimeException(
					"MMIX: low =0x"
							+ Long.toHexString(low)
							+ "NumberUtil.getSignedTetra((byte)opCode, (byte)x, (byte)y, (byte)Z) = 0x"
							+ NumberUtil.getSignedTetra((byte) opCode,
									(byte) x, (byte) y, (byte) Z));
		}
		machine.setSpecialRegister(X, high << 32 + low);
		if (machine.isPreviousStoreInstruction()) {
			super.setAddress();
			machine.setSpecialRegister(Y, virtualAddress);
			machine.setSpecialRegister(Z, machine.getGeneralRegister(x));
		} else {
			super.setOperandYZ();
			machine.setSpecialRegister(Y, valueY);
			machine.setSpecialRegister(Z, valueZ);
		}
	}

	private void halt() {
		if (log.isDebugEnabled()) {
			log.debug("TRAP to OS: HALT");
		}
		for (int i = 0; i < 256; i++) {

			try {
				if (inStream[i] != null) {
					((InputStream) inStream[i]).close();
				}
				if (outStream[i] != null) {
					((OutputStream) outStream[i]).close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log.debug(e.getMessage());
			}

		}
		machine.setShouldHalt(true);
		//throw new RuntimeException("MMIX: TRAP to OS: HALT");
	}

	private void fSeek() {
		long offset = machine.getSpecialRegister(BB);
		FileOutputStream outS = outS = (FileOutputStream) outStream[z];
		// System.out.
		// if(offset >= 0){
		// outS.getFD().
		// }else{
		//			
		// }

	}

	private void fTell() {
		// TODO Auto-generated method stub

	}

	private void fPuts() {
		long strAdd = machine.getSpecialRegister(BB);
		if (z == StdOut) {
			System.out.print(machine.getStringByNull(strAdd));
		} else if (z == StdErr) {
			System.err.print(machine.getStringByNull(strAdd));
		} else if (z > 2) {
			FileOutputStream outS = (FileOutputStream) outStream[z];
			try {
				(outS).write(machine.getStringByNull(strAdd).getBytes());
			} catch (IOException e) {
				this.systemCallFail();
				log.debug(e.getMessage());
				// log.debug(e.getMessage());
			}
		}
		if (log.isDebugEnabled()) {
			log.debug("bufferA = 0x" + Long.toHexString(strAdd));
			machine.showMemory(log, strAdd, 16);

			log.debug("string in bufferA is: "
					+ machine.getStringByNull(strAdd));
		}

		log.debug(machine.getStringByNull(machine.getSpecialRegister(BB)));
	}

	private void fWrite() {
		long bufferA = machine.getSignedOcta(machine.getSpecialRegister(BB));
		long size = machine.getSignedOcta(machine.getSpecialRegister(BB) + 8);
		if (z == StdOut || z == StdErr) {
			System.out.print(machine.getString(bufferA, (int) size));
		} else if (z > 2) {
			try {
				((FileOutputStream) outStream[z]).write(machine.getByteArray(
						bufferA, (int) size), 0, (int) size);
			} catch (IOException e) {
				this.systemCallFail();
				log.debug(e.getMessage());
			}
		}
		if (log.isDebugEnabled()) {
			log.debug("bufferA = " + Long.toHexString(bufferA));
			log.debug("size = " + size);

			machine.showMemory(log, bufferA, (int) size);
		}

	}

	private void fGets() {
		long paramAddr = machine.getSpecialRegister(BB);
		long bufferA = machine.getSignedOcta(paramAddr);
		long size = machine.getSignedOcta(paramAddr + 8);

		// use buffer with same size as mmix program.
		int realSize = 0;
		byte[] buffer = new byte[(int) size - 1];
		if (z == StdIn) {
			try {
				// while (true) {
				realSize = System.in.read(buffer);
				if (realSize != -1) {
					machine.setByteArray(bufferA, buffer, 0, realSize);
					machine.setByte(bufferA + realSize, 0);
					this.systemCallOk(realSize);
				} else {
					this.systemCallFail(-1);
				}
				// }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				log.debug(e.getMessage());
			}
		} else if (z >= 3) {
			FileInputStream inS = (FileInputStream) inStream[z];
			// byte[] b = new byte[(int) size];
			try {
				realSize = inS.read(buffer);
				if (realSize != -1) {
					machine.setByteArray(bufferA, buffer, 0, realSize);
					machine.setByte(bufferA + realSize, 0);
					this.systemCallOk(realSize);
				} else {
					this.systemCallFail(-1);
				}

			} catch (IOException e) {
				if (log.isDebugEnabled()) {
					log.debug(e.getMessage());
				}
				this.systemCallFail();
			}

		}
		if (log.isDebugEnabled()) {
			log.debug("bufferA = " + Long.toHexString(bufferA));
			log.debug("size = " + size);
			log.debug("realSize = " + realSize);
			machine.showMemory(log, bufferA, (int) size);
			log.debug("string in bufferA is: "
					+ machine.getStringByNull(bufferA));
		}
	}

	private void fRead() {
		long paramAddr = machine.getSpecialRegister(BB);
		long bufferA = machine.getSignedOcta(paramAddr);
		long size = machine.getSignedOcta(paramAddr + 8);

		int realSize = 0;
		int count = 0;
		byte[] buffer;
		if (z == StdIn) {
			try {
				while (size > count) {
					buffer = new byte[(int) size - count];
					realSize = System.in.read(buffer);

					if (realSize != -1) {
						machine.setByteArray(bufferA + count, buffer, 0,
								realSize);
						count += realSize;
						if (count == size) {
							this.systemCallOk();
							break;
						}
					} else {
						if (count == 0) {
							this.systemCallFail(-1 - size);
						} else {
							this.systemCallOk(count - size);
						}

					}
				}
			} catch (IOException e) {
				this.systemCallFail(-1 - size);
				log.debug(e.getMessage());
			}
		} else {
			FileInputStream inS = (FileInputStream) inStream[z];
			buffer = new byte[(int) size];
			try {
				realSize = inS.read(buffer);
				if (realSize != -1) {
					machine.setByteArray(bufferA, buffer, 0, realSize);
					machine.setByte(bufferA + realSize, 0);
					this.systemCallOk(realSize - size);
//					if(log.isInfoEnabled()){
//						log.info("read in " + new String(buffer));
//					}
				} else {
					this.systemCallFail(-1 - size);
				}

			} catch (IOException e) {
				if (log.isInfoEnabled()) {
					log.info(e.getMessage());
				}
				this.systemCallFail();
			}
		}
		if (log.isDebugEnabled()) {
			log.debug("bufferA = " + Long.toHexString(bufferA));
			log.debug("size = " + size);
			log.debug("realSize = " + realSize);
			machine.showMemory(log, bufferA, (int) size);

		}
	}

	private void fOpen() {
		machine.showMemory(log, Machine.POOL_SEGMENT);
		long paramAddr = machine.getSpecialRegister(BB);
		// bug 114
		// String temp = machine.getStringByNull(paramAddr);
		long strAddr = machine.getSignedOcta(paramAddr);
		String temp = machine.getStringByNull(strAddr);
		long mode = machine.getSignedOcta(paramAddr + 8);
		if (log.isInfoEnabled()) {
			log.info("fOpen: paramAddr = 0x" + Long.toHexString(paramAddr));
			log.info("file name is " + temp);
		}
		fileName[z] = temp;
		if (mode == TrapConstant.BinaryRead || mode == TrapConstant.TextRead) {
			try {
				inStream[z] = new java.io.FileInputStream(new File(temp));
				systemCallOk();
			} catch (FileNotFoundException e) {
				machine.setGlobalRegister(255, -1L);
				log.debug(e.getMessage());
			}
		} else {
			try {
				outStream[z] = new java.io.FileOutputStream(new File(temp));
				systemCallOk();
			} catch (FileNotFoundException e) {
				machine.setGlobalRegister(255, -1L);
				log.debug(e.getMessage());
			}
		}

	}

	private void systemCallOk() {
		systemCallOk(0L);
	}

	private void systemCallOk(long value) {
		machine.setGlobalRegister(255, value);
	}

	private void systemCallFail(long errorNo) {
		machine.setGlobalRegister(255, errorNo);
	}

	private void systemCallFail() {
		machine.setGlobalRegister(255, -1);
	}

	@Override
	protected void timing() {
		machine.comulativeV += 5;
	}

}
