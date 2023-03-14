package Common;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;


/**
 * Here we store the connection information about one node.
 * Can also store the socket, necessary to send messages.
 */
public class InfoNode {
	
	public InetAddress ip;
	public int port;
	public DatagramSocket socket;
	
	
	public InfoNode(InetAddress ip, int port, boolean createSocket) {
		this.port = port;
		this.ip = ip;
		if (createSocket) {
			try {
				socket = new DatagramSocket(port);
				this.ip = socket.getLocalAddress();
			} catch (SocketException e) {
				System.out.println("[Expetion] Error creating socket");
				throw new RuntimeException(e);
			}
		}
	}
	
	public InfoNode(int port) {
		try {
			if (!Constants.linux) // Windows
				socket = new DatagramSocket(port);
			else
				socket = new DatagramSocket();
		} catch (SocketException e) {
			System.out.println("[Expetion] Error creating socket");
			throw new RuntimeException(e);
		}
		this.ip = socket.getLocalAddress();
	}
	
	public InfoNode() {
		this.socket = null;
		this.ip	= null;
		this.port = -1;
	}
	
	
	@Override
	public String toString() {
		return "InfoNode{" +
				"ip=" + ip.toString() +
				", port=" + port +
				'}';
	}
}
