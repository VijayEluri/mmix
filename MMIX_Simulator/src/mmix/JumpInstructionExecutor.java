package mmix;

import static mmix.cons.InstructionConstant.GO;
import static mmix.cons.InstructionConstant.GOI;
import static mmix.cons.InstructionConstant.JMP;
import static mmix.cons.InstructionConstant.JMPB;

import org.apache.log4j.Logger;

/**
 * jumps and branches see page 15
 * 
 * for jumps instruction only.
 * 
 * 
 * @author Eddie Wu
 * @version 1.0
 * 
 */
public class JumpInstructionExecutor extends InstructionExecutor {
	Logger log = Logger.getLogger(this.getClass());

	@Override
	public void execute() {
		switch (opCode) {
		//bug
//		case JMP:
//			super.setThreeByteRA_Forward();
//		case JMPB:
//			super.setThreeByteRA_Backward();
//			if (log.isInfoEnabled()) {
//				log.debug("jump to 0x" + Long.toHexString(RA));
//			}
//			machine.virtualAt  = RA;
//			break;
		

		case JMP:
		case JMPB:
			setThreeByteRA();
			if (log.isInfoEnabled()) {
				log.debug("jump to 0x" + Long.toHexString(RA));
			}
			machine.virtualAt  = RA;
			break;
		
//			super.setAddressNormal();
//			machine.setGeneralRegister(x, machine.virtualAt  + 4);
//			log.debug("jump to" + Long.toHexString(virtualaddress));
//			machine.virtualAt  = virtualaddress;
//			break;
		case GO:
		case GOI:
			super.setAddress();
			machine.setGeneralRegister(x, machine.virtualAt  + 4);
			log.debug("jump to" + Long.toHexString(virtualAddress));
			machine.virtualAt  = virtualAddress;
			break;
		default:
			throw new RuntimeException("MMIX: invalid OpCode (" + opCode + ") for "
					+ this.getClass().getCanonicalName());
		}

	}
	
	protected void setThreeByteRA() {
		if(super.isForwardBranch()){
			super.setThreeByteRA_Forward();
		}else{
			super.setThreeByteRA_Backward();
		}
	}

	@Override
	protected void timing() {
		if (opCode == JMP || opCode == JMPB) {
			machine.comulativeV += 1;
		} else if (opCode == GO || opCode == GOI) {
			machine.comulativeV += 3;
		}

	}

}
