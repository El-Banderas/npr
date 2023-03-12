package Common;

import java.net.InetAddress;
import java.net.UnknownHostException;

public final class Constants {
    public static boolean linux = true;
    public static int refreshRate = 500;

    /**
     * Connection's
     */

    private static final String MCAST_ADDR = "FF7E:230::1234";
    public static InetAddress MulticastGroup;


    static {
        try {
            MulticastGroup = InetAddress.getByName(MCAST_ADDR);
        } catch (UnknownHostException e) {
            //throw new RuntimeException(e);
            System.out.println("Error creating multicast adress");
        }
    }
    public static int portCarsTowersLinux = 6000;

    /**
     * Map Constants, in Windows?
     */
    public static int minXmap = 0;
    public static int maxXmap = 100;
    public static int minYmap = 0;
    public static int maxYmap = 100;


    /**
     * Tower
     */
    public static int towerCommunicationRadius = 100;
    // When in linux, port of tower:
    public static int towerPort = 7000;

    /**
     * Car
     */
    public static int carPort = 6000;
}
