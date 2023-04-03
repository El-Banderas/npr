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
import AWFullP.ReceiveMessages;
import AWFullP.SendMessages;
import AWFullP.FwdLayer.AWFullPFwdLayer;
import AWFullP.FwdLayer.SelfCarMessage;
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
	private AWFullPFwdLayer fwrInfoHelloCar;


	public Car(CarInfo info, List<TowerInfo> towers) throws IOException
	{
		this.me = info;
		//this.towers = towers;
		this.socket = new InfoNodeMulticast().socket;
		this.shared = new SharedClass(me, this.socket, towers);

		this.fwrInfoHelloCar = new AWFullPFwdLayer(MessageConstants.TTLCarHelloMessage, shared.id, -1);

		this.myIp = Constants.getMyIp();
	}


	@Override
	public void run()
	{
		CarTerminal carTerminal = new CarTerminal(shared);
		Thread thread_1 = new Thread(carTerminal);
		thread_1.start();

		Thread thread_2 = new Thread(this::receiveMessages);
		thread_2.start();

		Timer timer_1 = new Timer(false);
		timer_1.scheduleAtFixedRate(wrap(this::sendHellos), 0, Constants.refreshRate);
	}


	private void sendHellos()
	{
		try {
			SendMessages.carHello(this.socket, this.me, fwrInfoHelloCar);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	private void receiveMessages()
	{
		while (true) {
			try {
				this.me.pos.updatePosition();
				//System.out.println("Posição atual: " + info.pos.x + " | " + info.pos.y);
				AWFullPacket message = ReceiveMessages.forwardHandleMessage(this.socket, this.socket, myIp, me);
				//AWFullPacket message = ReceiveMessages.receiveData(socket);
				handleMessage(message);
			} catch (IOException e) {
				this.shared.addEntryMessages(MessageConstants.TIMEOUT);
			} catch (SelfCarMessage ignore) {}
		}
	}

	private void handleMessage(AWFullPacket message) throws UnknownHostException
	{
		shared.addEntryMessages(message.appLayer.getType());
	}

	private static TimerTask wrap(Runnable r)
	{
		return new TimerTask() {
			@Override
			public void run() {r.run();}
		};
	}
}
