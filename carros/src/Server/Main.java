package Server;

import java.net.SocketException;
import java.net.UnknownHostException;

import Common.*;


/**
 *
 */
public class Main
{
	public static void main(String[] args) throws UnknownHostException, SocketException
	{
		TowerInfoWithIP towerInfo = new TowerInfoWithIP("t1", new Position());
		InfoNode cloudInfo = new InfoNode(Constants.CloudIP, Constants.cloudPort);
		InfoNode thisServer = new InfoNode(null, Constants.serverPort);
		// ID is necessary to send info to cars.
		String id = Car.Main.idGenerator(8);
		Server server = new Server(thisServer, cloudInfo, towerInfo, id);
		server.run();
	}
}
