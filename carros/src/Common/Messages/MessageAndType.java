package Common.Messages;

import java.net.InetAddress;


public class MessageAndType
{
	public int type;
	public byte[] content;
	public InetAddress ipSender;
	
	
	public MessageAndType(int type, byte[] content, InetAddress ipSender)
	{
		this.type = type;
		this.content = content;
		this.ipSender = ipSender;
	}
	
	
	@Override
	public String toString()
	{
		return new String(
				"{"
			+	" from: " + ipSender.toString()
			+	" ; type: " + type
			+	" ; size: " + content.length
			+	"}"
			);
	}
}
