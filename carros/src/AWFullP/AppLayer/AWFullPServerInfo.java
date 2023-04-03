package AWFullP.AppLayer;

import java.net.DatagramPacket;

import AWFullP.MessageConstants;

public class AWFullPServerInfo extends AWFullPAppLayer
{
	
	
	public AWFullPServerInfo()
	{
		super(MessageConstants.SERVER_INFO);
	}
	
	public AWFullPServerInfo(byte[] arr)
	{
		super(arr);
	}
	
	public AWFullPServerInfo(DatagramPacket packet)
	{
		super(packet);
	}
	
	
}
