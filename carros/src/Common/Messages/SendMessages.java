package Common.Messages;

import Common.CarInfo;
import Common.Constants;
import Common.InfoNode;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
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
		
		DatagramPacket packet = new DatagramPacket(buf, buf.length, Constants.MulticastGroup, Constants.portMulticast);
		sendMessage(sender, packet);
	}
	
	public static void carHellos(DatagramSocket sender, CarInfo info)
	{
		//logger.info("Car Sends Hello");
		
		byte[] buf = ByteBuffer.allocate(MessagesConstants.sizeBufferMessages)
				.putInt(MessagesConstants.CarHelloMessage)
				.put(info.id.getBytes())
				.put("Hello".getBytes())
				.array();
		
		DatagramPacket packet = new DatagramPacket(buf, buf.length, Constants.MulticastGroup, Constants.portMulticast);
		sendMessage(sender, packet);
	}
	
	public static void carHelloTower(DatagramSocket sender, InfoNode destination)
	{
		//logger.info("Cars Sends Hello to Tower");
		
		byte[] buf = ByteBuffer.allocate(MessagesConstants.sizeBufferMessages)
				.putInt(MessagesConstants.CarHelloMessage)
				.put("Hello".getBytes())
				.array();
		
		DatagramPacket packet = new DatagramPacket(buf, buf.length, Constants.MulticastGroup, Constants.portMulticast);
		sendMessage(sender, packet);
	}
	
	public static void towerHelloServer(DatagramSocket sender, InfoNode destination)
	{
		logger.info("Tower Sends Hello to Server");
		
		byte[] buf = ByteBuffer.allocate(MessagesConstants.sizeBufferMessages)
				.putInt(MessagesConstants.TowerHelloMessage)
				.put("Hello".getBytes())
				.array();
		
		DatagramPacket packet = new DatagramPacket(buf, buf.length, destination.ip, destination.port);
		sendMessage(sender, packet);
	}
	
	public static void serverHelloCloud(DatagramSocket sender)
	{
		logger.info("Server Sends Hello to Cloud");
		
		byte[] buf = ByteBuffer.allocate(MessagesConstants.sizeBufferMessages)
				.putInt(MessagesConstants.ServerHelloMessage)
				.put("Hello".getBytes())
				.array();

		DatagramPacket packet = new DatagramPacket(buf, buf.length, Constants.CloudIP, Constants.cloudPort);
		sendMessage(sender, packet);
	}
	
	public static void carSendAccident(DatagramSocket send)
	{
		//logger.info("Car Sends Accident!");
		
		byte[] buf = ByteBuffer.allocate(MessagesConstants.sizeBufferMessages)
				.putInt(MessagesConstants.AccidentMessage)
				.array();
		
		DatagramPacket packet = new DatagramPacket(buf, buf.length, Constants.MulticastGroup, Constants.portMulticast);
		sendMessage(send, packet);
	}
	
	public static void forwardMessage(MessageAndType message, DatagramSocket sendSocket, InfoNode thisServer)
	{
		logger.info("Message Forwarded to Server: " + message.toString());
		
		DatagramPacket packet = new DatagramPacket(message.content, message.content.length, thisServer.ip, thisServer.port);
		sendMessage(sendSocket, packet);
	}
	
	public static void towerHelloCar(DatagramSocket sendSocket)
	{
		logger.info("Tower Sends Hello to Car");
		
		byte[] buf = ByteBuffer.allocate(MessagesConstants.sizeBufferMessages)
				.putInt(MessagesConstants.TowerHelloMessage)
				.array();
		
		DatagramPacket packet = new DatagramPacket(buf, buf.length, Constants.MulticastGroup, Constants.portMulticast);
		sendMessage(sendSocket, packet);
	}
	
	private static void sendMessage(DatagramSocket send, DatagramPacket packet)
	{
		try {
			send.send(packet);
		} catch (IOException e) {
			logger.severe("IOException when sending " + packet.toString() + " to " + send.toString());
			logger.throwing("SendMessages", "sendMessage", e);
		}
	}
}
