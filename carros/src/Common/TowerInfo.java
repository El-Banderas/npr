package Common;

public class TowerInfo {
	public String name;
	public InfoNode connectionInfo;
	public InfoNode connectionInfoLinuxSend;
	public InfoNodeMulticast connectionInfoLinuxReceive;
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
				", Socket=" + connectionInfo.socket +
				", pos=" + pos +
				'}';
	}

	/**
	 * Used in linux, creates 2 sockets
	 * @param name
	 * @param pos
	 */
	public TowerInfo(String name, Position pos) {
		this.name = name;
		this.pos = pos;
		this.connectionInfo = null;
		this.connectionInfoLinuxReceive = new InfoNodeMulticast(true);
		// Port doesn't matter to send.
		this.connectionInfoLinuxSend = new InfoNode(Constants.carPort);
	}
}
