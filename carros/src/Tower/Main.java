package Tower;

import Common.*;

import java.io.IOException;
import java.net.*;


/**
 * 0: Name (TODO: temporary - vai ser gerado aleatoriamente ou lido do 'working directory')
 * 1: IP Server
 * 2: Posx (TODO: temporary - para coincidir com ficheiro; vai ser lido do .xy, e transmitido aos carros)
 * 3: Posy (TODO: temporary - para coincidir com ficheiro; vai ser lido do .xy, e transmitido aos carros)
 * 	Example: "t1 2001:9::20 40 40"
 */
public class Main
{
	public static void main(String[] args) throws IOException
	{
		// Gather server info
		InetAddress server_ip = Inet6Address.getByName(args[1]);
		InfoNode server_info = new InfoNode(server_ip, Constants.serverPort);

		// Gather tower info
		String tower_name = args[0]; //TODO: gerado aleatoriamente ou lido do 'working directory'
		Position tower_pos = new Position(Integer.parseInt(args[2]), Integer.parseInt(args[3])); // TODO: lido do .xy
		TowerInfo tower_info = new TowerInfo(tower_name, tower_pos);
		
		// Instanciate and run tower
		Tower tower = new Tower(tower_info, server_info);
		tower.run();
	}
}
