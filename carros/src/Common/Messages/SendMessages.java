package Common.Messages;

import Common.InfoNode;

import java.io.IOException;
import java.net.DatagramPacket;

public class SendMessages {

    public static void carHelloTower(InfoNode sender, InfoNode destination){
        byte[]  buf = "Hello".getBytes();
        DatagramPacket packet
                = new DatagramPacket(buf, buf.length, destination.ip, destination.port);
        sendMessage(sender, packet);
    }

    public static void sendMessage(InfoNode send, DatagramPacket packet){
        try {
            send.socket.send(packet);
        } catch (IOException e) {
//            throw new RuntimeException(e);
            System.out.println("[ERROR] in sending message");
        }
    }
}
