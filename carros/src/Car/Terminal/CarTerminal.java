package Car.Terminal;

import java.util.Timer;
import java.util.TimerTask;

import AWFullP.SendMessages;
import Car.SharedClass;
import Common.Constants;
import Common.FWRMessages.FWRInfo;
import Common.Messages.MessagesConstants;
import Common.Position;
import Common.TowerInfo;


public class CarTerminal implements Runnable
{	
	private SharedClass shared;
	
	private boolean inAccident = false;
	private Timer accidentBroadcast = new Timer(false);
	
	
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
		FWRInfo fwrInfo = new FWRInfo(MessagesConstants.TTLBreakMessage, shared.id, shared.getAndIncrementSeqNumber());
		SendMessages.carSendBreak(this.shared.socket, fwrInfo);
	}

	
	private void accidentHandler()
	{
		System.out.println("Accident happened!");
		
		this.inAccident = true;
		
		accidentBroadcast.scheduleAtFixedRate(wrap(()->
		{
	TowerInfo getNearestTower = shared.getNearestTower();
		int distance = (int) Position.distance(shared.info.pos, getNearestTower.pos);

		FWRInfo fwrInfo = new FWRInfo(MessagesConstants.TTLAccidentMessage, getNearestTower.pos, distance, shared.id, shared.getAndIncrementSeqNumber());
		SendMessages.carSendAccident(shared.socket, fwrInfo);
	


		}
		), 0, Constants.refreshRate);
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
