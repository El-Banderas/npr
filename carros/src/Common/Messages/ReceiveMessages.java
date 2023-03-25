package Common.Messages;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;
import java.util.logging.Logger;


public class ReceiveMessages {
	
	private static Logger logger =  Logger.getLogger("npr.messages.received");
	
	public static MessageAndType receiveData(DatagramSocket socket) throws IOException
	{
		byte[] buf = new byte[MessagesConstants.sizeBufferMessages];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		socket.receive(packet);
		
		ByteBuffer bbuf = ByteBuffer.wrap(buf);
		int type = bbuf.getInt();
		
		byte[] remaining = new byte[bbuf.remaining()];
		bbuf.get(remaining, 0 /*bbuf.position()*/, bbuf.remaining());
		
		MessageAndType received = new MessageAndType(type, remaining, packet.getAddress());
		
		logger.info("Received Message:\n" + received.toString());
		
		return received;
	}
}
