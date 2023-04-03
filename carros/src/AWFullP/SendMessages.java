package AWFullP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Logger;

import AWFullP.AppLayer.AWFullPCarAccident;
import AWFullP.AppLayer.AWFullPCarBreak;
import AWFullP.AppLayer.AWFullPCarHello;
import AWFullP.AppLayer.AWFullPServerHello;
import AWFullP.AppLayer.AWFullPTowerHello;
import AWFullP.FwdLayer.AWFullPFwdLayer;
import Common.CarInfo;
import Common.Constants;
import Common.InfoNode;
import Common.TowerInfo;


public class SendMessages
{
	private static Logger logger =  Logger.getLogger("npr.messages.sent");
	
	
	public static void carHello(DatagramSocket sender, CarInfo info, AWFullPFwdLayer fwrInfo)
	{
		//logger.info("Car Sends Hello");
		
		AWFullPCarHello aw_app = new AWFullPCarHello(info.getID());
		AWFullPacket awpacket = new AWFullPacket(aw_app);
		
		sendMessage(sender, Constants.MulticastGroup, Constants.portMulticast, awpacket, fwrInfo);
	}
	
	public static void carSendBreak(DatagramSocket sender, AWFullPFwdLayer fwrInfo)
	{
		//logger.info("Car Sends Break");
		
		AWFullPCarBreak aw_app = new AWFullPCarBreak();
		AWFullPacket awpacket = new AWFullPacket(aw_app);
		
		sendMessage(sender, Constants.MulticastGroup, Constants.portMulticast, awpacket, fwrInfo);
	}
	
	public static void carSendAccident(DatagramSocket sender, TowerInfo towerInfo, CarInfo carInfo, AWFullPFwdLayer fwrInfo )
	{
		//logger.info("Car Sends Accident!");
		
		AWFullPCarAccident aw_app = new AWFullPCarAccident(towerInfo.getName(), carInfo.getID(), carInfo.getPosition());
		AWFullPacket awpacket = new AWFullPacket(aw_app);
		
		sendMessage(sender, Constants.MulticastGroup, Constants.portMulticast, awpacket, fwrInfo);
	}
	
	//TODO: FWRInfo?
	public static void towerHelloCar(DatagramSocket sender)
	{
		//logger.info("Tower Sends Hello to Car");
		
		AWFullPTowerHello aw_app = new AWFullPTowerHello();
		AWFullPacket awpacket = new AWFullPacket(aw_app);
		
		sendMessage(sender, Constants.MulticastGroup, Constants.portMulticast, awpacket);
	}
	
	public static void towerHelloServer(DatagramSocket sender, InfoNode destination)
	{
		//logger.info("Tower Sends Hello to Server");
		
		AWFullPTowerHello aw_app = new AWFullPTowerHello();
		AWFullPacket awpacket = new AWFullPacket(aw_app);
		
		sendMessage(sender, destination.ip, destination.port, awpacket);
	}
	
	public static void serverHelloCloud(DatagramSocket sender, InfoNode destination)
	{
		//logger.info("Server Sends Hello to Cloud");
		
		AWFullPServerHello aw_app = new AWFullPServerHello();
		AWFullPacket awpacket = new AWFullPacket(aw_app);
		
		sendMessage(sender, destination.ip, destination.port, awpacket);
	}
	
	
	
	public static void sendMessage(DatagramSocket sender, InetAddress to, int port, AWFullPacket message)
	{
		DatagramPacket packet = new DatagramPacket(message.toBytes(), message.toBytes().length, to, port);
		
		try {
			sender.send(packet);
			logger.info("\nSent packet " + message.toString() + " to " + to.toString() + ":" + port + "\n");
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
			logger.info("\nSent packet " + after.toString() + " to " + to.toString() + ":" + port + "\n");
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
			logger.info("\nSent packet " + after.toString() + " to " + packet.getAddress().toString() + ":" + packet.getPort() + "\n");
		} catch (IOException e) {
			logger.severe("IOException when sending packet " + after.toString() + " to " + packet.getAddress().toString() + ":" + packet.getPort());
			logger.throwing("SendMessages", "sendMessage", e);
		}
	}
}
