package Common;


public class Position {
	
	public int x;
	public int y;
	public float timestamp;
	
	
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	
	public void getPosition(){
	}
	
	public static double distance(Position p1, Position p2){
		return Math.sqrt(Math.pow(p2.x-p1.x, 2) +  Math.pow(p2.y-p1.y, 2));
	}
}
