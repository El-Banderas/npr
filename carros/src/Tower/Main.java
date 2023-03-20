package Tower;

import Common.Messages.SendMessages;
import Tower.sendHellos;
import Cloud.CloudConstants;
import Common.*;
import Common.Messages.MessageAndType;
import Common.Messages.MessagesConstants;
import Common.Messages.ReceiveMessages;
import Server.ServerConstants;

import static Common.Messages.SendMessages.towerHelloServer;

import java.io.IOException;
import java.net.*;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Arguments:
 * Windows
 * The IP of the cloud isn't necessary, is a constant stored in the CloudConstants file.
 * Name (unecessary) | This port | Port Server | PosX | PosY
 * The port must be the same as the file.
 * Example: "t1 8000 9000 40 40"
 *
 *
 * Linux, depois remover a posição, e acrescentar o IP da cloud, para saber a quem mandar mensagens
 * O server do lado direito é o "2001:9::20"
 * Name (unecessary) | IP Server | PosX | PosY
 * The port must be the same as the file.
 * Example: "t1 2001:9::20 40 40"
 */
public class Main {
	private static DatagramSocket sendSocket;
	public static void main(String[] args) throws IOException {
		System.out.println("[TOWER] Is core?: " + Constants.core);

		String name = args[0];

		Position pos = null;
		InfoNode thisServer;

		TowerInfo thisTower ;
		if (Constants.core) {
			//towerIPInfo = new InfoNodeMulticast(true);
			// TODO: Change position tower
			InetAddress ipServer = Inet6Address.getByName(args[1]);
			pos = new Position(Integer.parseInt(args[2]), Integer.parseInt(args[3]));
			thisServer = new InfoNode(ipServer, ServerConstants.port, false);
			thisTower = new TowerInfo(name, pos);
		}
		else {
			pos = new Position(Integer.parseInt(args[3]), Integer.parseInt(args[4]));
			InfoNode infoNodo = new InfoNodeWindows(Integer.parseInt(args[1]), true);
			thisTower = new TowerInfo(name, infoNodo, pos);
			thisServer = new InfoNode(InetAddress.getByName("localhost"),Integer.parseInt(args[2]), false );
		}

		DatagramSocket receiveSocket = thisTower.receiveSocket();
		sendSocket = thisTower.sendSocket();
		// Multicast sockets got the setTimeout when created

		if (!Constants.core){
			receiveSocket.setSoTimeout(Constants.refreshRate);
		}
		TimerTask timerTask = new sendHellos(sendSocket, thisServer);
		Timer timer = new Timer(true);
		timer.scheduleAtFixedRate(timerTask, 0, Constants.refreshRate);
		//receiveSocket.setSoTimeout(Constants.refreshRate);
		//MulticastSocket socketReceive = new MulticastSocket(Constants.portCarsTowersLinux);
		//socketReceive.joinGroup(Constants.MulticastGroup);
		//socketReceive.setSoTimeout(Constants.refreshRate);

		while(true){
			try {
				//socketReceive.receive(packet);
				MessageAndType message = ReceiveMessages.receiveData(receiveSocket);
				//receiveSocket.receive(packet);
				handleMessage(message, thisServer);
			} catch (IOException e) {
				System.out.println("[TOWER] Timeout passed. Nothing received. " );
				//System.out.println("Receiving in: " +socketReceive.getLocalAddress());
				//System.out.println("Receiving in: " +socketReceive.getInterface());

			}

		}
	}

	private static void handleMessage(MessageAndType message, InfoNode thisServer) {
		SendMessages.forwardMessage(message, sendSocket, thisServer);
		switch (message.type){
			case MessagesConstants.HelloMessage:
				System.out.println("Received Hello");
				break;

			default:
				System.out.println("Received message, type unkown: " + message.type);

		}
	}
}
