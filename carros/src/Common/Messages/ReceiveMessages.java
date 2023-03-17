package Common.Messages;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;


public class ReceiveMessages {


	public static MessageAndType receiveData(DatagramSocket socket) throws IOException {
		byte[] buf = new byte[MessagesConstants.sizeBufferMessages];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		socket.receive(packet);
		int type = ByteBuffer.wrap(buf).getInt();
		MessageAndType received = new MessageAndType(type, packet.getData(), packet.getAddress());
		return received;
	}
}
