package AWFullP.AppLayer;

import java.net.DatagramPacket;

import AWFullP.MessageConstants;

public class AWFullPServerHello extends AWFullPAppLayer
{
	
	
	public AWFullPServerHello()
	{
		super(MessageConstants.SERVER_HELLO);
	}
	
	public AWFullPServerHello(byte[] arr)
	{
		super(arr);
	}
	
	public AWFullPServerHello(DatagramPacket packet)
	{
		super(packet);
	}
	
	
}
