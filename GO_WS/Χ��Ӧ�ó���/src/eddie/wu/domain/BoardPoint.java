/*
 * Created on 2005-4-20
 *


 */
package eddie.wu.domain;

import java.io.Serializable;

/**
 * @author eddie
 * 
 *         棋盘上的点的抽象，不是棋子的抽象。 点的坐标有很多种表达方式，内部以数组为准。
 */
public class BoardPoint implements Cloneable, Serializable {
	private static final byte ARRAY_COORDINATE = 1;

	private Point point;
	private Point twinForKo;

	private int color = ColorUtil.BLANK;

	/**
	 * flag used in calculate breath and divide the blank point blank.
	 */
	private boolean calculatedFlag = false;

	/**
	 * use to decide DAJIE(打劫)<br/>
	 * 比如第50步提劫，那么第51步就不能立即在twinForKo处回提。
	 */
	private short prohibitStep = 0;

	/**
	 * the point belong to the block, many to one relation.
	 */
	private BasicBlock block = null;

	/**
	 * @return Returns the prohibitStep.
	 */
	public short getProhibitStep() {
		return prohibitStep;
	}

	/**
	 * @param prohibitStep
	 *            The prohibitStep to set.
	 */
	public void setProhibitStep(short prohibitStep) {
		this.prohibitStep = prohibitStep;
	}

	/**
	 * @return Returns the calculatedFlag.
	 */
	public boolean isCalculatedFlag() {
		return calculatedFlag;
	}

	/**
	 * @param calculatedFlag
	 *            The calculatedFlag to set.
	 */
	public void setCalculatedFlag(boolean calculateBreathFlag) {
		this.calculatedFlag = calculateBreathFlag;
	}

	public BoardPoint() {

	}

	/**
	 * default color is blank
	 * 
	 * @param point
	 */
	public BoardPoint(Point point) {
		this(point, ColorUtil.BLANK);
	}

	public BoardPoint(Point point, int color) {
		this.point = point;
		this.color = color;
	}

	// extension for other coordinate representation.
	// public BoardPoint(byte a, byte b,byte type){
	//
	//
	// }

	public BasicBlock getBasicBlock() {
		return block;
	}

	/**
	 * for the blank point, get Block will return null
	 * 
	 * @return Returns the block.
	 */
	public Block getBlock() {
		if (block == null)
			return null;// not happen in goboard.
		if (block.isBlank())
			return null;
		// throw new RuntimeException(
		// "getBlock() does not work for Blank block!");
		else
			return (Block) block;
	}

	/**
	 * @param block
	 *            The block to set.
	 */
	public void setBlock(Block block) {
		this.block = block;
		block.addPoint(point);
	}

	public void setBasicBlock(BasicBlock block) {

		if (block.isBlank()) {
			this.setBlankBlock((BlankBlock) block);
		} else {
			this.setBlock((Block) block);
		}
	}

	/**
	 * maintain two-direction relationship
	 * 
	 * @param block
	 *            The block to set.
	 */
	public void setBlankBlock(BlankBlock block) {
		this.block = block;
		block.addPoint(this.getPoint());
	}

	/**
	 * @return Returns the breaths.
	 */
	public int getBreaths() {
		return this.getBlock().getBreaths();
	}

	/**
	 * @return Returns the color.
	 */
	public int getColor() {
		return color;
	}

	public byte getEnemyColor() {
		if (color == ColorUtil.BLACK)
			return ColorUtil.WHITE;
		else if (color == ColorUtil.WHITE)
			return ColorUtil.BLACK;
		else
			throw new RuntimeException("getEnemyColor--blank!");
	}

	/**
	 * @param color
	 *            The color to set.
	 */
	public void setColor(int color) {
		this.color = color;
	}

	/**
	 * @return Returns the column.
	 */
	public byte getColumn() {
		return point.getColumn();
	}

	/**
	 * @return Returns the row.
	 */
	public byte getRow() {
		return point.getRow();
	}

	public short getOneDimensionCoordinate() {
		return point.getOneDimensionCoordinate();

	}

	public int getTotalNumberOfPoint() {
		if (block == null)
			return 1;
		return block.getNumberOfPoint();
	}

	/**
	 * @return Returns the point.
	 */
	public Point getPoint() {
		return point;
	}

	/**
	 * @param point
	 *            The point to set.
	 */
	public void setPoint(Point point) {
		this.point = point;
	}

	public String toString() {
		return "color=" + this.color + "; point=" + point.toString() + "]";

	}

	public int hashCode() {
		return point.hashCode();
	}

	public boolean equals(Object object) {
		if (object instanceof BoardPoint == false) {
			return false;
		}
		BoardPoint other = (BoardPoint) object;
		if (other.getColor() != color) {
			return false;
		}
		if (other.getPoint().equals(point) == false) {
			return false;
		}
		if (block == null) {
			return other.getBlock() == null;
		} else {
			return block.equals(other.getBlock());
		}
	}

	public Object clone() {
		BoardPoint temp = null;
		try {
			temp = (BoardPoint) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		temp.setBlock((Block) getBlock().clone());

		return temp;
	}

	public boolean isLeftTop() {
		return point.isLeftTop();
	}

	// public boolean isValidCoordinate() {
	// return point.isValid();
	// }

	/**
	 * @return Returns the twinForKo.
	 */
	public Point getTwinForKo() {
		return twinForKo;
	}

	/**
	 * @param twinForKo
	 *            The twinForKo to set.
	 */
	public void setTwinForKo(Point twinForKo) {
		this.twinForKo = twinForKo;
	}

	public BlankBlock getBlankBlock() {
		if (block == null)
			return null;
		if (block.isBlank())
			return (BlankBlock) block;
		else
			return null;
		// throw new RuntimeException(
		// "getBlankBlock() does not work for Black/Whtie block!");
	}

	public Point getNeighbour(Delta delta) {
		return point.getNeighbour(delta);
	}
}