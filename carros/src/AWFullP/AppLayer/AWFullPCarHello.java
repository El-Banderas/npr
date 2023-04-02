package AWFullP.AppLayer;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.util.Arrays;

import AWFullP.MessageConstants;

public class AWFullPCarHello extends AWFullPAppLayer
{
	private String carID;
	
	
	public AWFullPCarHello(String carID)
	{
		super(MessageConstants.CAR_HELLO);
		
		this.carID = carID;
	}
	
	public AWFullPCarHello(byte[] arr)
	{
		super(arr);
		
		ByteBuffer buf = ByteBuffer.wrap(arr);
		buf.position(MessageConstants.APP_HEADER_SIZE); //skip super

		byte[] carID_bytes = new byte[MessageConstants.ID_SIZE];
		buf.get(carID_bytes, 0, MessageConstants.ID_SIZE);
		this.carID = new String(carID_bytes).trim();
	}
	
	public AWFullPCarHello(DatagramPacket packet)
	{
		this(packet.getData());
	}
	
	
	public String getCarID() {return this.carID;}
	
	@Override
	public byte[] toBytes()
	{
		byte[] carID_bytes = Arrays.copyOf(this.carID.getBytes(), MessageConstants.ID_SIZE);
		
		byte[] buf = ByteBuffer.allocate(MessageConstants.CAR_HELLO_SIZE)
				.put(super.toBytes())
				.put(carID_bytes)
				.array();
		
		return buf;
	}
}
