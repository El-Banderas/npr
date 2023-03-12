package Car;

import Common.InfoNode;
import Common.Position;

/**
 * Esta classe pode ser melhorada, com um Hashmap que pode variar de carro para carro...
 * Vale a pena pensar nisso :)
 */
public class CarInfo {
	public String brand;
	public String color;
	public Position pos;
	public InfoNode infoNode;
	
	public CarInfo(Position pos, InfoNode infoNode) {
		this.brand = "Mercedes";
		this.color = "Blue";
		this.pos = pos;
		this.infoNode = infoNode;
	}
}
