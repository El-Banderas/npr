package Car;

import Common.Position;

import java.util.HashMap;
import java.util.Map;

public class AmbulanceInfo {
    public Map<Integer, Position> path;

    public AmbulanceInfo(Map<Integer, Position> path) {
        this.path = path;
    }
}
