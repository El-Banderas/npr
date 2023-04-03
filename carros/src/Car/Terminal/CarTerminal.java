package Car.Terminal;

import java.util.Timer;
import java.util.TimerTask;

import AWFullP.MessageConstants;
import AWFullP.SendMessages;
import AWFullP.FwdLayer.AWFullPFwdLayer;
import Car.SharedClass;
import Common.Constants;
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
			"Accident resolved"
		});

		menu.setHandler(1, ()->{});
		menu.setHandler(2, this::breakHandler);
		menu.setHandler(3, this::accidentHandler);
		menu.setHandler(4, this::stopAccidentHandler);

		menu.setPreCondition(3, ()->!this.inAccident);
		menu.setPreCondition(4, ()->this.inAccident);

		while(true) {
			shared.printMessagesInfo();
			menu.runOnce();
		}
	}


	private void breakHandler()
	{
		System.out.println("Pressed Break!");
		AWFullPFwdLayer fwrInfo = new AWFullPFwdLayer(MessageConstants.TTLBreakMessage, shared.id, shared.getAndIncrementSeqNumber());
		SendMessages.carSendBreak(this.shared.socket, fwrInfo);
	}


	private void accidentHandler()
	{
		System.out.println("Accident happened!");

		this.accidentBroadcast = new Timer(false);
		accidentBroadcast.scheduleAtFixedRate(wrap(()->
		{
			TowerInfo getNearestTower = shared.getNearestTower();
			int distance = (int) Position.distance(shared.info.pos, getNearestTower.pos);
			
			AWFullPFwdLayer fwrInfo = new AWFullPFwdLayer(MessageConstants.TTLAccidentMessage, getNearestTower.pos, distance, shared.id, shared.getAndIncrementSeqNumber());
			SendMessages.carSendAccident(shared.socket, getNearestTower, shared.info, fwrInfo);
		}
		), 0, Constants.refreshRate);
		
		this.inAccident = true;
	}

	private void stopAccidentHandler()
	{
		System.out.println("Accident resolved!");

		this.inAccident = false;

		accidentBroadcast.cancel();
	}

	private static TimerTask wrap(Runnable r)
	{
		return new TimerTask() {
			@Override
			public void run() {r.run();}
		};
	}


}
