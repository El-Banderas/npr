package AWFullP;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;

import AWFullP.AppLayer.AWFullPAppLayer;
import AWFullP.AppLayer.AWFullPCarAccident;
import AWFullP.AppLayer.AWFullPCarBreak;
import AWFullP.AppLayer.AWFullPCarHello;
import AWFullP.AppLayer.AWFullPCarInRange;
import AWFullP.AppLayer.AWFullPServerHello;
import AWFullP.AppLayer.AWFullPServerInfo;
import AWFullP.AppLayer.AWFullPTowerAnnounce;
import AWFullP.FwdLayer.AWFullPFwdLayer;


public class AWFullPacket
{
	private boolean isForwarded;
	public AWFullPFwdLayer forwardInfo;
	public AWFullPAppLayer appLayer;
	
	
	private AWFullPacket(int appType, byte[] content)
	{
		switch(appType) {
		
			case MessageConstants.CAR_HELLO:
				this.appLayer = new AWFullPCarHello(content);
				break;
		
			case MessageConstants.CAR_BREAK:
				this.appLayer = new AWFullPCarBreak(content);
				break;
		
			case MessageConstants.CAR_ACCIDENT:
				this.appLayer = new AWFullPCarAccident(content);
				break;
		
			case MessageConstants.TOWER_ANNOUNCE:
				this.appLayer = new AWFullPTowerAnnounce(content);
				break;
		
			case MessageConstants.SERVER_HELLO:
				this.appLayer = new AWFullPServerHello(content);
				break;
		
			case MessageConstants.CAR_IN_RANGE:
				this.appLayer = new AWFullPCarInRange(content);
				break;
		
			case MessageConstants.SERVER_INFO:
				this.appLayer = new AWFullPServerInfo(content);
				break;
		
			default:
				System.out.println("Type unknown: " + appType);
				this.appLayer = new AWFullPAppLayer(content); //do this anyway lmao
		}
	}
	
	public AWFullPacket(byte[] content)
	{
		this(AWFullPAppLayer.getType(content), content);
		
		ByteBuffer buf = ByteBuffer.wrap(content);
		this.isForwarded = buf.get() == 1;

		this.forwardInfo = new AWFullPFwdLayer(content);
	}
	
	public AWFullPacket(DatagramPacket packet)
	{
		this(packet.getData());
	}
	
	public AWFullPacket(AWFullPAppLayer appLayer)
	{
		//this(appLayer.getType(), appLayer.toBytes()); //instead of direct attribution, to validate contents
		this.appLayer = appLayer;
		this.forwardInfo = new AWFullPFwdLayer();
		this.isForwarded = false;
	}
	
	public AWFullPacket(AWFullPAppLayer appLayer, AWFullPFwdLayer forwardInfo)
	{
		//this(appLayer.getType(), appLayer.toBytes()); //instead of direct attribution, to validate contents
		this.appLayer = appLayer;
		this.forwardInfo = forwardInfo;
		this.isForwarded = true;
	}
	
	
	public boolean isForwarded() {return this.isForwarded;}
	
	public byte[] toBytes()
	{
		byte isForwarded_byte = (byte) (this.isForwarded ? 1 : 0);
		byte[] forwardInfo_bytes = this.forwardInfo.toBytes();
		byte[] appLayer_bytes = this.appLayer.toBytes();
		
		byte[] buf = ByteBuffer.allocate(MessageConstants.AWFULLP_HEADER_SIZE + MessageConstants.GEO_HEADER_SIZE + appLayer_bytes.length)
				.put(isForwarded_byte)
				.put(forwardInfo_bytes)
				.put(appLayer_bytes)
				.array();
		
		return buf;
	}

	public int getType(){
		return appLayer.getType();
	}
	public boolean hasDestinationPosition(){
		if (forwardInfo.getDist() <= 0) return false;
		return true;
	}
	
	@Override
	public String toString()
	{
		return new String(
				"{"
			+	" ; fwd: " + this.forwardInfo.toString()
			+	" ; app: " + this.appLayer.toString()
			+	"}"
			);
	}
}
