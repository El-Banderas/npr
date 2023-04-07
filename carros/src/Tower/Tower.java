package Tower;

import java.io.IOException;
import java.net.DatagramSocket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

import AWFullP.AWFullPacket;
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
	private static Logger logger =  Logger.getLogger("npr.tower");
	
	// Node information
	private TowerInfo me;
	private InfoNode local_server;

	// Connection information
	private DatagramSocket wlan_socket;
	private DatagramSocket vanet_socket; //TODO: Multicast

	// Others
	//...


	public Tower(TowerInfo tower, InfoNode server) throws IOException
	{
		this.me = tower;
		this.local_server = server;

		this.wlan_socket = new DatagramSocket(Constants.towerPort);
		
		this.vanet_socket = new InfoNodeMulticast("eth1").socket;
	}


	@Override
	public void run()
	{
		// Send Hellos
		Timer timer_1 = new Timer(false);
		timer_1.scheduleAtFixedRate(wrap(this::sendHellos), 0, Constants.refreshRate);

		// Receive Messages
		Thread thread_1 = new Thread(this::receiveMessages);
		thread_1.start();
	}

	private void sendHellos()
	{
		SendMessages.towerAnnouncement(this.vanet_socket, this.me);
		System.out.println("Enviou hello");
	}

	private void receiveMessages()
	{
		while (true) {
			try {
				AWFullPacket message = ReceiveMessages.receiveData(this.vanet_socket);
				handleMessage(message);
			} catch (IOException e) {
				// TIMEOUT
				//System.out.println("[Tower] Timeout passed. Nothing received.");
			}
		}
	}

	private void handleMessage(AWFullPacket message)
	{
		//TODO: filtrar mensagens de outras torres (if (message.getTowerID() != this.me.getName()) return)
		
		switch(message.appLayer.getType()) {
		
			case MessageConstants.CAR_HELLO:
				AWFullPCarHello aw_ch = (AWFullPCarHello) message.appLayer;
				AWFullPCarInRange aw_cir = new AWFullPCarInRange(this.me.getName(), aw_ch.getCarID());
				AWFullPacket awp = new AWFullPacket(aw_cir);
				sendToServer(awp);
				break;
		
			case MessageConstants.CAR_ACCIDENT:
				sendToServer(message);
				break;
			
			default:
				logger.info("Received unexpected message: " + message.toString());
		}
	}

	private void sendToServer(AWFullPacket message)
	{
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

