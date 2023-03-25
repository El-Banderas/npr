package Car.Terminal;

import Car.SharedClass;
import Common.Messages.SendMessages;


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
		SendMessages.carSendAccident(this.shared.info.sendSocket());
	}
	
	private void breakHandler() {
		System.out.println("Pressed Break!");
		SendMessages.carSendBreak(this.shared.info.sendSocket());
	}
}
