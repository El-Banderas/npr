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
	
	
	/**
	 * Topologia atual:
	 *
	 * T1 (8000) - S1 (9000)
	 * 						\
	 * 						Cloud (5000)
	 * 						/
	 * T2 (8001) - S2 (9001)
	 */
	
	
	/*	
	 * +-------------------------+------------------------------------------------------------+
	 * |     FF7E:230::1234      |                          Unicast                           |
	 * +-------------------------+------------------------------------------------------------+
	 * | Car (6000) <-> (8000) Tower (7000)   <->   (9000) Server (9000)   <->   (5000) Cloud |
	 * +-------------------------+------------------------------------------------------------+
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
