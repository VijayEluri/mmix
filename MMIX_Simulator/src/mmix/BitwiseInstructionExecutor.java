package mmix;

import static mmix.cons.InstructionConstant.AND;
import static mmix.cons.InstructionConstant.ANDI;
import static mmix.cons.InstructionConstant.ANDN;
import static mmix.cons.InstructionConstant.ANDNI;
import static mmix.cons.InstructionConstant.MUX;
import static mmix.cons.InstructionConstant.MUXI;
import static mmix.cons.InstructionConstant.NAND;
import static mmix.cons.InstructionConstant.NANDI;
import static mmix.cons.InstructionConstant.NOR;
import static mmix.cons.InstructionConstant.NORI;
import static mmix.cons.InstructionConstant.NXOR;
import static mmix.cons.InstructionConstant.NXORI;
import static mmix.cons.InstructionConstant.OR;
import static mmix.cons.InstructionConstant.ORI;
import static mmix.cons.InstructionConstant.ORN;
import static mmix.cons.InstructionConstant.ORNI;
import static mmix.cons.InstructionConstant.SADD;
import static mmix.cons.InstructionConstant.SADDI;
import static mmix.cons.InstructionConstant.XOR;
import static mmix.cons.InstructionConstant.XORI;
import static mmix.cons.SpecialRegisterConstant.M;

import java.math.BigInteger;

/**
 * Bitwise operations - see page 10 Bitwise Instruction Executor.
 * 
 * 
 * @author Eddie Wu
 * @version 1.0
 * 
 */
public class BitwiseInstructionExecutor extends InstructionExecutor {

	@Override
	public void execute() {
		setOperandYZ();
		switch (opCode) {

		case AND:
		case ANDI:
			machine.setGeneralRegister(x, valueY & valueZ);
			break;
		case OR:
		case ORI:
			machine.setGeneralRegister(x, valueY | valueZ);
			break;
		case XOR:
		case XORI:
			machine.setGeneralRegister(x, valueY ^ valueZ);
			break;
		case ANDN:
		case ANDNI:
			machine.setGeneralRegister(x, valueY & (~valueZ));
			break;

		case ORN:
		case ORNI:
			machine.setGeneralRegister(x, valueY | (~valueZ));
			break;
		// ==
		case NAND:
		case NANDI:
			machine.setGeneralRegister(x, ~(valueY & valueZ));
			break;
		case NOR:
		case NORI:
			machine.setGeneralRegister(x, ~(valueY | valueZ));
			break;
		case NXOR:
		case NXORI:
			machine.setGeneralRegister(x, ~(valueY ^ valueZ));
			break;
		// ==
		case MUX:
		case MUXI:
			machine.setGeneralRegister(x, (valueY & machine.getSpecialRegister(M))
					| (valueZ & (~machine.getSpecialRegister(M))));
			break;
		case SADD:
		case SADDI:
			long temp = valueY & (~valueZ);
			int count = BigInteger.valueOf(temp).bitCount();
			//bug 119 == is not included before unit test.
			if (temp >= 0) {
				machine.setGeneralRegister(x, count);
			} else {
				machine.setGeneralRegister(x, 64 - count);
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
