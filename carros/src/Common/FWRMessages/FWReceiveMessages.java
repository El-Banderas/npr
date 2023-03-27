package Common.FWRMessages;

import Common.CarInfo;
import Common.Messages.MessageAndType;
import Common.Messages.MessagesConstants;
import Common.Position;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

public class FWReceiveMessages {
    private static Logger logger = Logger.getLogger("npr.messages.received");

    /**
     * Receives and forwards, when in case, received messages.
     *
     * @param receiveSocket - To receive messages.
     * @param sendSocket    - To resend messages, with TTL and positions changed.
     * @param myIp          - To filter messages send from the same car.
     * @param carInfo       - To update messages that are send, with position and ID of car.
     * @return
     * @throws IOException
     */
    public static MessageAndType forwardHandleMessage(DatagramSocket receiveSocket, DatagramSocket sendSocket, InetAddress myIp, CarInfo carInfo) throws IOException {

        byte[] buf = new byte[MessagesConstants.sizeBufferMessages];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        receiveSocket.receive(packet);
        if (packet.getAddress().equals(myIp)) return null;
        FWRInfo fwrinfo = new FWRInfo(buf);


        // Maybe forward message?
        // TODO: Check Distance and duplicate messages
        System.out.println("Vai fazer fwr? " + fwrinfo.TTL);
        if (fwrinfo.TTL > 1) {
            System.out.println("Vai fazer fwr");
            fwrinfo.updateInfo(carInfo);
            FWSendMessages.sendFWRMessage(sendSocket, fwrinfo);
        }
        // Return content of message

        ByteBuffer bbuf = ByteBuffer.wrap(fwrinfo.content);

        // After removing FW content
        int type = bbuf.getInt();
        byte[] remaining = new byte[bbuf.remaining()];
        bbuf.get(remaining, 0 /*bbuf.position()*/, bbuf.remaining());

        MessageAndType received = new MessageAndType(type, remaining, packet.getAddress());

        //logger.info("Received Message:\n" + received.toString());

        return received;
    }
}
