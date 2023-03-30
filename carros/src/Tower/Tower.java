package Tower;

import java.io.IOException;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import Common.Constants;
import Common.InfoNode;
import Common.TowerInfo;
import Common.Messages.MessageAndType;
import Common.Messages.MessagesConstants;
import Common.Messages.ReceiveMessages;
import Common.Messages.SendMessages;



public class Tower implements Runnable {
	
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
		HashMap<String, Integer> message = new HashMap<>();
		message.put(this.info.getId(), this.info.getHowManyCars()); //TODO
		SendMessages.towerHelloServer(this.info.sendSocket(), this.server);
		SendMessages.towerHelloCar(this.info.sendSocket());
		SendMessages.towerHelloCloud(this.info.sendSocket(), message); //TODO
	}
	
	private void receiveMessages()
	{
		while(true) {
			try {
				MessageAndType message = ReceiveMessages.receiveData(this.info.receiveSocket());
				handleMessage(message, this.server);
			} catch (IOException e) {
				//System.out.println("[Tower] Timeout passed. Nothing received.");
			}
		}
	}

	private void sendToServer(MessageAndType message)
	{
		SendMessages.towerHelloServer(this.info.sendSocket(), message); //TODO
	}
	
	private void handleMessage(MessageAndType message, InfoNode thisServer)
	{
		SendMessages.forwardMessage(message, this.info.sendSocket(), thisServer);
		if (message.type == MessagesConstants.CarInRangeMessage || message.type == MessagesConstants.AccidentMessage) {
			sendToServer(message);
		}
	}
	
	private static TimerTask wrap(Runnable r)
	{
		return new TimerTask() {
			@Override
			public void run() {r.run();}
		};
	}
}
