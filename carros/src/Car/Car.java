package Car;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import AWFullP.AWFullPacket;
import AWFullP.MessageConstants;
import AWFullP.SendMessages;
import AWFullP.FWRMessages.FWRInfo;
import AWFullP.FWRMessages.FWReceiveMessages;
import AWFullP.FWRMessages.SelfCarMessage;
import Car.Terminal.CarTerminal;
import Common.CarInfo;
import Common.Constants;
import Common.InfoNodeMulticast;
import Common.TowerInfo;


public class Car implements Runnable
{
	// Node information
	private CarInfo me;
	//private List<TowerInfo> towers;

	// Connection information
	private DatagramSocket socket;
	private InetAddress myIp; //TODO: temporary. Use random string

	// Others
	private SharedClass shared; //for CLI
	private FWRInfo fwrInfoHelloCar;


	public Car(CarInfo info, List<TowerInfo> towers) throws IOException
	{
		this.me = info;
		//this.towers = towers;
		this.socket = new InfoNodeMulticast().socket;
		this.shared = new SharedClass(me, this.socket, towers);

		this.fwrInfoHelloCar = new FWRInfo(MessageConstants.TTLCarHelloMessage, shared.id, -1);

		this.myIp = Constants.getMyIp();

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
		SendMessages.carHello(this.socket, this.me, fwrInfoHelloCar);
	}

	private void receiveMessages()
	{
		while (true) {
			try {
				this.me.pos.updatePosition();
				//System.out.println("Posição atual: " + info.pos.x + " | " + info.pos.y);
				AWFullPacket message = FWReceiveMessages.forwardHandleMessage(this.socket, this.socket, myIp, me);
				handleMessage(message);
			} catch (IOException e) {
				this.shared.addEntryMessages(MessageConstants.TIMEOUT);
			} catch (SelfCarMessage e) {

			}
		}
	}

	private void handleMessage(AWFullPacket message) throws UnknownHostException
	{
			shared.addEntryMessages(message.getType());
	}

	private static TimerTask wrap(Runnable r)
	{
		return new TimerTask() {
			@Override
			public void run() {r.run();}
		};
	}
}
