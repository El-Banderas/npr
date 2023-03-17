package Common;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;


/**
 * Only used to initialize InfoNodes in Windows, where the IP is localhost.
 */

public class InfoNodeWindows extends InfoNode{

	// Used to create towers
	public InfoNodeWindows(int port, boolean createSocket) throws UnknownHostException {
		super( InetAddress.getByName("localhost"), port, createSocket);
	}
	
	// Used to create car, port can be random, ip is localhost
	public InfoNodeWindows() {
		super();
		try {
			InetAddress ip = InetAddress.getByName("localhost");
			DatagramSocket socket= new DatagramSocket();
			int port = socket.getLocalPort();
			this.ip = ip;
			this.port = port;
			this.socket = socket;
		} catch (SocketException e) {
			//throw new RuntimeException(e);
			System.out.println("[Error] Creating socket");
		} catch (UnknownHostException e) {
			//throw new RuntimeException(e);
			System.out.println("[Error] Getting localhost");
		}
	}
}
