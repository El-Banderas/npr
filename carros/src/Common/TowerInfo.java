package Common;

import java.net.DatagramSocket;

public class TowerInfo {
	public String name;
	public InfoNode connectionInfoWindowsReceive;
	public InfoNode connectionInfoLinuxSend;
	public InfoNodeMulticast connectionInfoLinuxReceive;
	public Position pos;

	public TowerInfo(String name, InfoNode connectionInfo, Position pos) {
		this.name = name;
		this.connectionInfoWindowsReceive = connectionInfo;
		this.pos = pos;
	}

	@Override
	public String toString() {
		return "TowerInfo{" +
				"name='" + name + '\'' +
				", Socket=" + connectionInfoWindowsReceive.socket +
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
		this.connectionInfoWindowsReceive = null;
		this.connectionInfoLinuxReceive = new InfoNodeMulticast(true);
		// Port doesn't matter to send.
		this.connectionInfoLinuxSend = new InfoNode(Constants.carPort);
	}

	public DatagramSocket receiveSocket(){
		if (Constants.linux)
			return this.connectionInfoLinuxReceive.socket;
		else
			return this.connectionInfoWindowsReceive.socket;
	}
	public DatagramSocket sendSocket(){
		if (Constants.linux)
			return this.connectionInfoLinuxSend.socket;
		else
			return this.connectionInfoWindowsReceive.socket;
	}
}
