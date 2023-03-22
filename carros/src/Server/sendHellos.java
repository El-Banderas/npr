package Server;

import java.net.DatagramSocket;
import java.util.TimerTask;

import static Common.Messages.SendMessages.serverHelloCloud;


public class sendHellos extends TimerTask {
	
	public DatagramSocket sendSocket;
	
	
	public sendHellos(DatagramSocket sendSocket) {
		this.sendSocket = sendSocket;
	}
	
	
	@Override
	public void run() {
		serverHelloCloud(sendSocket);
	}
}
