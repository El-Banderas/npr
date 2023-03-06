package Common;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class InfoNode {
    public InetAddress ip;
    public int port;
    public DatagramSocket socket;

    @Override
    public String toString() {
        return "InfoNode{" +
                "ip=" + ip.toString() +
                ", port=" + port +
                '}';
    }

    public InfoNode(InetAddress ip, int port, boolean createSocket) {
        this.ip = ip;
        this.port = port;
        if (createSocket) {
            try {
                socket = new DatagramSocket(port);
            } catch (SocketException e) {
                System.out.println("[Expetion] Error creating socket");
                throw new RuntimeException(e);
            }
        }
    }

    public InfoNode() {
    }
}
