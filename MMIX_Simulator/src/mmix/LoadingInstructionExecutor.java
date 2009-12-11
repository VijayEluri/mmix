package mmix;

import static mmix.cons.InstructionConstant.*;

public class LoadingInstructionExecutor extends InstructionExecutor {

	// BigDecimal absoluteAddress;
	/**
	 * though the range of long is not as big as range [0, 2 power 64). it is
	 * reasonable to use long since we can only simulate less then 2G
	 * machineState.memory
	 */
	// protected int address;
	/**
	 * the simulator can only afford about at most 1 G machineState.memory so
	 * the 64 bit value in the register is not fully used when addressing.
	 */
	// protected void setAddress() {
	// address = (int) (machine.generalRegister[y] +
	// machine.generalRegister[z]);
	// if (address >= machine.memory.length
	// || machine.memory.length < 0) {
	// throw new
	// RuntimeException("MMIX: address is out of machineState.memory!");
	// }
	// }
	@Override
	public void execute() {

		if (super.isImmediateInstruction()) {
			setAddressImmediate();
		} else {
			setAddressNormal();
		}

		switch (opCode) {
		case LDB:
		case LDBI:
			machine
					.setGeneralRegister(x, machine
							.getSignedByte(virtualAddress));
			break;
		case LDBU:
		case LDBUI:
			machine.setGeneralRegister(x, machine
					.getUnsignedByte(virtualAddress));
			break;

		case LDW:
		case LDWI:

			machine
					.setGeneralRegister(x, machine
							.getSignedWyde(virtualAddress));
			break;
		case LDWU:
		case LDWUI:

			machine.setGeneralRegister(x, machine
					.getUnsignedWyde(virtualAddress));

			break;

		case LDT:
		case LDTI:

			// signed extension is implicitly happened when assign int to long.
			// the difference between signed and unsigned are shown when convert
			// from tetra
			// to octa.
			machine.setGeneralRegister(x, machine
					.getSignedTetra(virtualAddress));
			break;
		case LDTU:
		case LDTUI:

			machine.setGeneralRegister(x, machine
					.getUnsignedTetra(virtualAddress));
			break;
		case LDO:
		case LDOI:

			// machine.setGeneralRegister(x, machine.getSignedOcta(address));
			machine
					.setGeneralRegister(x, machine
							.getSignedOcta(virtualAddress));
			break;
		case LDOU:
		case LDOUI:
			// can only be signed number for octa.
			// machine.setGeneralRegister(x, machine.getSignedOcta(address));
			machine
					.setGeneralRegister(x, machine
							.getSignedOcta(virtualAddress));
			break;
		case LDHT:
		case LDHTI:
			machine.setGeneralRegister(x, machine
					.getUnsignedTetra(virtualAddress) << 32);
			break;
		default:
			throw new RuntimeException("MMIX: invalid OpCode (" + opCode
					+ ") for " + this.getClass().getCanonicalName());
		}

	}

	@Override
	protected void timing() {
		machine.comulativeU += 1;
		machine.comulativeV += 1;
	}
}
