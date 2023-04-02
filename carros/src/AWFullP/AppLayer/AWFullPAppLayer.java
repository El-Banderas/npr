package AWFullP.AppLayer;

//import java.io.IOException;
import java.net.DatagramPacket;
//import java.net.DatagramSocket;
//import java.net.InetAddress;
import java.nio.ByteBuffer;

import AWFullP.MessageConstants;

public class AWFullPAppLayer implements IAWFullPAppLayer
{
	private int type;
	
	
	protected AWFullPAppLayer(int type)
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

	public static int getType(byte[] arr)
	{
		ByteBuffer buf = ByteBuffer.wrap(arr);
		return buf.getInt();
	}

	public static int getType(DatagramPacket packet)
	{
		return getType(packet.getData());
	}
	
	public byte[] toBytes()
	{
		byte[] buf = ByteBuffer.allocate(MessageConstants.APP_HEADER_SIZE)
				.putInt(this.type)
				.array();
		
		return buf;
	}
	
	/*
	public void sendTo(DatagramSocket from, InetAddress to, int port) throws IOException
	{
		byte[] content = this.toBytes();
		DatagramPacket packet = new DatagramPacket(content, content.length, to, port);
		from.send(packet);
	}
	*/
}
