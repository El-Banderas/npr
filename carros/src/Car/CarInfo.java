package Car;

import Common.InfoNode;
import Common.Position;

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
