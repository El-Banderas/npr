package AWFullP.AppLayer;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.util.Arrays;

import AWFullP.MessageConstants;
import Common.Position;
import Common.TowerInfo;


public class AWFullPTowerAnnounce extends AWFullPAppLayer
{
	private String towerID;
	private float posx;
	private float posy;
	private float max_speed;
	
	
	public AWFullPTowerAnnounce(TowerInfo info)
	{
		super(MessageConstants.TOWER_ANNOUNCE);
		
		this.towerID = info.getName();
		this.posx = info.getPosition().x;
		this.posy = info.getPosition().y;
		this.max_speed = info.getMaxSpeed();
	}
	
	public AWFullPTowerAnnounce(byte[] arr)
	{
		super(arr);
		
		ByteBuffer buf = ByteBuffer.wrap(arr);
		buf.position(
				MessageConstants.AWFULLP_HEADER_SIZE +
				MessageConstants.GEO_HEADER_SIZE +
				MessageConstants.APP_HEADER_SIZE
				);

		byte[] towerID_bytes = new byte[MessageConstants.ID_SIZE];
		buf.get(towerID_bytes, 0, MessageConstants.ID_SIZE);
		this.towerID = new String(towerID_bytes).trim();

		this.posx = buf.getFloat();
		this.posy = buf.getFloat();
		this.max_speed = buf.getFloat();
	}
	
	public AWFullPTowerAnnounce(DatagramPacket packet)
	{
		this(packet.getData());
	}
	
	
	public String getTowerID() {return this.towerID;}
	public Position getPos() {return new Position(this.posx, this.posy);}
	public float getMaxSpeed() {return this.max_speed;}
	
	@Override
	public byte[] toBytes()
	{
		byte[] towerID_bytes = Arrays.copyOf(this.towerID.getBytes(), MessageConstants.ID_SIZE);
		
		byte[] buf = ByteBuffer.allocate(MessageConstants.TOWER_ANNOUNCE_SIZE)
				.put(super.toBytes())
				.put(towerID_bytes)
				.putFloat(this.posx)
				.putFloat(this.posy)
				.putFloat(this.max_speed)
				.array();
		
		return buf;
	}
	
	public String toString()
	{
		return new String(
				"{"
			+	" ; super: " + super.toString()
			+	" ; id: " + this.towerID
			+	" ; posx: " + this.posx
			+	" ; posy: " + this.posy
			+	" ; max_speed: " + this.max_speed
			+	"}"
			);
	}
}
