package Server;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import AWFullP.AWFullPacket;
import AWFullP.MessageConstants;
import AWFullP.ReceiveMessages;
import AWFullP.SendMessages;
import AWFullP.AppLayer.AWFullPCarAccident;
import AWFullP.AppLayer.AWFullPCarHello;
import AWFullP.AppLayer.AWFullPCarInRange;
//import AWFullP.SendMessages;
import Common.Constants;
import Common.InfoNode;
import Common.TowerInfo;


public class Server implements Runnable
{
	private static Logger logger =  Logger.getLogger("npr.server");
	
	// Node information
	//private InfoNode me;
	private InfoNode cloud;
	private TowerInfo tower;

	// Connection information
	private DatagramSocket socket;

	// Others
	private List<String> carsInRange;
	
	
	public Server(InfoNode server, InfoNode cloud, TowerInfo tower) throws SocketException
	{
		//this.me = server;
		this.cloud = cloud;
		this.tower = tower;

		this.socket = new DatagramSocket(Constants.serverPort);

		this.carsInRange = new ArrayList<>();
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
		SendMessages.serverHelloCloud(socket, cloud);
		//AWFullPacket message = new AWFullPacket(MessagesConstants.ServerInfoMessage, this.getAllTowersInfo().toString().getBytes(), this.socket.getLocalAddress()); //TODO
		//SendMessages.sendMessage(this.socket, this.cloud.ip, this.cloud.port, message);
	}
	
	private void receiveMessages()
	{
		while(true) {
			try {
				AWFullPacket message = ReceiveMessages.receiveData(this.socket);
				handleMessage(message);
			} catch (IOException ignore) {
				// TIMEOUT
				//logger.info("Timeout passed. Nothing received.");
			}
		}
	}
	
	private void handleMessage(AWFullPacket message)
	{
		switch (message.appLayer.getType()) {
		
			case MessageConstants.CAR_HELLO:
				AWFullPCarHello aw_ch = (AWFullPCarHello) message.appLayer;
				String carID_ch = aw_ch.getCarID();
				if (!this.carsInRange.contains(carID_ch)){
					this.carsInRange.add(carID_ch);
					//TODO: Se está a enviar em batches então não fazer isto sempre:
					//AWFullPCarInRange aw_cir = new AWFullPCarInRange(this.tower.getName(), carID_ch);
					//sendToCloud(new AWFullPacket(aw_cir));
					checkAndSendBatch();
				}
				logger.info("Received Hello from car: " + carID_ch);
				break;
				
			case MessageConstants.TOWER_HELLO:
				logger.info("Received Hello from tower");
				//TowerInfo towersInfo = (TowerInfo) message.content; //TODO
				//this.towersInfo.put(towersInfo.getName(), towersInfo); //TODO
				break;
				
			case MessageConstants.CAR_BREAK:
				logger.info("Received Break");
				break;
				
			case MessageConstants.CAR_ACCIDENT:
				logger.info("Received Accident");
				AWFullPCarAccident aw_ca = (AWFullPCarAccident) message.appLayer;
				sendToCloud(new AWFullPacket(aw_ca));
				break;
				
			default:
				logger.info("Received unknown message: " + message.toString());
		}
	}
	
	//TODO: Assim está a enviar um a um, e não um batch. Vai ser preciso criar um novo tipo de pacote para batches
	private void checkAndSendBatch() {
		if (carsInRange.size() >= MessageConstants.BATCH_SIZE) {
			for(String carID : carsInRange) {
				AWFullPCarInRange aw_cir = new AWFullPCarInRange(this.tower.getName(), carID);
				sendToCloud(new AWFullPacket(aw_cir));
			}
			carsInRange.clear();
		}
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
