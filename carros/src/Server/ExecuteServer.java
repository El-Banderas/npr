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


public class ExecuteServer {
	
	private InfoNode thisServer;
	private InfoNode cloud;

	
	public ExecuteServer(InfoNode thisServer, InfoNode cloud) {
		this.cloud = cloud;
		this.thisServer = thisServer;
	}
	
	
	public void run() throws SocketException {
		byte[] buf = new byte[256];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		thisServer.socket.setSoTimeout(Constants.refreshRate);
		while(true){
			try {
				SendMessages.serverHelloCloud(thisServer.socket, cloud);
				MessageAndType message = ReceiveMessages.receiveData(thisServer.socket);
				//receiveSocket.receive(packet);
				handleMessage(message);
			} catch (IOException e) {
				System.out.println("[Server] Timeout passed. Nothing received.");
				System.out.println("Receiving in: "+thisServer.socket.getLocalAddress() +" | "+  thisServer.socket.getLocalPort() );
			}
		}
	}
	private static void handleMessage(MessageAndType message) {
		switch (message.type){
			case MessagesConstants.CarHelloMessage:
				System.out.println("Received Hello from car");
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

}
