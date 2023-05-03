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
	public Timestamp whenToSend;
	public Position pos;



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

		this.whenToSend = new Timestamp(buf.getLong());

		float posx = buf.getFloat();
		float posy = buf.getFloat();
		this.pos = new Position(posx, posy);
	}

	public AWFullPCloudAmbulanceServer(DatagramPacket packet)
	{
		this(packet.getData());
	}
	
	

	@Override
	public byte[] toBytes()
	{
//		byte[] towerID_bytes = Arrays.copyOf(this.towerID.getBytes(), MessageConstants.ID_SIZE);
		
		byte[] buf = ByteBuffer.allocate(MessageConstants.TOWER_ANNOUNCE_SIZE)
				.put(super.toBytes())
				.putLong(whenToSend.getTime())
				.putFloat(this.pos.x)
				.putFloat(this.pos.y)
				.array();
		
		return buf;

	}

}
