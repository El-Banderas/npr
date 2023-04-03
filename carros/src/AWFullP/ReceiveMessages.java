package AWFullP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Logger;

import AWFullP.FwdLayer.SelfCarMessage;
import Common.CarInfo;
import Common.Constants;


public class ReceiveMessages
{
	private static Logger logger =  Logger.getLogger("npr.messages.received");
	
	
	public static AWFullPacket receiveData(DatagramSocket socket) throws IOException
	{
		byte[] buf = new byte[MessageConstants.sizeBufferMessages];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		socket.receive(packet);

		AWFullPacket received = new AWFullPacket(packet);

		logger.info("\nReceived packet " + received.toString() + " from " + packet.getAddress().toString() + ":" + packet.getPort());

		return received;
	}
	
	/**
	 * Receives and forwards, when in case, received messages.
	 *
	 * @param receiveSocket - To receive messages.
	 * @param sendSocket    - To resend messages, with TTL and positions changed.
	 * @param myIp          - To filter messages send from the same car.
	 * @param carInfo       - To update messages that are send, with position and ID of car.
	 * @return
	 * @throws IOException
	 */
	public static AWFullPacket forwardHandleMessage(DatagramSocket receiveSocket, DatagramSocket sendSocket, InetAddress myIp, CarInfo carInfo) throws IOException, SelfCarMessage
	{
		byte[] buf = new byte[MessageConstants.sizeBufferMessages];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		receiveSocket.receive(packet);
		
		if (packet.getAddress().equals(myIp))
			logger.info("\nPossible self message (me: " + myIp + ", other: " + packet.getAddress() + ")\n"); //throw new SelfCarMessage();
		
		AWFullPacket aw = new AWFullPacket(buf);

		String before = aw.toString();
		
		// Maybe forward message?
		// TODO: Check Distance and duplicate messages
		if (aw.forwardInfo.getTTL() > 1) {
			aw.forwardInfo.updateInfo(carInfo);
			SendMessages.sendMessage(sendSocket, Constants.MulticastGroup, Constants.portMulticast, aw);
			logger.info("\nForwarding packet " + before + " ---> " + aw + " from " + packet.getAddress().toString() + ":" + packet.getPort() + " to " + Constants.MulticastGroup.toString() + ":" + Constants.portMulticast + "\n");
		} else {
			logger.info("\nDiscarding packet " + before + " from " + packet.getAddress().toString() + ":" + packet.getPort() + " to " + Constants.MulticastGroup.toString() + ":" + Constants.portMulticast + "\n");
		}
		
		return aw;
	}
}
