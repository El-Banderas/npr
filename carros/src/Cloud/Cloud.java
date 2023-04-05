package Cloud;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import AWFullP.AWFullPacket;
import AWFullP.MessageConstants;
import AWFullP.ReceiveMessages;
import AWFullP.AppLayer.AWFullPCarAccident;
import AWFullP.AppLayer.AWFullPCarInRange;
import AWFullP.AppLayer.AWFullPServerInfo;
import Common.Constants;
import Common.InfoNode;
import Common.Position;


public class Cloud implements Runnable
{
	private static Logger logger =  Logger.getLogger("npr.cloud");
	
	// Node information
	//private InfoNode me;

	// Connection information
	private DatagramSocket socket;

	// Others
	private Map<String, List<String>> towerEventMap;


	public Cloud(InfoNode cloud) throws SocketException
	{
		//this.me = cloud;

		this.socket = new DatagramSocket(cloud.port, cloud.ip);

		this.towerEventMap = new HashMap<>();
	}


	@Override
	public void run()
	{
		try {
			this.socket.setSoTimeout(Constants.refreshRate);
		} catch (SocketException e1) {
			e1.printStackTrace();
			System.exit(-1);
		}

		Thread thread_1 = new Thread(this::receiveMessages);
		thread_1.start();
	}


	private void receiveMessages()
	{
		while(true) {
			try {
				AWFullPacket message = ReceiveMessages.receiveData(this.socket);
				handleMessage(message);
			} catch (IOException ignore) {
				// TIMEOUT
				//logger.info("Timeout passed. Nothing received.");
			}
		}
	}

	private void handleMessage(AWFullPacket message)
	{
		switch(message.appLayer.getType()) {
		
			case MessageConstants.SERVER_INFO:
				AWFullPServerInfo aw_si = (AWFullPServerInfo) message.appLayer;
				String towerID_si = aw_si.getTowerID();
				towerEventMap.computeIfAbsent(towerID_si, k -> new ArrayList<>()).add("Info: " + aw_si.getPayload().toString()); //TODO
				break;
				
			case MessageConstants.CAR_IN_RANGE:
				AWFullPCarInRange aw_cir = (AWFullPCarInRange) message.appLayer;
				String towerID_cir = aw_cir.getTowerID();
				String carID_cir = aw_cir.getCarID();
				towerEventMap.computeIfAbsent(towerID_cir, k -> new ArrayList<>()).add("Car in range: " + carID_cir);
				break;
				
			case MessageConstants.CAR_ACCIDENT:
				AWFullPCarAccident aw_ca = (AWFullPCarAccident) message.appLayer;
				String towerID_ca = aw_ca.getTowerID();
				Position location_ca = aw_ca.getLocation();
				towerEventMap.computeIfAbsent(towerID_ca, k -> new ArrayList<>()).add("Accident at location: " + location_ca.toString());
				break;
				
			default:
				logger.info("Received unknown message: " + message.toString());
		}
	}

	/*private static TimerTask wrap(Runnable r)
	{
		return new TimerTask() {
			@Override
			public void run() {r.run();}
		};
	}*/
}
