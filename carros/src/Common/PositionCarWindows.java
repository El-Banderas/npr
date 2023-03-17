package Common;


public class PositionCarWindows extends Position{

	public PositionCarWindows(int x, int y) {
		super(x, y);
	}
	
	
	public void getPosition(){
		x++;
		y++;
		if (x > Constants.maxXmap) x = Constants.minXmap;
		if (y > Constants.maxYmap) y = Constants.minYmap;
	}
}
