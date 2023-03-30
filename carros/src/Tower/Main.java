package Tower;

import Common.*;

import java.io.IOException;
import java.net.*;


/**
 * 1: Name (TODO: temporary - vai ser gerado aleatoriamente ou lido do 'working directory')
 * 2: IP Server
 * 3: Posx (TODO: temporary - para coincidir com ficheiro; vai ser lido do .xy, e transmitido aos carros)
 * 4: Posy (TODO: temporary - para coincidir com ficheiro; vai ser lido do .xy, e transmitido aos carros)
 * 	Example: "t1 2001:9::20 40 40"
 */
public class Main
{
	public static void main(String[] args) throws IOException
	{
		String name = args[0]; //TODO: gerado aleatoriamente ou lido do 'working directory'
		
		InetAddress ipServer = Inet6Address.getByName(args[1]);
		Position pos = new Position(Integer.parseInt(args[2]), Integer.parseInt(args[3])); // TODO: lido do .xy
		InfoNode thisServer = new InfoNode(ipServer, Constants.serverPort);
		TowerInfo thisTower = new TowerInfo(name, pos);
		
		Tower tower = new Tower(thisTower, thisServer);
		tower.run();
	}
}
