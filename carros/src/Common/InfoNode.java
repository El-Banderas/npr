package Common;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;


/**
 * Here we store the connection information about one node.
 * Can also store the socket, necessary to send messages.
 */
public class InfoNode
{
	public final InetAddress ip;
	public final int port;


	public InfoNode(InetAddress ip, int port) throws SocketException
	{
		this.port = port;
		this.ip = ip;
	}

	public InfoNode(DatagramSocket socket)
	{
		this.port = socket.getLocalPort();
		this.ip = socket.getLocalAddress();
	}


	@Override
	public String toString()
	{
		return "InfoNode{" +
				"ip=" + ip.toString() +
				", port=" + port +
				'}';
	}
}
