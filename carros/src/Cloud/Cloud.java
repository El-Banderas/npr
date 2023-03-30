package Cloud;

import Common.Constants;
import Common.InfoNode;

import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.TimerTask;

import AWFullP.MessagesConstants;
import AWFullP.ReceiveMessages;
import AWFullP.AWFullPacket;


public class Cloud implements Runnable
{
	private InfoNode cloud;
	private ArrayList<String> history;
	
	
	public Cloud(InfoNode cloud)
	{
		this.cloud = cloud;
		this.history = new ArrayList<String>();
	}
	
	
	@Override
	public void run()
	{
		try {
			cloud.socket.setSoTimeout(Constants.refreshRate);
		} catch (SocketException e1) {
			e1.printStackTrace();
		}
		
		Thread thread_1 = new Thread(this::receiveMessages);
		thread_1.start();
	}
	
	
	private void receiveMessages()
	{
		while(true) {
			try {
				AWFullPacket message = ReceiveMessages.receiveData(this.cloud.socket);
				handleMessage(message);
			} catch (IOException e) {
				//System.out.println("[Cloud] Timeout passed. Nothing received.");
			}
		}
	}
	
	private void handleMessage(AWFullPacket message)
	{
		switch(message.type) {
			case MessagesConstants.ServerHelloMessage:
				//System.out.println("Received Hello from server");
				break;
			case MessagesConstants.CarInRangeMessage:
				String id = new String(message.content); //TODO
				history.add("Car in range: " + id);
				break;
			case MessagesConstants.AccidentMessage:
				String location = new String(message.content); //TODO
				history.add("Accident at location: " + location);
				break;
			default:
				//System.out.println("Received message, type unknown: " + message.type);
		}
	}

	public ArrayList<String> getHistory()
	{
		return this.history;
	}
	
	private static TimerTask wrap(Runnable r)
	{
		return new TimerTask() {
			@Override
			public void run() {r.run();}
		};
	}
}
