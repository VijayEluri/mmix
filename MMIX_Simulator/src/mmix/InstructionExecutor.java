package mmix;

import org.apache.log4j.Logger;

import static mmix.cons.SpecialRegisterConstant.*;

public abstract class InstructionExecutor {
	Logger log = Logger.getLogger(this.getClass());
	protected Machine machine;

	// bug 6: private static int opCodeMask = 0X11000000;
	// private static int opCodeMask = 0Xff000000;
	//
	// private static int xMask = 0X00ff0000;
	//
	// private static int yMask = 0X0000ff00;
	//
	// private static int zMask = 0X000000ff;
	/**
	 * the following field is used to control re-exectute when resuming
	 */
	protected boolean resume;

	protected int ropCode;

	protected int resumeZ;

	protected int opCode;

	/**
	 * the following are the fields common to all executors.
	 */
	protected int x;

	protected int y;

	protected int z;

	protected long valueY;

	protected long valueZ;

	protected long RA;// virtural address relative to machine.at;

	protected long virtualAddress;// virtural for address

	// protected int address;// absolute phsical address;

	public abstract void execute();

	/**
	 * all parameters are greater than or equal to 0;
	 * 
	 * @param opCode
	 * @param x
	 * @param y
	 * @param z
	 * @return
	 */
	public InstructionExecutor setInstruction(int opCode, int x, int y, int z) {
		this.opCode = opCode;
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}

	public void setMachineState(Machine machineState) {
		this.machine = machineState;
	}

	/**
	 * not suitbale for floating point
	 */
	protected void setOperandYZ() {
		if (resume) {
			if (z == 0) {
				if (ropCode == 0) {
					_setOperandYZ() ;
				} else if (ropCode == 1) {
					valueY = machine.getSpecialRegister(Y);
					valueZ = machine.getSpecialRegister(Z);
				}else
				if (ropCode == 2) {

				}else 
				if (ropCode >= 3) {

				}
			} else if (z == 1) {
				if (ropCode == 0) {
					_setOperandYZ() ;
				} else if (ropCode == 1) {
					valueY = machine.getSpecialRegister(YY);
					valueZ = machine.getSpecialRegister(ZZ);
				}
				if (ropCode == 2) {

				}
				if (ropCode == 3) {
					//virtual address translation
				}
			}

		}
		_setOperandYZ() ;
	}

	private void _setOperandYZ() {
		if (this.isImmediateInstruction()) {
			this.setOperandImmediate();
		} else {
			setOperand();
		}
	}

	private void setOperand() {
		valueY = machine.getGeneralRegister(y);
		valueZ = machine.getGeneralRegister(z);
	}

	private void setOperandImmediate() {
		valueY = machine.getGeneralRegister(y);
		valueZ = z;
	}

	protected void setRA() {
		if (this.isForwardBranch()) {
			this.setRA_Forward();
		} else {
			this.setRA_Backward();
		}
	}

	protected void setRA_Forward() {
		// bug 8:RA = machine.at + 4 * y * 256 + z;
		RA = machine.virtualAt + 4 * ((y << 8) + z);
		// this.phyRA = machine.memoryMap(RA);
	}

	protected void setThreeByteRA_Forward() {
		log.debug("setThreeByteRA_Forward()");
		// bug 8:RA = machine.virtueAt + 4 * y * 256 + z;
		RA = machine.virtualAt + 4 * ((x << 16) + (y << 8) + z);
		log.debug(Long.toHexString(machine.virtualAt));
		log.debug(Long.toHexString(4 * ((x << 16) + (y << 8) + z)));
		log.debug(Long.toHexString(machine.virtualAt + 4
				* ((x << 16) + (y << 8) + z)));
		// phyRA = machine.memoryMap(RA);
	}

	protected void setRA_Backward() {

		// RA = machine.virtualAt - 4 * (0xffff - ((y << 8) + z));
		RA = machine.virtualAt - 4 * (0x10000 - ((y << 8) + z));
		// phyRA = machine.memoryMap(RA);
	}

	protected void setThreeByteRA_Backward() {

		// RA = machine.virtualAt - 4 * (0xffffff - ((x << 16) + (y << 8) + z));
		RA = machine.virtualAt - 4 * (0x1000000 - ((x << 16) + (y << 8) + z));
		// phyRA = machine.memoryMap(RA);
	}

	protected void setAddress() {
		if (this.isImmediateInstruction()) {
			this.setAddressImmediate();
		} else {
			this.setAddressNormal();
		}
	}

	protected void setAddressNormal() {
		long temp = (machine.getGeneralRegister(y) + machine
				.getGeneralRegister(z));
		virtualAddress = temp;
		// address = machine.memoryMap(temp);// bug 1:machine.memoryMap(temp);
	}

	/**
	 * currently we only consider the user mode program, it's address is always positive.
	 */
	protected void setAddressImmediate() {
		long temp = machine.getGeneralRegister(y) + z;
		virtualAddress = temp;
		// address = machine.memoryMap(temp);// bug2:machine.memoryMap(temp);
	}

	protected boolean isImmediateInstruction() {
		if (opCode % 2 == 0) {
			return false;
		} else {
			return true;
		}
	}

	protected boolean isForwardBranch() {
		if (opCode % 2 == 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return this.getClass().getCanonicalName();
	}

	/**
	 * to make sure all subclass implement timing.
	 */
	protected abstract void timing();
}
