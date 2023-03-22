package Tower;

import Common.InfoNode;
import Common.Messages.SendMessages;

import java.net.DatagramSocket;
import java.util.TimerTask;

import static Common.Messages.SendMessages.towerHelloServer;

public class sendHellos extends TimerTask {
    public DatagramSocket sendSocket;
    public InfoNode thisServer;

    public sendHellos(DatagramSocket sendSocket, InfoNode server) {
        this.sendSocket = sendSocket;
        this.thisServer = server;
    }

    @Override
    public void run() {
        towerHelloServer(sendSocket, thisServer);
        SendMessages.towerHelloCar(sendSocket);

    }
}
