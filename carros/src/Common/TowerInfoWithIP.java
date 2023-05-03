package Common;

import java.net.InetAddress;

public class TowerInfoWithIP extends TowerInfo
{
	public InetAddress ip;


	public TowerInfoWithIP(String towerID, Position pos, InetAddress sender) {
		super(towerID, pos);
		this.ip = sender;
	}
	public TowerInfoWithIP(String towerID, Position pos) {
		super(towerID, pos);
	}

	public void setIP(InetAddress serverIP) {
		this.ip = serverIP;
	}
}
