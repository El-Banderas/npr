import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

public class Main {

	/**
	 * Generate executable for all applications to test in linux
	 * First argument -> 0 -> Car
	 * Car arguments (file): "src/Car/TowersPosWindows"
	 * First argument -> 1 -> Tower
	 * Tower arguments: t1 40 40
	 * @param args
	 */

	public static void main(String[] args) throws IOException {
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