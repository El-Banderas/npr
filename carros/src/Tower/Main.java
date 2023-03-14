package Tower;

import Cloud.CloudConstants;
import Common.*;
import Common.Messages.MessageAndType;
import Common.Messages.MessagesConstants;
import Common.Messages.ReceiveMessages;

import java.io.IOException;
import java.net.*;

import static Common.Messages.SendMessages.towerHelloCloud;

/**
 * Arguments:
 * Windows
 * The IP of the cloud isn't necessary, is a constant stored in the CloudConstants file.
 * Name (unecessary) | Port | PosX | PosY
 * The port must be the same as the file.
 * Example: "t1 8000 40 40"
 *
 *
 * Linux, depois remover a posição, e acrescentar o IP da cloud, para saber a quem mandar mensagens
 * Name (unecessary) | PosX | PosY
 * The port must be the same as the file.
 * Example: "t1 40 40"
 */
public class Main {
	public static void main(String[] args) throws IOException {
		System.out.println("[TOWER] Is linux?: " + Constants.linux);
		
		String name = args[0];

		Position pos = null;
		InfoNode cloud;

		TowerInfo thisTower ;
		if (Constants.linux) {
			//towerIPInfo = new InfoNodeMulticast(true);
			// TODO: Change position tower
			pos = new Position(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
			cloud = new InfoNode(Constants.CloudIP, CloudConstants.port, false);
			thisTower = new TowerInfo(name, pos);

		}
		else {
			pos = new Position(Integer.parseInt(args[2]), Integer.parseInt(args[3]));
			InfoNode infoNodo = new InfoNodeWindows(Integer.parseInt(args[1]), true);
			thisTower = new TowerInfo(name, infoNodo, pos);
			cloud = new InfoNode(InetAddress.getByName("localhost"),CloudConstants.port, false );
		}

		byte[] buf = new byte[256];
		DatagramSocket receiveSocket = thisTower.receiveSocket();
		DatagramSocket sendSocket = thisTower.sendSocket();
		// Multicast sockets got the setTimeout when created

		if (!Constants.linux){
			receiveSocket.setSoTimeout(Constants.refreshRate);
		}
		//receiveSocket.setSoTimeout(Constants.refreshRate);
		//MulticastSocket socketReceive = new MulticastSocket(Constants.portCarsTowersLinux);
		//socketReceive.joinGroup(Constants.MulticastGroup);
		//socketReceive.setSoTimeout(Constants.refreshRate);

		while(true){
			try {
				towerHelloCloud(sendSocket, cloud);
				//socketReceive.receive(packet);
				MessageAndType message = ReceiveMessages.receiveData(receiveSocket);
				//receiveSocket.receive(packet);
				handleMessage(message);
			} catch (IOException e) {
				System.out.println("[TOWER] Timeout passed. Nothing received. " );
				//System.out.println("Receiving in: " +socketReceive.getLocalAddress());
				//System.out.println("Receiving in: " +socketReceive.getInterface());

			}

		}
	}

	private static void handleMessage(MessageAndType message) {
		switch (message.type){
			case MessagesConstants.HelloMessage:
				System.out.println("Received Hello");
				break;
			default:
				System.out.println("Received message, type unkown: " + message.type);

		}
	}
}
