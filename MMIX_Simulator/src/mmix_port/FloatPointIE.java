//package mmix_port;
//
//import static mmix.cons.InstructionConstant.FADD;
//import static mmix.cons.InstructionConstant.FCMP;
//import static mmix.cons.InstructionConstant.FCMPE;
//import static mmix.cons.InstructionConstant.FDIV;
//import static mmix.cons.InstructionConstant.FEQL;
//import static mmix.cons.InstructionConstant.FEQLE;
//import static mmix.cons.InstructionConstant.FINT;
//import static mmix.cons.InstructionConstant.FIX;
//import static mmix.cons.InstructionConstant.FIXU;
//import static mmix.cons.InstructionConstant.FLOT;
//import static mmix.cons.InstructionConstant.FLOTU;
//import static mmix.cons.InstructionConstant.FMUL;
//import static mmix.cons.InstructionConstant.FREM;
//import static mmix.cons.InstructionConstant.FSQRT;
//import static mmix.cons.InstructionConstant.FSUB;
//import static mmix.cons.InstructionConstant.FUNE;
//import static mmix.cons.InstructionConstant.LDSF;
//import static mmix.cons.InstructionConstant.LDSFI;
//import static mmix.cons.InstructionConstant.SFLOT;
//import static mmix.cons.InstructionConstant.SFLOTU;
//import static mmix.cons.InstructionConstant.STSF;
//import static mmix.cons.InstructionConstant.STSFI;
//import static mmix.cons.SpecialRegisterConstant.E;
//import mmix.InstructionExecutor;
//
///**
// * <p>
// * FloatPointIE.java
// * </p>
// * 
// * @<Cases for ind...@>= case FADD: x=fplus(y,z); fin_float:
// *         round_mode=cur_round; store_fx: exc|=exceptions;@+ goto store_x; case
// *         FSUB: a=z;@+if (fcomp(a,zero_octa)!=2) a.h^=sign_bit;
// *         x=fplus(y,a);@+goto fin_float; case FMUL: x=fmult(y,z);@+goto
// *         fin_float; case FDIV: x=fdivide(y,z);@+goto fin_float; case FREM:
// *         x=fremstep(y,z,2500);@+goto fin_float; case FSQRT: x=froot(z,y.l);
// *         fin_unifloat:@+if (y.h || y.l>4) goto illegal_inst; round_mode=(y.l?
// *         y.l: cur_round);@+goto store_fx; case FINT:
// *         x=fintegerize(z,y.l);@+goto fin_unifloat; case FIX:
// *         x=fixit(z,y.l);@+goto fin_unifloat; case FIXU:
// *         x=fixit(z,y.l);@+exceptions&=~W_BIT;@+goto fin_unifloat; case FLOT:
// *         case FLOTI: case FLOTU: case FLOTUI: case SFLOT: case SFLOTI: case
// *         SFLOTU: case SFLOTUI: x=floatit(z,y.l,op&0x2,op&0x4);@+goto
// *         fin_unifloat;
// * 
// * 
// * @author Eddie Wu
// * @version 1.0
// * 
// */
//public class FloatPointIE extends InstructionExecutor {
//
//	@Override
//	public void execute() {
//		switch (opCode) {
//		case FADD:
//			if(Dy.isNaN() || Dz.isNaN()){
//				standardconvention();		
//			}
//			fAdd();
//			break;
//		case FSUB:
//			if(Dy.isNaN() || Dz.isNaN()){
//				standardconvention();		
//			}
//			fSub();
//			break;
//		case FMUL:
//			new ArithUtil().fmult(y, z);
//			
//			break;
//		case FDIV:
//			if(Dy.isNaN() || Dz.isNaN()){
//				standardconvention();		
//			}
//			fDiv();
//			break;
//		case FREM:
//			if(Dy.isNaN() || Dz.isNaN()){
//				standardconvention();		
//			}
//			fRem();
//			break;
//		case FSQRT:
//			if(Dy.isNaN() || Dz.isNaN()){
//				standardconvention();		
//			}
//			fSqrt();
//			break;
//		case FINT:
//			if(Dy.isNaN() || Dz.isNaN()){
//				standardconvention();		
//			}
//			machine.setGeneralRegister(x, Dz.longValue());
//			break;
//		case FCMP:
//			machine.setGeneralRegister(x, Dy.compareTo(Dz));
//			break;
//		case FEQL:
//			machine.setGeneralRegister(x, Dy.equals(Dz) ? 1 : 0);
//			break;
//		case FCMPE:
//			Ulps = Double.longBitsToDouble(machine.getSpecialRegister(E));
//			ulps = Ulps.doubleValue();
//			machine.setGeneralRegister(x, Dy.compareTo(Double
//					.valueOf(dz + ulps)));
//			break;
//		case FEQLE:
//			Ulps = Double.longBitsToDouble(machine.getSpecialRegister(E));
//			ulps = Ulps.doubleValue();
//			machine.setGeneralRegister(x,
//					Dy.equals(Double.valueOf(dz + ulps)) ? 1 : 0);
//			break;
//		case FUNE:
//			break;
//		case FIX:
//			machine.setGeneralRegister(x, Dz.intValue());
//			break;
//		case FIXU:
//			break;
//		case FLOT:// since float point register is also used as integer register
//			// no need to use seperate Float loading instruction.
//			break;
//		case FLOTU:
//			break;
//		case SFLOT:
//			break;
//		case SFLOTU:
//			break;
//		case LDSF:
//		case LDSFI:
//			break;
//		case STSF:
//		case STSFI:
//			break;
//		default:
//			throw new RuntimeException("MMIX: invalid OpCode (" + opCode
//					+ ") for " + this.getClass().getCanonicalName());
//		}
//
//	}
//
//	@Override
//	protected void timing() {
//		// TODO Auto-generated method stub
//
//	}
//
//}
