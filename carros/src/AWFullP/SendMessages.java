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
import AWFullP.FwdLayer.FWSendMessages;
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
		
		AWFullPCarHello awpacket = new AWFullPCarHello(info.id);
		byte[] buf = awpacket.toBytes();
		
		sendMessage(sender, Constants.MulticastGroup, Constants.portMulticast, buf, fwrInfo);
	}
	
	public static void carSendBreak(DatagramSocket sender, FWRInfo fwrInfo)
	{
		//logger.info("Car Sends Break");
		
		AWFullPCarBreak awpacket = new AWFullPCarBreak();
		byte[] buf = awpacket.toBytes();
		
		sendMessage(sender, Constants.MulticastGroup, Constants.portMulticast, buf, fwrInfo);
	}
	
	public static void carSendAccident(DatagramSocket sender, TowerInfo towerInfo, CarInfo carInfo, FWRInfo fwrInfo )
	{
		//logger.info("Car Sends Accident!");
		
		AWFullPCarAccident awpacket = new AWFullPCarAccident(towerInfo.getName(), carInfo.id, carInfo.pos);
		byte[] buf = awpacket.toBytes();
		
		sendMessage(sender, Constants.MulticastGroup, Constants.portMulticast, buf, fwrInfo);
	}
	
	//TODO: Juntar ao TowerHelloServer ou assim
	public static void towerHelloCar(DatagramSocket sender)
	{
		logger.info("Tower Sends Hello to Car");
		
		AWFullPTowerHello awpacket = new AWFullPTowerHello();
		byte[] buf = awpacket.toBytes();
		
		sendMessage(sender, Constants.MulticastGroup, Constants.portMulticast, buf);
	}
	
	public static void towerHelloServer(DatagramSocket sender, InfoNode destination)
	{
		logger.info("Tower Sends Hello to Server");
		
		AWFullPTowerHello awpacket = new AWFullPTowerHello();
		byte[] buf = awpacket.toBytes();
		
		sendMessage(sender, destination.ip, destination.port, buf);
	}
	
	public static void serverHelloCloud(DatagramSocket sender, InfoNode destination)
	{
		logger.info("Server Sends Hello to Cloud");
		
		AWFullPServerHello awpacket = new AWFullPServerHello();
		byte[] buf = awpacket.toBytes();
		
		sendMessage(sender, destination.ip, destination.port, buf);
	}
	
	public static void sendMessage(DatagramSocket sender, InetAddress to, int port, byte[] content)
	{
		try {
			DatagramPacket packet = new DatagramPacket(content, content.length, to, port);
			sender.send(packet);
		} catch (IOException e) {
			logger.severe("IOException when sending binary packet to " + to.toString() + ":" + port);
			logger.throwing("SendMessages", "sendMessage", e);
		}
	}
	
	public static void sendMessage(DatagramSocket sender, InetAddress to, int port, byte[] content, FWRInfo fwrInfo)
	{
			DatagramPacket packet = new DatagramPacket(content, content.length, to, port);
			try {
				sender.send(packet);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//FWSendMessages.sendFWRMessage(sender, packet, fwrInfo);
	}
	
	public static void sendMessage(DatagramSocket sender, InetAddress to, int port, AWFullPacket message)
	{
		DatagramPacket packet = new DatagramPacket(message.getContent(), message.getContent().length, to, port);
		try {
			sender.send(packet);
		} catch (IOException e) {
			logger.severe("IOException when sending binary packet to " + to.toString() + ":" + port);
			logger.throwing("SendMessages", "sendMessage", e);
		}
	}
	
	public static void sendMessage(DatagramSocket sender, InetAddress to, int port, AWFullPacket message, FWRInfo fwrInfo)
	{
		DatagramPacket packet = new DatagramPacket(message.getContent(), message.getContent().length, to, port);
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
