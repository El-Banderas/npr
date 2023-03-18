package Cloud;

import Common.Constants;
import Common.InfoNode;
import Common.Messages.MessageAndType;
import Common.Messages.MessagesConstants;
import Common.Messages.ReceiveMessages;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;


public class ExecuteCloud {

	private Common.InfoNode cloud;


	public ExecuteCloud(InfoNode cloud) {
		this.cloud = cloud;
	}
	
	
	public void run() throws SocketException {
		byte[] buf = new byte[256];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		cloud.socket.setSoTimeout(Constants.refreshRate);
		while(true){
			try {
				MessageAndType message = ReceiveMessages.receiveData(cloud.socket);
				//receiveSocket.receive(packet);
				handleMessage(message);
			} catch (IOException e) {
				System.out.println("[TOWER] Timeout passed. Nothing received.");
				System.out.println("Receiving in: "+cloud.socket.getLocalAddress() +" | "+  cloud.socket.getLocalPort() );
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
