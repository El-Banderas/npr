package Cloud;

import Common.*;

import java.net.SocketException;
import java.net.UnknownHostException;


/**
 * 
 */
public class Main
{	
	public static void main(String[] args) throws UnknownHostException, SocketException
	{
		InfoNode cloudInfo = new InfoNode(Constants.getMyIp(), Constants.cloudPort);
		Cloud cloud = new Cloud(cloudInfo);
		cloud.run();
	}
}
