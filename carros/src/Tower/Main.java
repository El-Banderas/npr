package Tower;

import Common.*;
import Server.ServerConstants;

import java.io.IOException;
import java.net.*;


/**
 * Arguments:
 * Windows
 * The IP of the cloud isn't necessary, is a constant stored in the CloudConstants file.
 * Name (unecessary) | This port | Port Server | PosX | PosY
 * The port must be the same as the file.
 * Example: "t1 8000 9000 40 40"
 *
 *
 * Linux, depois remover a posição, e acrescentar o IP da cloud, para saber a quem mandar mensagens
 * O server do lado direito é o "2001:9::20"
 * Name (unecessary) | IP Server | PosX | PosY
 * The port must be the same as the file.
 * Example: "t1 2001:9::20 40 40"
 */
public class Main {
	
	public static void main(String[] args) throws IOException
	{
		String name = args[0];
		Position pos;
		InfoNode thisServer;
		TowerInfo thisTower;
		
		if (Constants.core) {
			//towerIPInfo = new InfoNodeMulticast(true);
			// TODO: Change position tower
			InetAddress ipServer = Inet6Address.getByName(args[1]);
			pos = new Position(Integer.parseInt(args[2]), Integer.parseInt(args[3]));
			thisServer = new InfoNode(ipServer, ServerConstants.port, false);
			thisTower = new TowerInfo(name, pos);
		} else {
			pos = new Position(Integer.parseInt(args[3]), Integer.parseInt(args[4]));
			InfoNode infoNodo = new InfoNodeWindows(Integer.parseInt(args[1]), true);
			thisTower = new TowerInfo(name, infoNodo, pos);
			thisServer = new InfoNode(InetAddress.getByName("localhost"),Integer.parseInt(args[2]), false);
		}
		
		Tower tower = new Tower(thisTower, thisServer);
		tower.run();
	}
}
