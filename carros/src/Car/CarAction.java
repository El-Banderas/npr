package Car;

import Common.Position;

public class CarAction {
    public Position pos;
    public String node;
    public String action;

    public CarAction(Position pos, String node, String action) {
        this.pos = pos;
        this.node = node;
        this.action = action;
    }
}
