package Common;

import java.io.IOException;
import java.net.DatagramSocket;


public class TowerInfo
{	
	public String name;
	public Position pos;
	
	public InfoNode connectionInfoSend;
	public InfoNodeMulticast connectionInfoReceive;
	
	public DatagramSocket inside_socket;
	public DatagramSocket outside_socket; //TODO: Multicast
	
	
	/**
	 * @param name
	 * @param pos
	 * @throws IOException 
	 */
	public TowerInfo(String name, Position pos) throws IOException
	{
		this.name = name;
		this.pos = pos;
		
		this.inside_socket = new DatagramSocket(Constants.towerPort);
		this.outside_socket = new DatagramSocket(Constants.portMulticast); //TODO: Multicast
		
		this.connectionInfoSend = new InfoNode(inside_socket);
		this.connectionInfoReceive = new InfoNodeMulticast();
	}
	
	
	public String getName(){return name;}
}
