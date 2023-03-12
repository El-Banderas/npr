package Common;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;

/**
 * Here we store the connection information about one node.
 * Can also store the socket, necessary to send messages.
 */
public class InfoNodeMulticast extends InfoNode {
	public InetAddress ip;
	public int port;
	public MulticastSocket socket;

	@Override
	public String toString() {
		return "InfoNode{" +
				"ip=" + ip.toString() +
				", port=" + port +
				'}';
	}

    public InfoNodeMulticast(boolean createSocket) {
        try {
            socket = new MulticastSocket(Constants.portCarsTowersLinux);
            socket.joinGroup(Constants.MulticastGroup);
            this.ip = socket.getLocalAddress();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
   }
