package AWFullP.FwdLayer;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.util.Arrays;

import AWFullP.MessageConstants;
import Common.CarInfo;
import Common.Position;

//import java.io.IOException;
//import java.net.DatagramSocket;
//import java.net.InetAddress;

public class AWFullPFwdLayer
{
	private 	int 	ttl 		= (int) 1;
	private 	float 	posX 		= (int) -1;
	private 	float 	posY 		= (int) -1;
	private 	float 	dist 		= (int) -1;
	private 	int 	seq 		= (int) -1;
	private 	String 	senderID 	= "";
	
	
	public AWFullPFwdLayer()
	{
	}
	
	public AWFullPFwdLayer(int ttl, String senderID, int seq)
	{
		this.ttl 		= ttl;
		this.senderID 	= senderID;
		this.seq 		= seq;
	}
	
	public AWFullPFwdLayer(int ttl, Position pos, float distance, String id, int seq)
	{
		this.ttl 		= ttl;
		this.posX 		= pos.x;
		this.posY 		= pos.y;
		this.dist 		= distance;
		this.senderID 	= id;
		this.seq 		= seq;
	}
	
	public AWFullPFwdLayer(int ttl, float posX, float posY, float dist, int seq, String senderID)
	{
		this.ttl 		= ttl;
		this.posX 		= posX;
		this.posY 		= posY;
		this.dist 		= dist;
		this.seq 		= seq;
		this.senderID 	= senderID;
	}
	
	public AWFullPFwdLayer(byte[] arr)
	{
		ByteBuffer buf = ByteBuffer.wrap(arr);
		buf.position(MessageConstants.AWFULLP_HEADER_SIZE);
		
		this.ttl 	= buf.getInt();
		this.posX 	= buf.getFloat();
		this.posY 	= buf.getFloat();
		this.dist 	= buf.getFloat();
		this.seq 	= buf.getInt();
		
		byte[] senderID_bytes = new byte[MessageConstants.ID_SIZE];
		buf.get(senderID_bytes, 0, MessageConstants.ID_SIZE);
		this.senderID = new String(senderID_bytes).trim();
	}
	
	public AWFullPFwdLayer(DatagramPacket packet)
	{
		this(packet.getData());
	}
	
	
	public int getTTL() {return this.ttl;}
	public float getPosX() {return this.posX;}
	public float getPosY() {return this.posY;}
	public float getDist() {return this.dist;}
	public int getSeq()  {return this.seq;}
	public String getSenderID() {return this.senderID;}
	
	public byte[] toBytes()
	{
		byte[] senderID_bytes = Arrays.copyOf(this.senderID.getBytes(), MessageConstants.ID_SIZE);
		
		byte[] buf = ByteBuffer.allocate(MessageConstants.GEO_HEADER_SIZE)
				.putInt(this.ttl)
				.putFloat(this.posX)
				.putFloat(this.posY)
				.putFloat(this.dist)
				.putInt(this.seq)
				.put(senderID_bytes)
				.array();
		
		return buf;
	}
	
	// We don't change seq number because the message is relative to the original car, not the cars that resend.
	public void updateInfo(CarInfo carInfo)
	{
		this.ttl -= 1;
		this.dist = (int) Position.distance(new Position(this.posX, this.posY), carInfo.getPosition());
		this.senderID = carInfo.getID();
	}
	
	public String toString()
	{
		return new String(
				"{"
			+	" ; ttl: " + this.ttl
			+	" ; posX: " + this.posX
			+	" ; posY: " + this.posY
			+	" ; dist: " + this.dist
			+	" ; seq: " + this.seq
			+	" ; senderID: " + this.senderID
			+	"}"
			);
	}
}
