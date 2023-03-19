package Car;

import Common.Messages.SendMessages;

import java.net.DatagramSocket;
import java.util.TimerTask;

public class sendHellos extends TimerTask {
    public DatagramSocket sendSocket;

    public sendHellos(DatagramSocket sendSocket) {
        this.sendSocket = sendSocket;
    }

    @Override
    public void run() {
        SendMessages.carHellos(sendSocket);

    }
}
