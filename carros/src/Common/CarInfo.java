package Common;

import java.io.IOException;
import java.net.DatagramSocket;


public class CarInfo
{
	public String id;
	public Position oldPos;
	public Position pos;
	
	public InfoNode connectionInfoLinuxSend;
	public InfoNodeMulticast connectionInfoLinuxReceive;
	public DatagramSocket socket;
	
	
	public CarInfo(Position pos, String id) throws IOException
	{
		this.oldPos = pos;
		this.pos = pos;
		this.id = id;
		this.socket = new DatagramSocket(Constants.carPort); //TODO: Multicast
		this.connectionInfoLinuxSend = new InfoNode(socket);
		this.connectionInfoLinuxReceive = new InfoNodeMulticast();
		
	}

	public float getVelocity()
	{
		// TODO:...
		return 0;
	}

	public Position getDirection()
	{
		// TODO: ...
		return new Position(pos.x- oldPos.x,pos.y - oldPos.y);
	}
}
