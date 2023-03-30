package Tower;

import Common.*;

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
 * Example: "t1 2001:9::20 40 40"
 */
public class Main {
	
	public static void main(String[] args) throws IOException
	{
		String name = args[0];
		
		// TODO: Change position tower
		InetAddress ipServer = Inet6Address.getByName(args[1]);
		Position pos = new Position(Integer.parseInt(args[2]), Integer.parseInt(args[3]));
		InfoNode thisServer = new InfoNode(ipServer, Constants.serverPort, false);
		TowerInfo thisTower = new TowerInfo(name, pos);
		
		Tower tower = new Tower(thisTower, thisServer);
		tower.run();
	}
}
