package AWFullP;

import java.net.DatagramPacket;

import AWFullP.AppLayer.AWFullPAppLayer;
//import AWFullP.FWRMessages.FWRInfo;
import AWFullP.AppLayer.AWFullPCarHello;


public class AWFullPacket
{
	//public FWRInfo forwardInfo; //TODO
	public AWFullPAppLayer appLayer;
	
	
	private AWFullPacket(int type, byte[] content)
	{
		switch(type) {
		
			case MessageConstants.CAR_HELLO:
				this.appLayer = new AWFullPCarHello(content);
				break;
		
			case MessageConstants.CAR_BREAK:
				this.appLayer = new AWFullPCarHello(content);
				break;
		
			case MessageConstants.CAR_ACCIDENT:
				this.appLayer = new AWFullPCarHello(content);
				break;
		
			case MessageConstants.TOWER_HELLO:
				this.appLayer = new AWFullPCarHello(content);
				break;
		
			case MessageConstants.SERVER_HELLO:
				this.appLayer = new AWFullPCarHello(content);
				break;
		
			case MessageConstants.CarInRangeMessage:
				this.appLayer = new AWFullPCarHello(content);
				break;
		
			case MessageConstants.ServerInfoMessage:
				this.appLayer = new AWFullPCarHello(content);
				break;
		}
	}
	
	public AWFullPacket(byte[] content)
	{
		this(AWFullPAppLayer.getType(content), content);
	}
	
	public AWFullPacket(DatagramPacket packet)
	{
		this(AWFullPAppLayer.getType(packet), packet.getData());
	}
	
	public AWFullPacket(AWFullPAppLayer appLayer)
	{
		this(appLayer.getType(), appLayer.toBytes()); //instead of direct attribution, to validate contents
	}
	
	
	public int getType() {return this.appLayer.getType();}
	public byte[] getContent() {return this.appLayer.toBytes();}
	
	public byte[] toBytes()
	{
		return appLayer.toBytes();
	}
	
	@Override
	public String toString()
	{
		return new String(
				"{"
			+	" ; type: " + this.getType()
			+	" ; size: " + this.getContent().length
			+	"}"
			);
	}
}
