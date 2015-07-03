package eddie.wu.api;

import java.util.Set;

import eddie.wu.domain.BoardColorState;
import eddie.wu.domain.Point;
import eddie.wu.domain.Step;

/**
 * 
 * 
 * There are so many different implementation of GoBoard. But I did not program
 * to interface, so i can not reuse the Applet (which should be reused because
 * the develop of UI is so trivial to me.) one kind of GoBoard is for
 * calculation, rule validation, keep track of internal status. which is the M
 * in the MVC. another kind of GoBoard is for displaying the point and
 * interactive with player. which is the V&C in MVC
 * 
 * @author eddie
 */
public interface GoBoardInterface {
	/**
	 * 
	 * @param step
	 * @return
	 */
	public boolean oneStepForward(Step step);

	/**
	 * the impl. need to record at least its last step.
	 * @return
	 */
	public boolean oneStepBackward();

	/**
	 * 
	 * @return current board status
	 */
	public byte[][] getMatrixState();

	/**
	 * 
	 * @return the points eaten in current step.
	 */
	public Set<Point> getEatenPoints();

	// void output();

	/**
	 * 
	 * @return
	 */
	//BoardColorState getBoardColorState();
}
