package Tower;

import java.io.IOException;
import java.net.DatagramSocket;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import AWFullP.AWFullPacket;
import AWFullP.AppLayer.AWFullPCloudAmbulanceServer;
import AWFullP.AppLayer.AWFullPTowerAnnounce;
import AWFullP.FwdLayer.AWFullPFwdLayer;
import AWFullP.MessageConstants;
import AWFullP.ReceiveMessages;
import AWFullP.SendMessages;
import AWFullP.AppLayer.AWFullPCarHello;
import AWFullP.AppLayer.AWFullPCarInRange;
import Common.Constants;
import Common.InfoNode;
import Common.InfoNodeMulticast;
import Common.TowerInfo;



public class Tower implements Runnable
{
	private static Logger logger = Logger.getLogger("npr.tower");
	static {
		ConsoleHandler handler = new ConsoleHandler();
		handler.setFormatter(new SimpleFormatter());
		handler.setLevel(Level.ALL);
		logger.addHandler(handler);
		
		logger.setLevel(Level.ALL);
		Logger.getLogger("npr.messages.received").setLevel(Level.WARNING);
		Logger.getLogger("npr.messages.sent").setLevel(Level.FINE);
	}
	
	// Node information
	private TowerInfo me;
	private InfoNode local_server;

	// Connection information
	private DatagramSocket wlan_socket;
	private DatagramSocket vanet_socket;
	private AWFullPFwdLayer fwrInfo;

	// Filter Amb messages
	private Set<AWFullPFwdLayer> messagesAlreadyReceived; // This map is to store already received messages; TODO: Apagar mensagens mais antigas


	// Others
	//...


	public Tower(TowerInfo tower, InfoNode server) throws IOException
	{
		logger.config(tower.toString());
		logger.config(server.toString());
		
		this.me = tower;
		this.local_server = server;

		this.wlan_socket = new DatagramSocket(Constants.towerPort);
		this.vanet_socket = new InfoNodeMulticast("eth1").socket;
		this.fwrInfo = new AWFullPFwdLayer(MessageConstants.TTLTowerHello, me.getName(), -1);
		this.messagesAlreadyReceived = new HashSet<>();
	}


	@Override
	public void run()
	{
		// Send hello to server, so it can forward to cloud and know this tower and it's position.
		AWFullPTowerAnnounce aw_app = new AWFullPTowerAnnounce(me);
		AWFullPacket awpacket = new AWFullPacket(aw_app);
		SendMessages.sendMessage(this.wlan_socket, this.local_server.ip, this.local_server.port, awpacket);

		// Send Hellos
		Timer timer_1 = new Timer(false);
		timer_1.scheduleAtFixedRate(wrap(this::sendHellos), 0, Constants.refreshRate);

		// Receive Messages
		Thread thread_1 = new Thread(this::receiveMessagesVANET);
		thread_1.start();

		Thread thread_2 = new Thread(this::receiveMessagesWLAN);
		thread_2.start();


	}

	private void sendHellos()
	{
		SendMessages.towerAnnouncement(this.vanet_socket, this.me, fwrInfo);
	}

	private void receiveMessagesVANET()
	{
		while (true) {
			try {
				AWFullPacket message = ReceiveMessages.receiveData(this.vanet_socket);
				handleMessage(message);
			} catch (IOException e) {
				// TIMEOUT
				//logger.fine("Timeout passed. Nothing received.");
			}
		}
	}
	private void receiveMessagesWLAN()
	{
		while (true) {
			try {
				AWFullPacket message = ReceiveMessages.receiveData(this.wlan_socket);
				System.out.println("Escuta WLAN?");
				handleMessage(message);
			} catch (IOException e) {
				// TIMEOUT
				//logger.fine("Timeout passed. Nothing received.");
			}
		}
	}

	private void handleMessage(AWFullPacket message)
	{
		switch(message.appLayer.getType()) {
				
			case MessageConstants.CAR_HELLO:
				AWFullPCarHello aw_ch = (AWFullPCarHello) message.appLayer;
				AWFullPCarInRange aw_cir = new AWFullPCarInRange(this.me.getName(), aw_ch.getCarID());
				AWFullPacket awp = new AWFullPacket(aw_cir);
				sendToServer(awp);
				break;
				
			case MessageConstants.CAR_ACCIDENT, MessageConstants.AMBULANCE_PATH:
				sendToServer(message);
				break;
				
			case MessageConstants.TOWER_ANNOUNCE:
				// Probably self announce. Ignore
				break;
			case MessageConstants.CLOUD_AMBULANCE_PATH:
				System.out.println("[TOWER] Recebeu info do servidor, posição");
				AWFullPCloudAmbulanceServer aw_amb = (AWFullPCloudAmbulanceServer) message.appLayer;
				AWFullPFwdLayer aw_fwd =  message.forwardInfo;
				// We need to check if we haven't already send this message.
				// Probably was the WLAN socket that send.
				if (!messagesAlreadyReceived.contains(aw_fwd)) {
					messagesAlreadyReceived.add(aw_fwd);
					// DatagramSocket sender, InetAddress to, int port, AWFullPacket message
					SendMessages.sendMessage(vanet_socket, Constants.MulticastGroup, Constants.portMulticast, message);
				}
				break;

			default:
				//logger.warning("Received unexpected message: " + message.toString());
		}
	}

	private void sendToServer(AWFullPacket message)
	{
		//logger.finer("Forwarding to server: " + message.toString());
		//this.wlan_socket.setNetworkInterface()

		SendMessages.sendMessage(this.wlan_socket, this.local_server.ip, this.local_server.port, message);
	}

	private static TimerTask wrap(Runnable r)
	{
		return new TimerTask() {
			@Override
			public void run() {r.run();}
		};
	}
}

