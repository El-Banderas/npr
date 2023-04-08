package AWFullP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import AWFullP.FwdLayer.SelfCarMessage;
import Common.CarInfo;
import Common.Constants;


public class ReceiveMessages
{
	private static Logger logger = Logger.getLogger("npr.messages.received");
	static {
		ConsoleHandler handler = new ConsoleHandler();
		handler.setFormatter(new SimpleFormatter());
		handler.setLevel(Level.ALL);
		logger.addHandler(handler);
		
		logger.setLevel(Level.SEVERE);
	}


	public static AWFullPacket receiveData(DatagramSocket socket) throws IOException
	{
		byte[] buf = new byte[MessageConstants.BUFFER_SIZE];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		socket.receive(packet);

		AWFullPacket received = new AWFullPacket(packet);

		logger.finest("\nReceived packet " + received.toString() + " from " + packet.getAddress().toString() + ":" + packet.getPort());

		return received;
	}


	public static AWFullPacket parseMessageCar(DatagramSocket receiveSocket, InetAddress myIp, String myID) throws IOException, SelfCarMessage
	{
		byte[] buf = new byte[MessageConstants.BUFFER_SIZE];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		receiveSocket.receive(packet);
		
		AWFullPacket aw = new AWFullPacket(buf);

		String before = aw.toString();
		
		if (packet.getAddress().equals(myIp) || aw.forwardInfo.getSenderID().equals(myID)) {
			logger.warning("\nPossible self message (me: " + myIp + ", other: " + packet.getAddress() + ", message: " + before + ")\n");
			throw new SelfCarMessage();
		}
		
		return aw;
	}


	public static void maybeForwardMessage(AWFullPacket aw, DatagramSocket sendSocket, CarInfo carInfo)
	{
		String before = aw.toString();

		// TODO: Check Distance and duplicate messages
		aw.forwardInfo.updateInfo(carInfo);

		logger.finer("\nForwarding packet " + before + " ---> " + aw );
		SendMessages.sendMessage(sendSocket, Constants.MulticastGroup, Constants.portMulticast, aw);
	}
}
