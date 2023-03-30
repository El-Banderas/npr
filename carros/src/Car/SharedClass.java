package Car;

import Car.Terminal.MessageEntry;

import java.net.DatagramSocket;
import java.util.TreeMap;

import AWFullP.MessagesConstants;


public class SharedClass
{
	public DatagramSocket socket;
	public TreeMap<Integer, MessageEntry> receivedMessages;
	
	
	public SharedClass(DatagramSocket socket)
	{
		this.socket = socket;
		receivedMessages = new TreeMap<>();
	}
	
	
	public void addEntryMessages(Integer typeMessage)
	{
		if (receivedMessages.containsKey(typeMessage)) {
			receivedMessages.get(typeMessage).addEntry();
		} else {
			String textMessage = "Type " + MessagesConstants.convertTypeString(typeMessage);
			receivedMessages.put(typeMessage, new MessageEntry(textMessage));
		}
	}
	
	public void printMessagesInfo()
	{
		for (MessageEntry message : receivedMessages.values()) {
			System.out.println(message.toString());
		}
	}
}
