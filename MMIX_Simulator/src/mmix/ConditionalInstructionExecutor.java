package mmix;

import static mmix.cons.InstructionConstant.*;

/**
 * 
 * @author wueddie-wym-wrz
 * 
 */
public class ConditionalInstructionExecutor extends InstructionExecutor {

	public static int u = 0; // memory access measure

	public static int v = 1; // oops

	@Override
	public void execute() {
		switch (opCode) {
		case CSN:
			if (machine.getGeneralRegister(y) < 0) {
				machine.setGeneralRegister(x, machine.getGeneralRegister(z));
			}
			break;
		case CSNI:
			if (machine.getGeneralRegister(y) < 0) {
				machine.setGeneralRegister(x, z);
			}
			break;
		case CSZ:
			if (machine.getGeneralRegister(y) == 0) {
				machine.setGeneralRegister(x, machine.getGeneralRegister(z));
			}
			break;
		case CSZI:
			if (machine.getGeneralRegister(y) == 0) {
				machine.setGeneralRegister(x, z);
			}
			break;

		case CSP:
			if (machine.getGeneralRegister(y) > 0) {
				machine.setGeneralRegister(x, machine.getGeneralRegister(z));
			}
			break;
		case CSPI:
			if (machine.getGeneralRegister(y) > 0) {
				machine.setGeneralRegister(x, z);
			}
			break;
		case CSOD:
			if (machine.getGeneralRegister(y) % 2 != 0) {
				machine.setGeneralRegister(x, machine.getGeneralRegister(z));
			}
			break;
		case CSODI:
			if (machine.getGeneralRegister(y) % 2 != 0) {
				machine.setGeneralRegister(x, z);
			}
			break;

		case CSNN:
			if (machine.getGeneralRegister(y) >= 0) {
				machine.setGeneralRegister(x, machine.getGeneralRegister(z));
			}
			break;
		case CSNNI:
			if (machine.getGeneralRegister(y) >= 0) {
				machine.setGeneralRegister(x, z);
			}
			break;
		case CSNZ:
			if (machine.getGeneralRegister(y) != 0) {
				machine.setGeneralRegister(x, machine.getGeneralRegister(z));
			}
			break;
		case CSNZI:
			if (machine.getGeneralRegister(y) != 0) {
				machine.setGeneralRegister(x, z);
			}
			break;

		case CSNP:
			if (machine.getGeneralRegister(y) <= 0) {
				machine.setGeneralRegister(x, machine.getGeneralRegister(z));
			}
			break;
		case CSNPI:
			if (machine.getGeneralRegister(y) <= 0) {
				machine.setGeneralRegister(x, z);
			}
			break;
		case CSEV:
			if (machine.getGeneralRegister(y) % 2 == 0) {
				machine.setGeneralRegister(x, machine.getGeneralRegister(z));
			}
			break;
		case CSEVI:
			if (machine.getGeneralRegister(y) % 2 == 0) {
				machine.setGeneralRegister(x, z);
			}
			break;

		/**
		 * ZERO OR Conditional Set
		 */
		case ZSN:
			if (machine.getGeneralRegister(y) < 0) {
				machine.setGeneralRegister(x, machine.getGeneralRegister(z));
			} else {
				machine.setGeneralRegister(x, 0);
			}
			break;
		case ZSNI:
			if (machine.getGeneralRegister(y) < 0) {
				machine.setGeneralRegister(x, z);
			} else {
				machine.setGeneralRegister(x, 0);
			}
			break;
		case ZSZ:
			if (machine.getGeneralRegister(y) == 0) {
				machine.setGeneralRegister(x, machine.getGeneralRegister(z));
			} else {
				machine.setGeneralRegister(x, 0);
			}
			break;
		case ZSZI:
			if (machine.getGeneralRegister(y) == 0) {
				machine.setGeneralRegister(x, z);
			} else {
				machine.setGeneralRegister(x, 0);
			}
			break;

		case ZSP:
			if (machine.getGeneralRegister(y) > 0) {
				machine.setGeneralRegister(x, machine.getGeneralRegister(z));
			} else {
				machine.setGeneralRegister(x, 0);
			}
			break;
		case ZSPI:
			if (machine.getGeneralRegister(y) > 0) {
				machine.setGeneralRegister(x, z);
			} else {
				machine.setGeneralRegister(x, 0);
			}
			break;
		case ZSOD:
			if (machine.getGeneralRegister(y) % 2 != 0) {
				machine.setGeneralRegister(x, machine.getGeneralRegister(z));
			} else {
				machine.setGeneralRegister(x, 0);
			}
			break;
		case ZSODI:
			if (machine.getGeneralRegister(y) % 2 != 0) {
				machine.setGeneralRegister(x, z);
			} else {
				machine.setGeneralRegister(x, 0);
			}
			break;

		case ZSNN:
			if (machine.getGeneralRegister(y) >= 0) {
				machine.setGeneralRegister(x, machine.getGeneralRegister(z));
			} else {
				machine.setGeneralRegister(x, 0);
			}
			break;
		case ZSNNI:
			if (machine.getGeneralRegister(y) >= 0) {
				machine.setGeneralRegister(x, z);
			} else {
				machine.setGeneralRegister(x, 0);
			}
			break;
		case ZSNZ:
			if (machine.getGeneralRegister(y) != 0) {
				machine.setGeneralRegister(x, machine.getGeneralRegister(z));
			} else {
				machine.setGeneralRegister(x, 0);
			}
			break;
		case ZSNZI:
			if (machine.getGeneralRegister(y) != 0) {
				machine.setGeneralRegister(x, z);
			} else {
				machine.setGeneralRegister(x, 0);
			}
			break;

		case ZSNP:
			if (machine.getGeneralRegister(y) <= 0) {
				machine.setGeneralRegister(x, machine.getGeneralRegister(z));
			} else {
				machine.setGeneralRegister(x, 0);
			}
			break;
		case ZSNPI:
			if (machine.getGeneralRegister(y) <= 0) {
				machine.setGeneralRegister(x, z);
			} else {
				machine.setGeneralRegister(x, 0);
			}
			break;
		case ZSEV:
			if (machine.getGeneralRegister(y) % 2 == 0) {
				machine.setGeneralRegister(x, machine.getGeneralRegister(z));
			} else {
				machine.setGeneralRegister(x, 0);
			}
			break;
		case ZSEVI:
			if (machine.getGeneralRegister(y) % 2 == 0) {
				machine.setGeneralRegister(x, z);
			} else {
				machine.setGeneralRegister(x, 0);
			}
			break;
		default:
			throw new RuntimeException("MMIX: invalid OpCode (" + opCode + ") for "
					+ this.getClass().getCanonicalName());
		}
	}

	@Override
	protected void timing() {
		machine.comulativeV += 1;
	}
}
