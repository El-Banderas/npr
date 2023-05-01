package Tower;

import java.io.IOException;
import java.net.InetAddress;

import Common.Constants;
import Common.InfoNode;
import Common.Position;
import Common.TowerInfo;


/**
 * 0: Name (TODO: gerado aleatoriamente ou lido do 'working directory', e transmitido aos carros)
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
		InetAddress server_ip = InetAddress.getByName(args[1]);
		InfoNode server_info = new InfoNode(server_ip, Constants.serverPort);

		// Gather tower info
		String tower_name = args[0]; //TODO: gerado aleatoriamente ou lido do 'working directory', e transmitido aos carros
		Position tower_pos = new Position(); // TODO: lido do .xy, e transmitido aos carros
		TowerInfo tower_info = new TowerInfo(tower_name, tower_pos);

		// Instanciate and run tower
		Tower tower = new Tower(tower_info, server_info);
		tower.run();
	}
	
	
	/*private static String idGenerator(int n)
	{
		String alphaNumeric = "0123456789" + "abcdefghijklmnopqrstuvxyz";

		StringBuilder sb = new StringBuilder(n);

		for (int i = 0; i < n; i++) {
			int index = (int)(alphaNumeric.length() * Math.random());
			sb.append(alphaNumeric.charAt(index));
		}

		return sb.toString();
	}*/
}
