package mmix;

import static mmix.cons.InstructionConstant.*;
import static mmix.cons.SpecialRegisterConstant.*;

/**
 * FloatPointInstructionExecutor
 * 
 * 
 * @author Eddie Wu
 * @version 1.0
 * 
 */
public class FloatPointInstructionExecutor extends InstructionExecutor {
	Double Dx;
	Double Dy;
	Double Dz;
	Double Ulps;
	double dx;
	double dy;
	double dz;
	double ulps;

	@Override
	public void execute() {
		super.setOperandYZ();
		Dy = Double.longBitsToDouble(valueY);// all NaN will be compressed to
		// same representation.
		Dz = Double.longBitsToDouble(valueZ);
		dy = Dy.doubleValue();
		dz = Dz.doubleValue();
		
		switch (opCode) {
		case FADD:
			if(Dy.isNaN() || Dz.isNaN()){
				standardconvention();		
			}
			fAdd();
			break;
		case FSUB:
			if(Dy.isNaN() || Dz.isNaN()){
				standardconvention();		
			}
			fSub();
			break;
		case FMUL:
			if(Dy.isNaN() || Dz.isNaN()){
				standardconvention();		
			}
			fMul();
			break;
		case FDIV:
			if(Dy.isNaN() || Dz.isNaN()){
				standardconvention();		
			}
			fDiv();
			break;
		case FREM:
			if(Dy.isNaN() || Dz.isNaN()){
				standardconvention();		
			}
			fRem();
			break;
		case FSQRT:
			if(Dy.isNaN() || Dz.isNaN()){
				standardconvention();		
			}
			fSqrt();
			break;
		case FINT:
			if(Dy.isNaN() || Dz.isNaN()){
				standardconvention();		
			}
			machine.setGeneralRegister(x, Dz.longValue());
			break;
		case FCMP:
			machine.setGeneralRegister(x, Dy.compareTo(Dz));
			break;
		case FEQL:
			machine.setGeneralRegister(x, Dy.equals(Dz) ? 1 : 0);
			break;
		case FCMPE:
			Ulps = Double.longBitsToDouble(machine.getSpecialRegister(E));
			ulps = Ulps.doubleValue();
			machine.setGeneralRegister(x, Dy.compareTo(Double
					.valueOf(dz + ulps)));
			break;
		case FEQLE:
			Ulps = Double.longBitsToDouble(machine.getSpecialRegister(E));
			ulps = Ulps.doubleValue();
			machine.setGeneralRegister(x,
					Dy.equals(Double.valueOf(dz + ulps)) ? 1 : 0);
			break;
		case FUNE:
			break;
		case FIX:
			machine.setGeneralRegister(x, Dz.intValue());
			break;
		case FIXU:
			break;
		case FLOT:// since float point register is also used as integer register
			// no need to use seperate Float loading instruction.
			break;
		case FLOTU:
			break;
		case SFLOT:
			break;
		case SFLOTU:
			break;
		case LDSF:
		case LDSFI:
			break;
		case STSF:
		case STSFI:
			break;
		default:
			throw new RuntimeException("MMIX: invalid OpCode (" + opCode
					+ ") for " + this.getClass().getCanonicalName());
		}

	}



	private void fSqrt() {
		dx = Math.sqrt(dz);
		machine.setGeneralRegister(x, Double.doubleToRawLongBits(dx));
	}



	private void fRem() {
		dx = dy % dz;
		machine.setGeneralRegister(x, Double.doubleToRawLongBits(dx));
	}



	private void fDiv() {
		dx = dy / dz;
		machine.setGeneralRegister(x, Double.doubleToRawLongBits(dx));
	}



	private void fMul() {
		dx = dy * dz;
		machine.setGeneralRegister(x, Double.doubleToRawLongBits(dx));
	}



	private void fSub() {
		dx = dy - dz;
		machine.setGeneralRegister(x, Double.doubleToRawLongBits(dx));
	}

	
	
	private void standardconvention() {
//		if(){
//			
//		}
		
	}

	private void fAdd() {
		if (dy == Double.POSITIVE_INFINITY && dz == Double.NEGATIVE_INFINITY) {
			machine.setArithmeticException(I);
			machine.setGeneralRegister(x, 0xfff8000000000000L);
			return;
		} else if (dy == Double.NEGATIVE_INFINITY
				&& dz == Double.POSITIVE_INFINITY) {
			machine.setArithmeticException(I);
			machine.setGeneralRegister(x, Double.doubleToRawLongBits(Double.NaN));
			return;
		}
		dx = dy + dz;
		if(dx == 0){
			if(machine.isRoundDown()){
				if(dy == 0.0 && dz == 0.0){
					machine.setGeneralRegister(x, Double.doubleToRawLongBits(0.0));
				}else{
					machine.setGeneralRegister(x, Double.doubleToRawLongBits(-0.0));
				}
			}else{
				if(dy == -0.0 && dz == -0.0){
					machine.setGeneralRegister(x, Double.doubleToRawLongBits(-0.0));
				}else{
					machine.setGeneralRegister(x, Double.doubleToRawLongBits(0.0));
				}
			}
		}
		
		
		machine.setGeneralRegister(x, Double.doubleToRawLongBits(dx));
	}

	@Override
	protected void timing() {
		// TODO Auto-generated method stub

	}

}
