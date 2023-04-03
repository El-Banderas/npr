package AWFullP.AppLayer;

import java.net.DatagramPacket;

import AWFullP.MessageConstants;

public class AWFullPTowerHello extends AWFullPAppLayer
{
	
	
	public AWFullPTowerHello()
	{
		super(MessageConstants.TOWER_HELLO);
	}
	
	public AWFullPTowerHello(byte[] arr)
	{
		super(arr);
	}
	
	public AWFullPTowerHello(DatagramPacket packet)
	{
		super(packet);
	}
	
	
}
