package Server;

import Common.Constants;
import Common.InfoNode;
import Common.TowerInfo;

import static AWFullP.SendMessages.serverHelloCloud;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import AWFullP.MessagesConstants;
import AWFullP.ReceiveMessages;
import AWFullP.SendMessages;
import AWFullP.AWFullPacket;


public class Server implements Runnable
{
	// Node information
	private InfoNode me;
	private InfoNode cloud;
	private Map<String, TowerInfo> towersInfo;

	// Connection information
	private DatagramSocket socket;
	
	// Others
	private List<String> carsInRange;
	
	
	public Server(InfoNode server, InfoNode cloud) throws SocketException
	{
		this.me = server;
		this.cloud = cloud;
		this.towersInfo = new HashMap<>();
		
		this.socket = new DatagramSocket(Constants.serverPort);
		
		this.carsInRange = new ArrayList<String>();
	}
	
	
	@Override
	public void run()
	{
		try {
			socket.setSoTimeout(Constants.refreshRate);
		} catch (SocketException e1) {
			e1.printStackTrace();
			System.exit(-1);
		}
		
		Timer timer_1 = new Timer(false);
		timer_1.scheduleAtFixedRate(wrap(this::sendHellos), 0, Constants.refreshRate);
		
		Thread thread_1 = new Thread(this::receiveMessages);
		thread_1.start();
	}
	
	
	private void sendHellos()
	{
		AWFullPacket message = new AWFullPacket(MessagesConstants.ServerInfoMessage, this.getAllTowersInfo().toString().getBytes(), this.socket.getLocalAddress()); //TODO
		SendMessages.sendMessage(this.socket, this.cloud.ip, this.cloud.port, message);
	}
	
	private void receiveMessages()
	{
		while(true) {
			try {
				AWFullPacket message = ReceiveMessages.receiveData(this.socket);
				handleMessage(message);
			} catch (IOException ignore) {
				// TIMEOUT
				//System.out.println("[Server] Timeout passed. Nothing received.");
			}
		}
	}
	
	private void handleMessage(AWFullPacket message)
	{
		switch (message.type) {
			case MessagesConstants.CAR_HELLO:
				String id = message.ipSender.toString(); //Usar ip em vez de id, para já (TODO)
				if (!this.carsInRange.contains(id)){
					this.carsInRange.add(id);
					sendToCloud(new AWFullPacket(MessagesConstants.CarInRangeMessage, id.getBytes(), message.ipSender)); //TODO
				}
				System.out.println("Received Hello from car: " + id);
				break;
			case MessagesConstants.TowerHelloMessage:
				//System.out.println("Received Hello from tower");
				TowerInfo towersInfo = (TowerInfo) message.content; //TODO
				this.towersInfo.put(towersInfo.getName(), towersInfo);
				break;
			case MessagesConstants.BreakMessage:
				//System.out.println("Received Break");
				break;
			case MessagesConstants.AccidentMessage:
				//System.out.println("Received Accident");
				sendToCloud(message);
				break;
			default:
				//System.out.println("Received message, type unknown: " + message.type);
				break;
		}
	}
	
	private Map<String, TowerInfo> getAllTowersInfo() {
		return this.towersInfo;
	}
	
	public int getHowManyCars()
	{
		int result = this.carsInRange.size();
		AWFullPacket message = new AWFullPacket(MessagesConstants.CarInRangeMessage, Integer.toString(result).getBytes(), cloud.ip); //TODO
		this.sendToCloud(message);
		this.carsInRange.clear();
		return result;
	}
	
	private void sendToCloud(AWFullPacket message)
	{
		SendMessages.sendMessage(this.socket, this.cloud.ip, this.cloud.port, message);
	}
	
	private static TimerTask wrap(Runnable r)
	{
		return new TimerTask() {
			@Override
			public void run() {r.run();}
		};
	}
}
