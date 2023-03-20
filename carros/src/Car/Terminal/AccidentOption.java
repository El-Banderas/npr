package Car.Terminal;

import Common.Messages.SendMessages;

import java.net.DatagramSocket;


public class AccidentOption extends Option{

	public AccidentOption() {
		this.option = TerminalConstants.AccidentOption;
		this.text = TerminalConstants.AccidentText;
	}

	@Override
	public void action(DatagramSocket send) {
		System.out.println("Accident happened!");
		SendMessages.carSendAccident(send);
	}
}
