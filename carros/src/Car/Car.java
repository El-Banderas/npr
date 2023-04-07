package Car;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

import AWFullP.*;
import AWFullP.FwdLayer.AWFullPFwdLayer;
import AWFullP.FwdLayer.DontForward;
import AWFullP.FwdLayer.SelfCarMessage;
import Car.Terminal.CarTerminal;
import Common.CarInfo;
import Common.Constants;
import Common.InfoNodeMulticast;
import Common.TowerInfo;


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
	private Map<AWFullPFwdLayer, AWFullPacket> queueToResendMessages;
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
		this.queueToResendMessages = new HashMap<>();
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
				AWFullPacket message = ReceiveMessages.parseMessageCar(this.socket, myIp, me.getID());
				//AWFullPacket message = ReceiveMessages.receiveData(socket);
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
		if (!alreadyReceivedMessage(message) || message.getType() == MessageConstants.CAR_HELLO ) {
			// Only store new messages if they are hello.
			// Otherwise, maybe was already received
			shared.addEntryMessages(message.appLayer.getType());

			if (message.forwardInfo.getTTL() > 1){

				// Check if we should hold or just send message.
				// So, we could store in map or set.
				try {
					if (message.hasDestinationPosition(me.getPosition())) {

						queueToResendMessages.put(message.forwardInfo, message);
						ReceiveMessages.maybeForwardMessage(message, this.socket, me);
					}
					else {
						if (!messagesAlreadyReceived.contains(message.forwardInfo)){
							messagesAlreadyReceived.add(message.forwardInfo);
							ReceiveMessages.maybeForwardMessage(message, this.socket, me);

						}
						else {
							System.out.println("\n\n\nAposto que isto nunca vai aparecer\n\n\n");
						}
					}
				} catch (DontForward e) {
					System.out.println("Não vou reencaminhar");
					shared.addEntryMessages(MessageConstants.IGNORED_MESSAGE_DISTANCE);
				}
			}
		}
		else {
			if (message.getType() != MessageConstants.CAR_HELLO) System.out.println("Mensagem repetida do tipo: " + message.getType());
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
		return queueToResendMessages.containsKey(message.forwardInfo) || messagesAlreadyReceived.contains(message.forwardInfo);
	}

	private static TimerTask wrap(Runnable r)
	{
		return new TimerTask() {
			@Override
			public void run() {r.run();}
		};
	}
}
