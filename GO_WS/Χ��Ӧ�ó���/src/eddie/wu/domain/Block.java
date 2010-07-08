/*
 * Created on 2005-4-21
 *
 * Eddie Wu CopyRight @2005
 */
package eddie.wu.domain;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import eddie.wu.linkedblock.ColorUtil;

/**
 * @author eddie some concern; because the blank/white block is different from
 *         blank point block. and we use same type block to present them, so
 *         must decide whether to invoke special method according to the color.
 * 
 * 棋块--breath only retain in the block. the breath of the point in the block is
 * nonsense. 1. block is divide into three subtype; one is blank point block(and
 * breath block is special. surround only by same color block) another is black
 * block the last is white block
 * 
 * 2. black/white block has enemy Block. blank point block has no enemy Block.
 * 3. black/white block has breath Block. blank point block has no breath Block.
 * 4. black/white block has breath point set. blank point block has no set of
 * breath point
 * 
 * fix 1; decide to contain point. not BoardPoint. avoid directional reference.
 */
public class Block implements Cloneable, java.io.Serializable{
	Log log=LogFactory.getLog(Block.class);
    /* general/common attribute */
    private byte color;//棋块颜色

    private Set<Point> allPoints = new HashSet<Point>();//棋块子点集合

    /* only used by blank/white block */
    private Set<Point> allBreathPoints = new HashSet<Point>();//棋块气点集合

    private Set<Block> enemyBlocks = new HashSet<Block>();

    private Set<Block> breathBlocks = new HashSet<Block>();

    

    public void changeBlockForEnemyBlock(Block newBlock) {
        Block block = null;
        for (Iterator iter = enemyBlocks.iterator(); iter.hasNext();) {
            block = (Block) iter.next();
            block.removeEnemyBlock(this);
            block.addEnemyBlock(newBlock);

        }

    }

    public void initAfterChangeToBlankblock() {
        allBreathPoints.clear();//棋块气点集合
        enemyBlocks.clear();
        breathBlocks.clear();

    }

    public Block() {

    }

    public Block(byte color) {
        this.color = color;
    }

    public void addPoint(Point Point) {
        allPoints.add(Point);
    }

    public void addPoint(Block block) {
        allPoints.addAll(block.getAllPoints());
    }

    public boolean removePoint(Point point) {
        return allPoints.remove(point);
    }

    public void addBreathPoint(Point point) {
    	if(point.isNotValid()){
    		throw new RuntimeException("point is not valid when add Breath"+point);
    	}
    	if(allBreathPoints.add(point)){
        	
        }else{//bug fix. it is possible to return false; because of shared breath.
        	//throw new RuntimeException("failed when remove Breath Point"+point);
        	log.info("return false when add Breath Point:"+point);
        }
    }

    public void removeBreathPoint(Point point) {
    	if(point.isNotValid()){
    		throw new RuntimeException("point is not valid when remove Breath Point"+point);
    	}
    	if(allBreathPoints.remove(point)){
        	
        }else{//bug fix. it is possible to return false; because of shared breath.
        	//throw new RuntimeException("failed when remove Breath Point"+point);
        	log.info("return false when remove Breath Point:"+point);
        }        
    }

    //	a special point on behalf the whole bolck.
    //	byte row;
    //	byte column;

    //	public Block(byte row, byte column){
    //		super();
    //		this.row=row;
    //		this.column=column;
    //		color=Constant.BLANK_POINT;
    //	}
    /**
     * @return Returns the allPoints.
     */
    public Set getAllPoints() {
        return allPoints;
    }

    /**
     * @param allPoints
     *            The allPoints to set.
     */
    public void setAllPoints(Set<Point> allPoints) {
        this.allPoints = allPoints;
    }

    /**
     * @return Returns the color.
     */
    public byte getColor() {
        return color;
    }

