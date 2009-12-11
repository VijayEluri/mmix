package mmix;

import static mmix.cons.InstructionConstant.*;

/**
 * 
 * <p>
 * WydeImmediateInstructionExecutor.java
 * </p>
 * 
 * 
 * @author Eddie Wu
 * @version 1.0
 * 
 */
public class WydeImmediateInstructionExecutor extends InstructionExecutor {
	// bug 8: int wyde_YZ = 0;
	long wyde_YZ = 0;

	protected void setUnsignedWyde_YZ() {
		wyde_YZ = NumberUtil.getUnsignedWyde(y, z);
	}

	@Override
	public void execute() {
		setUnsignedWyde_YZ();
		switch (opCode) {
		case SETH:
			machine.setGeneralRegister(x, wyde_YZ << 48);

			break;
		case SETMH:
			machine.setGeneralRegister(x, wyde_YZ << 32);
			break;
		case SETML:
			machine.setGeneralRegister(x, wyde_YZ << 16);
			break;
		case SETL:
			machine.setGeneralRegister(x, wyde_YZ);
			break;

		// ==
		case INCH:
			machine.setGeneralRegister(x,machine.getGeneralRegister(x) + (wyde_YZ << 48));

			break;
		case INCMH:
			machine.setGeneralRegister(x,machine.getGeneralRegister(x) + (wyde_YZ << 32));
			break;
		case INCML:
			machine.setGeneralRegister(x,machine.getGeneralRegister(x) + (wyde_YZ << 16));
			break;
		case INCL:
			machine.setGeneralRegister(x,machine.getGeneralRegister(x) + wyde_YZ);
			break;
		// ==
		case ORH:
			machine.setGeneralRegister(x,machine.getGeneralRegister(x) | (wyde_YZ << 48));

			break;
		case ORMH:
			machine.setGeneralRegister(x,machine.getGeneralRegister(x) | (wyde_YZ << 32));
			break;
		case ORML:
			machine.setGeneralRegister(x,machine.getGeneralRegister(x) | (wyde_YZ << 16));
			break;
		case ORL:
			machine.setGeneralRegister(x,machine.getGeneralRegister(x) | wyde_YZ);
			break;

		case ANDNH:
			machine.setGeneralRegister(x,machine.getGeneralRegister(x) & ~(wyde_YZ << 48));

			break;
		case ANDNMH:
			machine.setGeneralRegister(x,machine.getGeneralRegister(x) & ~(wyde_YZ << 32));
			break;
		case ANDNML:
			machine.setGeneralRegister(x,machine.getGeneralRegister(x) & ~(wyde_YZ << 16));
			break;
		case ANDNL:
			machine.setGeneralRegister(x, ~wyde_YZ);
			break;
		default:
			throw new RuntimeException("MMIX: invalid OpCode (" + opCode + ") for "
					+ this.getClass().getCanonicalName());
		}

	}

	@Override
	protected void timing() {
		machine.comulativeV++;
	}

}
