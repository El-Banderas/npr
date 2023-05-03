package Server;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import AWFullP.AWFullPacket;
import AWFullP.AppLayer.*;
import AWFullP.MessageConstants;
import AWFullP.ReceiveMessages;
import AWFullP.SendMessages;
import Common.Constants;
import Common.InfoNode;
import Common.TowerInfo;


public class Server implements Runnable
{
	private static Logger logger = Logger.getLogger("npr.server");
	static {
		ConsoleHandler handler = new ConsoleHandler();
		handler.setFormatter(new SimpleFormatter());
		handler.setLevel(Level.ALL);
		logger.addHandler(handler);
		
		logger.setLevel(Level.ALL);
		Logger.getLogger("npr.messages.received").setLevel(Level.FINE);
		Logger.getLogger("npr.messages.sent").setLevel(Level.FINE);
	}
	
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
		logger.config(server.toString());
		logger.config(cloud.toString());
		logger.config(tower.toString());
		
		//this.me = server;
		this.cloud = cloud;
		this.tower = tower;

		this.socket = new DatagramSocket(Constants.serverPort);

		this.carsInRange = new ArrayList<>();

		// Initial message so the cloud knows this server and tower

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
		timer_1.scheduleAtFixedRate(wrap(this::sendBatch), 0, Constants.refreshRate); //fazer flush regularmente
		
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
				//logger.info("Timeout passed. Nothing received.");
			}
		}
	}
	
	private void handleMessage(AWFullPacket message)
	{
		//TODO: filtrar mensagens de outras torres (if (message.getTowerID() != this.tower.getName()) return)
		
		switch (message.appLayer.getType()) {
			// Update position of tower with announce from tower.
			case MessageConstants.TOWER_ANNOUNCE:
				AWFullPTowerAnnounce aw_ta = (AWFullPTowerAnnounce) message.appLayer;
				this.tower.setPos(aw_ta.getPos());
				sendToCloud(new AWFullPacket(aw_ta));
				break;

			case MessageConstants.CAR_IN_RANGE:
				AWFullPCarInRange aw_cir = (AWFullPCarInRange) message.appLayer;
				//if(this.tower.getName() != aw_cir.getTowerID()) break;
				String carID_cir = aw_cir.getCarID();
				if (!this.carsInRange.contains(carID_cir)) {
					this.carsInRange.add(carID_cir);
					checkAndSendBatch();
				}
				break;
				
			case MessageConstants.CAR_ACCIDENT:
				AWFullPCarAccident aw_ca = (AWFullPCarAccident) message.appLayer;
				sendToCloud(new AWFullPacket(aw_ca));
				break;
			case MessageConstants.AMBULANCE_PATH:
				AWFullPAmbPath aw_ap = (AWFullPAmbPath) message.appLayer;
				//if(this.tower.getName() != aw_ca.getTowerID()) break;
				sendToCloud(new AWFullPacket(aw_ap));
				break;
			case MessageConstants.CLOUD_AMBULANCE_PATH:
				AWFullPCloudAmbulanceServer aw_cap = (AWFullPCloudAmbulanceServer) message.appLayer;
				//if(this.tower.getName() != aw_ca.getTowerID()) break;
				handlePathAmbulanceFromCloud(aw_cap);
				break;

			default:
				logger.warning("Received unexpected message: " + message.toString());
		}
	}

	private void handlePathAmbulanceFromCloud(AWFullPCloudAmbulanceServer awCap) {
		// TODO: Falta agora adicionar um TimerTask para depois enviar para a torre quando for preciso
		System.out.println("Recebeu info da cloud para avisar de ambulância");
		System.out.println(awCap);
		System.out.println(awCap.pos);
		Timestamp now = new Timestamp(System.currentTimeMillis());

		int delay = awCap.whenToSend.compareTo(now);
		System.out.println("Delay: " + delay);
		new java.util.Timer().schedule(
				new java.util.TimerTask() {
					@Override
					public void run() {
						// your code here
					}
				},
				delay
		);
	}

	private void checkAndSendBatch()
	{
		if (carsInRange.size() >= MessageConstants.MAX_BATCH_SIZE)
			this.sendBatch();
	}
	
	private void sendBatch()
	{
		if (carsInRange.size() == 0) return;
		
		List<String> batch = new ArrayList<String>(MessageConstants.MAX_BATCH_SIZE);
		for(int i = 0; i < MessageConstants.MAX_BATCH_SIZE && i < carsInRange.size(); i++) {
			batch.add(carsInRange.get(0));
			carsInRange.remove(0);
		}
		
		SendMessages.serverInfoBatchCloud(socket, tower, batch, cloud);
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
