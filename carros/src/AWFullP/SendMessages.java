package AWFullP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;
import java.util.logging.Logger;

import AWFullP.AppLayer.AWFullPCarAccident;
import AWFullP.AppLayer.AWFullPCarBreak;
import AWFullP.AppLayer.AWFullPCarHello;
import AWFullP.AppLayer.AWFullPServerInfo;
import AWFullP.AppLayer.AWFullPTowerAnnounce;
import AWFullP.FwdLayer.AWFullPFwdLayer;
import Common.CarInfo;
import Common.Constants;
import Common.InfoNode;
import Common.TowerInfo;


public class SendMessages
{
	private static Logger logger =  Logger.getLogger("npr.messages.sent");
	private static boolean debug = false;
	
	
	public static void carHello(DatagramSocket sender, CarInfo info, AWFullPFwdLayer fwrInfo)
	{
		//logger.info("Car Sends Hello");
		
		AWFullPCarHello aw_app = new AWFullPCarHello(info);
		AWFullPacket awpacket = new AWFullPacket(aw_app);
		
		sendMessage(sender, Constants.MulticastGroup, Constants.portMulticast, awpacket, fwrInfo);
	}
	
	public static void carSendBreak(DatagramSocket sender, CarInfo info, AWFullPFwdLayer fwrInfo)
	{
		//logger.info("Car Sends Break");
		
		AWFullPCarBreak aw_app = new AWFullPCarBreak(info);
		AWFullPacket awpacket = new AWFullPacket(aw_app);
		
		sendMessage(sender, Constants.MulticastGroup, Constants.portMulticast, awpacket, fwrInfo);
	}
	
	public static void carSendAccident(DatagramSocket sender, TowerInfo towerInfo, CarInfo carInfo, AWFullPFwdLayer fwrInfo )
	{
		//logger.info("Car Sends Accident!");
		
		AWFullPCarAccident aw_app = new AWFullPCarAccident(towerInfo.getName(), carInfo);
		AWFullPacket awpacket = new AWFullPacket(aw_app);
		
		sendMessage(sender, Constants.MulticastGroup, Constants.portMulticast, awpacket, fwrInfo);
	}
	
	//TODO: FWRInfo?
	public static void towerAnnouncement(DatagramSocket sender, TowerInfo tower , AWFullPFwdLayer fwrInfo )
	{
		//logger.info("Tower Sends Hello to Car");
		
		AWFullPTowerAnnounce aw_app = new AWFullPTowerAnnounce(tower);
		AWFullPacket awpacket = new AWFullPacket(aw_app);
		
		sendMessage(sender, Constants.MulticastGroup, Constants.portMulticast, awpacket, fwrInfo);
	}
	
	public static void serverInfoBatchCloud(DatagramSocket sender, TowerInfo towerInfo, List<String> cars, InfoNode destination)
	{
		//logger.info("Server Sends Batch to Cloud");
		
		AWFullPServerInfo aw_app = new AWFullPServerInfo(towerInfo.getName(), cars);
		AWFullPacket awpacket = new AWFullPacket(aw_app);
		
		sendMessage(sender, destination.ip, destination.port, awpacket);
	}
	
	
	
	public static void sendMessage(DatagramSocket sender, InetAddress to, int port, AWFullPacket message)
	{
		DatagramPacket packet = new DatagramPacket(message.toBytes(), message.toBytes().length, to, port);
		
		try {
			sender.send(packet);
			if (debug) logger.info("\nSent packet " + message.toString() + " to " + to.toString() + ":" + port + "\n");
		} catch (IOException e) {
			logger.severe("IOException when sending packet " + message.toString() + " to " + to.toString() + ":" + port);
			logger.throwing("SendMessages", "sendMessage", e);
		}
	}
	
	public static void sendMessage(DatagramSocket sender, InetAddress to, int port, AWFullPacket before, AWFullPFwdLayer fwrInfo)
	{
		AWFullPacket after = new AWFullPacket(before.appLayer, fwrInfo);
		
		DatagramPacket readyToSend = new DatagramPacket(after.toBytes(), after.toBytes().length, to, port);
		
		try {
			sender.send(readyToSend);
			if (debug) logger.info("\nSent packet " + after.toString() + " to " + to.toString() + ":" + port + "\n");
		} catch (IOException e) {
			logger.severe("IOException when sending packet " + after.toString() + " to " + to.toString() + ":" + port);
			logger.throwing("SendMessages", "sendMessage", e);
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
			if (debug) logger.info("\nSent packet " + after.toString() + " to " + packet.getAddress().toString() + ":" + packet.getPort() + "\n");
		} catch (IOException e) {
			logger.severe("IOException when sending packet " + after.toString() + " to " + packet.getAddress().toString() + ":" + packet.getPort());
			logger.throwing("SendMessages", "sendMessage", e);
		}
	}
}
