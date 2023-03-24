package Car;

import Common.CarInfo;
import Common.Constants;
//import Common.TowerInfo;
import Common.Messages.MessageAndType;
import Common.Messages.MessagesConstants;
import Common.Messages.ReceiveMessages;
import Common.Messages.SendMessages;

import java.io.IOException;
import java.net.*;
//import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class Car implements Runnable {
	
	private CarInfo info;
	private DatagramSocket sendSocket;
	private DatagramSocket receiveSocket;
	private InetAddress myIp;
	private SharedClass shared;
	//private List<TowerInfo> towers;

	
	public Car(CarInfo info, SharedClass shared/*, List<TowerInfo> towers*/) {
		this.info = info;
		this.receiveSocket = info.receiveSocket();
		this.sendSocket = info.sendSocket();
		this.shared = shared;
		try {
			//System.out.println("My IP: "+ Constants.getMyIp());
			myIp = Inet6Address.getByName(Constants.getMyIp());
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
		//this.towers = towers;
	}
	
	
	@Override
	public void run()
	{
		Timer timer = new Timer(false);
		timer.scheduleAtFixedRate(wrap(this::sendHellos), 0, Constants.refreshRate);
		
		Thread thread_1 = new Thread(this::receiveMessages);
		thread_1.start();
	}
	
	
	private void sendHellos()
	{
		SendMessages.carHellos(this.sendSocket, this.info);
	}
	
	private void receiveMessages()
	{
		while (true) {
			try {
				this.info.pos.updatePosition();
				//System.out.println("Posição atual: " + info.pos.x + " | " + info.pos.y);
				MessageAndType message = ReceiveMessages.receiveData(this.receiveSocket);
				handleMessage(message);
			} catch (IOException e) {
				this.shared.addEntryMessages(MessagesConstants.Timeout);
			}
		}
	}
	
	private void handleMessage(MessageAndType message) throws UnknownHostException
	{
		if (message.ipSender.equals(myIp) ) {
			// There are no timeouts, because we always receive message from ourselves
			shared.addEntryMessages(MessagesConstants.Timeout);
			return;
		}
		shared.addEntryMessages(message.type);
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
