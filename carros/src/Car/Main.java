package Car;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Common.CarInfo;
import Common.Position;
import Common.TowerInfo;


/**
 * 	0: File path that stores the information about towers
 * 	Example: "src/Car/TowersPosWindows" "Path_Ambulance"
 */
public class Main
{
	private static String getNameNode(String workingDirectory){
		Pattern pattern = Pattern.compile("\\/\\w+\\/\\w+.\\d+\\w\\/(\\w\\d+).conf");
			Matcher matcher = pattern.matcher(workingDirectory);
			if (matcher.find()) {
				return matcher.group(1);
			}
			return "None";
	}
	public static void main(String[] args)
	{
		System.out.println(" ----  > Execute car");
		String id = idGenerator(8);
		System.out.println("Id: " + id);

		AmbulanceInfo ambulanceInfo = null;
		// It it is a car, we pass anothre argument, the path that it will use.
		if (args.length > 1) {
			System.out.println("[CAR] Is an ambulance");
			ambulanceInfo = parseFileAmbulances(args[1]);

		}

		System.out.println("Working Directory = " + System.getProperty("user.dir"));
		Position pos = new Position();
		System.out.println("Node Coordinates = " + pos.x + " " + pos.y);

		CarInfo info = null;
		Car carMove = null;
		// The name of the car is used in terminal, and stored in CarInfo
		String carName = getNameNode(System.getProperty("user.dir"));
		List <CarAction> actions = new ArrayList<>();
		List<TowerInfo> towers = parseFileConfigs(args[0], carName, actions);
		try {
			info = new CarInfo(id, pos, carName);
			if (ambulanceInfo != null)
				carMove = new Car(info, towers, ambulanceInfo);
			else
				carMove = new Car(info, towers);

		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		Thread thread_1 = new Thread(carMove);
		thread_1.start();
	}

	// Parse file that contains information about the RSUs
	private static List<TowerInfo> parseFileConfigs(String filePath, String carName, List<CarAction> actions) {
		List<TowerInfo> res = new ArrayList<>();
		try {
			Scanner scanner = new Scanner(new File(filePath));
			scanner.nextLine(); // Ignore first line, header

			// Parse towers positions
			Pattern pattern = Pattern.compile("(\\w+);(\\d+),(\\d+);");

			while (scanner.hasNextLine()) {
				String fileLine = scanner.nextLine();
				Matcher matcher = pattern.matcher(fileLine);
				if (matcher.find()) {
					String name = matcher.group(1);
					Position pos = new Position(Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)));
					res.add(new TowerInfo(name, pos));
				} else
					break;
			}

			scanner.nextLine(); // Ignore first line, header
			// Parse towers positions
			Pattern patternActions = Pattern.compile("(\\w+);(\\d+),(\\d+);(\\w+);");

			while (scanner.hasNextLine()) {
				String fileLine = scanner.nextLine();
				Matcher matcher = patternActions.matcher(fileLine);
				if (matcher.find()) {
					String node = matcher.group(1);
					System.out.println("1");
					if (node.equals(carName)) {
						System.out.println("Add action: " + matcher.group(4));
						Position pos = new Position(Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)));
						CarAction read = new CarAction(pos, carName, matcher.group(4));
						actions.add(read);
						System.out.println("Adicionou ação");
					} else {
						System.out.println("Ignore action: " + matcher.group(1));

					}
				}
				else {
					System.out.println("Invalid line");
				}


				scanner.close();
				return res;
			}
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	// Parse file that contains information about the RSUs
	private static AmbulanceInfo parseFileAmbulances(String filePath)
	{
		Map<Integer, Position> positions = new HashMap<>();
		try {
			Scanner scanner = new Scanner(new File(filePath));
			scanner.nextLine(); // Ignore first line, header

			Pattern pattern = Pattern.compile("(\\d+);(\\d+),(\\d+);");

			while (scanner.hasNextLine()) {
				String fileLine = scanner.nextLine();
				Matcher matcher = pattern.matcher(fileLine);
				if (matcher.find()) {
					int time = Integer.parseInt(matcher.group(1));
					Position pos = new Position(Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)));
					positions.put(time, pos);
				} else
					System.out.println("Invalid line in tower info file");
			}

			scanner.close();
			return new AmbulanceInfo(positions);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return null;
	}

	public static String idGenerator(int n)
	{
		String alphaNumeric = "0123456789" + "abcdefghijklmnopqrstuvxyz";

		StringBuilder sb = new StringBuilder(n);

		for (int i = 0; i < n; i++) {
			int index = (int)(alphaNumeric.length() * Math.random());
			sb.append(alphaNumeric.charAt(index));
		}

		return sb.toString();
	}
}
