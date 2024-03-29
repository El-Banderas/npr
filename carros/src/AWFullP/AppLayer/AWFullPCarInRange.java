package AWFullP.AppLayer;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.util.Arrays;

import AWFullP.MessageConstants;

public class AWFullPCarInRange extends AWFullPAppLayer
{
	private String towerID;
	private String carID;
	
	
	public AWFullPCarInRange(String towerID, String carID)
	{
		super(MessageConstants.CAR_IN_RANGE);
		
		this.towerID = towerID;
		this.carID = carID;
	}
	
	public AWFullPCarInRange(byte[] arr)
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
		
		byte[] carID_bytes = new byte[MessageConstants.ID_SIZE];
		buf.get(carID_bytes, 0, MessageConstants.ID_SIZE);
		this.carID = new String(carID_bytes).trim();
	}
	
	public AWFullPCarInRange(DatagramPacket packet)
	{
		this(packet.getData());
	}
	
	
	public String getTowerID() {return this.towerID;}
	public String getCarID() {return this.carID;}
	
	@Override
	public byte[] toBytes()
	{
		byte[] towerID_bytes = Arrays.copyOf(this.towerID.getBytes(), MessageConstants.ID_SIZE);
		byte[] carID_bytes = Arrays.copyOf(this.carID.getBytes(), MessageConstants.ID_SIZE);
		
		byte[] buf = ByteBuffer.allocate(MessageConstants.CAR_IN_RANGE_SIZE)
				.put(super.toBytes())
				.put(towerID_bytes)
				.put(carID_bytes)
				.array();
		
		return buf;
	}
	
	public String toString()
	{
		return new String(
				"{"
			+	" ; super: " + super.toString()
			+	" ; tower: " + this.towerID
			+	" ; car: " + this.carID
			+	"}"
			);
	}
}