    /**
     * @param color
     *            The color to set.
     */
    public void setColor(byte color) {
        this.color = color;
        //		for(Iterator iter=allPoints.iterator();iter.hasNext();){
        //			((BoardPoint)iter.next()).setColor(ColorUtil.BLANK_POINT);
        //		}
    }

    public short getBreaths() {
        return (short) this.allBreathPoints.size();
    }

    public short getTotalNumberOfPoint() {
        return (short) this.allPoints.size();
    }

    public void addEnemyBlock(Block block) {
        this.enemyBlocks.add(block);
    }

    public void removeEnemyBlock(Block block) {
        this.enemyBlocks.remove(block);
    }

    //mock implementation
    public Point getTopLeftPoint() {
        return getBehalfPoint();
    }

    public Point getBehalfPoint() {
        return (Point) allPoints.iterator().next();
    }

    //	public void addBreath(){
    //		this.breath +=1;
    //	}
    public String toString() {
        StringBuffer temp = new StringBuffer("[color=");

        if (color == ColorUtil.BLACK || color == ColorUtil.WHITE) {
            temp.append(color);
            temp.append(",allPoints=");
            temp.append(allPoints);
            //temp.append("]");
            temp.append(",allBreathPoints=");
            temp.append(allBreathPoints);
            temp.append(",breathBlocks=");
            temp.append(this.breathBlocks);
        } else {
            temp.append(color);
            temp.append(",allPoints=");
            temp.append(allPoints);
        }

        /*
         * bug fix 2
         */
        //cause recursive invocation and get Stack Over Flow Error!
        //temp.append(",enemyBlocks=");
        //temp.append(this.enemyBlocks);
        temp.append("]");
        return temp.toString();
    }

    public boolean equals(Object o) {
        if (o instanceof Block) {
            Block other = (Block) o;
            if (other.getColor() == this.getColor()
                    && this.getAllPoints().equals(other.getAllPoints())
                    && this.getAllBreathPoints().equals(
                            other.getAllBreathPoints())) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public int hashCode() {
        return this.allPoints.hashCode() + this.allBreathPoints.hashCode() * 17;
    }

    /**
     *  
     */
    public void changeColorToBlank() {
        // TODO Auto-generated method stub
        allBreathPoints.clear();
        color = ColorUtil.BLANK_POINT;
        enemyBlocks = null;
        breathBlocks = null;
    }

    /**
     * @return Returns the allBreathPoints.
     */
    public Set<Point> getAllBreathPoints() {
        return allBreathPoints;
    }

    /**
     * @param allBreathPoints
     *            The allBreathPoints to set.
     */
    public void setAllBreathPoints(Set<Point> allBreathPoints) {
        this.allBreathPoints = allBreathPoints;
    }

    /**
     * @return Returns the breathBlock.
     */
    public Set<Block> getBreathBlocks() {
        return breathBlocks;
    }

    public void addBreathBlock(Block abreathBlock) {
        breathBlocks.add(abreathBlock);
    }

    /**
     * @param breathBlock
     *            The breathBlock to set.
     */
    public void setBreathBlocks(Set breathBlock) {
        this.breathBlocks = breathBlock;
    }

    /**
     * @return Returns the enemyBlock.
     */
    public Set getEnemyBlocks() {
        return enemyBlocks;
    }

    /**
     * @param enemyBlock
     *            The enemyBlock to set.
     */
    public void setEnemyBlocks(Set enemyBlock) {
        this.enemyBlocks = enemyBlock;
    }
    
    public Object clone(){
        Block temp=null;
	    try{
	     temp=(Block)super.clone();
	    }catch(CloneNotSupportedException e){
	        e.printStackTrace();
	    }	
	    temp.allBreathPoints=(Set<Point>)((HashSet<Point>)allBreathPoints).clone();
	    temp.allPoints=(Set)((HashSet)allPoints).clone();
	    temp.enemyBlocks=(Set)((HashSet)enemyBlocks).clone();
	    return temp;
    }
}