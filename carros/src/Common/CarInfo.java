package Common;

import java.io.IOException;


public class CarInfo
{
	public String id;
	public Position pos;
	public Position oldPos;


	public CarInfo(String id, Position pos) throws IOException
	{
		this.id = id;
		this.pos = pos;
		this.oldPos = pos;
	}

	public float getVelocity()
	{
		// TODO: ...
		return 0;
	}

	public Position getDirection()
	{
		// TODO: ...
		return new Position(pos.x- oldPos.x,pos.y - oldPos.y);
	}
}
