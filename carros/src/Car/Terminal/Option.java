package Car.Terminal;

import Car.CarInfo;

import java.net.DatagramSocket;

public abstract class Option {
    public  Integer option;
    public  String text;
    public abstract void action(DatagramSocket send);
}
