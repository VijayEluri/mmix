package mmix;

import static mmix.cons.InstructionConstant.*;
import static mmix.cons.SpecialRegisterConstant.*;

import org.apache.log4j.Logger;

/**
 * Subroutine calls - see page 16
 * 
 * 
 * @author eddie.wu
 * 
 */
public class SubroutineInstructionExecutor extends InstructionExecutor {
	Logger log = Logger.getLogger(this.getClass());
	int specialZ;

	int valueZ;

	@Override
	public void execute() {

		// setAddress();
		switch (opCode) {
		case PUSHJ:
			// setRA_Forward();
		case PUSHJB:
			machine.showInternalStatus(log);
			setRA();
			machine.push(x);
			machine.setSpecialRegister(J,machine.virtualAt + 4);// next ins.
			machine.virtualAt = RA;
			machine.showInternalStatus(log);
			break;

		case PUSHGO:
			// setAddressNormal();
		case PUSHGOI:
			setAddress();
			machine.push(x);
			machine.setSpecialRegister(J,machine.virtualAt + 4);// next ins.
			machine.virtualAt = virtualAddress;
			break;
		case POP:
			machine.showInternalStatus(log);

			machine.pop(x);
			machine.virtualAt = (int) (machine.getSpecialRegister(J) + 4 * NumberUtil
					.getUnsignedWyde(y, z));
			machine.showInternalStatus(log);
			break;
		case UNSAVE:
			break;
		case SAVE:
			save();
			break;
		default:
			throw new RuntimeException("MMIX: invalid OpCode (" + opCode
					+ ") for " + this.getClass().getCanonicalName());
		}

	}

	private void save() {
		if (x < machine.getSpecialRegister(G)) {
			throw new RuntimeException("MMIX: x < rG when save");
		}
		machine.save( x);
		

	}

	@Override
	protected void timing() {
		// TODO Auto-generated method stub

	}

}
