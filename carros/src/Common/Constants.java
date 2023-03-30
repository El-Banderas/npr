package Common;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Enumeration;


public final class Constants
{
	public static int refreshRate = 1000;
	
	
	/**
	 * Topologia atual:
	 *
	 * T1 (8000) - S1 (9000)
	 * 						\
	 * 						Cloud (5000)
	 * 						/
	 * T2 (8001) - S2 (9001)
	 */
	
	
	public static int cloudPort = 5000;
	public static int carPort = 6000;
	public static int towerPort = 7000;
	public static int portMulticast = 8000;
	public static int serverPort = 9000;
	
	
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
