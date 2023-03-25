package Tower;

import Common.*;

import java.io.IOException;
import java.net.*;


/**
 * 	0: Name (unecessary)
 * 	1: IP Server
 * 	2: PosX
 * 	3: PosY
 * 	Example: "t1 2001:9::20 40 40"
 */
public class Main {
	
	public static void main(String[] args) throws IOException
	{
		String name = args[0];
		
		//towerIPInfo = new InfoNodeMulticast(true);
		InetAddress ipServer = Inet6Address.getByName(args[1]);
		
		System.out.println("Working Directory = " + System.getProperty("user.dir"));
		Position pos = new Position();
		System.out.println("Node Coordinates = " + pos.x + " " + pos.y);
		
		InfoNode thisServer = new InfoNode(ipServer, Constants.serverPort, false);
		TowerInfo thisTower = new TowerInfo(name, pos);
		
		Tower tower = new Tower(thisTower, thisServer);
		tower.run();
	}
}
