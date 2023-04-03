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
	private 	byte 	ttl 		= (byte) 1;
	private 	int 	posX 		= (int) -1;
	private 	int 	posY 		= (int) -1;
	private 	int 	dist 		= (int) -1;
	private 	int 	seq 		= (int) -1;
	private 	String 	senderID 	= "";
	
	
	public AWFullPFwdLayer()
	{
	}
	
	public AWFullPFwdLayer(byte ttl, String senderID, int seq)
	{
		this.ttl = ttl;
		this.senderID = senderID;
		this.seq = seq;
	}
	
	public AWFullPFwdLayer(byte ttl, Position pos, int distance, String id, int seq)
	{
		this.ttl = ttl;
		this.posX = pos.x;
		this.posY = pos.y;
		this.dist = distance;
		this.senderID = id;
		this.seq = seq;
	}
	
	public AWFullPFwdLayer(byte ttl, int posX, int posY, int dist, int seq, String senderID)
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
		
		this.ttl 	= buf.get();
		this.posX 	= buf.getInt();
		this.posY 	= buf.getInt();
		this.dist 	= buf.getInt();
		this.seq 	= buf.getInt();
		
		byte[] senderID_bytes = new byte[MessageConstants.ID_SIZE];
		buf.get(senderID_bytes, 0, MessageConstants.ID_SIZE);
		this.senderID = new String(senderID_bytes).trim();
	}
	
	public AWFullPFwdLayer(DatagramPacket packet)
	{
		this(packet.getData());
	}
	
	
	public byte getTTL() {return this.ttl;}
	public int getPosX() {return this.posX;}
	public int getPosY() {return this.posY;}
	public int getDist() {return this.dist;}
	public int getSeq()  {return this.seq;}
	public String getSenderID() {return this.senderID;}
	
	public byte[] toBytes()
	{
		byte[] senderID_bytes = Arrays.copyOf(this.senderID.getBytes(), MessageConstants.ID_SIZE);
		
		byte[] buf = ByteBuffer.allocate(MessageConstants.GEO_HEADER_SIZE)
				.put 	(this.ttl)
				.putInt (this.posX)
				.putInt (this.posY)
				.putInt (this.dist)
				.putInt (this.seq)
				.put 	(senderID_bytes)
				.array();
		
		return buf;
	}
	
	// We don't change seq number because the message is relative to the original car, not the cars that resend.
	public void updateInfo(CarInfo carInfo)
	{
		this.ttl -= 1;
		this.dist = (int) Position.distance(new Position(this.posX, this.posY), carInfo.pos);
		this.senderID = carInfo.id;
	}
}
