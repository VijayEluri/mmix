package mmix;

import static mmix.cons.InstructionConstant.*;
import static mmix.cons.SpecialRegisterConstant.C;

import org.apache.log4j.Logger;

/**
 * jumps and branches see page 15
 * 
 * for branches instruction only.
 * 
 * @author Eddie Wu
 * @version 1.0
 * 
 */
public class BranchInstructionExecutor extends InstructionExecutor {
	Logger log = Logger.getLogger(this.getClass());
	long valueX;

	boolean branchTaken = false;

	@Override
	public void execute() {
		branchTaken = false;
		valueX = machine.getGeneralRegister(x);
		//if (opCode % 2 == 0) {
		if(super.isForwardBranch()){
			this.setRA_Forward();
		} else {
			this.setRA_Backward();
		}
		switch (opCode) {
		case BN:
		case BNB:
			if (valueX < 0) {
				machine.virtualAt = RA;
				branchTaken = true;
			}
			break;
		case BZ:
		case BZB:
			if (valueX == 0) {
				machine.virtualAt = RA;
				branchTaken = true;
			}
			break;

		case BP:
		case BPB:
			if (valueX > 0) {
				machine.virtualAt = RA;
				branchTaken = true;
			}
			break;
		case BOD:
		case BODB:
			if (valueX % 2 != 0) {
				machine.virtualAt = RA;
				branchTaken = true;
			}
			break;
		// not
		case BNN:
		case BNNB:
			if (valueX >= 0) {
				machine.virtualAt = RA;
				branchTaken = true;
			}
			break;
		case BNZ:
		case BNZB:
			if (valueX != 0) {
				machine.virtualAt = RA;
				branchTaken = true;
			}
			break;

		case BNP:
		case BNPB:
			if (valueX <= 0) {
				machine.virtualAt = RA;
				branchTaken = true;
			}
			break;
		case BEV:
		case BEVB:
			if (valueX % 2 == 0) {
				machine.virtualAt = RA;
				branchTaken = true;
			}
			break;
		// probably the U and V will be different . so keep duplicate code.
		case PBN:
		case PBNB:
			if (valueX < 0) {
				machine.virtualAt = RA;
				branchTaken = true;
			}
			break;
		case PBZ:
		case PBZB:
			if (valueX == 0) {
				machine.virtualAt = RA;
				branchTaken = true;
			}
			break;

		case PBP:
		case PBPB:
			if (valueX > 0) {
				machine.virtualAt = RA;
				branchTaken = true;
			}
			break;
		case PBOD:
		case PBODB:
			if (valueX % 2 != 0) {
				machine.virtualAt = RA;
				branchTaken = true;
			}
			break;
		// not
		case PBNN:
		case PBNNB:
			if (valueX >= 0) {
				machine.virtualAt = RA;
				branchTaken = true;
			}
			break;
		case PBNZ:
		case PBNZB:
			if (valueX != 0) {
				machine.virtualAt = RA;
				branchTaken = true;
			}
			break;

		case PBNP:
		case PBNPB:
			if (valueX <= 0) {
				machine.virtualAt = RA;
				branchTaken = true;
			}
			break;
		case PBEV:
		case PBEVB:
			if (valueX % 2 == 0) {
				machine.virtualAt = RA;
				branchTaken = true;
			}
			break;
		default:
			throw new RuntimeException("MMIX: invalid OpCode (" + opCode
					+ ") for " + this.getClass().getCanonicalName());
		}
	}

	

	@Override
	protected void timing() {
		machine.countBranch++;
		if (opCode >= BN && opCode <= BEVB) {
			if (branchTaken) {
				machine.comulativeV += 3;
				machine.increaseSpecialRegister(C, 3);
			} else {
				machine.countCorrectPrediction++;
				machine.comulativeV += 1;
				machine.increaseSpecialRegister(C, 1);
			}
		} else if (opCode >= PBN && opCode <= PBEVB) {
			if (branchTaken) {
				machine.countCorrectPrediction++;
				machine.comulativeV += 1;
				machine.increaseSpecialRegister(C, 1);
			} else {
				machine.comulativeV += 3;
				machine.increaseSpecialRegister(C, 3);
			}
		} else {
			throw new RuntimeException("MMIX: invalid opCode");
		}
	}

	
}
