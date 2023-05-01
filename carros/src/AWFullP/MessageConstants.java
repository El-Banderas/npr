package AWFullP;

public class MessageConstants
{

	public static final int BUFFER_SIZE = 950;
	public static final int MAX_BATCH_SIZE = 4;
	public static final int ID_SIZE 			= (int) 8;
	
	
	
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
	
	
	
	//=== AWFullP Layer ===//
	/*
	 * 
	 * |		byte		|		byte		|		byte		|		byte		|
	 * +--------------------+-------------------+-------------------+-------------------+
	 * | 	Is Forwarded?	|
	 * +--------------------+
	 */
	public static final byte AWFULLP_HEADER_SIZE = (byte) Byte.BYTES;
	
	
	//=== Geo Forwarding Layer ===//
	/*
	 * 
	 * |		byte		|		byte		|		byte		|		byte		|
	 * +--------------------+-------------------+-------------------+-------------------+
	 * |									   TTL										|
	 * +--------------------------------------------------------------------------------+
	 * |									  Pos X										|
	 * +--------------------------------------------------------------------------------+
	 * |									  Pos Y										|
	 * +--------------------------------------------------------------------------------+
	 * |							 Distance to Destination							|
	 * +--------------------------------------------------------------------------------+
	 * |								 Sequence Number								|
	 * +--------------------------------------------------------------------------------+
	 * |									Sender ID									|
	 * |																				|
	 * +--------------------------------------------------------------------------------+
	 * 
	 * 1. Broadcast
	 * 2. Distancia maior ? discard : Espera delay baseado em distância
	 * 3. Recebeu pacote repetido (alguem já enviou) ? discard : Broadcast
	 * 4. Destino envia ACK
	 */
	public static final int GEO_HEADER_SIZE 	= (int) 2 * Integer.BYTES + 3 * Float.BYTES + 1 * ID_SIZE;

	/**
	 * Before send a message, should wait to see if a better send is in localisation
	 * That time is proportional to the distance to target.
	 */
	public static final float Delay_Before_Send_Message = 1;
	/**
	 * Number of tries that a car tries sending a message to a geographical destination.
	 */
	public static final float Number_Of_Tries_FWD = 10;
	
	//=== Application Layer ===//
	/*
	 * |		byte		|		byte		|		byte		|		byte		|
	 * +--------------------+-------------------+-------------------+-------------------+
	 * |									   TYPE										|
	 * +--------------------------------------------------------------------------------+
	 * 
	 * 
	 * CAM - Veiculo -> Veiculos e Torre (com TTL) (rate variável)
	 * 		Posição
	 * 		Velocidade
	 * 		Direção
	 * 
	 * DENM - Veiculo -> Veiculos e Torre
	 * 		Travão (local) (+ frequente que CAM)
	 * 		Acidente (cloud) (contínua até ser resolvido)
	 * 
	 * Torre -> Veiculo
	 * 		Excesso de velocidade
	 * 
	 * Server:
	 * 		Contar carros
	 * 
	 * Cloud:
	 * 		Atribui ids?
	 * 		Guardar histórico de estatisticas (contagem de carros e assim)
	 */
	public static final int APP_HEADER_SIZE 	= (int) 1 * Integer.BYTES;
	
	
	//== Car Hello ==/
	/*
	 * |		byte		|		byte		|		byte		|		byte		|
	 * +--------------------+-------------------+-------------------+-------------------+
	 * |									 Car ID										|
	 * |																				|
	 * +--------------------------------------------------------------------------------+
	 * |									  Pos x										|
	 * +--------------------------------------------------------------------------------+
	 * |									  Pos y										|
	 * +--------------------------------------------------------------------------------+
	 * |									  Speed										|
	 * +--------------------------------------------------------------------------------+
	 * |								   Acceleration									|
	 * +--------------------------------------------------------------------------------+
	 * |									Direction x									|
	 * +--------------------------------------------------------------------------------+
	 * |									Direction y									|
	 * +--------------------------------------------------------------------------------+
	 */
	public static final int CAR_HELLO 			= (int) 1;
	public static final int CAR_HELLO_SIZE 		= (int) APP_HEADER_SIZE + 1 * ID_SIZE + 6 * Float.BYTES;
	public static final byte TTLCarHelloMessage = (byte) 1;
	
	
	//== Car Break ==/
	/*
	 * |		byte		|		byte		|		byte		|		byte		|
	 * +--------------------+-------------------+-------------------+-------------------+
	 * |									 Car ID										|
	 * |																				|
	 * +--------------------------------------------------------------------------------+
	 * |									  Pos x										|
	 * +--------------------------------------------------------------------------------+
	 * |									  Pos y										|
	 * +--------------------------------------------------------------------------------+
	 * |									Direction x									|
	 * +--------------------------------------------------------------------------------+
	 * |									Direction y									|
	 * +--------------------------------------------------------------------------------+
	 */
	public static final int CAR_BREAK 			= (int) 2;
	public static final int CAR_BREAK_SIZE 		= (int) APP_HEADER_SIZE + 1 * ID_SIZE + 4 * Float.BYTES;
	public static final byte TTLBreakMessage 	= (byte) 6;
	
	
	//== Timeout ==//
	// Not a message. To update terminal
	public static final int TIMEOUT 			= (int) 3;
	
	
	//== Car accident ==//
	/*
	 * |		byte		|		byte		|		byte		|		byte		|
	 * +--------------------+-------------------+-------------------+-------------------+
	 * |									Tower ID									|
	 * |																				|
	 * +--------------------------------------------------------------------------------+
	 * |									 Car ID										|
	 * |																				|
	 * +--------------------------------------------------------------------------------+
	 * |									  Pos x										|
	 * +--------------------------------------------------------------------------------+
	 * |									  Pos y										|
	 * +--------------------------------------------------------------------------------+
	 * |									Direction x									|
	 * +--------------------------------------------------------------------------------+
	 * |									Direction y									|
	 * +--------------------------------------------------------------------------------+
	 */
	public static final int CAR_ACCIDENT 		= (int) 4;
	public static final int CAR_ACCIDENT_SIZE 	= (int) APP_HEADER_SIZE + 2 * ID_SIZE + 4 * Float.BYTES;
	public static final byte TTLAccidentMessage = (byte) 3;

