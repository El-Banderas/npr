package Car;

import Common.Messages.SendMessages;

import java.net.DatagramSocket;
import java.util.TimerTask;

public class sendHellos extends TimerTask {
	
	public CarInfo info;
	public DatagramSocket sendSocket;
	
	
	public sendHellos(DatagramSocket sendSocket, CarInfo info) {
		this.sendSocket = sendSocket;
		this.info = info;
	}
	
	
	@Override
	public void run() {
		SendMessages.carHellos(sendSocket, info);
	}
}
