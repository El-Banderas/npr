package Tower;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Timer;
import java.util.TimerTask;

import Common.Constants;
import Common.InfoNode;
import Common.TowerInfo;
import Common.Messages.MessageAndType;
import Common.Messages.MessagesConstants;
import Common.Messages.ReceiveMessages;
import Common.Messages.SendMessages;


public class Tower implements Runnable {
	
	private TowerInfo info;
	private InfoNode server;
	
	
	public Tower(TowerInfo info, InfoNode server)
	{
		this.info = info;
		this.server = server;
	}
	
	
	@Override
	public void run()
	{
		DatagramSocket receiveSocket = info.receiveSocket();
		DatagramSocket sendSocket = info.sendSocket();
		// Multicast sockets got the setTimeout when created

		if(!Constants.core) {
			try {
				receiveSocket.setSoTimeout(Constants.refreshRate);
			} catch (SocketException e) {
				e.printStackTrace();
			}
		}
		
		TimerTask timerTask = new sendHellos(sendSocket, server);
		Timer timer = new Timer(true);
		timer.scheduleAtFixedRate(timerTask, 0, Constants.refreshRate);
		//receiveSocket.setSoTimeout(Constants.refreshRate);
		//MulticastSocket socketReceive = new MulticastSocket(Constants.portCarsTowersLinux);
		//socketReceive.joinGroup(Constants.MulticastGroup);
		//socketReceive.setSoTimeout(Constants.refreshRate);

		while(true) {
			try {
				MessageAndType message = ReceiveMessages.receiveData(receiveSocket);
				handleMessage(message, server);
			} catch (IOException e) {
				System.out.println("[TOWER] Timeout passed. Nothing received." );
				//System.out.println("Receiving in: " +socketReceive.getLocalAddress());
				//System.out.println("Receiving in: " +socketReceive.getInterface());
			}
		}
	}
	
	
	private void handleMessage(MessageAndType message, InfoNode thisServer)
	{
		if (message.ipSender.equals(info.sendSocket().getLocalSocketAddress())){
			System.out.println("Recebeu próprio hello");
		}
		if (message.ipSender.equals(info.sendSocket().getInetAddress())){
			System.out.println("[2] Recebeu próprio hello");
		}

		System.out.println("Receive message: " + message.type);
		SendMessages.forwardMessage(message, this.info.sendSocket(), thisServer);
		
		switch(message.type) {
			case MessagesConstants.CarHelloMessage:
				System.out.println("Received Hello");
				break;
			default:
				System.out.println("Received message, type unknown: " + message.type);
		}
	}
}
