package AWFullP.AppLayer;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.util.Arrays;

import AWFullP.MessageConstants;
import Common.Position;

public class AWFullPCarAccident extends AWFullPAppLayer
{
	private String towerID;
	private String carID;
	private Position location;
	
	
	public AWFullPCarAccident(String towerID, String carID, Position location)
	{
		super(MessageConstants.CAR_ACCIDENT);
		
		this.towerID = towerID;
		this.carID = carID;
		this.location = location;
	}
	
	public AWFullPCarAccident(byte[] arr)
	{
		super(arr);
		
		ByteBuffer buf = ByteBuffer.wrap(arr);
		buf.position(MessageConstants.APP_HEADER_SIZE); //skip super
		
		byte[] towerID_bytes = new byte[MessageConstants.ID_SIZE];
		buf.get(towerID_bytes, 0, MessageConstants.ID_SIZE);
		this.towerID = new String(towerID_bytes).trim();
		
		byte[] carID_bytes = new byte[MessageConstants.ID_SIZE];
		buf.get(carID_bytes, 0, MessageConstants.ID_SIZE);
		this.carID = new String(carID_bytes).trim();
		
		int x = buf.getInt();
		int y = buf.getInt();
		this.location = new Position(x,y);
	}
	
	public AWFullPCarAccident(DatagramPacket packet)
	{
		this(packet.getData());
	}
	
	
	public Position getLocation() {return this.location;}
	public String getTowerID() {return this.towerID;}
	public String getCarID() {return this.carID;}
	
	@Override
	public byte[] toBytes()
	{
		byte[] towerID_bytes = Arrays.copyOf(this.towerID.getBytes(), MessageConstants.ID_SIZE);
		byte[] carID_bytes = Arrays.copyOf(this.carID.getBytes(), MessageConstants.ID_SIZE);
		
		byte[] buf = ByteBuffer.allocate(MessageConstants.CAR_ACCIDENT_SIZE)
				.put(super.toBytes())
				.put(towerID_bytes)
				.put(carID_bytes)
				.putInt(this.location.x)
				.putInt(this.location.y)
				.array();
		
		return buf;
	}
}
