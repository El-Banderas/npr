package Car;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Common.CarInfo;
import Common.Position;
import Common.TowerInfo;


/**
 * 	0: File path that stores the information about towers
 * 	Example: "src/Car/TowersPosWindows"
 */
public class Main
{
	public static void main(String[] args)
	{
		System.out.println(" ----  > Execute car");
		String id = idGenerator(8);
		System.out.println("Id: " + id);
		
		List<TowerInfo> towers = parseFile(args[0]);
		
		System.out.println("Working Directory = " + System.getProperty("user.dir"));
		Position pos = new Position();
		System.out.println("Node Coordinates = " + pos.x + " " + pos.y);
		
		CarInfo info = null;
		Car carMove = null;
		try {
			info = new CarInfo(id, pos);
			carMove = new Car(info, towers);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		Thread thread_1 = new Thread(carMove);
		thread_1.start();
	}
	
	// Parse file that contains information about the RSUs
	private static List<TowerInfo> parseFile(String filePath)
	{
		List<TowerInfo> res = new ArrayList<>();
		try {
			Scanner scanner = new Scanner(new File(filePath));
			scanner.nextLine(); // Ignore first line, header
			
			Pattern pattern = Pattern.compile("(\\w+);(\\d+),(\\d+);");
			
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
			e.printStackTrace();
			System.exit(-1);
		}
		return null;
	}
	
	private static String idGenerator(int n)
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
