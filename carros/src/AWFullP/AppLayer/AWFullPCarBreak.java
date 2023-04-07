package AWFullP.AppLayer;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.util.Arrays;

import AWFullP.MessageConstants;
import Common.CarInfo;
import Common.Position;
import Common.Vector;

public class AWFullPCarBreak extends AWFullPAppLayer
{
	private String carID;
	private float posx;
	private float posy;
	private float dirx;
	private float diry;
	
	
	public AWFullPCarBreak(CarInfo info)
	{
		super(MessageConstants.CAR_BREAK);
		
		this.carID = info.getID();
		this.posx = info.getPosition().x;
		this.posy = info.getPosition().y;
		this.dirx = info.getDirection().x;
		this.diry = info.getDirection().y;
	}
	
	public AWFullPCarBreak(byte[] arr)
	{
		super(arr);
		
		ByteBuffer buf = ByteBuffer.wrap(arr);
		buf.position(
				MessageConstants.AWFULLP_HEADER_SIZE +
				MessageConstants.GEO_HEADER_SIZE +
				MessageConstants.APP_HEADER_SIZE
				);

		byte[] carID_bytes = new byte[MessageConstants.ID_SIZE];
		buf.get(carID_bytes, 0, MessageConstants.ID_SIZE);
		this.carID = new String(carID_bytes).trim();

		this.posx = buf.getFloat();
		this.posy = buf.getFloat();
		this.dirx = buf.getFloat();
		this.diry = buf.getFloat();
	}
	
	public AWFullPCarBreak(DatagramPacket packet)
	{
		this(packet.getData());
	}
	
	
	public String getCarID() {return this.carID;}
	public Position getPos() {return new Position(this.posx, this.posy);}
	public Vector getDirection() {return new Vector(this.dirx, this.diry);}
	
	@Override
	public byte[] toBytes()
	{
		byte[] carID_bytes = Arrays.copyOf(this.carID.getBytes(), MessageConstants.ID_SIZE);
		
		byte[] buf = ByteBuffer.allocate(MessageConstants.CAR_BREAK_SIZE)
				.put(super.toBytes())
				.put(carID_bytes)
				.putFloat(this.posx)
				.putFloat(this.posy)
				.putFloat(this.dirx)
				.putFloat(this.diry)
				.array();
		
		return buf;
	}
	
	public String toString()
	{
		return new String(
				"{"
			+	" ; super: " + super.toString()
			+	" ; id: " + this.carID
			+	" ; posx: " + this.posx
			+	" ; posy: " + this.posy
			+	" ; dirx: " + this.dirx
			+	" ; diry: " + this.diry
			+	"}"
			);
	}
}
