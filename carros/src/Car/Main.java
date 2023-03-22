package Car;

import Car.Terminal.CarTerminal;
import Common.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Windows: File path that stores the information about towers
 * Example: "src/Car/TowersPosWindows"
 * Linux parece que é igual...
 *
 */
public class Main {
	
	// Parse file that contains information about the RSU's
	public static List<TowerInfo> parseFile(String filePath){
		try {
			List<TowerInfo> res = new ArrayList<>();
			Scanner scanner = new Scanner(new File(filePath));
			// Ignore first line, header
			scanner.nextLine();
			Pattern pattern;
			// Linux contains IPS; Windows contains PORTS

			if (Constants.core)
				pattern = Pattern.compile("(\\w+);(\\d+),(\\d+);");
			else
				pattern = Pattern.compile("(\\w+);(\\d+);(\\d+),(\\d+);");
			while (scanner.hasNextLine()) {
				String fileLine = scanner.nextLine();
				System.out.println(fileLine);
				Matcher matcher = pattern.matcher(fileLine);
				if (matcher.find()) {
					// Tower name
					String name = matcher.group(1);

					InfoNode infoNode;
					// Position must be separated beacuse ip occupies 2 groups
					Position pos = null;
					// If is in linux, receives an IP
					if (Constants.core) {
						infoNode = new InfoNode(Constants.MulticastGroup, Constants.towerPort, false);
						pos = new Position(Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)));
					}
					else // If is in windows, receives a port, because IP it's localhost
					{
						int port = Integer.parseInt(matcher.group(2));
						infoNode = new InfoNodeWindows(port, false);
						pos = new Position(Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4)));
					}

					res.add(new TowerInfo(name, infoNode, pos));
				}else
					System.out.println("Invalid line in tower info file");
			}

			scanner.close();
			return res;
		} catch (FileNotFoundException e) {
			System.out.println("[EXPETION] File not found!");
			//e.printStackTrace();
		} catch (UnknownHostException e) {
			System.out.println("[EXPETION] Invalid IP");

			//throw new RuntimeException(e);
		}
		return null;
	}

	public static void main(String[] args) {

		System.out.println("[CAR] Is core?: " + Constants.core);
		
		// Information about towers
		List<TowerInfo> towers = parseFile(args[0]);
		System.out.println(towers);
		String id = idGenerator(8);
		System.out.println("Id: " + id);
		Position pos;
		CarInfo info;
		
		if (!Constants.core) {
			pos = new PositionCarWindows(0,0);
			InfoNodeWindows infoCarConnection = new InfoNodeWindows();
			info = new CarInfo(pos, infoCarConnection, id);
		}
		else {
			System.out.println("Working Directory = " + System.getProperty("user.dir"));
			pos = new Position();
			System.out.println("Node Coordinates = " + pos.x + " " + pos.y);
			
			info = new CarInfo(pos, id);
		}
		
		// Depois separar em 2 threads: comunicações e terminal
		SharedClass shared = new SharedClass(info);
		
		CarTerminal carTerminal = new CarTerminal(shared);
		Thread thread = new Thread(carTerminal);
		thread.start();
		
		CarMove carMove = new CarMove(info, towers, shared);
		carMove.run();
	}


	private static String idGenerator(int n) {
		String alphaNumeric = "0123456789" + "abcdefghijklmnopqrstuvxyz";

		StringBuilder sb = new StringBuilder(n);
		
		for (int i = 0; i < n; i++) {
			int index = (int)(alphaNumeric.length() * Math.random());
			sb.append(alphaNumeric.charAt(index));
		}

		return sb.toString();
	}
}
