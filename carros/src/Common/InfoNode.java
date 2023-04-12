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
		this.ip = ip;
		this.port = port;
	}

	public InfoNode(DatagramSocket socket)
	{
		this.ip = socket.getLocalAddress();
		this.port = socket.getLocalPort();
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
