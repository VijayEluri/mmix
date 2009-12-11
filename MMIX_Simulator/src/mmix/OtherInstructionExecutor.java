package mmix;

import static mmix.cons.InstructionConstant.GET;
import static mmix.cons.InstructionConstant.GETA;
import static mmix.cons.InstructionConstant.GETAB;
import static mmix.cons.InstructionConstant.PUT;
import static mmix.cons.InstructionConstant.SWYM;
import static mmix.cons.SpecialRegisterConstant.A;
import static mmix.cons.SpecialRegisterConstant.G;
import static mmix.cons.SpecialRegisterConstant.L;

/**
 * <p>
 * OtherInstructionExecutor.java
 * </p>
 * 
 * 
 * @author Eddie Wu
 * @version 1.0
 * 
 */
public class OtherInstructionExecutor extends InstructionExecutor {
	// int specialZ;

	@Override
	public void execute() {
		switch (opCode) {
		case SWYM:
			break;
		case GETA:
			setRA_Forward();
			machine.setGeneralRegister(x, RA);
			break;
		case GETAB:
			setRA_Backward();
			machine.setGeneralRegister(x, RA);
			break;
		case GET:
			if (y != 0 || z < 0 || z >= 32) {
				throw new RuntimeException("MMIX: specialZ<0 || specialZ>32");
			} else {
				machine.setGeneralRegister(x, machine.getSpecialRegister(z));
			}
			break;
		case PUT:
			super.setOperandYZ();
			// if (z < 0 || z > 32) { --bug 1254
			if (x < 0 || x >= 32) {
				throw new RuntimeException("MMIX: specialZ<0 || specialZ>32");
			}
			if (x >= 8 && z <= 11) {
				// can not put into
			} else if (x >= 12 && z <= 18) {
				// TODO
				// can not put into
			} else if (x == L) {
				if (valueZ > 255 || valueZ < 32
						|| valueZ > machine.getSpecialRegister(G)) {
					// can not change rG.
				} else {
					// can not increase rL.
					machine.setSpecialRegister(L, Math.min(machine
							.getSpecialRegister(L), valueZ));
				}
			} else if (x == A) {
				if (valueZ > 0x3ffff) {
					// can not change rA.
				} else {
					machine.setSpecialRegister(A, valueZ);
				}
			} else if (x == G) {
				if (valueZ > 255 || valueZ < 32
						|| valueZ < machine.getSpecialRegister(L)) {
					// can not change rG.
				} else {
					machine.setSpecialRegister(G, valueZ);
				}
			} else {
				machine.setSpecialRegister(x, valueZ);
			}
			break;
		default:
			throw new RuntimeException("MMIX: invalid OpCode (" + opCode
					+ ") for " + this.getClass().getCanonicalName());
		}

	}

	@Override
	protected void timing() {
		machine.comulativeV += 1;
	}

}
