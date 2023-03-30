package Common.Messages;

import Common.CarInfo;
import Common.Constants;
import Common.InfoNode;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.logging.Logger;


public class SendMessages
{
	private static Logger logger =  Logger.getLogger("npr.messages.sent");
	
	
	public static void carSendBreak(DatagramSocket sender)
	{
		//logger.info("Car Sends Break");
		
		byte[] buf = ByteBuffer.allocate(MessagesConstants.sizeBufferMessages)
				.putInt(MessagesConstants.BreakMessage)
				.array();
		
		sendMessage(sender, Constants.MulticastGroup, Constants.portMulticast, buf);
	}
	
	public static void carSendAccident(DatagramSocket sender)
	{
		//logger.info("Car Sends Accident!");
		
		byte[] buf = ByteBuffer.allocate(MessagesConstants.sizeBufferMessages)
				.putInt(MessagesConstants.AccidentMessage)
				.array();
		
		sendMessage(sender, Constants.MulticastGroup, Constants.portMulticast, buf);
	}
	
	public static void carHellos(DatagramSocket sender, CarInfo info)
	{
		//logger.info("Car Sends Hello");
		
		byte[] buf = ByteBuffer.allocate(MessagesConstants.sizeBufferMessages)
				.putInt(MessagesConstants.CarHelloMessage)
				.put(info.id.getBytes())
				.put("Hello".getBytes())
				.array();
		
		sendMessage(sender, Constants.MulticastGroup, Constants.portMulticast, buf);
	}
	
	public static void carHelloTower(DatagramSocket sender, InfoNode destination)
	{
		//logger.info("Cars Sends Hello to Tower");
		
		byte[] buf = ByteBuffer.allocate(MessagesConstants.sizeBufferMessages)
				.putInt(MessagesConstants.CarHelloMessage)
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
	
	
	//TODO: temporary public
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
	public static void sendMessage(DatagramSocket sender, InetAddress to, int port, MessageAndType message)
	{
		try {
			DatagramPacket packet = new DatagramPacket(message.content, message.content.length, to, port);
			sender.send(packet);
		} catch (IOException e) {
			logger.severe("IOException when sending " + message.toString() + " to " + to.toString() + ":" + port);
			logger.throwing("SendMessages", "sendMessage", e);
		}
	}
}