	//== Ambulance Path ==// O que são os primeiros 4 bytes
	/*
	 * |		byte		|		byte		|		----		|		----		|
	 * +--------------------+-------------------+-------------------+-------------------+
	 * |									Number of stops								|
	 * |																				|
	 * +--------------------------------------------------------------------------------+
	 * |									  Time 1									|
	 * +--------------------------------------------------------------------------------+
	 * |									  Pos x 1									|
	 * +--------------------------------------------------------------------------------+
	 * |									  Pos y	 1									|
	 * +--------------------------------------------------------------------------------+
	 * |									  Time 2									|
	 * +--------------------------------------------------------------------------------+
	 * |									  Pos x 2									|
	 * +--------------------------------------------------------------------------------+
	 * |									  Pos y	 2									|

	 */
	public static final int AMBULANCE_PATH 		= (int) 10;
	//public static final int AMB_PATH_SIZE 	= (int) APP_HEADER_SIZE + 2 * ID_SIZE + 4 * Float.BYTES;
	public static final byte TTLAmbulance_Path = (byte) 2;
	
	
	//== Tower Announce ==//
	/*
	 * |		byte		|		byte		|		byte		|		byte		|
	 * +--------------------+-------------------+-------------------+-------------------+
	 * |									Tower ID									|
	 * |																				|
	 * +--------------------------------------------------------------------------------+
	 * |									  Pos x										|
	 * +--------------------------------------------------------------------------------+
	 * |									  Pos y										|
	 * +--------------------------------------------------------------------------------+
	 * |									Max Speed									|
	 * +--------------------------------------------------------------------------------+
	 */
	public static final int TOWER_ANNOUNCE 		= (int) 5;
	public static final byte TTLTowerHello = (byte) 1;

	public static final int TOWER_ANNOUNCE_SIZE = (int) APP_HEADER_SIZE + 1 * ID_SIZE + 3 * Float.BYTES;
	
	
	//== Server Info ==//
	/*
	 * |		byte		|		byte		|		byte		|		byte		|
	 * +--------------------+-------------------+-------------------+-------------------+
	 * |									Tower ID									|
	 * |																				|
	 * +--------------------------------------------------------------------------------+
	 * |									Batch size									|
	 * +--------------------------------------------------------------------------------+
	 * |								   Cars In Range								|
	 * |									   ...										|
	 * +--------------------------------------------------------------------------------+
	 */
	public static final int SERVER_INFO 		= (int) 6;
	public static final int SERVER_INFO_SIZE 	= (int) APP_HEADER_SIZE + 1 * ID_SIZE + 1 * Integer.BYTES + ID_SIZE * MAX_BATCH_SIZE;
	
	
	//== Car in Range ==//
	/*
	 * |		byte		|		byte		|		byte		|		byte		|
	 * +--------------------+-------------------+-------------------+-------------------+
	 * |									Tower ID									|
	 * |																				|
	 * +--------------------------------------------------------------------------------+
	 * |									 Car ID										|
	 * |																				|
	 * +--------------------------------------------------------------------------------+
	 */
	public static final int CAR_IN_RANGE 		= (int) 7;
	public static final int CAR_IN_RANGE_SIZE 	= (int) APP_HEADER_SIZE + 2 * ID_SIZE;


	public static final int IGNORED_MESSAGE_DISTANCE = (int) 9;

	
	
	// Can receive a negative number beacuse negative types are used to indicate duplicate messages.
	public static String convertTypeString(int type)
	{
		if (type < 0) type = -type;
		switch(type) {
			case CAR_HELLO:
				return "Hello Message from Car";
			case TOWER_ANNOUNCE:
				return "Hello Message from Tower";
			case CAR_BREAK:
				return "Break Message";
			case TIMEOUT:
				return "Timeout triggered";
			case CAR_ACCIDENT:
				return "Accident Happened";
			case CAR_IN_RANGE:
				return "Cars in Range";
			case IGNORED_MESSAGE_DISTANCE:
				return "Ignored message because distance";
			case AMBULANCE_PATH:
				return "Ambulance path";
			default:
				return "Unexpected (" + type + ")";
		}
	}
}
