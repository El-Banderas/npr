package Server;

import Common.Constants;
import Common.InfoNode;
import Common.Messages.MessageAndType;
import Common.Messages.MessagesConstants;
import Common.Messages.ReceiveMessages;
import Common.Messages.SendMessages;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.util.Timer;
import java.util.TimerTask;


public class ExecuteServer {
	
	private InfoNode thisServer;
	private InfoNode cloud;
	private int howManyCars;

	
	public ExecuteServer(InfoNode thisServer, InfoNode cloud) {
		this.cloud = cloud;
		this.thisServer = thisServer;
		this.howManyCars = 0;
	}
	
	
	public void run() throws SocketException {
		byte[] buf = new byte[256];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		thisServer.socket.setSoTimeout(Constants.refreshRate);
		TimerTask timerTask = new sendHellos(this.thisServer.socket);
		Timer timer = new Timer(true);
		timer.scheduleAtFixedRate(timerTask, 0, Constants.refreshRate);
		while(true){
			try {
				MessageAndType message = ReceiveMessages.receiveData(thisServer.socket);
				//receiveSocket.receive(packet);
				handleMessage(message);
			} catch (IOException e) {
				System.out.println("[Server] Timeout passed. Nothing received.");
				System.out.println("Receiving in: "+thisServer.socket.getLocalAddress() +" | "+  thisServer.socket.getLocalPort() );
			}
		}
	}
	private void handleMessage(MessageAndType message) {
		switch (message.type){
			case MessagesConstants.CarHelloMessage:
				this.howManyCars++;
				System.out.println("Received "+this.howManyCars+" Hello from car");
				break;
			case MessagesConstants.TowerHelloMessage:
				System.out.println("Received Hello from tower");
				break;
			case MessagesConstants.BreakMessage:
				System.out.println("Received Break");
				break;

			case MessagesConstants.AccidentMessage:
				System.out.println("Received Accident");
				break;


			default:
				System.out.println("Received message, type unkown: " + message.type);
		}
	}

	public int getHowManyCars() {
		return howManyCars;
	}
}
