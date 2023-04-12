package Car;

import java.net.DatagramSocket;
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
	private int currentSeqNumberMessage;
	private List<TowerInfo> towers;
	public CarInfo info;



	public SharedClass(CarInfo info, DatagramSocket socket, List<TowerInfo> towers)
	{
		this.socket = socket;
		receivedMessages = new TreeMap<>();
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
		Position carPos = info.getPosition();
		double minDist = Double.MAX_VALUE;
		TowerInfo closestTower = null;
		for (TowerInfo oneTower : towers){
			double thisDistance = Position.distance(carPos, oneTower.getPosition());
			 if (thisDistance < minDist) {
				 minDist = thisDistance;
				 closestTower = oneTower;
			 }
		}
		return closestTower;
	}
}
