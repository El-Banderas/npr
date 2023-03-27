package Car.Terminal;

import Car.SharedClass;
import Common.FWRMessages.FWRInfo;
import Common.Messages.MessagesConstants;
import Common.Messages.SendMessages;
import Common.Position;
import Common.TowerInfo;


public class CarTerminal implements Runnable {
	
	private SharedClass shared;
	
	
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
			"Accident happened"
		});
		
		menu.setHandler(1, ()->{});
		menu.setHandler(2, this::breakHandler);
		menu.setHandler(3, this::accidentHandler);
		
		while(true) {
			shared.printMessagesInfo();
			menu.runOnce();
		}
	}
	
	
	private void accidentHandler() {
		System.out.println("Accident happened!");
		TowerInfo getNearestTower = shared.getNearestTower();
		int distance = (int) Position.distance(shared.info.pos, getNearestTower.pos);

		FWRInfo fwrInfo = new FWRInfo(MessagesConstants.TTLAccidentMessage, getNearestTower.pos, distance, shared.id, shared.getAndIncrementSeqNumber());
		SendMessages.carSendAccident(shared.info.sendSocket(), fwrInfo);
	}
	
	private void breakHandler() {
		System.out.println("Pressed Break!");
		FWRInfo fwrInfo = new FWRInfo(MessagesConstants.TTLBreakMessage, shared.id, shared.getAndIncrementSeqNumber());

		SendMessages.carSendBreak(this.shared.info.sendSocket(), fwrInfo);
	}


}
