package Common;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;
import java.net.SocketException;

/**
 * Here we store the connection information about one node.
 * Can also store the socket, necessary to send messages.
 */
public class InfoNodeMulticast extends InfoNode {
	
	public InfoNodeMulticast() {
		super();
		try {
			InetSocketAddress group = new InetSocketAddress(Constants.MulticastGroup, Constants.portMulticast);
			NetworkInterface netIf = NetworkInterface.getByName("eth1");
			MulticastSocket multicastSocket = new MulticastSocket(Constants.portMulticast);
			multicastSocket.joinGroup(group, netIf);
			
			this.socket = multicastSocket;
			this.port = Constants.portMulticast;
			this.ip = socket.getLocalAddress();
			
			System.out.println("IP: " + this.ip);
			System.out.println("Remote Socket: " + multicastSocket.getRemoteSocketAddress());
			System.out.println("Local Socket: " + multicastSocket.getLocalSocketAddress());
			System.out.println("Interface: " + multicastSocket.getNetworkInterface());
			
			try {
				multicastSocket.setSoTimeout(Constants.refreshRate);
			} catch (SocketException e) {
				throw new RuntimeException(e);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


	@Override
	public String toString() {
		return "InfoNode{" +
				"ip=" + ip.toString() +
				", port=" + port +
				'}';
	}
}
