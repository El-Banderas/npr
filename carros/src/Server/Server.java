package Server;

import Common.Constants;
import Common.InfoNode;
import Common.Messages.MessageAndType;
import Common.Messages.MessagesConstants;
import Common.Messages.ReceiveMessages;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class Server implements Runnable {
	
	private InfoNode thisServer;
	private InfoNode cloud;
	private List<String> carsInRange;
	
	
	public Server(InfoNode thisServer, InfoNode cloud)
	{
		this.cloud = cloud;
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
		
		TimerTask timerTask = new sendHellos(this.thisServer.socket);
		Timer timer = new Timer(true);
		timer.scheduleAtFixedRate(timerTask, 0, Constants.refreshRate);
		
		while(true) {
			try {
				MessageAndType message = ReceiveMessages.receiveData(thisServer.socket);
				handleMessage(message);
			} catch (IOException e) {
				System.out.println("[Server] Timeout passed. Nothing received.");
				System.out.println("Receiving in: " + thisServer.socket.getLocalAddress() + " | " + thisServer.socket.getLocalPort());
			}
		}
	}
	
	private void handleMessage(MessageAndType message)
	{
		switch (message.type) {
			case MessagesConstants.CarHelloMessage:
				String id = message.ipSender.toString(); //Usar ip em vez de id, para já
				if (!this.carsInRange.contains(id))
					this.carsInRange.add(id);
				System.out.println("Received Hello from car");
				break;
			case MessagesConstants.TowerHelloMessage:
				System.out.println("Received Hello from tower");
				break;
			case MessagesConstants.BreakMessage:
				System.out.println("Received Break");
				break;
			case MessagesConstants.AccidentMessage:
				System.out.println("Received Accident");
				break;
			default:
				System.out.println("Received message, type unkown: " + message.type);
		}
	}
	
	
	public int getHowManyCars() {return this.carsInRange.size();}
}
