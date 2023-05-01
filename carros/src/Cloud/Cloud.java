package Cloud;

import java.io.FileWriter;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import AWFullP.AWFullPacket;
import AWFullP.AppLayer.AWFullPAmbPath;
import AWFullP.MessageConstants;
import AWFullP.ReceiveMessages;
import AWFullP.AppLayer.AWFullPCarAccident;
import AWFullP.AppLayer.AWFullPServerInfo;
import Car.AmbulanceInfo;
import Common.Constants;
import Common.InfoNode;
import Common.Position;


public class Cloud implements Runnable
{
	private static Logger logger = Logger.getLogger("npr.cloud");

	static {
		ConsoleHandler handler = new ConsoleHandler();
		handler.setFormatter(new SimpleFormatter());
		handler.setLevel(Level.ALL);
		logger.addHandler(handler);
		
		logger.setLevel(Level.ALL);
		Logger.getLogger("npr.messages.received").setLevel(Level.FINE);
		Logger.getLogger("npr.messages.sent").setLevel(Level.FINE);
	}
	
	// Node information
	//private InfoNode me;

	// Connection information
	private DatagramSocket socket;

	// Others
	private Map<String, List<String>> towerEventMap;


	public Cloud(InfoNode cloud) throws IOException
	{
		logger.config(cloud.toString());
		
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


	FileWriter hist;

	{
		try {
			hist = new FileWriter("hist.txt");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


	private void receiveMessages()
	{
		while(true) {
			try {

				AWFullPacket message = ReceiveMessages.receiveData(this.socket);
				handleMessage(message);
				hist.write(message.getType());
			} catch (IOException ignore) {
				// TIMEOUT
				//logger.fine("Timeout passed. Nothing received.");
			}
		}
	}

	private void handleMessage(AWFullPacket message)
	{
		switch(message.appLayer.getType()) {
			case MessageConstants.TOWER_ANNOUNCE:
				System.out.println("Recebu info de uma torre?");
				break;

			case MessageConstants.SERVER_INFO:
				System.out.println("[Cloud] Recebeu info do servidor");
				AWFullPServerInfo aw_si = (AWFullPServerInfo) message.appLayer;
				String towerID_si = aw_si.getTowerID();
				for(String carID : aw_si.getCarsInRange())
					towerEventMap.computeIfAbsent(towerID_si, k -> new ArrayList<>()).add("Car in range: " + carID);
				break;
				
			case MessageConstants.CAR_ACCIDENT:
				System.out.println("[ALERTA CM] Houve um acidente");

				AWFullPCarAccident aw_ca = (AWFullPCarAccident) message.appLayer;
				String towerID_ca = aw_ca.getTowerID();
				Position location_ca = aw_ca.getLocation();
				towerEventMap.computeIfAbsent(towerID_ca, k -> new ArrayList<>()).add("Accident at location: " + location_ca.toString());
				break;
			case MessageConstants.AMBULANCE_PATH:
				System.out.println("[Cloud] Ambulance info");
				AWFullPAmbPath aw_ap = (AWFullPAmbPath) message.appLayer;
				handleAmbulanceInfo(aw_ap.getAmbulanceInfo());
				break;

			default:
				System.out.println("Não sei o que é: " +message.appLayer.getType());
				logger.warning("Received unexpected message: " + message.toString());
		}
	}

	private void handleAmbulanceInfo(AmbulanceInfo ambulanceInfo) {
		return;
	}

	/*private static TimerTask wrap(Runnable r)
	{
		return new TimerTask() {
			@Override
			public void run() {r.run();}
		};
	}*/
}
