package Car;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

import AWFullP.*;
import AWFullP.FwdLayer.AWFullPFwdLayer;
import AWFullP.FwdLayer.SelfCarMessage;
import Car.Terminal.CarTerminal;
import Common.*;


public class Car implements Runnable
{
	//private static Logger logger =  Logger.getLogger("npr.car");
	
	// Node information
	private CarInfo me;
	//private List<TowerInfo> towers;

	// Connection information
	private DatagramSocket socket;
	private InetAddress myIp; //TODO: temporary. Use random string

	// Others
	private SharedClass shared; //for CLI
	private AWFullPFwdLayer fwrInfoHelloCar;
	// This map is used to resend messages that are not confirmed
	// Se calhar vai desaparecer...
	//private Map<AWFullPFwdLayer, AWFullPacket> queueToResendMessages;
	private Map<AWFullPFwdLayer, SendMessagePeriodically> sendMessagesClasses;
	// This map is to store already received messages
	// TODO: Se calhar depois apagar mensagens mais antigas? Para não acumular imensas
	private Set<AWFullPFwdLayer> messagesAlreadyReceived;


	public Car(CarInfo info, List<TowerInfo> towers) throws IOException
	{
		this.me = info;
		//this.towers = towers;
		this.socket = new InfoNodeMulticast("eth0").socket;
		this.shared = new SharedClass(me, this.socket, towers);

		this.fwrInfoHelloCar = new AWFullPFwdLayer(MessageConstants.TTLCarHelloMessage, shared.info.getID(), -1);

		this.myIp = Constants.getMyIp();
		this.sendMessagesClasses = new HashMap<>();
		this.messagesAlreadyReceived = new HashSet<>();
	}


	@Override
	public void run()
	{
		CarTerminal carTerminal = new CarTerminal(shared);
		Thread thread_1 = new Thread(carTerminal);
		thread_1.start();

		Thread thread_2 = new Thread(this::receiveMessages);
		thread_2.start();

		Timer timer_1 = new Timer(false);
		timer_1.scheduleAtFixedRate(wrap(this::sendHellos), 0, Constants.refreshRate);
	}


	private void sendHellos()
	{
		SendMessages.carHello(this.socket, this.me, fwrInfoHelloCar);
	}

	private void receiveMessages()
	{
		while (true) {
			try {
				this.me.update();
				//System.out.println("Posição atual: " + info.pos.x + " | " + info.pos.y);
				//AWFullPacket message = ReceiveMessages.receiveData(socket);
				AWFullPacket message = ReceiveMessages.parseMessageCar(this.socket, myIp, me.getID());
				handleMessage(message);
			} catch (IOException e) {
				this.shared.addEntryMessages(MessageConstants.TIMEOUT);
			} catch (SelfCarMessage ignore) {}
		}
	}

	private void handleMessage(AWFullPacket message) throws UnknownHostException
	{
		// TODO: Depois reencaminhar mensagens que estão no map de reenvio
		// TODO: O forward message devia estar fora dos ifs, é para testar
		if (message.forwardInfo.getTTL() > 1 ) {
			// Is a new message
			if (!messagesAlreadyReceived.contains(message.forwardInfo)) {
				shared.addEntryMessages(message.appLayer.getType());
				if (message.hasDestinationPosition()) {
						resendMessageWithDestination(message);
					}
					else {
						System.out.println("Aciciona mensagem");
						messagesAlreadyReceived.add(message.forwardInfo);
						ReceiveMessages.maybeForwardMessage(message, this.socket, me);
					}
				}
			else {
				System.out.println("Recebeu mensagem repetida");
			}
		}
		else {
			shared.addEntryMessages(message.appLayer.getType());
		}
	}

	private void resendMessageWithDestination(AWFullPacket message) {

		float distanceMessage = message.forwardInfo.getDist();
		double myDistance = Position.distance(message.forwardInfo.getPosition(), me.getPosition());
		System.out.println("Compara distância, minha vs. mensagem:" + myDistance +" vs. " + distanceMessage);
		if (myDistance > distanceMessage){
			if (sendMessagesClasses.containsKey(message.forwardInfo)){
				sendMessagesClasses.get(message.forwardInfo).cancel();
				messagesAlreadyReceived.add(message.forwardInfo);
				System.out.println("Someone better appear to send my message. Cancel sending message");
			}
			else {
				System.out.println("Não vou reecaminhar porque estou mais longe");
			}
		}
		else {
			message.forwardInfo.updateInfo(me);

			Timer timer_1 = new Timer(false);
			SendMessagePeriodically newSender = new SendMessagePeriodically(this.socket, message);
			sendMessagesClasses.put(message.forwardInfo, newSender);
			timer_1.scheduleAtFixedRate(newSender, (long) (message.forwardInfo.getDist() * MessageConstants.Delay_Before_Send_Message), Constants.refreshRate);
			System.out.println("Adiciona mensagem");

		}
	}

	/**
	 * Here we check if a message has been received before. It can be stored in two places, or be a Hello Message.
	 * Hellos message are not stored.
	 * @param message
	 * @return If the message is new or not to this car.
	 */
	private boolean alreadyReceivedMessage(AWFullPacket message)
	{
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
