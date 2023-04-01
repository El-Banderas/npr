package AWFullP;

public class MessagesConstants
{
	/*
	 * byte 	1 byte 		Stores whole numbers from -128 to 127
	 * short 	2 bytes 	Stores whole numbers from -32,768 to 32,767
	 * int 		4 bytes 	Stores whole numbers from -2,147,483,648 to 2,147,483,647
	 * long 	8 bytes 	Stores whole numbers from -9,223,372,036,854,775,808 to 9,223,372,036,854,775,807
	 * float 	4 bytes 	Stores fractional numbers. Sufficient for storing 6 to 7 decimal digits
	 * double 	8 bytes 	Stores fractional numbers. Sufficient for storing 15 decimal digits
	 * boolean 	1 bit 		Stores true or false values
	 * char 	2 bytes 	Stores a single character
	 */
	
	
	
	
	/*
	 * | TTL | PosX | PosY | Distancia ao destino | Num_seq | Source ID |	 * 
	 * 
	 * Broadcast -> Distancia maior ? discarta : Espera delay baseado em distância -> Recebeu pacote repetido (alguem já enviou) ? discarta : Broadcast
	 * 
	 * Destino envia ACK
	 */
	
	
	
	
	/*
	 * |		byte		|		byte		|		byte		|		byte		|
	 * +--------------------+-------------------+-------------------+-------------------+
	 * |  PROTOCOL_VERSION	|				   TYPE					|		 TTL		|
	 * +--------------------+-------------------+---------------------------------------+
	 * |
	 * +--------------------------------------------------------------------------------+
	 * 
	 */
	
	public static final int PROTOCOL_FIELD 	= (int) ((2^8 -1) << 8*3);
	public static final int TYPE_FIELD 		= (int) ((2^16-1) << 8*1);
	public static final int TTL_FIELD 		= (int) ((2^8 -1) << 8*0);
	
	public static final byte PROTOCOL_MAJOR_VERSION = (byte) (1 << 5);
	public static final byte PROTOCOL_MINOR_VERSION = (byte) 1;
	public static final byte PROTOCOL_VERSION = PROTOCOL_MAJOR_VERSION | PROTOCOL_MINOR_VERSION;
	
	public static final byte MAJOR_TYPE = (byte) 0b11100000; // N = 8
	public static final byte MINOR_TYPE = (byte) 0b00011111; // N = 32
	
	public static final byte V2V 		= (byte) 0b00000000; // (0 << 5) //AdHoc local broadcast within TTL
		//public static final byte PING 		= (byte) 0;
		//public static final byte PONG 		= (byte) 1;
	public static final byte V2I 		= (byte) 0b00100000; // (1 << 5) //AdHoc forwarding to RSU (at least until TTL)
	public static final byte V2P 		= (byte) 0b01000000; // (2 << 5) //Not implemented!
	public static final byte V2D 		= (byte) 0b01100000; // (3 << 5) //Not implemented!
	public static final byte U4 		= (byte) 0b10000000; // (4 << 5) //Undefined
	public static final byte U5 		= (byte) 0b10100000; // (5 << 5) //Undefined
	public static final byte U6 		= (byte) 0b11000000; // (6 << 5) //Undefined
	public static final byte U7 		= (byte) 0b11100000; // (7 << 5) //Undefined
	
	
	
	// Still-Alive messages (CBOR encoded payload for QoS statistics and announcements)
	public static final byte PING 		= (byte) 0;
	public static final byte PONG 		= (byte) 1;
	// Usual REST methods with URL + payload
	public static final byte GET 		= (byte) 2;
	public static final byte POST 		= (byte) 4;
	public static final byte PUT 		= (byte) 5;
	// Reply messages with (REPLY) or without (ACK) CBOR encoded payload
	public static final byte REPLY 		= (byte) 3;
	public static final byte ACK 		= (byte) 3;
	
	
	
	public static final int CAR_HELLO = 1;
	public static final int BreakMessage 		= (byte) 2;
	public static final int Timeout 			= (byte) 3; // No message. To update terminal
	public static final int AccidentMessage 	= (byte) 4;
	public static final int TowerHelloMessage 	= (byte) 5;
	public static final int ServerHelloMessage = (byte) 6;
	public static final int CarInRangeMessage 	= (byte) 7;
	public static final int ServerInfoMessage 	= (byte) 8;

	public static final int TTLCarHelloMessage = 1;
	public static final int TTLBreakMessage = 2;
	public static final int TTLAccidentMessage = 3;
	
	
	
	public static String convertTypeString(int type)
	{
		switch(type) {
			case CAR_HELLO:
				return "Hello Message from Car";
			case TowerHelloMessage:
				return "Hello Message from Tower";
			case BreakMessage:
				return "Break Message";
			case Timeout:
				return "Timeout triggered";
			case AccidentMessage:
				return "Accident Happened";
			case CarInRangeMessage:
				return "Cars in Range";
			default:
				return "Type unknown";
		}
	}
	
	public static int sizeBufferMessages = 950;
	public static final int maxSizeTowerName = 8;
}
