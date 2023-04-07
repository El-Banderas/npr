package Car;

import AWFullP.AWFullPacket;
import AWFullP.ReceiveMessages;
import AWFullP.SendMessages;
import Common.Constants;

import java.net.DatagramSocket;
import java.util.TimerTask;

public class SendMessagePeriodically extends TimerTask {
    private DatagramSocket socket;
    private AWFullPacket message;
    public SendMessagePeriodically(DatagramSocket socket, AWFullPacket message) {
        this.socket = socket;
        this.message = message;
    }

    @Override
    public void run() {
        SendMessages.sendMessage(socket, Constants.MulticastGroup, Constants.portMulticast, message);
        System.out.println("Envia mensagem (TTL) " + message.forwardInfo.getTTL());

    }
}
