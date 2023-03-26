package Tower;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import Common.Constants;
import Common.InfoNode;
import Common.TowerInfo;
import Common.Messages.MessageAndType;
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
		SendMessages.towerHelloServer(this.info.sendSocket(), this.server);
		SendMessages.towerHelloCar(this.info.sendSocket());
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
	
	private void handleMessage(MessageAndType message, InfoNode thisServer)
	{
		System.out.println("Recebeu mensagem tipo: " + message.type);
		SendMessages.forwardMessage(message, this.info.sendSocket(), thisServer);
	}
	
	private static TimerTask wrap(Runnable r) {
		return new TimerTask() {
			@Override
			public void run() {
				r.run();
			}
		};
	}
}
