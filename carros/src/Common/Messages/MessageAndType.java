package Common.Messages;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.ByteBuffer;


public class MessageAndType
{
	public int type;
	public byte[] content;
	public InetAddress ipSender;
	
	
	public MessageAndType(int type, byte[] content, InetAddress ipSender)
	{
		this.type = type;
		this.content = content;
		this.ipSender = ipSender;
	}
	
	public MessageAndType(DatagramPacket packet)
	{
		ByteBuffer buf = ByteBuffer.wrap(packet.getData());
		
		this.type = buf.getInt();
		
		this.content = new byte[buf.remaining()];
		buf.get(this.content, 0 /*bbuf.position()*/, buf.remaining());
		
		this.ipSender = packet.getAddress();
	}
	
	
	public byte[] toBytes()
	{
		byte[] buf = ByteBuffer.allocate(MessagesConstants.sizeBufferMessages)
				.putInt(this.type)
				.put(this.content)
				.array();
		
		return buf;
	}
	
	@Override
	public String toString()
	{
		return new String(
				"{"
			+	" from: " + ipSender.toString()
			+	" ; type: " + type
			+	" ; size: " + content.length
			+	"}"
			);
	}
}
