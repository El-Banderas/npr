package Car;

import Common.CarInfo;
import Common.Constants;
import Common.InfoNodeMulticast;
import Common.TowerInfo;

import java.io.IOException;
import java.net.*;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import AWFullP.AWFullPacket;
import AWFullP.MessagesConstants;
import AWFullP.ReceiveMessages;
import AWFullP.SendMessages;
import Car.Terminal.CarTerminal;


public class Car implements Runnable
{
	// Node information
	private CarInfo me;
	private List<TowerInfo> towers;

	// Connection information
	public InfoNodeMulticast multicast_connection; //TODO: Temporary. will be socket
	private DatagramSocket socket; //TODO: Multicast
	private InetAddress myIp; //TODO: temporary. Use random string
	
	// Others
	private SharedClass shared; //for CLI

	
	public Car(CarInfo info, List<TowerInfo> towers) throws IOException
	{
		this.me = info;
		this.towers = towers;
		
		this.multicast_connection = new InfoNodeMulticast();
		this.socket = this.multicast_connection.socket;
		this.myIp = Constants.getMyIp();
		
		this.shared = new SharedClass(this.socket);
	}
	
	
	@Override
	public void run()
	{
		CarTerminal carTerminal = new CarTerminal(shared);
		Thread thread_1 = new Thread(carTerminal);
		thread_1.start();
		
		Timer timer = new Timer(false);
		timer.scheduleAtFixedRate(wrap(this::sendHellos), 0, Constants.refreshRate);
		
		Thread thread_2 = new Thread(this::receiveMessages);
		thread_2.start();
	}
	
	
	private void sendHellos()
	{
		SendMessages.carHellos(this.socket, this.me);
	}
	
	private void receiveMessages()
	{
		while (true) {
			try {
				this.me.pos.updatePosition();
				//System.out.println("Posição atual: " + info.pos.x + " | " + info.pos.y);
				AWFullPacket message = ReceiveMessages.receiveData(this.socket);
				handleMessage(message);
			} catch (IOException e) {
				this.shared.addEntryMessages(MessagesConstants.Timeout);
			}
		}
	}
	
	private void handleMessage(AWFullPacket message) throws UnknownHostException
	{
		if (message.ipSender.equals(myIp) ) {
			// There are no timeouts - we always receive message from ourselves
			shared.addEntryMessages(MessagesConstants.Timeout);
			return;
		}
		shared.addEntryMessages(message.type);
	}
	
	private static TimerTask wrap(Runnable r)
	{
		return new TimerTask() {
			@Override
			public void run() {r.run();}
		};
	}
}
