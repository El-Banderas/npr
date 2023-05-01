package Car;

import AWFullP.AWFullPacket;
import AWFullP.SendMessages;
import Common.Constants;
import Common.Position;

import java.net.DatagramSocket;
import java.util.TimerTask;

public class SendMessagePeriodically extends TimerTask
{
	private DatagramSocket socket;
	private AWFullPacket message;
	// The shared class is necessary to store how many messages were sent with destination.
	private SharedClass shared;
	private Position pos;

	
	public SendMessagePeriodically(DatagramSocket socket, AWFullPacket message, SharedClass shared, Position pos)
	{
		this.socket = socket;
		this.message = message;
		this.shared = shared;
		this.pos = pos;
	}

	
	@Override
	public void run()
	{
		shared.addSendMessages(message.getType(), pos);
		SendMessages.sendMessage(socket, Constants.MulticastGroup, Constants.portMulticast, message);
	}
}
