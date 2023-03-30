package Car.Terminal;

import java.util.Timer;
import java.util.TimerTask;

import Car.SharedClass;
import Common.Constants;
import Common.Messages.SendMessages;


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
		SendMessages.carSendBreak(this.shared.info.socket);
	}
	
	private void accidentHandler()
	{
		System.out.println("Accident happened!");
		
		this.inAccident = true;
		
		accidentBroadcast.scheduleAtFixedRate(wrap(()->
		{
			SendMessages.carSendAccident(this.shared.info.socket);
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
