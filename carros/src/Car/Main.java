package Car;

import Car.Terminal.CarTerminal;
import Common.*;

/*
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
*/


/**
 * Windows: File path that stores the information about towers
 * Example: "src/Car/TowersPosWindows"
 * Linux parece que é igual...
 *
 */
public class Main {

	public static void main(String[] args)
	{
		String id = idGenerator(8);
		System.out.println("Id: " + id);
		
		//List<TowerInfo> towers = parseFile(args[0]);
		
		System.out.println("Working Directory = " + System.getProperty("user.dir"));
		Position pos = new Position();
		System.out.println("Node Coordinates = " + pos.x + " " + pos.y);
		CarInfo info = new CarInfo(pos, id);
		
		// 2 Threads: Terminal and Comms
		SharedClass shared = new SharedClass(info);
		
		CarTerminal carTerminal = new CarTerminal(shared);
		Thread thread_1 = new Thread(carTerminal);
		thread_1.start();
		
		Car carMove = new Car(info, shared/*, towers*/);
		Thread thread_2 = new Thread(carMove);
		thread_2.start();
	}
	
	/*
	// Parse file that contains information about the RSUs
	private static List<TowerInfo> parseFile(String filePath)
	{
		List<TowerInfo> res = new ArrayList<>();
		try {
			Scanner scanner = new Scanner(new File(filePath));
			scanner.nextLine(); // Ignore first line, header
			
			Pattern pattern;
			// Linux contains IPs; Windows contains Ports
			pattern = Pattern.compile("(\\w+);(\\d+),(\\d+);");
			
			while (scanner.hasNextLine()) {
				String fileLine = scanner.nextLine();
				Matcher matcher = pattern.matcher(fileLine);
				if (matcher.find()) {
					String name = matcher.group(1);
					Position pos = new Position(Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)));
					res.add(new TowerInfo(name, pos));
				} else
					System.out.println("Invalid line in tower info file");
			}
			
			scanner.close();
			return res;
		} catch (FileNotFoundException e) {
			System.out.println("[ERROR] File not found!");
		}
		return null;
	}
	*/
	
	
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
