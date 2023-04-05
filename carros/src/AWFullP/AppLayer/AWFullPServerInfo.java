package AWFullP.AppLayer;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import AWFullP.MessageConstants;

public class AWFullPServerInfo extends AWFullPAppLayer
{
	private String towerID;
	private List<String> cars;
	
	
	public AWFullPServerInfo(String towerID, List<String> cars)
	{
		super(MessageConstants.SERVER_INFO);

		this.towerID = towerID;
		this.cars = cars;
	}
	
	public AWFullPServerInfo(byte[] arr)
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
		
		this.cars = new ArrayList<>(MessageConstants.BATCH_SIZE);
		for(int i = 0; i < MessageConstants.BATCH_SIZE; i++) {
			byte[] carID_bytes = new byte[MessageConstants.ID_SIZE];
			buf.get(carID_bytes, 0, MessageConstants.ID_SIZE);
			this.cars.add(new String(towerID_bytes).trim());
		}
	}
	
	public AWFullPServerInfo(DatagramPacket packet)
	{
		this(packet.getData());
	}
	

	public String getTowerID() {return this.towerID;}
	public List<String> getCarsInRange() {return this.cars;}
	
	@Override
	public byte[] toBytes()
	{
		byte[] towerID_bytes = Arrays.copyOf(this.towerID.getBytes(), MessageConstants.ID_SIZE);
		
		byte[] buf = new byte[MessageConstants.SERVER_INFO_SIZE]; 
		ByteBuffer bbuf = ByteBuffer.wrap(buf);
		bbuf.put(super.toBytes());
		bbuf.put(towerID_bytes);
		for(String carID : this.cars) {
			byte[] carID_bytes = Arrays.copyOf(carID.getBytes(), MessageConstants.ID_SIZE);
			bbuf.put(carID_bytes);
		}
		
		return buf;
	}
	
	public String toString()
	{
		return new String(
				"{"
			+	" ; super: " + super.toString()
			+	" ; tower: " + this.towerID
			+	" ; cars: " + this.cars.toString()
			+	"}"
			);
	}
}
