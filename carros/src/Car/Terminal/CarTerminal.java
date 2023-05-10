package Car.Terminal;

import java.net.DatagramSocket;
import java.util.Timer;

import AWFullP.MessageConstants;
import AWFullP.SendMessages;
import AWFullP.FwdLayer.AWFullPFwdLayer;
import Car.SharedClass;
import Common.Position;
import Common.TowerInfo;


public class CarTerminal implements Runnable
{
	private SharedClass shared;

	private boolean inAccident = false;
	private Timer accidentBroadcast;
	private String name;


	public CarTerminal(SharedClass shared, String name)
	{
		this.shared = shared;
		this.name = name;
	}




    @Override
	public void run()
	{
		Menu menu = new Menu(new String[] {
			"Refresh Terminal",
			"Press break and notify others",
			"Accident happened",
			"Accident resolved",
			"Check send messages"
		}, this.name);

		menu.setHandler(1, ()->{});
		menu.setHandler(2, this::breakHandler);
		menu.setHandler(3, this::accidentHandler);
		menu.setHandler(4, this::stopAccidentHandler);
		menu.setHandler(5, this::seeSendMessages);

		menu.setPreCondition(3, ()->!this.inAccident);
		menu.setPreCondition(4, ()->this.inAccident);

		while(true) {
			shared.printMessagesInfo();
			menu.runOnce();
		}
	}

	private void seeSendMessages() {
		System.out.println("###############");
		System.out.println("Send messages:");
		for (MessageEntry type : shared.sendMessages.values()){
			System.out.println(type.toStringSend());
		}
		System.out.println("###############");

	}


	private void breakHandler()
	{
		sendBreak(this.shared);
	}

	// TODO: Se calhar isto vai para outra classe?
	public static void sendBreak(SharedClass givenShared)
	{
		System.out.println("Pressed Break!");
		AWFullPFwdLayer fwrInfo = new AWFullPFwdLayer(MessageConstants.TTLBreakMessage, givenShared.info.getID(), givenShared.getAndIncrementSeqNumber());
		SendMessages.carSendBreak(givenShared.socket, givenShared.info, fwrInfo);
	}
	public static void sendAccident(SharedClass givenShared) {
		System.out.println("Accident happened!");
// Meti em comentário porque é mais fácil para ver a implementação
		//	this.accidentBroadcast = new Timer(false);
		//	accidentBroadcast.scheduleAtFixedRate(wrap(()->
//		{
		TowerInfo getNearestTower = givenShared.getNearestTower();
		System.out.println("Posição destino ["+getNearestTower.getName()+"]: (" + getNearestTower.getPosition().x + " , "+ getNearestTower.getPosition().y +")");
		int distance = (int) Position.distance(givenShared.info.getPosition(), getNearestTower.getPosition());

		AWFullPFwdLayer fwrInfo = new AWFullPFwdLayer(MessageConstants.TTLAccidentMessage, getNearestTower.getPosition(), distance, givenShared.info.getID(), givenShared.getAndIncrementSeqNumber());
		SendMessages.carSendAccident(givenShared.socket, getNearestTower, givenShared.info, fwrInfo);

//		}
//		), 0, Constants.refreshRate);

//		this.inAccident = true;
	}

	private void accidentHandler()
	{
		sendAccident(shared);
	}

	private void stopAccidentHandler()
	{
		System.out.println("Accident resolved!");

		this.inAccident = false;

		accidentBroadcast.cancel();
	}

	/*private static TimerTask wrap(Runnable r)
	{
		return new TimerTask() {
			@Override
			public void run() {r.run();}
		};
	}*/
}
