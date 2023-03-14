package Common;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Enumeration;


public final class Constants {
	
	public static boolean linux = false;
	public static int refreshRate = 500;

	/**
	 * Connections
	 */
	private static final String MCAST_ADDR = "FF7E:230::1234";
	public static InetAddress MulticastGroup;


	static {
		try {
			MulticastGroup = InetAddress.getByName(MCAST_ADDR);
		} catch (UnknownHostException e) {
			//throw new RuntimeException(e);
			System.out.println("Error creating multicast adress");
		}
	}
	public static int portMulticast = 8000;

	
	/**
	 * Map Constants in Windows?
	 */
	public static int minXmap = 0;
	public static int maxXmap = 100;
	public static int minYmap = 0;
	public static int maxYmap = 100;


	/**
	 * Tower
	 */
	public static int towerCommunicationRadius = 100;
	// When in linux, port of tower:
	public static int towerPort = 7000;

	
	/**
	 * Car
	 */
	public static int carPort = 6000;

	
	/**
	 * Tower
	 */
	// Used in Core
	public static InetAddress CloudIP;

	
	static {
		try {
			CloudIP = InetAddress.getByName("2001:8::10");
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
	}

	public static String getMyIp() {
		Enumeration<NetworkInterface> nets = null;
		try {
			nets = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			throw new RuntimeException(e);
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
					return inetAddress.toString().substring(1).split("%")[0];
					//System.out.println("Encontrado + " + myIP);
					//return myIp;
				}
			}
		}
		return null;
	}
}

