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

	public static void main(String[] args)
	{
		// Information about towers
		List<TowerInfo> towers = parseFile(args[0]);
		System.out.println(towers);
		
		String id = idGenerator(8);
		System.out.println("Id: " + id);
		
		Position pos;
		CarInfo info;
		
		if (Constants.core) {
			System.out.println("Working Directory = " + System.getProperty("user.dir"));
			pos = new Position();
			System.out.println("Node Coordinates = " + pos.x + " " + pos.y);
			info = new CarInfo(pos, id);
		} else {
			pos = new PositionCarWindows(0,0);
			InfoNodeWindows infoCarConnection = new InfoNodeWindows();
			info = new CarInfo(pos, infoCarConnection, id);
		}
		
		// 2 Threads: Terminal and Comms
		SharedClass shared = new SharedClass(info);
		
		CarTerminal carTerminal = new CarTerminal(shared);
		Thread thread = new Thread(carTerminal);
		thread.start();
		
		CarMove carMove = new CarMove(info, towers, shared);
		carMove.run();
	}
	
	// Parse file that contains information about the RSUs
	private static List<TowerInfo> parseFile(String filePath)
	{
		List<TowerInfo> res = new ArrayList<>();
		try {
			Scanner scanner = new Scanner(new File(filePath));
			scanner.nextLine(); // Ignore first line, header
			
			Pattern pattern;
			// Linux contains IPs; Windows contains Ports
			if (Constants.core)
				pattern = Pattern.compile("(\\w+);(\\d+),(\\d+);");
			else
				pattern = Pattern.compile("(\\w+);(\\d+);(\\d+),(\\d+);");
			
			while (scanner.hasNextLine()) {
				String fileLine = scanner.nextLine();
				Matcher matcher = pattern.matcher(fileLine);
				if (matcher.find()) {
					String name = matcher.group(1);
					
					InfoNode infoNode;
					Position pos;
					if (Constants.core) {
						infoNode = new InfoNode(Constants.MulticastGroup, Constants.towerPort, false);
						pos = new Position(Integer.parseInt(matcher.group(2)), Integer.parseInt(matcher.group(3)));
					} else {
						int port = Integer.parseInt(matcher.group(2));
						infoNode = new InfoNodeWindows(port, false);
						pos = new Position(Integer.parseInt(matcher.group(3)), Integer.parseInt(matcher.group(4)));
					}
					
					res.add(new TowerInfo(name, infoNode, pos));
				} else
					System.out.println("Invalid line in tower info file");
			}
			
			scanner.close();
			return res;
		} catch (FileNotFoundException e) {
			System.out.println("[ERROR] File not found!");
		} catch (UnknownHostException e) {
			System.out.println("[ERROR] Invalid IP");
		}
		return null;
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
