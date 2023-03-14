package Car;

import Common.InfoNode;
import Common.InfoNodeMulticast;
import Common.Position;

/**
 * Esta classe pode ser melhorada, com um Hashmap que pode variar de carro para carro...
 * Vale a pena pensar nisso :)
 */
public class CarInfo {
	public String brand;
	public String color;
	public Position pos;
	public InfoNode sendInfo;
	public InfoNode receiveInfo;
	//Depois mudar
	public InfoNodeMulticast receiveInfo2;

	public CarInfo(Position pos, InfoNode sendInfo, InfoNode receiveInfo) {
		this.brand = "Mercedes";
		this.color = "Blue";
		this.pos = pos;
		this.receiveInfo = receiveInfo;
		this.sendInfo = sendInfo;

	}
	public CarInfo(Position pos, InfoNode sendInfo, InfoNodeMulticast receiveInfo) {
		this.brand = "Mercedes";
		this.color = "Blue";
		this.pos = pos;
		this.receiveInfo = receiveInfo;
		this.sendInfo = sendInfo;
		this.receiveInfo2 = receiveInfo;

	}
	// Used in windows, later fix
	public CarInfo(Position pos, InfoNode sendInfo) {
		this.brand = "Mercedes";
		this.color = "Blue";
		this.pos = pos;
		this.sendInfo = sendInfo;
	}
}
