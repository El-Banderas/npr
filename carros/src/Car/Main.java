package Car;

import Common.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.InetAddress;
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
			if (Constants.linux)
				pattern = Pattern.compile("(\\w+);((\\d+.){3}\\d+);(\\d+),(\\d+);");
			else
				pattern = Pattern.compile("(\\w+);(\\d+.);(\\d+),(\\d+);");

			while (scanner.hasNextLine()) {
				String fileLine = scanner.nextLine();
				System.out.println("Linha do ficheiro:");
				System.out.println(fileLine);
				Matcher matcher = pattern.matcher(fileLine);
				if (matcher.find()) {
					// Tower name
					String name = matcher.group(1);

					InfoNode infoNode;
					// Position must be separated beacuse ip occupies 2 groups
					Position pos;
					// If is in linux, receives an IP
					if (Constants.linux) {
						InetAddress ip = InetAddress.getByName(matcher.group(2));
						infoNode = new InfoNode(ip, Constants.towerPort, false);
						pos = new Position(Integer.parseInt(matcher.group(4)), Integer.parseInt(matcher.group(5)));
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

		System.out.println("Is linux?: " + Constants.linux);
		// Information about towers
		List<TowerInfo> towers = parseFile(args[0]);
		System.out.println(towers);
		Position pos;
		CarInfo info;
		if (!Constants.linux) {
			pos = new PositionCarWindows(0,0);
			InfoNodeWindows infoCarConnection = new InfoNodeWindows();
			info = new CarInfo(pos, infoCarConnection);
		}
		else {
			// Depois alterar para usar a forma como se deteta posição no CORE
			pos = new PositionCarWindows(0,0);
			InfoNode infoCarConnection = new InfoNode(Constants.carPort);
			info = new CarInfo(pos, infoCarConnection);
		}
		// Depois separar em 2 threads: comunicações e terminal
		CarMove carMove = new CarMove(info, towers);
		carMove.run();
	}
}
