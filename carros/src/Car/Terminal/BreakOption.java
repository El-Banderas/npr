package Car.Terminal;

import Common.Messages.SendMessages;

import java.net.DatagramSocket;


public class BreakOption extends Option{
	
	public BreakOption() {
		this.option = TerminalConstants.BreakOption;
		this.text = TerminalConstants.BreakText;
	}

	@Override
	public void action(DatagramSocket send) {
		System.out.println("Pressed Break!");
		SendMessages.carSendBreak(send);
	}
}
