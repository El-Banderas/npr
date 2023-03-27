package Common.Messages;

public class MessagesConstants {
	
	public static final int CarHelloMessage = 1;
	public static final int TTLCarHelloMessage = 1;
	public static final int BreakMessage = 2;
	public static final int TTLBreakMessage = 3;
	public static final int AccidentMessage = 4;
	public static final int TTLAccidentMessage = 5;
	public static final int TowerHelloMessage = 5;
	public static final int ServerHelloMessage = 6;
	
	
	public static final int Timeout = 3; // No message. To update terminal
	
	public static String convertTypeString(int type){
		switch(type) {
			case CarHelloMessage:
				return "Hello Message from Car";
			case TowerHelloMessage:
				return "Hello Message from Tower";
			case BreakMessage:
				return "Break Message";
			case Timeout:
				return "Timeout triggered";
			case AccidentMessage:
				return "Accident Happened";
			default:
				return "Type unknown";
		}
	}
	
	public static int sizeBufferMessages = 1200;
}
