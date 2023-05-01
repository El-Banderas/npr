package Car;

import AWFullP.AWFullPacket;
import AWFullP.SendMessages;
import Common.Constants;

import java.net.DatagramSocket;
import java.util.TimerTask;

public class SendMessagePeriodically extends TimerTask
{
	private DatagramSocket socket;
	private AWFullPacket message;

	
	public SendMessagePeriodically(DatagramSocket socket, AWFullPacket message)
	{
		this.socket = socket;
		this.message = message;
	}

	
	@Override
	public void run()
	{
		System.out.println("[Send Message perio...]Send message");
		SendMessages.sendMessage(socket, Constants.MulticastGroup, Constants.portMulticast, message);
	}
}
