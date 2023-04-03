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
import AWFullP.FwdLayer.FWRInfo;
import Common.CarInfo;
import Common.Constants;
import Common.InfoNode;
import Common.TowerInfo;


public class SendMessages
{
	private static Logger logger =  Logger.getLogger("npr.messages.sent");
	
	
	public static void carHello(DatagramSocket sender, CarInfo info, FWRInfo fwrInfo)
	{
		//logger.info("Car Sends Hello");
		
		AWFullPCarHello aw_app = new AWFullPCarHello(info.id);
		AWFullPacket awpacket = new AWFullPacket(aw_app);
		
		sendMessage(sender, Constants.MulticastGroup, Constants.portMulticast, awpacket, fwrInfo);
	}
	
	public static void carSendBreak(DatagramSocket sender, FWRInfo fwrInfo)
	{
		//logger.info("Car Sends Break");
		
		AWFullPCarBreak aw_app = new AWFullPCarBreak();
		AWFullPacket awpacket = new AWFullPacket(aw_app);
		
		sendMessage(sender, Constants.MulticastGroup, Constants.portMulticast, awpacket, fwrInfo);
	}
	
	public static void carSendAccident(DatagramSocket sender, TowerInfo towerInfo, CarInfo carInfo, FWRInfo fwrInfo)
	{
		//logger.info("Car Sends Accident!");
		
		AWFullPCarAccident aw_app = new AWFullPCarAccident(towerInfo.getName(), carInfo.id, carInfo.pos);
		AWFullPacket awpacket = new AWFullPacket(aw_app);
		
		sendMessage(sender, Constants.MulticastGroup, Constants.portMulticast, awpacket, fwrInfo);
	}
	
	//TODO: FWRInfo?
	public static void towerHelloCar(DatagramSocket sender)
	{
		logger.info("Tower Sends Hello to Car");
		
		AWFullPTowerHello aw_app = new AWFullPTowerHello();
		AWFullPacket awpacket = new AWFullPacket(aw_app);
		
		sendMessage(sender, Constants.MulticastGroup, Constants.portMulticast, awpacket);
	}
	
	public static void towerHelloServer(DatagramSocket sender, InfoNode destination)
	{
		logger.info("Tower Sends Hello to Server");
		
		AWFullPTowerHello aw_app = new AWFullPTowerHello();
		AWFullPacket awpacket = new AWFullPacket(aw_app);
		
		sendMessage(sender, destination.ip, destination.port, awpacket);
	}
	
	public static void serverHelloCloud(DatagramSocket sender, InfoNode destination)
	{
		logger.info("Server Sends Hello to Cloud");
		
		AWFullPServerHello aw_app = new AWFullPServerHello();
		AWFullPacket awpacket = new AWFullPacket(aw_app);
		
		sendMessage(sender, destination.ip, destination.port, awpacket);
	}
	
	public static void sendMessage(DatagramSocket sender, InetAddress to, int port, AWFullPacket message)
	{
		DatagramPacket packet = new DatagramPacket(message.toBytes(), message.toBytes().length, to, port);
		try {
			sender.send(packet);
		} catch (IOException e) {
			logger.severe("IOException when sending binary packet to " + to.toString() + ":" + port);
			logger.throwing("SendMessages", "sendMessage", e);
		}
	}
	
	public static void sendMessage(DatagramSocket sender, InetAddress to, int port, AWFullPacket message, FWRInfo fwrInfo)
	{
		DatagramPacket packet = new DatagramPacket(message.toBytes(), message.toBytes().length, to, port);
		try {
			sender.send(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//FWSendMessages.sendFWRMessage(sender, packet, fwrInfo);
	}
	
	// When forwarding is necessary
	public static void sendMessage(DatagramSocket send, DatagramPacket packet, FWRInfo fwrInfo)
	{
		try {
			send.send(packet);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//FWSendMessages.sendFWRMessage(send, packet, fwrInfo);
	}
}
