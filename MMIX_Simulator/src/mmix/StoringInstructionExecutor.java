package mmix;

import static mmix.cons.ArithmeticExceptionConstant.*;
import static mmix.cons.InstructionConstant.*;

public class StoringInstructionExecutor extends InstructionExecutor {

	public static int u = 1; // machineState.memory access measure

	public static int v = 1; // oops
	protected static long BYTE_MASK = 0X00000000000000ffL;

	protected static long WYDE_MASK = 0X000000000000ffffL;

	protected static long TETRA_MASK = 0X00000000ffffffffL;

	protected static long HIGH_TETRA_MASK = 0Xffffffff00000000L;

	@Override
	public void execute() {
		// BigDecimal tmpY = new BigDecimal(machineState.genericRegister[y]);
		// BigDecimal tmpZ = new BigDecimal (machineState.genericRegister[z]);
		// absoluteAddress = tmpY.abs().add(tmpZ.abs());

//		int temp = 0;
//		int i = 0;
		if(this.isImmediateInstruction()){
			setAddressImmediate();
		}else{
			setAddressNormal();
		}
		
//		if (opCode % 2 == 0) {
//			setAddressNormal();
//		} else {
//			setAddressImmediate();
//		}
		switch (opCode) {
		case STBU:
		case STBUI:
			storeByte();
			break;
		case STB:
		case STBI:
			if (machine.getGeneralRegister(x) > 127
					|| machine.getGeneralRegister(x) < -128) {
				machine.setArithmeticException(V_BIT_SHIFT);// bug 12
				// D_BIT_SHIFT
			}
			storeByte();
			break;
		case STWU:
		case STWUI:
			storeWyde();
			break;// bug 11
		case STW:
		case STWI:
			storeWyde();
			if (machine.getGeneralRegister(x) > 32767
					|| machine.getGeneralRegister(x) < -32768) {
				machine.setArithmeticException(V_BIT_SHIFT);// bug 13
				// D_BIT_SHIFT
			}
			break;
		case STTU:
		case STTUI:
			storeTetra();
			break;
		case STT:
		case STTI:
			storeTetra();
			if (machine.getGeneralRegister(x) > 0x7fffffff
					|| machine.getGeneralRegister(x) < 0x80000000) {
				machine.setArithmeticException(V_BIT_SHIFT);// bug 13
				// D_BIT_SHIFT
			}
			break;
		case STOU:
		case STOUI:
			storeOcta();
			break;
		case STO:
		case STOI:
			storeOcta();
			break;
		case STCO:
		case STCOI:
			machine.setByte(virtualAddress, (byte) x);
			break;
		case STHT:
		case STHTI:
			storeHighTetra();
			break;
		default:
			throw new RuntimeException("MMIX: invalid OpCode (" + opCode
					+ ") for " + this.getClass().getCanonicalName());
		}
		// machine.comulativeU += 1;
		// machine.comulativeV += 1;
	}

	private void storeHighTetra() {
		machine.setTetra(virtualAddress, (int) (machine.getGeneralRegister(x) >>> 32));
	}

	private void storeOcta() {

		machine.setOcta(virtualAddress, machine.getGeneralRegister(x));
	}

	private void storeTetra() {
		int temp;
		temp = (int) (machine.getGeneralRegister(x) & TETRA_MASK);
		machine.setTetra(virtualAddress, temp);
	}

	private void storeWyde() {
		int temp;
		/**
		 * we only need to store the wyde. so truncate it to int
		 */
		temp = (int) (machine.getGeneralRegister(x) & WYDE_MASK);
		// if (address % 2 == 0) {
		// machine.memory[address] = (byte) (temp >> 8 & 0x00000011);
		// machine.memory[address + 1] = (byte) (temp & 0x00000011);
		// } else {
		// machine.memory[address - 1] = (byte) (temp >> 8 & 0x00000011);
		// machine.memory[address] = (byte) (temp & 0x00000011);
		// }
		machine.setWyde(virtualAddress, temp);
	}

	private void storeByte() {
		machine.setByte(virtualAddress,
				(byte) (machine.getGeneralRegister(x) & BYTE_MASK));
	}

	@Override
	protected void timing() {
		machine.comulativeU += 1;
		machine.comulativeV += 1;
	}
}
