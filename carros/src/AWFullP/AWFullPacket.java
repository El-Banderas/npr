package AWFullP;

import java.net.DatagramPacket;
import java.net.InetAddress;

import AWFullP.AppLayer.AWFullPAppLayer;
//import AWFullP.FWRMessages.FWRInfo;
import AWFullP.AppLayer.AWFullPCarHello;


public class AWFullPacket
{
	private InetAddress ipSender; //TODO:temporary
	//public FWRInfo forwardInfo; //TODO
	public AWFullPAppLayer appLayer;
	
	
	public AWFullPacket(int type, byte[] content, InetAddress ipSender)
	{
		this.ipSender = ipSender;
		
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
	
	public AWFullPacket(DatagramPacket packet)
	{
		this(AWFullPAppLayer.getType(packet), packet.getData(), packet.getAddress());
	}
	
	public AWFullPacket(AWFullPAppLayer appLayer)
	{
		this.appLayer = appLayer;
	}
	
	
	public int getType() {return this.appLayer.getType();}
	public InetAddress getIPSender() {return this.ipSender;}
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
