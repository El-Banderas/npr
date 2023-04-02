package AWFullP;

public class MessageConstants
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
	 * Broadcast -> Distancia maior ? discard : Espera delay baseado em distância -> Recebeu pacote repetido (alguem já enviou) ? discard : Broadcast
	 * Destino envia ACK
	 */
	
	
	/*
	 * General Header & Forward Info
	 * 
	 * |		byte		|		byte		|		byte		|		byte		|
	 * +--------------------+-------------------+-------------------+-------------------+
	 * |  PROTOCOL_VERSION	|				   TYPE					|		 TTL		|
	 * +--------------------+---------------------------------------+-------------------+
	 * |									  Pos X										|
	 * +--------------------------------------------------------------------------------+
	 * |									  Pos Y										|
	 * +--------------------------------------------------------------------------------+
	 * |							 Distance to Destination							|
	 * +--------------------------------------------------------------------------------+
	 * |								 Sequence Number								|
	 * +--------------------------------------------------------------------------------+
	 * |									Sender ID									|
	 * |									(8 bytes)									|
	 * +--------------------------------------------------------------------------------+
	 */
	public static final int FWD_HEADER_SIZE = 28;
	public static final int ID_SIZE = 8;
	
	
	
	/*
	 * Application Layer
	 * 
	 * |		byte		|		byte		|		byte		|		byte		|
	 * +--------------------+-------------------+-------------------+-------------------+
	 * |									   TYPE										|
	 * +--------------------------------------------------------------------------------+
	 * |									   ...										|
	 * +--------------------------------------------------------------------------------+
	 *
	 */
	public static final int APP_HEADER_SIZE = 4;
	
	
	public static final int CAR_HELLO 			= (int) 1;
	public static final int CAR_HELLO_SIZE 		= (int) APP_HEADER_SIZE + 8;
	public static final byte TTLCarHelloMessage = (byte) 1;
	/*
	 * |		byte		|		byte		|		byte		|		byte		|
	 * +--------------------+-------------------+-------------------+-------------------+
	 * |									 Car ID										|
	 * |									(8 bytes)									|
	 * +--------------------------------------------------------------------------------+
	 */
	
	public static final int CAR_BREAK 			= (int) 2;
	public static final int CAR_BREAK_SIZE 		= (int) APP_HEADER_SIZE + 0;
	public static final byte TTLBreakMessage 	= (byte) 2;
	/*
	 * |		byte		|		byte		|		byte		|		byte		|
	 * +--------------------+-------------------+-------------------+-------------------+
	 */
	
	public static final int TIMEOUT 			= (int) 3;
	// No message. To update terminal
	
	public static final int CAR_ACCIDENT 		= (int) 4;
	public static final int CAR_ACCIDENT_SIZE 	= (int) APP_HEADER_SIZE + 24;
	public static final byte TTLAccidentMessage = (byte) 3;
	/*
	 * |		byte		|		byte		|		byte		|		byte		|
	 * +--------------------+-------------------+-------------------+-------------------+
	 * |									Tower ID									|
	 * |									(8 bytes)									|
	 * +--------------------------------------------------------------------------------+
	 * |									 Car ID										|
	 * |									(8 bytes)									|
	 * +--------------------------------------------------------------------------------+
	 * |									  Pos x										|
	 * +--------------------------------------------------------------------------------+
	 * |									  Pos y										|
	 * +--------------------------------------------------------------------------------+
	 */
	
	public static final int TOWER_HELLO 		= (int) 5;
	public static final int TOWER_HELLO_SIZE 	= (int) APP_HEADER_SIZE + 0;
	/*
	 * |		byte		|		byte		|		byte		|		byte		|
	 * +--------------------+-------------------+-------------------+-------------------+
	 */
	
	public static final int SERVER_HELLO 		= (int) 6;
	public static final int SERVER_HELLO_SIZE 	= (int) APP_HEADER_SIZE + 0;
	/*
	 * |		byte		|		byte		|		byte		|		byte		|
	 * +--------------------+-------------------+-------------------+-------------------+
	 */
	
	public static final int CAR_IN_RANGE 		= (int) 7;
	public static final int CAR_IN_RANGE_SIZE 	= (int) APP_HEADER_SIZE + 16;
	/*
	 * |		byte		|		byte		|		byte		|		byte		|
	 * +--------------------+-------------------+-------------------+-------------------+
	 * |									Tower ID									|
	 * |									(8 bytes)									|
	 * +--------------------------------------------------------------------------------+
	 * |									 Car ID										|
	 * |									(8 bytes)									|
	 * +--------------------------------------------------------------------------------+
	 */
	
	public static final int SERVER_INFO 		= (int) 8;
	public static final int SERVER_INFO_SIZE 	= (int) APP_HEADER_SIZE + 0;
	/*
	 * |		byte		|		byte		|		byte		|		byte		|
	 * +--------------------+-------------------+-------------------+-------------------+
	 */
	
	
	
	public static String convertTypeString(int type)
	{
		switch(type) {
			case CAR_HELLO:
				return "Hello Message from Car";
			case TOWER_HELLO:
				return "Hello Message from Tower";
			case CAR_BREAK:
				return "Break Message";
			case TIMEOUT:
				return "Timeout triggered";
			case CAR_ACCIDENT:
				return "Accident Happened";
			case CAR_IN_RANGE:
				return "Cars in Range";
			default:
				return "Type unknown";
		}
	}

	public static final int sizeBufferMessages = 950;
	public static final int BATCH_SIZE = 4;
}
