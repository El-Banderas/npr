package Common.Messages;

public class MessageAndType {
    public int type;
    public byte[] content;

    public MessageAndType(int type, byte[] content) {
        this.type = type;
        this.content = content;
    }
}
