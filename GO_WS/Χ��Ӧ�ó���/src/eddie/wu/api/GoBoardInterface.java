package eddie.wu.api;
import eddie.wu.domain.Step;
import eddie.wu.linkedblock.BoardColorState;

/**
 * @author eddie
 *
 * There are so many different implementation of GoBoard.
 * But I did not program to interface, so i can not reuse 
 * the Applet (which should be reused because the develop of
 * UI is so trivial to me.)
 * one kind of GoBoard is for calculation, rule validation, keep track of
 * internal status. which is the M in the MVC.
 * another kind of GoBoard is for displaying the point and interactive with
 * player.  which is the V&C in MVC
 */
public interface GoBoardInterface {
	/**
	 * 
	 * @param step
	 * @return
	 */
	boolean oneStepForward(Step step) ;
	
	void output();
	
	/**
	 * 
	 * @return
	 */
	BoardColorState getBoardColorState();
}

