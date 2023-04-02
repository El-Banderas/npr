package AWFullP.AppLayer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;

import AWFullP.MessagesConstants;

public class AWFullPAppLayer implements IAWFullPAppLayer
{
	private int type;
	
	
	public AWFullPAppLayer(int type)
	{
		this.type = type;
	}
	
	public AWFullPAppLayer(byte[] arr)
	{
		ByteBuffer buf = ByteBuffer.wrap(arr);
		
		this.type = buf.getInt();
	}
	
	public AWFullPAppLayer(DatagramPacket packet)
	{
		this(packet.getData());
	}
	
	
	public int getType() {return this.type;}
	
	public byte[] toBytes()
	{
		byte[] buf = ByteBuffer.allocate(MessagesConstants.sizeBufferMessages)
				.putInt(this.type)
				.array();
		
		return buf;
	}
	
	public void sendTo(DatagramSocket from, InetAddress to, int port) throws IOException
	{
		byte[] content = this.toBytes();
		DatagramPacket packet = new DatagramPacket(content, content.length, to, port);
		from.send(packet);
	}
}
