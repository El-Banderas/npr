package Tower;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Timer;
import java.util.TimerTask;

import Common.Constants;
import Common.InfoNode;
import Common.TowerInfo;

import AWFullP.MessagesConstants;
import AWFullP.ReceiveMessages;
import AWFullP.SendMessages;
import AWFullP.AWFullPacket;



public class Tower implements Runnable
{
	// Node information
	private TowerInfo me;
	private InfoNode local_server;
	
	// Connection information
	private DatagramSocket wlan_socket; // WLAN
	private DatagramSocket vanet_socket; //TODO: Multicast
	
	// Others
	//...


	public Tower(TowerInfo tower, InfoNode server) throws SocketException
	{
		this.me = tower;
		this.local_server = server;
		
		this.wlan_socket = new DatagramSocket(Constants.towerPort);
		this.vanet_socket = new DatagramSocket(Constants.portMulticast); //TODO: Multicast
	}


	@Override
	public void run() {
		// Send Hellos
		Timer timer_1 = new Timer(false);
		timer_1.scheduleAtFixedRate(wrap(this::sendHellos), 0, Constants.refreshRate);

		// Receive Messages
		Thread thread_1 = new Thread(this::receiveMessages);
		thread_1.start();
	}


	private void sendHellos() {
		/*HashMap<String, Integer> message = new HashMap<>();
		message.put(this.info.getId(), this.info.getHowManyCars());
		SendMessages.towerHelloServer(this.info.sendSocket(), this.server);
		SendMessages.towerHelloCar(this.info.sendSocket());*/
	}

	private void receiveMessages() {
		/*while (true) {
			try {
				MessageAndType message = ReceiveMessages.receiveData(this.info.receiveSocket());
				handleMessage(message, this.server);
			} catch (IOException e) {
				//System.out.println("[Tower] Timeout passed. Nothing received.");
			}
		}*/
	}

	/*private void sendToServer(MessageAndType message) {
		SendMessages.towerHelloServer(this.info.sendSocket(), message);
	}*/


	/*private void handleMessage(MessageAndType message, InfoNode thisServer) {
		SendMessages.forwardMessage(message, this.info.sendSocket(), thisServer);
		if (message.type == MessagesConstants.CarInRangeMessage || message.type == MessagesConstants.AccidentMessage) {
			sendToServer(message);
		}
	}*/

	private static TimerTask wrap(Runnable r)
	{
		return new TimerTask() {
			@Override
			public void run() {r.run();}
		};
	}
}

