package AWFullP.AppLayer;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.util.Arrays;

import AWFullP.MessageConstants;

public class AWFullPServerInfo extends AWFullPAppLayer
{
	private String towerID;
	private int payload_length;
	private byte[] payload;
	
	
	public AWFullPServerInfo(String towerID, byte[] payload)
	{
		super(MessageConstants.SERVER_INFO);

		this.towerID = towerID;
		this.payload = payload;
		this.payload_length = payload.length;
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
		
		this.payload_length = buf.getInt();
		
		this.payload = new byte[payload_length];
		buf.get(this.payload);
	}
	
	public AWFullPServerInfo(DatagramPacket packet)
	{
		this(packet.getData());
	}
	

	public String getTowerID() {return this.towerID;}
	public int getPayloadLength() {return this.payload_length;}
	public byte[] getPayload() {return this.payload;}
	
	@Override
	public byte[] toBytes()
	{
		byte[] towerID_bytes = Arrays.copyOf(this.towerID.getBytes(), MessageConstants.ID_SIZE);
		
		byte[] buf = ByteBuffer.allocate(MessageConstants.SERVER_INFO_SIZE)
				.put(super.toBytes())
				.put(towerID_bytes)
				.putInt(this.payload_length)
				.put(this.payload)
				.array();
		
		return buf;
	}
	
	public String toString()
	{
		return new String(
				"{"
			+	" ; super: " + super.toString()
			+	" ; tower: " + this.towerID
			+	" ; length: " + this.payload_length
			+	"}"
			);
	}
}
