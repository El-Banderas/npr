package Car;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import AWFullP.*;
import AWFullP.AppLayer.AWFullPTowerAnnounce;
import AWFullP.FwdLayer.AWFullPFwdLayer;
import AWFullP.FwdLayer.SelfCarMessage;
import Car.Terminal.CarTerminal;
import Common.*;


public class Car implements Runnable
{
	private static Logger logger = Logger.getLogger("npr.car");
	static {
		ConsoleHandler handler = new ConsoleHandler();
		handler.setFormatter(new SimpleFormatter());
		handler.setLevel(Level.ALL);
		logger.addHandler(handler);
		
		logger.setLevel(Level.CONFIG);
		Logger.getLogger("npr.messages.received").setLevel(Level.SEVERE);
		Logger.getLogger("npr.messages.sent").setLevel(Level.SEVERE);
	}
	
	// Node information
	private CarInfo me;

	// Connection information
	private DatagramSocket socket;
	private InetAddress myIp; //TODO: temporary. Use ID

	// Others
	private SharedClass shared; //for CLI
	private AWFullPFwdLayer fwrInfoHelloCar;
	//private Map<AWFullPFwdLayer, AWFullPacket> queueToResendMessages; // This map is used to resend messages that are not confirmed
	private Map<AWFullPFwdLayer, SendMessagePeriodically> sendMessagesClasses;
	private Set<AWFullPFwdLayer> messagesAlreadyReceived; // This map is to store already received messages; TODO: Apagar mensagens mais antigas


	public Car(CarInfo info, List<TowerInfo> towers) throws IOException
	{
		logger.config(info.toString());
		logger.config(towers.toString());
		
		this.me = info;
		
		this.socket = new InfoNodeMulticast("eth0").socket;
		this.myIp = Constants.getMyIp();
		
		this.shared = new SharedClass(me, this.socket, towers);
		this.fwrInfoHelloCar = new AWFullPFwdLayer(MessageConstants.TTLCarHelloMessage, shared.info.getID(), -1);

		this.sendMessagesClasses = new HashMap<>();
		this.messagesAlreadyReceived = new HashSet<>();
	}

	public Car(CarInfo info, List<TowerInfo> towers, AmbulanceInfo ambulanceInfo) throws IOException {
		this(info, towers);
		TowerInfo getNearestTower = shared.getNearestTower();
		System.out.println("Send Ambulance info ["+getNearestTower.getName()+"]");
		int distance = (int) Position.distance(shared.info.getPosition(), getNearestTower.getPosition());

		AWFullPFwdLayer fwrInfo = new AWFullPFwdLayer(MessageConstants.TTLAmbulance_Path, getNearestTower.getPosition(), distance, shared.info.getID(), shared.getAndIncrementSeqNumber());

		SendMessages.sendAmbulanceInfo(shared.socket,shared.info, fwrInfo, ambulanceInfo);
	}

	@Override
	public void run()
	{
		CarTerminal carTerminal = new CarTerminal(shared, me.getName());
		Thread thread_1 = new Thread(carTerminal);
		thread_1.start();

		Thread thread_2 = new Thread(this::receiveMessages);
		thread_2.start();

		Timer timer_1 = new Timer(false);
		timer_1.scheduleAtFixedRate(wrap(this::sendHellos), 0, Constants.refreshRate);
	}


	private void sendHellos()
	{
		String action = me.actionTriggered();
		if (!action.equals("None")){
			System.out.println("Action triggered: " + action);
			if (action.equals("break")) {
				CarTerminal.sendBreak(shared);
			} else if (action.equals("accident")) {
				CarTerminal.sendAccident(shared);

			}
		}
		SendMessages.carHello(this.socket, this.me, fwrInfoHelloCar);
	}

	private void receiveMessages()
	{
		while (true) {
			try {
				this.me.update();
				logger.finer("Current position: " + this.me.getPosition().x + " , " + this.me.getPosition().y);
				AWFullPacket message = ReceiveMessages.parseMessageCar(this.socket, myIp, me.getID());
				handleMessage(message);
			} catch (IOException e) {
				this.shared.addEntryMessages(MessageConstants.TIMEOUT);
			} catch (SelfCarMessage ignore) {}
		}
	}

