package Server;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
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
import AWFullP.FwdLayer.AWFullPFwdLayer;
import AWFullP.MessageConstants;
import AWFullP.ReceiveMessages;
import AWFullP.SendMessages;
import Common.*;


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
	private TowerInfoWithIP tower;

	// Connection information
	private DatagramSocket socket;

	// Others
	private List<String> carsInRange;

	// ID is necessary to send messages to cars
	private final String id;
	private int sequenceNumber;

	
	public Server(InfoNode server, InfoNode cloud, TowerInfoWithIP tower, String id) throws SocketException
	{
		logger.config(server.toString());
		logger.config(cloud.toString());
		logger.config(tower.toString());
		
		//this.me = server;
		this.cloud = cloud;
		this.tower = tower;

		this.socket = new DatagramSocket(Constants.serverPort);

		this.carsInRange = new ArrayList<>();
		this.id = id;
		this.sequenceNumber = 0;
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
		timer_1.scheduleAtFixedRate(wrap(this::sendBatch), 0, Constants.longRefreshRate); //fazer flush regularmente (mas com menos frequencia)
		
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
		switch (message.appLayer.getType()) {
			// Update position of tower with announce from tower.
			case MessageConstants.TOWER_ANNOUNCE -> {
				AWFullPTowerAnnounce aw_ta = (AWFullPTowerAnnounce) message.appLayer;
				this.tower.setPos(aw_ta.getPos());
				this.tower.setIP(message.sender);
				sendToCloud(new AWFullPacket(aw_ta));
			}
			case MessageConstants.CAR_IN_RANGE -> {
				AWFullPCarInRange aw_cir = (AWFullPCarInRange) message.appLayer;
				if (!this.tower.getName().equals(aw_cir.getTowerID())) break; // Filter packets from other zones
				String carID_cir = aw_cir.getCarID();
				if (!this.carsInRange.contains(carID_cir)) {
					this.carsInRange.add(carID_cir);
					checkAndSendBatch();
				}
			}
			case MessageConstants.CAR_ACCIDENT -> {
				AWFullPCarAccident aw_ca = (AWFullPCarAccident) message.appLayer;
				if (!this.tower.getName().equals(aw_ca.getTowerID())) break; // Filter packets from other zones
				sendConfirmationCar(message);
				sendToCloud(new AWFullPacket(aw_ca));
			}
			case MessageConstants.AMBULANCE_PATH -> {
				AWFullPAmbPath aw_ap = (AWFullPAmbPath) message.appLayer;
				sendToCloud(new AWFullPacket(aw_ap));
				sendConfirmationCar(message);
			}
			case MessageConstants.CLOUD_AMBULANCE_PATH -> {
				AWFullPCloudAmbulanceServer aw_cap = (AWFullPCloudAmbulanceServer) message.appLayer;
				handlePathAmbulanceFromCloud(aw_cap);
			}
			default -> logger.warning("Received unexpected message: " + message.toString());
		}
	}

	// To confirm that the message was received
	private void sendConfirmationCar(AWFullPacket message) {
		message.forwardInfo.setThisMessageAsAck(); // AWFullPFwdLayer
		System.out.println("Send confirmation to tower (and then cars)");
		System.out.println(tower.ip);
		System.out.println(socket.getLocalAddress());
		SendMessages.sendMessage(socket, tower.ip, Constants.towerPort, message,message.forwardInfo );


	}

	private void handlePathAmbulanceFromCloud(AWFullPCloudAmbulanceServer awCap) {
		// TODO: Falta agora adicionar um TimerTask para depois enviar para a torre quando for preciso
		System.out.println("Recebeu info da cloud para avisar de ambulância, pos: " + awCap.pos);
		Timestamp now = new Timestamp(System.currentTimeMillis());

		long delay = awCap.whenToSend.getTime() - now.getTime();
		System.out.println("Delay: " + delay);
		new java.util.Timer().schedule(
				new java.util.TimerTask() {
					@Override
					public void run() {
						// your code here
						float distance = (float) Position.distance(tower.getPosition(), awCap.pos);
						AWFullPFwdLayer fwrInfo = new AWFullPFwdLayer(MessageConstants.TTLAccidentMessage, awCap.pos, distance, id , getAndIncrementSeqNumber());
						System.out.println("Send amb info to tower");
						SendMessages.sendAmbulanceInfoToTower(socket, tower.ip, fwrInfo, awCap);
					}
				},
				delay
		);
	}

	private int getAndIncrementSeqNumber() {
		return this.sequenceNumber++;
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
