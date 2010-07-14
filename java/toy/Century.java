/**
Try to solve the series of puzzles/games.
Old Dady -> Dummy Donkey -> Century -> Century and half
In Chinese, Hua Rong Dao
*/

package toy;

public class Century {
	public static void  main(String[] args){
		Century century = new Century();
		century.run();
	}

	public void run(){
		init();
		//list of moves
		List<Move> moves = new ArrayList<Move>();
		Move lastMove;
		for(Point point : blank){
			List<Point> list = point.periphery();
			for(Point p:list){
			    	List<Move> moveT = getBlock(p).getMoves(point);
				moves.addAll(moveT);
			}			
		}

		
		for(Iterator<Move> iter = moves.iterator();iter.hasNext()){
			Move move = iter.next();
			if(move.supplement(lastMove)){
				iter.remove();
			}
		}
		if(move.isEmpty()){
			//back track one level.
		}
		for(Iterator<Move> iter = moves.iterator();iter.hasNext()){
			Move move = iter.next();
			iter.remove();
			apply(Move);//change board status.
		}
		for(Move move : moves){
			
		
		}		
	
	}

	byte[][] state = new board[][];//check duplicate state
	Board blockBoard = new Board();
	List<Block> board = new ArrayList<Block>();
	List<Point> blank = new ArrayList<Point>();
	init(){
		board.add(new Block(1,new Point[]{new Point(3,1)},"Pawn1"));
		board.add(new Block(1,new Point[]{new Point(3,4)},"Pawn2"));


		board.add(new Block(2,new Point[]{new Point(1,3),new Point(1,4)},"Horizontal 1"));
		board.add(new Block(2,new Point[]{new Point(2,3),new Point(2,4)},"Horizontal 2"));
		board.add(new Block(2,new Point[]{new Point(4,3),new Point(4,4)},"Horizontal 3"));
		board.add(new Block(2,new Point[]{new Point(5,3),new Point(5,4)},"Horizontal 4"));
	
		blank.add(new Point(3,2));
		blank.add(new Point(3,3));
		board.add(new Block(2,new Point[]{new Point(4,1),new Point(5,1)},"Vertical 1"));
		board.add(new Block(4,new Point[]{new Point(1,1),new Point(2,1),new Point(1,2),new Point(2,2)},"Vertical 1"));
	}
} 

class Move{
	Block block;
	byte x;
	byte y;
	public boolean equals(Object o){
		if(o instanceof Move) {
			Move m = (Move)o;
			if(m.block==block){
				if(m.x==x && m.y==y){
				return true;
				}else return false;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	public boolean supplement(Move m){
		if(m==null) return false;
		if(m.block==block){
			if(m.x+x==0&&m.y+y==0)return ture;
			else return false;
		}else{
			return false;
		}
	}
}

class Board{

	Block[][] blocks =  new Block[][];
	void init(List<Block> list){
		for(Block block : list){
			for(Point p: positions){
				blocks[p.a][p.b] = block;
			}
		}

	}
}

class Block{
	byte points; //number of points in one block.
	Point[] positions;
	String name;
	byte type;// 1 for 1*1 block; 2 for 2*1; 3 for 2*2
	
	public Block(byte points,byte[] positions,String name){
		this.points=points;
		this.positions = positions;
		this.name = name;
		if(points==1){
			type=1;
		}else if(points==2){
			type = 2;
		}else if(points = 4){
			type = 3;
		}else{
			System.error.println("wrong");
		}
	}
}

class Point {
	static Point[][] points = new Point[6][5];
	static{
		for(int i=0;i<6;i++){
			for(int j=0;j<5;j++){
				points[i][j] = new Point(i,j);
			}
		}
	}
	private Point(){}
	getPoint(byte a, byte b){
		return points[a][b];
	}
	byte a;
	byte b;

	static List<Point> periphery = new ArrayList<Point>(4);
	static {
		periphery.add(new Point(1,1);
		periphery.add(new Point(1,-1);
		periphery.add(new Point(-1,1);
		periphery.add(new Point(-1,-1);
	
	}
	List<Point> periphery(){
		Point t;
		List<Point> list = new ArrayList<Point>(4);
		for(Point p : periphery){
			t = points[a+p.a][b+p.b];
			if(t!=null) list.add(t);
		}	
		return list;
	}
}





