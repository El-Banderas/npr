package Common;

import java.net.DatagramSocket;


public class TowerInfo
{	
	public String name;
	public InfoNode connectionInfoSend;
	public InfoNodeMulticast connectionInfoReceive;
	public Position pos;
	
	
	/**
	 * @param name
	 * @param pos
	 */
	public TowerInfo(String name, Position pos)
	{
		this.name = name;
		this.pos = pos;
		this.connectionInfoSend = new InfoNode(Constants.towerPort);
		this.connectionInfoReceive = new InfoNodeMulticast();
	}

	public String getName()
	{
		return name;
	}
	
	public DatagramSocket receiveSocket()
	{
		return this.connectionInfoReceive.socket;
	}
	
	public DatagramSocket sendSocket()
	{
		return this.connectionInfoSend.socket;
	}
}
