package Car;

import Common.Constants;
import Common.InfoNode;
import Common.InfoNodeMulticast;
import Common.Position;

import java.net.DatagramSocket;


public class CarInfo {

	public String id;
	public Position oldPos;
	public Position pos;
	public InfoNode connectionInfoWindowsReceive;
	public InfoNode connectionInfoLinuxSend;
	public InfoNodeMulticast connectionInfoLinuxReceive;
	
	
	public CarInfo(Position pos, String id) {
		this.oldPos = pos;
		this.pos = pos;
		this.id = id;
		this.connectionInfoWindowsReceive = null;
		this.connectionInfoLinuxSend = new InfoNode(Constants.carPort);
		this.connectionInfoLinuxReceive = new InfoNodeMulticast();
	}

	public CarInfo(Position pos, InfoNode sendInfo, String id) {
		this.oldPos = pos;
		this.pos = pos;
		this.id = id;
		this.connectionInfoWindowsReceive = sendInfo;
	}
	
	
	public DatagramSocket receiveSocket(){
		if (Constants.core)
			return this.connectionInfoLinuxReceive.socket;
		else
			return this.connectionInfoWindowsReceive.socket;
	}
	
	public DatagramSocket sendSocket(){
		if (Constants.core)
			return this.connectionInfoLinuxSend.socket;
		else
			return this.connectionInfoWindowsReceive.socket;
	}

	public float getVelocity(){
		// TODO:...
		return 0;
	}

	public Position getDirection(){
		// TODO: ...
		return new Position(pos.x- oldPos.x,pos.y - oldPos.y);
	}
}
