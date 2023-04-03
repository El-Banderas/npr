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
	public final DatagramSocket socket;
	public final InetAddress ip;
	public final int port;

	public InfoNodeMulticast() throws IOException
	{
		InetSocketAddress group = new InetSocketAddress(Constants.MulticastGroup, Constants.portMulticast);
		NetworkInterface netIf = NetworkInterface.getByName("eth1");
		MulticastSocket multicastSocket = new MulticastSocket(Constants.portMulticast);

		multicastSocket.joinGroup(group, netIf);
		//multicastSocket.joinGroup(Constants.MulticastGroup);
		multicastSocket.setSoTimeout(Constants.refreshRate);
		
		System.out.println(
				"Group Sock: " + group.toString() + "\n"
				+ "Interfaces: " + NetworkInterface.getNetworkInterfaces() + "\n"
				+ "Int DName: " + netIf.getDisplayName() + "\n"
				+ "Int Name: " + netIf.getName() + "\n"
				+ "Int idx: " + netIf.getIndex() + "\n"
				+ "Int Addr: " + netIf.getInetAddresses() + "\n"
				+ "Int Multi: " + netIf.supportsMulticast() + "\n"
				+ "Multi Int: " + multicastSocket.getNetworkInterface()
				);

		this.socket = multicastSocket;
		this.port = Constants.portMulticast;
		this.ip = Constants.MulticastGroup;
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
