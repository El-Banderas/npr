package AWFullP;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.ByteBuffer;


public class AWFullPacket
{
	public int type;
	public byte[] content;
	public InetAddress ipSender;
	
	
	/**
	 * Constructor for objects of class AWFullPacket.
	 *
	 * Creates a AWFullProtocol general packet. 
	 *
	 * @param type Packet type
	 * @param content Array of bytes to be sent in packet
	 * @param ipSender IP of the sender
	 */
	public AWFullPacket(int type, byte[] content, InetAddress ipSender)
	{
		this.type = type;
		this.content = content;
		this.ipSender = ipSender;
	}
	
	/**
	 * Constructor for objects of class AWFullPacket.
	 *
	 * Creates a AWFullProtocol general packet. 
	 *
	 * @param type Packet type
	 * @param content Array of bytes to be sent in packet
	 */
	public AWFullPacket(int type, byte[] content)
	{
		this.type = type;
		this.content = content;
		this.ipSender = null;
	}

	/**
	 * Constructor for objects of class AWFullPacket.
	 *
	 * Creates a AWFullProtocol general packet by decapsulating a DatagramPacket. 
	 *
	 * @param packet DatagramPacket to decapsulate
	 */
	public AWFullPacket(DatagramPacket packet)
	{
		ByteBuffer buf = ByteBuffer.wrap(packet.getData());
		
		this.type = buf.getInt();
		
		this.content = new byte[buf.remaining()];
		buf.get(this.content, 0 /*bbuf.position()*/, buf.remaining());
		
		this.ipSender = packet.getAddress();
	}
	
	
	/**
	 * Returns packet in byte array form, to be sent through a DatagramPacket.
	 *
	 * @return Packet in byte array form
	 */
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
