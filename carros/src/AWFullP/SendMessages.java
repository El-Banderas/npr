package AWFullP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

import AWFullP.FWRMessages.FWRInfo;
import AWFullP.FWRMessages.FWSendMessages;
import Common.CarInfo;
import Common.Constants;
import Common.InfoNode;


public class SendMessages
{
	private static Logger logger =  Logger.getLogger("npr.messages.sent");

	public static void carSendBreak(DatagramSocket sender, FWRInfo fwrInfo)
	{
		//logger.info("Car Sends Break");
		
		byte[] buf = ByteBuffer.allocate(MessagesConstants.sizeBufferMessages)
				.putInt(MessagesConstants.BreakMessage)
				.array();
		
		//DatagramPacket packet = new DatagramPacket(buf, buf.length, Constants.MulticastGroup, Constants.portMulticast);
		sendMessage(sender, Constants.MulticastGroup, Constants.portMulticast, buf, fwrInfo);
	}
	
	public static void carSendAccident(DatagramSocket sender,FWRInfo fwrInfo )
	{
		//logger.info("Car Sends Accident!");
		
		byte[] buf = ByteBuffer.allocate(MessagesConstants.sizeBufferMessages)
				.putInt(MessagesConstants.AccidentMessage)
				.array();
		
		sendMessage(sender, Constants.MulticastGroup, Constants.portMulticast, buf, fwrInfo);
	}
	
	public static void carHellos(DatagramSocket sender, CarInfo info, FWRInfo fwrInfo)
	{
		//logger.info("Car Sends Hello");
		
		byte[] buf = ByteBuffer.allocate(MessagesConstants.sizeBufferMessages)
				.putInt(MessagesConstants.CAR_HELLO)
				.put(info.id.getBytes())
				.put("Hello".getBytes())
				.array();
		
		sendMessage(sender, Constants.MulticastGroup, Constants.portMulticast, buf, fwrInfo);
	}
	
	public static void carHelloTower(DatagramSocket sender, InfoNode destination)
	{
		//logger.info("Cars Sends Hello to Tower");
		
		byte[] buf = ByteBuffer.allocate(MessagesConstants.sizeBufferMessages)
				.putInt(MessagesConstants.CAR_HELLO)
				.put("Hello".getBytes())
				.array();
		
		sendMessage(sender, Constants.MulticastGroup, Constants.portMulticast, buf);
	}
	
	public static void towerHelloServer(DatagramSocket sender, InfoNode destination)
	{
		logger.info("Tower Sends Hello to Server");
		
		byte[] buf = ByteBuffer.allocate(MessagesConstants.sizeBufferMessages)
				.putInt(MessagesConstants.TowerHelloMessage)
				.put("Hello".getBytes())
				.array();
		
		sendMessage(sender, destination.ip, destination.port, buf);
	}
	
	public static void serverHelloCloud(DatagramSocket sender, InfoNode destination)
	{
		logger.info("Server Sends Hello to Cloud");
		
		byte[] buf = ByteBuffer.allocate(MessagesConstants.sizeBufferMessages)
				.putInt(MessagesConstants.ServerHelloMessage)
				.put("Hello".getBytes())
				.array();

		sendMessage(sender, destination.ip, destination.port, buf);
	}
	
	public static void towerHelloCar(DatagramSocket sender)
	{
		logger.info("Tower Sends Hello to Car");
		
		byte[] buf = ByteBuffer.allocate(MessagesConstants.sizeBufferMessages)
				.putInt(MessagesConstants.TowerHelloMessage)
				.array();
		
		sendMessage(sender, Constants.MulticastGroup, Constants.portMulticast, buf);
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

	//TODO: temporary public
	public static void sendMessage(DatagramSocket sender, InetAddress to, int port, byte[] content, FWRInfo fwrInfo)
	{
			DatagramPacket packet = new DatagramPacket(content, content.length, to, port);
			//sender.send(packet);
			FWSendMessages.sendFWRMessage(sender, packet, fwrInfo);
	}

	//TODO: temporary public
	public static void sendMessage(DatagramSocket sender, InetAddress to, int port, AWFullPacket message)
	{
		DatagramPacket packet = new DatagramPacket(message.content, message.content.length, to, port);
		try {
			sender.send(packet);
		} catch (IOException e) {
			logger.severe("IOException when sending binary packet to " + to.toString() + ":" + port);
			logger.throwing("SendMessages", "sendMessage", e);
		}
	}
	//TODO: temporary public
	public static void sendMessage(DatagramSocket sender, InetAddress to, int port, AWFullPacket message, FWRInfo fwrInfo)
	{
			DatagramPacket packet = new DatagramPacket(message.content, message.content.length, to, port);
	//		sender.send(packet);
			FWSendMessages.sendFWRMessage(sender, packet, fwrInfo);
	}
	// When forwarding is necessary
	private static void sendMessage(DatagramSocket send, DatagramPacket packet, FWRInfo fwrInfo)
	{
			FWSendMessages.sendFWRMessage(send, packet, fwrInfo);
	}
}
