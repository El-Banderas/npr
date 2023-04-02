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

public class AWFullPFwdLayer implements IAWFullPFwdLayer
{
	private 	byte 	protocolVersion 	= (byte) 0;
	private 	char 	type 				= (char) 0;
	private 	byte 	ttl 				= (byte) 1;
	private 	int 	posX 				= (int) -1;
	private 	int 	posY 				= (int) -1;
	private 	int 	dist 				= (int) -1;
	private 	int 	seq 				= (int) -1;
	private 	String 	senderID 			= "";
	
	
	public AWFullPFwdLayer(byte protocolVersion, char type, byte ttl, int posX, int posY, int dist, int seq, String senderID)
	{
		this.protocolVersion = protocolVersion;
		this.type = type;
		this.ttl = ttl;
		this.posX = posX;
		this.posY = posY;
		this.dist = dist;
		this.seq = seq;
		this.senderID = senderID;
	}
	
	public AWFullPFwdLayer(byte[] arr)
	{
		ByteBuffer buf = ByteBuffer.wrap(arr);
		
		this.protocolVersion 	= buf.get();
		this.type 				= buf.getChar();
		this.ttl 				= buf.get();
		this.posX 				= buf.getInt();
		this.posY 				= buf.getInt();
		this.dist 				= buf.getInt();
		this.seq 				= buf.getInt();
		
		byte[] senderID_bytes = new byte[MessageConstants.ID_SIZE];
		buf.get(senderID_bytes, 0, MessageConstants.ID_SIZE);
		this.senderID = new String(senderID_bytes).trim();
	}
	
	public AWFullPFwdLayer(DatagramPacket packet)
	{
		this(packet.getData());
	}
	
	
	public byte getProtocolVersion() {return this.protocolVersion;}
	public int getType() {return this.type;}
	public byte getTTL() {return this.ttl;}
	public int getPosX() {return this.posX;}
	public int getPosY() {return this.posY;}
	public int getDist() {return this.dist;}
	public int getSeq() {return this.seq;}
	public String getSenderID() {return this.senderID;}
	
	public byte[] toBytes()
	{
		byte[] senderID_bytes = Arrays.copyOf(this.senderID.getBytes(), MessageConstants.ID_SIZE);
		
		byte[] buf = ByteBuffer.allocate(MessageConstants.FWD_HEADER_SIZE)
				.put 	(this.protocolVersion)
				.putChar(this.type)
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
