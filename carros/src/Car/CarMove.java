package Car;

import Common.Constants;
//import Common.TowerInfo;
import Common.Messages.MessageAndType;
import Common.Messages.MessagesConstants;
import Common.Messages.ReceiveMessages;

import java.io.IOException;
import java.net.*;
//import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class CarMove {
	
	private CarInfo info;
	private DatagramSocket sendSocket;
	private DatagramSocket receiveSocket;
	private InetAddress myIp;
	private SharedClass shared;
	//private List<TowerInfo> towers;

	
	public CarMove(CarInfo info, SharedClass shared/*, List<TowerInfo> towers*/) {
		this.info = info;
		this.receiveSocket = info.receiveSocket();
		this.sendSocket = info.sendSocket();
		this.shared = shared;
		try {
			//System.out.println("My IP: "+ Constants.getMyIp());
			myIp = Inet6Address.getByName(Constants.getMyIp());
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
		//this.towers = towers;
	}
	
	
	public void run() {
		TimerTask timerTask = new sendHellos(sendSocket, this.info);
		Timer timer = new Timer(true);
		timer.scheduleAtFixedRate(timerTask, 0, Constants.refreshRate);
		while (true) {
			try {
				info.pos.updatePosition();
				//System.out.println("Posição atual: " + info.pos.x + " | " + info.pos.y);
				MessageAndType message = ReceiveMessages.receiveData(receiveSocket);

				//receiveSocket.receive(packet);
				handleMessage(message);
			} catch (IOException e) {
				shared.addEntryMessages(MessagesConstants.Timeout);

			}
		}
	}
	
	private void handleMessage(MessageAndType message) throws UnknownHostException {
		//System.out.println("Recebeu algo: " + message.type);
		if (message.ipSender.equals(myIp) ) {
			// There are no timeouts, because we receive always message from ourselves.
			shared.addEntryMessages(MessagesConstants.Timeout);

			return;
		}
		//System.out.println("IP2s: " + sendSocket.getLocalAddress());
		//System.out.println("IP3s: " + InetAddress.getLocalHost());
		shared.addEntryMessages(message.type);
		switch (message.type){
			case MessagesConstants.CarHelloMessage:
				//System.out.println("Received Hello from: " + message.ipSender);
				break;
			case MessagesConstants.TowerHelloMessage:
				//System.out.println("Received Hello from: " + message.ipSender);
				break;
			case MessagesConstants.BreakMessage:
				//System.out.println("Received Hello from: " + message.ipSender);
				break;
			case MessagesConstants.AccidentMessage:
				//System.out.println("Received Hello from: " + message.ipSender);
				break;

			default:
				System.out.println("Received message, type unknown: " + message.type);
		}
	}
}
