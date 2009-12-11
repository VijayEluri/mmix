package mmix;

import static mmix.cons.InstructionConstant.*;
import static mmix.cons.SpecialRegisterConstant.*;

public class SystemInstructionExecutor extends InstructionExecutor {
	// byte[] tt = new byte[8];

	@Override
	public void execute() {
		switch (opCode) {
		case LDUNC:
			//super.setAddressNormal();
		case LDUNCI:
			super.setAddress();

			machine.setGeneralRegister(x, machine.getSignedOcta(virtualAddress));
			break;
		case STUNC:
			//super.setAddressNormal();
		case STUNCI:
			super.setAddress();
			machine.setOcta(virtualAddress, machine.getGeneralRegister(x));
			break;
		case PRELD:
			break;
		case PREST:
			break;
		case PREGO:
			break;
		case SYNCID:
			break;
		case SYNCD:
			break;
		case SYNC:
			break;
		case CSWAP:

			long tmp = machine.getSignedOcta(virtualAddress);
			if (tmp == machine.getSpecialRegister(P)) {
				machine.setOcta(virtualAddress, machine.getGeneralRegister(x));
				machine.setGeneralRegister(x, 1);
			} else {
				machine.setSpecialRegister(P,tmp);
				machine.setGeneralRegister(x, 0);
			}
			break;
		case LDVTS:
			break;
		default:
			throw new RuntimeException("MMIX: invalid OpCode (" + opCode + ") for "
					+ this.getClass().getCanonicalName());
		}

	}

	@Override
	protected void timing() {
		// TODO Auto-generated method stub

	}

	// private void initializeByteArray() {
	// int i = address % 8;
	//
	// tt[0] = machine.memory[address - i];
	// tt[1] = machine.memory[address - i + 1];
	// tt[2] = machine.memory[address - i + 2];
	// tt[3] = machine.memory[address - i + 3];
	// tt[4] = machine.memory[address - i + 4];
	// tt[5] = machine.memory[address - i + 5];
	// tt[6] = machine.memory[address - i + 6];
	// tt[7] = machine.memory[address - i + 7];
	// }
}
