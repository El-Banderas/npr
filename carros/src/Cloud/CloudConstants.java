package Cloud;


import java.net.InetAddress;
import java.net.UnknownHostException;

public class CloudConstants {

	// Port in windows and linux.
	public static int port = 5000;
	public static InetAddress ip;

	static {
		try {
			ip = InetAddress.getByName("2001:8::10");
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
	}
}
