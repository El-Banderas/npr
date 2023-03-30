package Tower;

import Common.*;
import Server.Server;

import java.io.IOException;
import java.net.*;


//TODO
public class ExecuteTower
{
	private Server miniCloud;

	public ExecuteTower(Server miniCloud)
	{
		this.miniCloud = miniCloud;
	}
	
	
	public void receiveMessage(String message)
	{
		miniCloud.sendMessage(message);
	}
}
