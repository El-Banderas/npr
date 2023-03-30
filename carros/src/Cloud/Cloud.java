package Cloud;

import Common.Constants;
import Common.InfoNode;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.TimerTask;

import AWFullP.MessagesConstants;
import AWFullP.ReceiveMessages;
import AWFullP.AWFullPacket;


public class Cloud implements Runnable
{
	// Node information
	private InfoNode me;
	
	// Connection information
	private DatagramSocket socket;
	
	// Others
	private ArrayList<String> history;
	
	
	public Cloud(InfoNode cloud) throws SocketException
	{
		this.me = cloud;
		
		this.socket = new DatagramSocket(cloud.port, cloud.ip);
		
		this.history = new ArrayList<String>();
	}
	
	
	@Override
	public void run()
	{
		try {
			this.socket.setSoTimeout(Constants.refreshRate);
		} catch (SocketException e1) {
			e1.printStackTrace();
			System.exit(-1);
		}
		
		Thread thread_1 = new Thread(this::receiveMessages);
		thread_1.start();
	}
	
	
	private void receiveMessages()
	{
		while(true) {
			try {
				AWFullPacket message = ReceiveMessages.receiveData(this.socket);
				handleMessage(message);
			} catch (IOException ignore) {
				// TIMEOUT
				//System.out.println("Timeout passed. Nothing received.");
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
