package AWFullP.AppLayer;

import java.net.DatagramPacket;

import AWFullP.MessageConstants;

public class AWFullPCarBreak extends AWFullPAppLayer
{
	
	
	public AWFullPCarBreak()
	{
		super(MessageConstants.CAR_BREAK);
	}
	
	public AWFullPCarBreak(byte[] arr)
	{
		super(arr);
	}
	
	public AWFullPCarBreak(DatagramPacket packet)
	{
		super(packet);
	}
	
	
}
