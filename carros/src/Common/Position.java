package Common;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Position
{
	public int x;
	public int y;
	public long timestamp;


	public Position(int x, int y)
	{
		this.x = x;
		this.y = y;
		this.timestamp = System.nanoTime();
	}

	public Position()
	{
		this.updatePosition();
	}


	public void updatePosition()
	{
		// TODO: Pode ser feito só na inicialização

		// Read node name
		Pattern p = Pattern.compile("(\\/tmp\\/pycore\\.\\d+\\/)(\\w+)\\.conf");
		Matcher m = p.matcher(System.getProperty("user.dir"));
		m.find();
		String parent_dir = m.group(1);
		String node_name = m.group(2);

		// Read xy contents
		String xy = "0.0 0.0";
		try {
			Scanner scanner = new Scanner(new File(parent_dir + node_name + ".xy"));
			xy = scanner.nextLine();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		Pattern p1 = Pattern.compile("(\\d+)\\.\\d+ (\\d+)\\.\\d+");
		Matcher m1 = p1.matcher(xy);
		m1.find();

		this.x = Integer.parseInt(m1.group(1));
		this.y = Integer.parseInt(m1.group(2));
		this.timestamp = System.nanoTime();
	}

	public static double distance(Position p1, Position p2)
	{
		return Math.sqrt(Math.pow(p2.x-p1.x, 2) +  Math.pow(p2.y-p1.y, 2));
	}
	
	public String toString()
	{
		return new String(
				"{"
			+	" ; x: " + this.x
			+	" ; y: " + this.y
			+	"}"
			);
	}
}
