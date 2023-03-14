package Car;

import Car.Terminal.MessageEntry;
import Common.Messages.MessagesConstants;

import java.util.TreeMap;

public class SharedClass {
    public CarInfo info;

    public TreeMap<Integer, MessageEntry> receivedMessages;

    public SharedClass(CarInfo info) {
        this.info = info;
        receivedMessages = new TreeMap<>();
    }
    public void addEntryMessages(Integer typeMessage){
        if (receivedMessages.containsKey(typeMessage)){

            receivedMessages.get(typeMessage).addEntry();
        }
        else {

            String textMessage = "Type " + MessagesConstants.convertTypeString(typeMessage);
            receivedMessages.put(typeMessage, new MessageEntry(textMessage));
        }
    }

    public void printMessagesInfo() {
        for (MessageEntry message : receivedMessages.values()){
            System.out.println(message.toString());
        }
    }
}
