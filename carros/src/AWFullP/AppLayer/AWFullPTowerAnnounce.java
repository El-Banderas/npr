package AWFullP.AppLayer;

import java.net.DatagramPacket;

import AWFullP.MessageConstants;

public class AWFullPTowerAnnounce extends AWFullPAppLayer
{
	
	
	public AWFullPTowerAnnounce()
	{
		super(MessageConstants.TOWER_ANNOUNCE);
	}
	
	public AWFullPTowerAnnounce(byte[] arr)
	{
		super(arr);
	}
	
	public AWFullPTowerAnnounce(DatagramPacket packet)
	{
		super(packet);
	}
	
	
}
