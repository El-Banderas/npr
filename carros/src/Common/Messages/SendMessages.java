package Common.Messages;

import Common.CarInfo;
import Common.Constants;
import Common.InfoNode;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;


public class SendMessages {

	public static void carSendBreak(DatagramSocket sender)
	{
		System.out.println("Send Break to everyone");
		
		byte[] buf = ByteBuffer.allocate(MessagesConstants.sizeBufferMessages)
				.putInt(MessagesConstants.BreakMessage)
				.array();
		
		DatagramPacket packet = new DatagramPacket(buf, buf.length, Constants.MulticastGroup, Constants.portMulticast);
		sendMessage(sender, packet);
	}
	
	public static void carHellos(DatagramSocket sender, CarInfo info)
	{
		//System.out.println("Send hellos to everyone");
		
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
		byte[] buf = ByteBuffer.allocate(MessagesConstants.sizeBufferMessages)
				.putInt(MessagesConstants.CarHelloMessage)
				.put("Hello".getBytes())
				.array();
		
		DatagramPacket packet = new DatagramPacket(buf, buf.length, Constants.MulticastGroup, Constants.portMulticast);
		sendMessage(sender, packet);
	}
	
	public static void towerHelloServer(DatagramSocket sender, InfoNode destination)
	{
		byte[] buf = ByteBuffer.allocate(MessagesConstants.sizeBufferMessages)
				.putInt(MessagesConstants.TowerHelloMessage)
				.put("Hello".getBytes())
				.array();

		DatagramPacket packet = new DatagramPacket(buf, buf.length, destination.ip, destination.port);
		sendMessage(sender, packet);
	}
	
	public static void serverHelloCloud(DatagramSocket sender)
	{
		byte[] buf = ByteBuffer.allocate(MessagesConstants.sizeBufferMessages)
				.putInt(MessagesConstants.ServerHelloMessage)
				.put("Hello".getBytes())
				.array();

		DatagramPacket packet = new DatagramPacket(buf, buf.length, Constants.CloudIP, Constants.cloudPort);
		sendMessage(sender, packet);
	}
	
	public static void carSendAccident(DatagramSocket send)
	{
		System.out.println("!!!!Send accident to everyone");
		
		byte[] buf = ByteBuffer.allocate(MessagesConstants.sizeBufferMessages)
				.putInt(MessagesConstants.AccidentMessage)
				.array();
		
		DatagramPacket packet = new DatagramPacket(buf, buf.length, Constants.MulticastGroup, Constants.portMulticast);
		sendMessage(send, packet);
	}
	
	public static void forwardMessage(MessageAndType message, DatagramSocket sendSocket, InfoNode thisServer)
	{
		DatagramPacket packet = new DatagramPacket(message.content, message.content.length, thisServer.ip, thisServer.port);
		sendMessage(sendSocket, packet);
	}
	
	public static void towerHelloCar(DatagramSocket sendSocket)
	{
		System.out.println("Send accident to everyone");
		
		byte[] buf = ByteBuffer.allocate(MessagesConstants.sizeBufferMessages)
				.putInt(MessagesConstants.TowerHelloMessage)
				.array();
		
		DatagramPacket packet = new DatagramPacket(buf, buf.length, Constants.MulticastGroup, Constants.portMulticast);
		sendMessage(sendSocket, packet);
	}
	
	public static void sendMessage(DatagramSocket send, DatagramPacket packet)
	{
		try {
			send.send(packet);
		} catch (IOException e) {
			//throw new RuntimeException(e);
			System.out.println("[ERROR] in sending message");
		}
	}
}
