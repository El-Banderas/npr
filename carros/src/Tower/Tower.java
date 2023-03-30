package Tower;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import AWFullP.MessagesConstants;
import AWFullP.ReceiveMessages;
import AWFullP.SendMessages;
import AWFullP.AWFullPacket;
import Common.Constants;
import Common.InfoNode;
import Common.TowerInfo;


public class Tower implements Runnable
{
	private TowerInfo info;
	private InfoNode server;
	
	
	public Tower(TowerInfo info, InfoNode server)
	{
		this.info = info;
		this.server = server;
	}
	
	
	@Override
	public void run()
	{
		// Send Hellos
		Timer timer_1 = new Timer(false);
		timer_1.scheduleAtFixedRate(wrap(this::sendHellos), 0, Constants.refreshRate);

		// Receive Messages
		Thread thread_1 = new Thread(this::receiveMessages);
		thread_1.start();
	}
	
	
	private void sendHellos()
	{
		SendMessages.towerHelloServer(this.info.inside_socket, this.server);
		SendMessages.towerHelloCar(this.info.outside_socket);
	}
	
	private void receiveMessages()
	{
		while(true) {
			try {
				AWFullPacket message = ReceiveMessages.receiveData(this.info.outside_socket);
				handleMessage(message);
			} catch (IOException ignore) {
				//System.out.println("[Tower] Timeout passed. Nothing received.");
			}
		}
	}
	
	private void handleMessage(AWFullPacket message)
	{
		if (message.type == MessagesConstants.CarInRangeMessage || message.type == MessagesConstants.AccidentMessage) {
			sendToServer(message);
		}
	}

	private void sendToServer(AWFullPacket message)
	{
		SendMessages.sendMessage(this.info.inside_socket, this.server.ip, this.server.port, message);
	}
	
	private static TimerTask wrap(Runnable r)
	{
		return new TimerTask() {
			@Override
			public void run() {r.run();}
		};
	}
}
