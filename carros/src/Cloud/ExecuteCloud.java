package Cloud;

import Common.Constants;
import Common.InfoNode;

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

				cloud.socket.receive(packet);
				System.out.println("[TOWER] Message received");
			} catch (IOException e) {
				System.out.println("[TOWER] Timeout passed. Nothing received.");

				System.out.println("Receiving in: "+cloud.socket.getLocalAddress() +" | "+  cloud.socket.getLocalPort() );
			}
		}
	}
}
