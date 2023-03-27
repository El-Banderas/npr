package Car;

import Car.Terminal.MessageEntry;
import Common.CarInfo;
import Common.Messages.MessagesConstants;
import Common.Position;
import Common.TowerInfo;

import java.util.List;
import java.util.TreeMap;


public class SharedClass {
	
	public CarInfo info;
	public TreeMap<Integer, MessageEntry> receivedMessages;
	private int currentSeqNumberMessage;
	private List<TowerInfo> towers;
	public byte[] id;



	public SharedClass(CarInfo info, List<TowerInfo> towers)
	{
		this.info = info;
		receivedMessages = new TreeMap<>();
		this.currentSeqNumberMessage = 0;
		this.towers = towers;
		id = info.id.getBytes();
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
