package Car;

import Car.Terminal.MessageEntry;

import java.net.DatagramSocket;
import java.util.List;
import java.util.TreeMap;

import AWFullP.MessagesConstants;
import Common.CarInfo;
import Common.Position;
import Common.TowerInfo;


public class SharedClass
{
	public DatagramSocket socket;
	public TreeMap<Integer, MessageEntry> receivedMessages;
	private int currentSeqNumberMessage;
	public byte[] id;
	private List<TowerInfo> towers;
	private CarInfo info;



	public SharedClass(CarInfo info, DatagramSocket socket, List<TowerInfo> towers)
	{
		this.socket = socket;
		receivedMessages = new TreeMap<>();
		this.currentSeqNumberMessage = 0;
		this.towers = towers;
		id = info.id.getBytes();
		this.info = info;
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
	public int getAndIncrementSeqNumber(){
		this.currentSeqNumberMessage++;
		return this.currentSeqNumberMessage;
	}

	public TowerInfo getNearestTower(){
		Position carPos = info.pos;
		double minDist = Double.MAX_VALUE;
		TowerInfo closestTower = null;
		for (TowerInfo oneTower : towers){
			double thisDistance = Position.distance(carPos, oneTower.pos);
			 if (thisDistance < minDist) {
				 minDist = thisDistance;
				 closestTower = oneTower;
			 }
		}
		return closestTower;
	}
}
