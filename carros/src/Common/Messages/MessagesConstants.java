package Common.Messages;

public class MessagesConstants {
    public static final int CarHelloMessage = 1;
    public static final int TowerHelloMessage = 5;
    public static final int BreakMessage = 2;
    public static final int AccidentMessage = 4;


    // There is no message, is to update car terminal
    public static final int Timeout = 3;

    public static String convertTypeString(int type){
        switch (type){
            case CarHelloMessage:
                return "Hello Message from car";
            case TowerHelloMessage:
                return "Hello Message from Tower";
        case BreakMessage:
            return "Break Message"   ;
        case Timeout:
                return "Timeout triggered"   ;
            case AccidentMessage:
                return "Accident Happened"   ;

            default:
                return "Type unkown";
        }
    }
    public static int sizeBufferMessages = 1000;
}
