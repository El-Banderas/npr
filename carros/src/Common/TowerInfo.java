package Common;

import java.net.InetAddress;

public class TowerInfo {
    public String name;
    public InetAddress ip;
    public Position pos;

    public TowerInfo(String name, InetAddress ip, Position pos) {
        this.name = name;
        this.ip = ip;
        this.pos = pos;
    }

    @Override
    public String toString() {
        return "TowerInfo{" +
                "name='" + name + '\'' +
                ", ip=" + ip +
                ", pos=" + pos +
                '}';
    }
}
