package Server;

import Common.Constants;
import Common.InfoNode;

import java.net.SocketException;
import java.net.UnknownHostException;


/**
 * 
 */
public class Main {
	
	public static void main(String[] args) throws UnknownHostException, SocketException
	{
		InfoNode cloudInfo;
		InfoNode thisServer;
		
		cloudInfo = new InfoNode(Constants.CloudIP, Constants.cloudPort, false);
		thisServer = new InfoNode(null, Constants.serverPort, true);
		
		Server server = new Server(thisServer, cloudInfo);
		server.run();
	}
}
