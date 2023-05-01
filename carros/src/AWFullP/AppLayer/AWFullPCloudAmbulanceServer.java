package AWFullP.AppLayer;

import AWFullP.MessageConstants;
import Common.Position;
import Common.TowerInfo;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.sql.Timestamp;
import java.util.Arrays;


public class AWFullPCloudAmbulanceServer extends AWFullPAppLayer
{
	private Timestamp whenToSend;
	private Position pos;



	public AWFullPCloudAmbulanceServer(Position pos, Timestamp whenToSend)
	{
		super(MessageConstants.CLOUD_AMBULANCE_PATH);

		this.whenToSend = whenToSend ;
		this.pos = pos;
	}

	public AWFullPCloudAmbulanceServer(byte[] arr)
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
		/*
		this.towerID = new String(towerID_bytes).trim();

		this.posx = buf.getFloat();
		this.posy = buf.getFloat();
		this.max_speed = buf.getFloat();
		 */
	}

	public AWFullPCloudAmbulanceServer(DatagramPacket packet)
	{
		this(packet.getData());
	}
	
	

	@Override
	public byte[] toBytes()
	{
		/*
		byte[] towerID_bytes = Arrays.copyOf(this.towerID.getBytes(), MessageConstants.ID_SIZE);
		
		byte[] buf = ByteBuffer.allocate(MessageConstants.TOWER_ANNOUNCE_SIZE)
				.put(super.toBytes())
				.put(towerID_bytes)
				.putFloat(this.posx)
				.putFloat(this.posy)
				.putFloat(this.max_speed)
				.array();
		
		return buf;

		 */
		return new byte[10] ;
	}

}
