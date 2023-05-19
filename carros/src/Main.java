import java.io.IOException;
import java.util.Arrays;

import Common.Constants;


public class Main {

	/**
	 * Generate executable for all applications to test in linux
	 *
	 * First argument -> 0 -> Car
	 * Car arguments (file): "src/Car/TowersPosWindows"
	 * First argument -> 0 -> Car (Ambulance
	 * Car arguments (file): "src/Car/TowersPosWindows" file_amb
	 *
	 * First argument -> 1 -> Tower
	 * Tower arguments: t1 IP_Server
	 *
	 * First argument -> 2 -> Server
	 * Server arguments:
	 *
	 * First argument -> 3 -> Cloud
	 * Cloud arguments:
	 *
	 *
	 * @param args
	 */
	public static void main(String[] args) throws IOException {

		System.out.println("Local IP: " + Constants.getMyIp());
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
		if (Integer.parseInt(args[0]) == 2){
			System.out.println("Execute Server");

			Server.Main.main(restArguments);
			return;
		}
		if (Integer.parseInt(args[0]) == 3){
			System.out.println("Execute Cloud");

			Cloud.Main.main(restArguments);
			return;
		}
	}
}