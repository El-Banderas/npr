package AWFullP.FwdLayer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
//import java.util.logging.Logger;
import java.nio.ByteBuffer;

import AWFullP.AWFullPacket;
import AWFullP.MessageConstants;
import Common.CarInfo;

public class FWReceiveMessages
{
	//private static Logger logger = Logger.getLogger("npr.messages.received");
	
	
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
			throw new SelfCarMessage();
		FWRInfo fwrinfo = new FWRInfo(buf);
		
		// Maybe forward message?
		// TODO: Check Distance and duplicate messages
		if (fwrinfo.TTL > 1) {
			fwrinfo.updateInfo(carInfo);
			FWSendMessages.sendFWRMessage(sendSocket, fwrinfo);
		}
		
		// Return content of message
		// (after removing FW content?)
		ByteBuffer bbuf = ByteBuffer.wrap(fwrinfo.content);
		bbuf.getInt(); //<- where the fuck did this come from? TODO
		byte[] remaining = new byte[bbuf.remaining()];
		bbuf.get(remaining);
		
		AWFullPacket received = new AWFullPacket(remaining);
		
		// logger.info("Received Message:\n" + received.toString());
		
		return received;
	}
	
	/*public static AWFullPacket forwardHandleMessage1(DatagramSocket receiveSocket, DatagramSocket sendSocket, InetAddress myIp, CarInfo carInfo) throws IOException, SelfCarMessage
	{
		byte[] buf = new byte[MessageConstants.sizeBufferMessages];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		receiveSocket.receive(packet);
		
		if (packet.getAddress().equals(myIp))
			throw new SelfCarMessage();
		
		AWFullPacket aw = new AWFullPacket(buf);
		
		// Maybe forward message?
		// TODO: Check Distance and duplicate messages
		if (aw.forwardInfo.getTTL() > 1) {
			aw.forwardInfo.updateInfo(carInfo);
			FWSendMessages.sendFWRMessage(sendSocket, aw);
		}
		
		return aw;
	}*/
}
