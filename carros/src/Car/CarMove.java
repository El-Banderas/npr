package Car;

import Common.Constants;
import Common.Messages.MessageAndType;
import Common.Messages.MessagesConstants;
import Common.Messages.ReceiveMessages;
import Common.Messages.SendMessages;
import Common.Position;
import Common.TowerInfo;

import java.io.IOException;
import java.net.*;
import java.util.List;

public class CarMove {
	private CarInfo info;
	private  List<TowerInfo> towers;
	private DatagramSocket sendSocket;
	private DatagramSocket receiveSocket;
	private InetAddress myIp;

	public CarMove(CarInfo info, List<TowerInfo> towers) {
		this.info = info;
		this.towers = towers;
		receiveSocket = info.receiveSocket();
		sendSocket = info.sendSocket();
		try {
			System.out.println("My IP: "+ Constants.getMyIp());
			myIp = Inet6Address.getByName(Constants.getMyIp());
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}



	}
	
	public void run(){

			while(true){
				try {

				// Depois meter um if aqui para que no linux não atualize a posição
				info.pos.getPosition();
				System.out.println("Posição atual: " + info.pos.x + " | " + info.pos.y);
				//checkPossibleCommunication();
				SendMessages.carHellos(sendSocket );
				MessageAndType message = null;
					message = ReceiveMessages.receiveData(receiveSocket);

				//receiveSocket.receive(packet);
				handleMessage(message);
				Thread.sleep(Constants.refreshRate);
				} catch (IOException e) {
					//System.out.println();
					throw new RuntimeException(e);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}


	}

	/**
	 * This function checks distance to towers. Now is not necessary, we send hellos to everyone
	 */
	private void checkPossibleCommunication() {
		System.out.println("Check Possible communication");
		for (TowerInfo tower : towers){
			if (Position.distance(info.pos, tower.pos) < Constants.towerCommunicationRadius){
				SendMessages.carHelloTower(info.sendSocket(), tower.connectionInfoWindowsReceive);
				System.out.println("Send message");
			}
		}
	}
	private void handleMessage(MessageAndType message) throws UnknownHostException {
		if (message.ipSender.equals(myIp) ) {
			//System.out.println("Received from himself");
			return;
		}
		System.out.println("IPs: " + message.ipSender);
		//System.out.println("IP2s: " + sendSocket.getLocalAddress());
		//System.out.println("IP3s: " + InetAddress.getLocalHost());

		switch (message.type){
			case MessagesConstants.HelloMessage:
				System.out.println("Received Hello from: " + message.ipSender);
				break;
			default:
				System.out.println("Received message, type unkown: " + message.type);

		}
	}
}
