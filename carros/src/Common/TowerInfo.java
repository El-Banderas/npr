package Common;

public class TowerInfo {
    public String name;
    public InfoNode connectionInfo;
    public Position pos;

    public TowerInfo(String name, InfoNode connectionInfo, Position pos) {
        this.name = name;
        this.connectionInfo = connectionInfo;
        this.pos = pos;
    }

    @Override
    public String toString() {
        return "TowerInfo{" +
                "name='" + name + '\'' +
                ", ip=" + connectionInfo +
                ", pos=" + pos +
                '}';
    }
}
