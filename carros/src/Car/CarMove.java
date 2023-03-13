package Car;

import Common.Constants;
import Common.Messages.MessageAndType;
import Common.Messages.MessagesConstants;
import Common.Messages.ReceiveMessages;
import Common.Messages.SendMessages;
import Common.Position;
import Common.TowerInfo;

import java.io.IOException;
import java.net.SocketException;
import java.util.List;

public class CarMove {
	private CarInfo info;
	private  List<TowerInfo> towers;

	public CarMove(CarInfo info, List<TowerInfo> towers) {
		this.info = info;
		this.towers = towers;
	}
	
	public void run(){
		try {
			info.receiveInfo.socket.setSoTimeout(Constants.refreshRate);
		} catch (SocketException e) {
			throw new RuntimeException(e);
		}

		while(true){
				try {

				// Depois meter um if aqui para que no linux não atualize a posição
				info.pos.getPosition();
				System.out.println("Posição atual: " + info.pos.x + " | " + info.pos.y);
				checkPossibleCommunication();
				MessageAndType message = ReceiveMessages.receiveData(info.receiveInfo.socket);
				//receiveSocket.receive(packet);
				handleMessage(message);
				} catch (IOException e) {
					System.out.println("[Car] Nothing received.");
				}
			}

	}

	private void checkPossibleCommunication() {
		System.out.println("Check Possible communication");
		for (TowerInfo tower : towers){
			if (Position.distance(info.pos, tower.pos) < Constants.towerCommunicationRadius){
				SendMessages.carHelloTower(info.sendInfo, tower.connectionInfoWindowsReceive);
				System.out.println("Send message");
			}
		}
	}
	private void handleMessage(MessageAndType message) {
		if (message.ipSender.equals(this.info.sendInfo.socket.getLocalAddress()) ) return;
		switch (message.type){
			case MessagesConstants.HelloMessage:
				System.out.println("Received Hello");
				break;
			default:
				System.out.println("Received message, type unkown: " + message.type);

		}
	}
}
