package Common;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Enumeration;


public final class Constants
{
	public static int refreshRate = 1000; //ms
	public static int longRefreshRate = 10000; //ms

	/**
	 * This constant is used in tests.
	 * The radius is the maximum distance between the position that trigger's the action and the current position of vehicle.
	 */
	public static int radius_test = 10; //ms


	/*
	 * +-------------------------+------------------------------------------------------------+
	 * |     FF7E:230::1234      |                          Unicast                           |
	 * +-------------------------+------------------------------------------------------------+
	 * | Car (8000) <-> (8000) Tower (7000)   <->   (9000) Server (9000)   <->   (5000) Cloud |
	 * +-------------------------+------------------------------------------------------------+
	 */


	public static int cloudPort = 5000;
	public static int towerPort = 7000;
	public static int portMulticast = 8000;
	public static int serverPort = 9000;


	private static final String MCAST_ADDR = "FF7E:230::1234";
	public static InetAddress MulticastGroup;
	static {
		try {
			MulticastGroup = InetAddress.getByName(MCAST_ADDR);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}


	public static InetAddress CloudIP;
	static {
		try {
			CloudIP = InetAddress.getByName("2001:8::10");
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}


	public static InetAddress getMyIp()
	{
		Enumeration<NetworkInterface> nets = null;

		try {
			nets = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		for (NetworkInterface netint : Collections.list(nets)) {

			//out.println();
			//out.printf("Display name: %s\n", netint.getDisplayName());
			//out.printf("Name: %s\n", netint.getName());
			Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();

			for (InetAddress inetAddress : Collections.list(inetAddresses)) {
				//System.out.println("InetAddress: " + inetAddress);
				if (inetAddress.toString().contains("2001:")) {

					//String myIP = inetAddress.toString().split("%")[0];
					//System.out.println("Encontrado + " + myIP);
					//return myIp;

					//return inetAddress.toString().substring(1).split("%")[0];
					return inetAddress;
				}
			}
		}
		return null;
	}
}
