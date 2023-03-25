package Server;

import Common.Constants;
import Common.InfoNode;
import Common.Messages.MessageAndType;
import Common.Messages.MessagesConstants;
import Common.Messages.ReceiveMessages;

import static Common.Messages.SendMessages.serverHelloCloud;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class Server implements Runnable {
	
	private InfoNode thisServer;
	//private InfoNode cloud;
	private List<String> carsInRange;
	
	
	public Server(InfoNode thisServer, InfoNode cloud)
	{
		//this.cloud = cloud;
		this.thisServer = thisServer;
		this.carsInRange = new ArrayList<String>();
	}
	
	
	@Override
	public void run()
	{
		try {
			thisServer.socket.setSoTimeout(Constants.refreshRate);
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		
		Timer timer_1 = new Timer(false);
		timer_1.scheduleAtFixedRate(wrap(this::sendHellos), 0, Constants.refreshRate);
		
		Thread thread_1 = new Thread(this::receiveMessages);
		thread_1.start();
	}
	
	
	private void sendHellos()
	{
		serverHelloCloud(this.thisServer.socket);
	}
	
	private void receiveMessages()
	{
		while(true) {
			try {
				MessageAndType message = ReceiveMessages.receiveData(this.thisServer.socket);
				handleMessage(message);
			} catch (IOException e) {
				//System.out.println("[Server] Timeout passed. Nothing received.");
			}
		}
	}
	
	private void handleMessage(MessageAndType message)
	{
		switch (message.type) {
			case MessagesConstants.CarHelloMessage:
				String id = message.ipSender.toString(); //Usar ip em vez de id, para já (TODO)
				if (!this.carsInRange.contains(id))
					this.carsInRange.add(id);
				System.out.println("Received Hello from car: " + id);
				break;
			case MessagesConstants.TowerHelloMessage:
				//System.out.println("Received Hello from tower");
				break;
			case MessagesConstants.BreakMessage:
				//System.out.println("Received Break");
				break;
			case MessagesConstants.AccidentMessage:
				//System.out.println("Received Accident");
				break;
			default:
				//System.out.println("Received message, type unknown: " + message.type);
		}
	}
	
	public int getHowManyCars()
	{
		int result = this.carsInRange.size();
		this.carsInRange.clear();
		return result;
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
