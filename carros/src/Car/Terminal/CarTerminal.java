package Car.Terminal;

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


	public CarTerminal(SharedClass shared)
	{
		this.shared = shared;
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
		});

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
			System.out.println(type);
		}
		System.out.println("###############");

	}


	private void breakHandler()
	{
		System.out.println("Pressed Break!");
		AWFullPFwdLayer fwrInfo = new AWFullPFwdLayer(MessageConstants.TTLBreakMessage, shared.info.getID(), shared.getAndIncrementSeqNumber());
		SendMessages.carSendBreak(this.shared.socket, shared.info, fwrInfo);
	}


	private void accidentHandler()
	{
		System.out.println("Accident happened!");
// Meti em comentário porque é mais fácil para ver a implementação
	//	this.accidentBroadcast = new Timer(false);
	//	accidentBroadcast.scheduleAtFixedRate(wrap(()->
//		{
			TowerInfo getNearestTower = shared.getNearestTower();
			System.out.println("Posição destino ["+getNearestTower.getName()+"]: (" + getNearestTower.getPosition().x + " , "+ getNearestTower.getPosition().y +")");
			int distance = (int) Position.distance(shared.info.getPosition(), getNearestTower.getPosition());
			
			AWFullPFwdLayer fwrInfo = new AWFullPFwdLayer(MessageConstants.TTLAccidentMessage, getNearestTower.getPosition(), distance, shared.info.getID(), shared.getAndIncrementSeqNumber());
			SendMessages.carSendAccident(shared.socket, getNearestTower, shared.info, fwrInfo);
//		}
//		), 0, Constants.refreshRate);
		
//		this.inAccident = true;
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
