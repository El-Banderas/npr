package Tower;

import Common.*;

import java.io.IOException;
import java.net.*;


/**
 * 	1: IP Server
 * 	Example: "2001:9::20"
 */
public class Main {
	
	public static void main(String[] args) throws IOException
	{
		String name = idGenerator(8);
		
		InetAddress ipServer = Inet6Address.getByName(args[0]);
		
		System.out.println("Working Directory = " + System.getProperty("user.dir"));
		Position pos = new Position();
		System.out.println("Node Coordinates = " + pos.x + " " + pos.y);
		
		InfoNode thisServer = new InfoNode(ipServer, Constants.serverPort, false);
		TowerInfo thisTower = new TowerInfo(name, pos);
		
		Tower tower = new Tower(thisTower, thisServer);
		tower.run();
	}
	
	
	private static String idGenerator(int n) {
		String alphaNumeric = "0123456789" + "abcdefghijklmnopqrstuvxyz";

		StringBuilder sb = new StringBuilder(n);
		
		for (int i = 0; i < n; i++) {
			int index = (int)(alphaNumeric.length() * Math.random());
			sb.append(alphaNumeric.charAt(index));
		}

		return sb.toString();
	}
}
