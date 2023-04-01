package AWFullP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.logging.Logger;


public class ReceiveMessages
{
	private static Logger logger =  Logger.getLogger("npr.messages.received");


	public static AWFullPacket receiveData(DatagramSocket socket) throws IOException
	{
		byte[] buf = new byte[MessagesConstants.sizeBufferMessages];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		socket.receive(packet);

		AWFullPacket received = new AWFullPacket(packet);

		logger.info("Received Message:\n" + received.toString());

		return received;
	}
}
