package Server;

import java.net.SocketException;
import java.net.UnknownHostException;

import Common.Constants;
import Common.InfoNode;


/**
 * 
 */
public class Main
{
	public static void main(String[] args) throws UnknownHostException, SocketException
	{
		InfoNode cloudInfo;
		InfoNode thisServer;
		
		cloudInfo = new InfoNode(Constants.CloudIP, Constants.cloudPort);
		thisServer = new InfoNode(null, Constants.serverPort);
		
		Server server = new Server(thisServer, cloudInfo, 4, "t1"); //TODO
		server.run();
	}
}
