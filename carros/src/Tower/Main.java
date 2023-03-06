package Tower;

import Common.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Arguments:
 * Windows
 * Name (unecessary) | Port | PosX | PosY
 * The port must be the same as the file.
 * Example: "t1 8000 40 40"
 *
 *
 * Linux, depois remover a posição
 * Name (unecessary) | PosX | PosY
 * The port must be the same as the file.
 * Example: "t1 40 40"
 */
public class Main {
    public static void main(String[] args) throws UnknownHostException, SocketException{

        System.out.println("Is linux?: " + Constants.linux);
            String name = args[0];

            InfoNode towerIPInfo;
            Position pos;
        if (Constants.linux) {
            towerIPInfo = new InfoNode(null, Constants.towerPort, true);
            pos = new Position(Integer.parseInt(args[1]), Integer.parseInt(args[2]));

        }
        else {
            towerIPInfo = new InfoNodeWindows(Integer.parseInt(args[1]), true);
            pos = new Position(Integer.parseInt(args[2]), Integer.parseInt(args[3]));

        }
            TowerInfo thisTower = new TowerInfo(name, towerIPInfo, pos);
            System.out.println(thisTower);

            byte[] buf = new byte[256];
        DatagramPacket packet
                = new DatagramPacket(buf, buf.length);
        thisTower.connectionInfo.socket.setSoTimeout(Constants.refreshRate);
            while(true){
                try {

                    thisTower.connectionInfo.socket.receive(packet);
                    System.out.println("[TOWER] Message received");
                } catch (IOException e) {
                    System.out.println("[TOWER] Timeout passed. Nothing received.");
                    System.out.println("Receiving in: "+thisTower.connectionInfo.socket.getLocalAddress() +" | "+  thisTower.connectionInfo.socket.getLocalPort() );

                }

            }
        }
    }
