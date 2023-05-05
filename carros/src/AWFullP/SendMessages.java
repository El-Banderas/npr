package AWFullP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.sql.Timestamp;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import AWFullP.AppLayer.*;
import AWFullP.FwdLayer.AWFullPFwdLayer;
import Car.AmbulanceInfo;
import Common.*;


public class SendMessages
{
	private static Logger logger = Logger.getLogger("npr.messages.sent");
	static {
		ConsoleHandler handler = new ConsoleHandler();
		handler.setFormatter(new SimpleFormatter());
		handler.setLevel(Level.ALL);
		logger.addHandler(handler);
	}
	
	
	public static void carHello(DatagramSocket sender, CarInfo info, AWFullPFwdLayer fwrInfo)
	{
		logger.fine("Car Sends Hello");
		
		AWFullPCarHello aw_app = new AWFullPCarHello(info);
		AWFullPacket awpacket = new AWFullPacket(aw_app);
		
		sendMessage(sender, Constants.MulticastGroup, Constants.portMulticast, awpacket, fwrInfo);
	}
	
	public static void carSendBreak(DatagramSocket sender, CarInfo info, AWFullPFwdLayer fwrInfo)
	{
		logger.fine("Car Sends Break");
		
		AWFullPCarBreak aw_app = new AWFullPCarBreak(info);
		AWFullPacket awpacket = new AWFullPacket(aw_app);
		
		sendMessage(sender, Constants.MulticastGroup, Constants.portMulticast, awpacket, fwrInfo);
	}
	
	public static void carSendAccident(DatagramSocket sender, TowerInfo towerInfo, CarInfo carInfo, AWFullPFwdLayer fwrInfo)
	{
		logger.fine("Car Sends Accident!");

		AWFullPCarAccident aw_app = new AWFullPCarAccident(towerInfo.getName(), carInfo);
		AWFullPacket awpacket = new AWFullPacket(aw_app);

		sendMessage(sender, Constants.MulticastGroup, Constants.portMulticast, awpacket, fwrInfo);
	}

	public static void sendAmbulanceInfo(DatagramSocket sender, CarInfo carInfo, AWFullPFwdLayer fwrInfo ,AmbulanceInfo ambulanceInfo) {
		System.out.println("Send AMB info to tower");
		AWFullPAmbPath aw_app = new AWFullPAmbPath(carInfo, ambulanceInfo);
		AWFullPacket awpacket = new AWFullPacket(aw_app);

		sendMessage(sender, Constants.MulticastGroup, Constants.portMulticast, awpacket, fwrInfo);
	}

	// Server send ambulance info to tower, with fwd header done
	public static void sendAmbulanceInfoToTower(DatagramSocket sender, InetAddress tower , AWFullPFwdLayer fwrInfo , AWFullPCloudAmbulanceServer ambulanceInfo) {
		AWFullPacket awpacket = new AWFullPacket(ambulanceInfo);

		sendMessage(sender, tower, Constants.towerPort, awpacket, fwrInfo);
	}


	public static void towerAnnouncement(DatagramSocket sender, TowerInfo tower , AWFullPFwdLayer fwrInfo)
	{
		//logger.fine("Tower Sends Hello to Car");

		AWFullPTowerAnnounce aw_app = new AWFullPTowerAnnounce(tower);
		AWFullPacket awpacket = new AWFullPacket(aw_app);

		sendMessage(sender, Constants.MulticastGroup, Constants.portMulticast, awpacket, fwrInfo);
	}

	public static void serverInfoBatchCloud(DatagramSocket sender, TowerInfo towerInfo, List<String> cars, InfoNode destination)
	{
		//logger.fine("Server Sends Batch to Cloud");
		
		AWFullPServerInfo aw_app = new AWFullPServerInfo(towerInfo.getName(), cars);
		AWFullPacket awpacket = new AWFullPacket(aw_app);
		
		sendMessage(sender, destination.ip, destination.port, awpacket);
	}

	public static void cloudInfoAmbulanceServer(DatagramSocket sender, InetAddress destinationIP, Position destination, Timestamp whenToSend)
	{
		System.out.println("Cloud sends ambulance path to server: " + destinationIP);

		AWFullPCloudAmbulanceServer aw_app = new AWFullPCloudAmbulanceServer(destination, whenToSend);
		AWFullPacket awpacket = new AWFullPacket(aw_app);

		sendMessage(sender, destinationIP, Constants.serverPort, awpacket);
	}

	
	public static void sendMessage(DatagramSocket sender, InetAddress to, int port, AWFullPacket message)
	{
		DatagramPacket packet = new DatagramPacket(message.toBytes(), message.toBytes().length, to, port);
		
		try {
			sender.send(packet);
			//logger.finest("\nSent packet " + message.toString() + " to " + to.toString() + ":" + port + "\n");
		} catch (IOException e) {
			logger.severe("IOException when sending packet " + message.toString() + " to " + to.toString() + ":" + port);
			logger.throwing("SendMessages", "sendMessage1", e);
		}
	}
	
	
	public static void sendMessage(DatagramSocket sender, InetAddress to, int port, AWFullPacket before, AWFullPFwdLayer fwrInfo)
	{
		AWFullPacket after = new AWFullPacket(before.appLayer, fwrInfo);
		
		DatagramPacket readyToSend = new DatagramPacket(after.toBytes(), after.toBytes().length, to, port);
		
		try {
			sender.send(readyToSend);
			//logger.finest("\nSent packet " + after.toString() + " to " + to.toString() + ":" + port + "\n");
		} catch (IOException e) {
			logger.severe("IOException when sending packet " + after.toString() + " to " + to.toString() + ":" + port);
			logger.throwing("SendMessages", "sendMessage2", e);
		}
	}
	
	
	// When forwarding is necessary
	public static void sendMessage(DatagramSocket sender, DatagramPacket packet, AWFullPFwdLayer fwrInfo)
	{
		AWFullPacket before = new AWFullPacket(packet);
		AWFullPacket after = new AWFullPacket(before.appLayer, fwrInfo);
		
		DatagramPacket readyToSend = new DatagramPacket(after.toBytes(), after.toBytes().length, packet.getAddress(), packet.getPort());
		
		try {
			sender.send(readyToSend);
			//logger.finest("\nSent packet " + after.toString() + " to " + packet.getAddress().toString() + ":" + packet.getPort() + "\n");
		} catch (IOException e) {
			logger.severe("IOException when sending packet " + after.toString() + " to " + packet.getAddress().toString() + ":" + packet.getPort());
			logger.throwing("SendMessages", "sendMessage3", e);
		}
	}


}
