package Tower;

import Cloud.CloudConstants;
import Common.*;

import java.io.IOException;
import java.net.*;

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

		TowerInfo thisTower ;
		if (Constants.linux) {
			//towerIPInfo = new InfoNodeMulticast(true);
			// TODO: Change position tower
			pos = new Position(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
		//	cloud = new InfoNode(null, CloudConstants.port, false);
			thisTower = new TowerInfo(name, pos);

		}
		else {
			pos = new Position(Integer.parseInt(args[2]), Integer.parseInt(args[3]));
			InfoNode infoNodo = new InfoNodeWindows(Integer.parseInt(args[1]), true);
			thisTower = new TowerInfo(name, infoNodo, pos);
		}

		byte[] buf = new byte[256];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		DatagramSocket receiveSocket = thisTower.receiveSocket();
		receiveSocket.setSoTimeout(Constants.refreshRate);
		//MulticastSocket socketReceive = new MulticastSocket(Constants.portCarsTowersLinux);
		//socketReceive.joinGroup(Constants.MulticastGroup);
		//socketReceive.setSoTimeout(Constants.refreshRate);

		while(true){
			try {
				//towerHelloCloud(thisTower.connectionInfo, cloud);
				//socketReceive.receive(packet);
				receiveSocket.receive(packet);
				System.out.println("[TOWER] Message received: " + packet.getAddress() + " | " + packet.getPort());
			} catch (IOException e) {
				System.out.println("[TOWER] Timeout passed. Nothing received. " );
				//System.out.println("Receiving in: " +socketReceive.getLocalAddress());
				//System.out.println("Receiving in: " +socketReceive.getInterface());

			}

		}
	}
}
