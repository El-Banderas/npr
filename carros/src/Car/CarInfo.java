package Car;

import Common.Constants;
import Common.InfoNode;
import Common.InfoNodeMulticast;
import Common.Position;

import java.net.DatagramSocket;

/**
 * Esta classe pode ser melhorada, com um Hashmap que pode variar de carro para carro...
 * Vale a pena pensar nisso :)
 */
public class CarInfo {
	public String brand;
	public String color;
	public Position pos;
	public InfoNode connectionInfoWindowsReceive;
	public InfoNode connectionInfoLinuxSend;
	public InfoNodeMulticast connectionInfoLinuxReceive;
	//Depois mudar

	public CarInfo(Position pos) {
		this.brand = "Mercedes";
		this.color = "Blue";
		this.pos = pos;
		this.connectionInfoWindowsReceive = null;
		this.connectionInfoLinuxSend = new InfoNode(Constants.carPort);
		this.connectionInfoLinuxReceive = new InfoNodeMulticast();

	}

	// Used in windows, later fix
	public CarInfo(Position pos, InfoNode sendInfo) {
		this.brand = "Mercedes";
		this.color = "Blue";
		this.pos = pos;
		this.connectionInfoWindowsReceive = sendInfo;
	}
	public DatagramSocket receiveSocket(){
		if (Constants.linux)
			return this.connectionInfoLinuxReceive.socket;
		else
			return this.connectionInfoWindowsReceive.socket;
	}
	public DatagramSocket sendSocket(){
		if (Constants.linux)
			return this.connectionInfoLinuxSend.socket;
		else
			return this.connectionInfoWindowsReceive.socket;
	}
}
