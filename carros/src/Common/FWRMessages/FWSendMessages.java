package Common.FWRMessages;

import Common.Messages.MessagesConstants;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;
import java.util.logging.Logger;

/**
 * Scenarios of messages:
 * No destination, type of message:
 * TTL | -1 | ID Sender | Seq N | Message
 *
 * Redirect:
 * TTL | Pos Dest (x e y) | Dist Dest | Seq N  | sizeID | ID Sender  | Message
 *
 * Messages that need forwarding are send by the Hello Message Threads
 */

public class FWSendMessages {

    private static Logger logger =  Logger.getLogger("npr.messages.sent");
    public static void sendFWRMessage(DatagramSocket send, DatagramPacket packet, FWRInfo fwrInfo)
    {
        byte[] content = packet.getData();
        int contentLen = content.length;
        byte[] fwrInfoBytes = fwrInfo.toByteArray();

        int fwrLen = fwrInfoBytes.length;
        byte[] join = new byte[contentLen + fwrLen];

        System.arraycopy(fwrInfoBytes, 0, join, 0, fwrLen);
        System.arraycopy(content, 0, join, fwrLen, contentLen);

        DatagramPacket readyToSend = new DatagramPacket(join, join.length, packet.getAddress(), packet.getPort());
        //System.out.println("Send message: ");
        //System.out.println(fwrInfo);
        try {
            send.send(readyToSend);
        } catch (IOException e) {
            logger.severe("IOException when sending " + packet.toString() + " to " + send.toString());
            logger.throwing("SendMessages", "sendMessage", e);        }
    }

}
