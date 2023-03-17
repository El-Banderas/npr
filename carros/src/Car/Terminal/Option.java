package Car.Terminal;

import java.net.DatagramSocket;


public abstract class Option {

	public  Integer option;
	public  String text;

	public abstract void action(DatagramSocket send);
}
