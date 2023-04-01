package Cloud;

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
		InfoNode cloudInfo = new InfoNode(Constants.getMyIp(), Constants.cloudPort);
		Cloud cloud = new Cloud(cloudInfo);
		cloud.run();
	}
}