	private void handleMessage(AWFullPacket message) throws UnknownHostException
	{
		// Upon receiving a tower announce, add it to the list
		if (message.appLayer.getType() == MessageConstants.TOWER_ANNOUNCE) {
			AWFullPTowerAnnounce aw_ta = (AWFullPTowerAnnounce) message.appLayer;
			TowerInfo tower = new TowerInfo(aw_ta.getTowerID(), aw_ta.getPos(), aw_ta.getMaxSpeed());
			if (!this.shared.knowsTower(tower))
				this.shared.addTower(tower);
		}

		// TODO: O forward message devia estar fora dos ifs, é para testar
		// Ack from tower
		if (sendMessagesClasses.containsKey(message.forwardInfo) && message.forwardInfo.getDist() == 0){
			System.out.println("Receive ACK from tower");
			sendMessagesClasses.get(message.forwardInfo).cancel();
			sendMessagesClasses.remove(message.forwardInfo);
			messagesAlreadyReceived.add(message.forwardInfo);
			shared.addEntryMessages(message.appLayer.getType());

		}
		// In case is an accident message, and we want it to stop
		if (message.forwardInfo.getTTL() > 1 || message.hasDestinationPosition() ) {
			
			//if (message.getType() != MessageConstants.CAR_HELLO ) System.out.println("2");

			// Is a new message
			if (!messagesAlreadyReceived.contains(message.forwardInfo) ) {
				
				// If the message is in destination, we fwd one time, and not wait for confirmation
				if (message.hasDestinationPosition() && !inDestination(message.forwardInfo)) {
					
					if (!sendMessagesClasses.containsKey(message.forwardInfo))
						shared.addEntryMessages(message.appLayer.getType());
					
					resendMessageWithDestination(message);
				}
				else {
					System.out.println("Add message");
					messagesAlreadyReceived.add(message.forwardInfo);
					ReceiveMessages.maybeForwardMessage(message, this.socket, me);
					if (inDestination(message.forwardInfo)){
						shared.addEntryMessages(MessageConstants.CLOUD_AMBULANCE_PATH_IN_DESTINATION);
					}
					else
						shared.addEntryMessages(message.appLayer.getType());
					shared.addSendMessages(message.appLayer.getType());
				}
			}
			else
				shared.addRepeatedMessages(message.appLayer.getType());

			//System.out.println("Received repeated message");
		}
		else
			shared.addEntryMessages(message.appLayer.getType());
	}

	private boolean inDestination(AWFullPFwdLayer forwardInfo) {
		if (Position.distance(forwardInfo.getPosition(), me.getPosition()) < MessageConstants.RADIUS_DESTINATION_POSITION) {
			System.out.println("Message reached destination!");
			return true;
		}
		else return false;
	}

	private void resendMessageWithDestination(AWFullPacket message)
	{
		float distanceMessage = message.forwardInfo.getDist();
		double myDistance = Position.distance(message.forwardInfo.getPosition(), me.getPosition());
		
		System.out.println("Compara distância, minha vs. mensagem:" + myDistance +" vs. " + distanceMessage);
		if (myDistance > distanceMessage){
			if (sendMessagesClasses.containsKey(message.forwardInfo)){
				System.out.println("Someone better appear to send my message. Cancel sending message");
				// Cancel timertask
				sendMessagesClasses.get(message.forwardInfo).cancel();
				sendMessagesClasses.remove(message.forwardInfo);
				messagesAlreadyReceived.add(message.forwardInfo);
			}
			else
				System.out.println("Não vou reencaminhar porque estou mais longe");
		}
		else {
			message.forwardInfo.updateInfo(me);
			Timer timer_1 = new Timer(false);
			SendMessagePeriodically newSender = new SendMessagePeriodically(this.socket, message, shared, message.forwardInfo.getPosition());
			sendMessagesClasses.put(message.forwardInfo, newSender);
			timer_1.scheduleAtFixedRate(newSender, (long) (message.forwardInfo.getDist() * MessageConstants.Delay_Before_Send_Message), Constants.refreshRate);
			System.out.println("Adiciona mensagem");

		}
	}

	private boolean alreadyReceivedMessage(AWFullPacket message)
	{
		//Message can be stored in two places, or be a Hello Message.
		//Hellos message are not stored.
		return sendMessagesClasses.containsKey(message.forwardInfo) || messagesAlreadyReceived.contains(message.forwardInfo);
	}

	private static TimerTask wrap(Runnable r)
	{
		return new TimerTask() {
			@Override
			public void run() {r.run();}
		};
	}
}
