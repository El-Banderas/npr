package Common;

import java.net.DatagramSocket;


public class TowerInfo {
	
	public String name;
	public InfoNode connectionInfoLinuxSend;
	public InfoNodeMulticast connectionInfoLinuxReceive;
	public Position pos;
	
	
	/**
	 * Used in linux, creates 2 sockets
	 * @param name
	 * @param pos
	 */
	public TowerInfo(String name, Position pos) {
		this.name = name;
		this.pos = pos;
		this.connectionInfoLinuxSend = new InfoNode(Constants.towerPort);
		this.connectionInfoLinuxReceive = new InfoNodeMulticast();
	}
	
	
	public DatagramSocket receiveSocket(){
		return this.connectionInfoLinuxReceive.socket;
	}
	
	public DatagramSocket sendSocket(){
		return this.connectionInfoLinuxSend.socket;
	}
}
