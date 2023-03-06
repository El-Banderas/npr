package Car;

import Common.Position;

public class CarInfo {
    public String brand;
    public String color;
    public Position pos;
    public CarInfo(Position pos) {
        this.brand = "Mercedes";
        this.color = "Blue";
        this.pos = pos;
    }
}
