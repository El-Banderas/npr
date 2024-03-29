package Common;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;

/**
 * Here we store the connection information about one node.
 * Can also store the socket, necessary to send messages.
 */
public class InfoNodeMulticast
{
	public final MulticastSocket socket;
	public final InetAddress ip;
	public final int port;

	public InfoNodeMulticast(String interfaceName) throws IOException
	{
		InetSocketAddress group = new InetSocketAddress(Constants.MulticastGroup, Constants.portMulticast);
		NetworkInterface netIf = NetworkInterface.getByName(interfaceName);
		MulticastSocket multicastSocket = new MulticastSocket(Constants.portMulticast);

		multicastSocket.joinGroup(group, netIf);
		multicastSocket.setSoTimeout(Constants.refreshRate);
		multicastSocket.setNetworkInterface(netIf);
		
		this.socket = multicastSocket;
		this.port = Constants.portMulticast;
		this.ip = Constants.MulticastGroup;
	}


	@Override
	public String toString()
	{
		return new String(
				"{"
			+	"ip=" + (ip != null ? ip.toString() : "null")
			+	", port=" + port
			+	"}"
			);
	}
}
