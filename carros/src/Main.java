import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;

import static java.lang.System.out;
public class Main {

	/**
	 * Generate executable for all applications to test in linux
	 * First argument -> 0 -> Car
	 * Car arguments (file): "src/Car/TowersPosWindows"
	 * First argument -> 1 -> Tower
	 * Tower arguments: t1 40 40
	 * @param args
	 */
	static void displayInterfaceInformation(NetworkInterface netint) throws SocketException {
		out.printf("Display name: %s\n", netint.getDisplayName());
		out.printf("Name: %s\n", netint.getName());
		Enumeration<InetAddress> inetAddresses = netint.getInetAddresses();

		for (InetAddress inetAddress : Collections.list(inetAddresses)) {
			out.printf("InetAddress: %s\n", inetAddress);
		}
	}
	public static void main(String[] args) throws IOException {
		Enumeration<NetworkInterface> nets = NetworkInterface.getNetworkInterfaces();
		for (NetworkInterface netint : Collections.list(nets))
			displayInterfaceInformation(netint);
		//System.out.println("Local IP: " + Inet6Address.getLocalHost());
		String[] restArguments = Arrays.copyOfRange(args, 1, args.length);
		if (Integer.parseInt(args[0]) == 0){
			System.out.println("Execute car");
			Car.Main.main(restArguments);
			return;
		}
		if (Integer.parseInt(args[0]) == 1){
			System.out.println("Execute Tower");

			Tower.Main.main(restArguments);
			return;
		}
	}
}