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
	private InfoNode cloud;
	private Map<String, TowerInfo> towersInfo;
	DatagramSocket cloudSendSocket = new DatagramSocket();
	
	
	public Server(InfoNode thisServer, List<String> carsInRange, InfoNode cloud,  Map<String, TowerInfo> towersInfo)
	{
		//this.cloud = cloud;
		this.thisServer = thisServer;
		this.carsInRange = new ArrayList<String>();
		this.towersInfo = new HashMap<>();
		this.cloud = cloud;
		this.towersInfo = new HashMap<>();
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
		MessageAndType message = new MessageAndType(this.cloud.ip, MessagesConstants.ServerInfoMessage, this.getAllTowersInfo());
		SendMessages.sendMessage(message, this.info.sendSocket());
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
				if (!this.carsInRange.contains(id)){
					this.carsInRange.add(id);
					sendToCloud(new MessageAndType(MessagesConstants.CarInRangeMessage, id.getBytes()));
				}
				System.out.println("Received Hello from car: " + id);
				break;
			case MessagesConstants.TowerHelloMessage:
				//System.out.println("Received Hello from tower");
				TowerInfo towersInfo = (TowerInfo) message.message;
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
		MessageAndType message = new MessageAndType(cloud.ip, MessagesConstants.CarInRangeMessage, result);
		SendMessages.sendMessage(message, this.cloudSendSocket());
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

	private void sendToCloud(MessageAndType message) {
		SendMessages.serverMessageCloud(this.thisServer.socket, message);
	}

}
