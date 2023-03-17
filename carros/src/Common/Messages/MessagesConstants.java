package Common.Messages;

public class MessagesConstants {
    public static final int HelloMessage = 1;
    public static final int BreakMessage = 2;

    // There is no message, is to update car terminal
    public static final int Timeout = 3;

    public static String convertTypeString(int type){
        switch (type){
            case HelloMessage:
                return "Hello Message";
        case BreakMessage:
            return "Break Message"   ;
        case Timeout:
                return "Timeout triggered"   ;

            default:
                return "Type unkown";
        }
    }
    public static int sizeBufferMessages = 1000;
}
