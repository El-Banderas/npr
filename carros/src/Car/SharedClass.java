package Car;

import java.net.DatagramSocket;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import AWFullP.MessageConstants;
import Car.Terminal.MessageEntry;
import Common.CarInfo;
import Common.Position;
import Common.TowerInfo;


public class SharedClass
{
	public DatagramSocket socket;
	public TreeMap<Integer, MessageEntry> receivedMessages;
	public HashMap<Position, MessageEntry> sendMessages;
	private int currentSeqNumberMessage;
	private List<TowerInfo> towers;
	public CarInfo info;



	public SharedClass(CarInfo info, DatagramSocket socket, List<TowerInfo> towers)
	{
		this.socket = socket;
		receivedMessages = new TreeMap<>();
		sendMessages = new HashMap<Position, MessageEntry>();
		this.currentSeqNumberMessage = 0;
		this.towers = towers;
		this.info = info;
	}


	public void addEntryMessages(Integer typeMessage)
	{
		if (receivedMessages.containsKey(typeMessage)) {
			receivedMessages.get(typeMessage).addEntry();
		} else {
			String textMessage = "Type " + MessageConstants.convertTypeString(typeMessage);
			receivedMessages.put(typeMessage, new MessageEntry(textMessage));
		}
	}
	public void addRepeatedMessages(int type) {
		int typeMessage = -type;
		if (receivedMessages.containsKey(typeMessage)) {
			receivedMessages.get(typeMessage).addEntry();
		} else {
			String textMessage = "Type (duplicate) " + MessageConstants.convertTypeString(typeMessage);
			receivedMessages.put(typeMessage, new MessageEntry(textMessage));
		}
	}
	public void addSendMessages(int typeMessage, Position pos)
	{

		if (sendMessages.containsKey(pos)) {
			sendMessages.get(pos).addEntry();
		} else {
			String textMessage = "Type " + MessageConstants.convertTypeString(typeMessage) + " destination " + pos.toString();
			sendMessages.put(pos, new MessageEntry(textMessage));
		}
	}
	public void addSendMessages(int typeMessage)
	{
		Position noDestination = new Position(-typeMessage, -typeMessage);
		if (sendMessages.containsKey(noDestination)) {
			sendMessages.get(noDestination).addEntry();
		} else {
			String textMessage = "Type " + MessageConstants.convertTypeString(typeMessage) + " send";
			sendMessages.put(noDestination, new MessageEntry(textMessage));
		}
	}

	public void printMessagesInfo()
	{
		for (MessageEntry message : receivedMessages.values()) {
			System.out.println("[SharedClass] " + message.toString());
		}
	}
	public int getAndIncrementSeqNumber(){
		this.currentSeqNumberMessage++;
		return this.currentSeqNumberMessage;
	}

	public TowerInfo getNearestTower(){
		return TowerInfo.getNearestTower(info.getPosition(), towers);
	}




}
