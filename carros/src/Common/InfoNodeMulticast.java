package Common;

import java.io.IOException;
import java.net.MulticastSocket;
import java.net.SocketException;

/**
 * Here we store the connection information about one node.
 * Can also store the socket, necessary to send messages.
 */
public class InfoNodeMulticast extends InfoNode {
	
	@SuppressWarnings("deprecation")
	public InfoNodeMulticast() {
		super();
		try {
			MulticastSocket multicastSocket = new MulticastSocket(Constants.portMulticast);
			multicastSocket.joinGroup(Constants.MulticastGroup); //deprecated
			//multicastSocket.setInterface(Constants.MulticastGroup);
			
			this.socket = multicastSocket;
			this.port = Constants.portMulticast;
			this.ip = socket.getLocalAddress();
			System.out.println("Add multicast: " + this.ip);
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
