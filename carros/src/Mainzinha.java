import java.net.InetAddress;

public class Mainzinha {

    public static void main(String args[]) throws Exception {

        InetAddress ca = InetAddress.getByName("127.0.0.10");
        InetAddress com = InetAddress.getByName("127.0.0.10");
        if (ca.equals(com)) {
            System.out.println("same");
        } else {
            System.out.println("not the same");
        }
    }

}