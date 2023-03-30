package Common;
import java.net.DatagramSocket;


public class CarInfo
{
	public String id;
	public Position oldPos;
	public Position pos;
	public InfoNode connectionInfoLinuxSend;
	public InfoNodeMulticast connectionInfoLinuxReceive;
	
	
	public CarInfo(Position pos, String id)
	{
		this.oldPos = pos;
		this.pos = pos;
		this.id = id;
		this.connectionInfoLinuxSend = new InfoNode(Constants.carPort);
		this.connectionInfoLinuxReceive = new InfoNodeMulticast();
	}
	
	
	public DatagramSocket receiveSocket()
	{
		return this.connectionInfoLinuxReceive.socket;
	}
	
	public DatagramSocket sendSocket()
	{
		return this.connectionInfoLinuxSend.socket;
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
