package Common;

import java.io.IOException;
import java.net.MulticastSocket;

/**
 * Here we store the connection information about one node.
 * Can also store the socket, necessary to send messages.
 */
public class InfoNodeMulticast extends InfoNode {

	@Override
	public String toString() {
		return "InfoNode{" +
				"ip=" + ip.toString() +
				", port=" + port +
				'}';
	}

    public InfoNodeMulticast(boolean createSocket) {
        super();
        try {
            if (createSocket) {

                MulticastSocket multicastSocket = new MulticastSocket(Constants.portMulticast);
                multicastSocket.joinGroup(Constants.MulticastGroup);
                //multicastSocket.setInterface(Constants.MulticastGroup);

                this.socket = multicastSocket;
                this.port = Constants.portMulticast;
                this.ip =socket.getLocalAddress();
                System.out.println("Add multicast: " + this.ip);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
   }
