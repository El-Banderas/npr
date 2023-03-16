package Server;

import Common.Constants;
import Common.InfoNode;
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
				thisServer.socket.receive(packet);
				System.out.println("[Server] Message received");
			} catch (IOException e) {
				System.out.println("[Server] Timeout passed. Nothing received.");
				System.out.println("Receiving in: "+thisServer.socket.getLocalAddress() +" | "+  thisServer.socket.getLocalPort() );
			}
		}
	}
}
