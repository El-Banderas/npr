package Common.Messages;

import Common.Constants;
import Common.InfoNode;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;

public class SendMessages {

	public static void carHellos(DatagramSocket sender){
		if (Constants.linux){
			System.out.println("Send hellos to everyone");
		byte[] buf = ByteBuffer.allocate(MessagesConstants.sizeBufferMessages).putInt(MessagesConstants.HelloMessage).put("Hello".getBytes()).array();
		DatagramPacket packet = new DatagramPacket(buf, buf.length, Constants.MulticastGroup, Constants.portMulticast);
		sendMessage(sender, packet);
		}

	}
	public static void carHelloTower(DatagramSocket sender, InfoNode destination){
		byte[] buf = ByteBuffer.allocate(MessagesConstants.sizeBufferMessages).putInt(MessagesConstants.HelloMessage).put("Hello".getBytes()).array();
		DatagramPacket packet;
		if (Constants.linux){
			packet = new DatagramPacket(buf, buf.length, Constants.MulticastGroup, Constants.portMulticast);
		}
		else {
			packet = new DatagramPacket(buf, buf.length, destination.ip, destination.port);
		}
		sendMessage(sender, packet);
	}

	public static void towerHelloCloud(DatagramSocket sender, InfoNode destination){
		byte[]  buf = "Hello".getBytes();
		DatagramPacket packet = new DatagramPacket(buf, buf.length, destination.ip, destination.port);
		sendMessage(sender, packet);
	}

	public static void sendMessage(DatagramSocket send, DatagramPacket packet){
		try {
			send.send(packet);
		} catch (IOException e) {
			//throw new RuntimeException(e);
			System.out.println("[ERROR] in sending message");
		}
	}
}
