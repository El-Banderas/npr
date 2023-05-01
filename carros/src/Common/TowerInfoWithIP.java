package Common;

import java.net.InetAddress;
import java.util.List;
import java.util.Set;

public class TowerInfoWithIP extends TowerInfo
{
	public InetAddress serverIP;


	public TowerInfoWithIP(String towerID, Position pos, InetAddress sender) {
		super(towerID, pos);
		this.serverIP = sender;
	}
}
