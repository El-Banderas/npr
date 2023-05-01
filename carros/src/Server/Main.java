package Server;

import java.net.SocketException;
import java.net.UnknownHostException;

import Common.Constants;
import Common.InfoNode;
import Common.Position;
import Common.TowerInfo;


/**
 *
 */
public class Main
{
	public static void main(String[] args) throws UnknownHostException, SocketException
	{
		TowerInfo towerInfo = new TowerInfo("t1", new Position());
		InfoNode cloudInfo = new InfoNode(Constants.CloudIP, Constants.cloudPort);
		InfoNode thisServer = new InfoNode(null, Constants.serverPort);

		Server server = new Server(thisServer, cloudInfo, towerInfo);
		server.run();
	}
}
