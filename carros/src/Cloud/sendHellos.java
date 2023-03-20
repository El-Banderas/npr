package Cloud;

import Common.InfoNode;

import java.net.DatagramSocket;
import java.util.TimerTask;

import static Common.Messages.SendMessages.serverHelloCloud;
import static Common.Messages.SendMessages.towerHelloServer;

public class sendHellos extends TimerTask {
    public DatagramSocket sendSocket;
    public InfoNode cloud;

    public sendHellos(DatagramSocket sendSocket, InfoNode cloud) {
        this.sendSocket = sendSocket;
        this.cloud = cloud;
    }

    @Override
    public void run() {
        serverHelloCloud(sendSocket, cloud);

    }
}
