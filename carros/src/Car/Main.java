package Car;

import Common.Constants;
import Common.Position;
import Common.PositionCarWindows;
import Common.TowerInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static List<TowerInfo> parseFile(String filePath){
        try {
            List<TowerInfo> res = new ArrayList<>();
            Scanner scanner = new Scanner(new File(filePath));
            // Ignore first line
            scanner.nextLine();

            Pattern pattern = Pattern.compile("(\\w+);((\\d+.){3}\\d+);(\\d+),(\\d+);");
            //Pattern pattern = Pattern.compile("(.*)");
            while (scanner.hasNextLine()) {
                String fileLine = scanner.nextLine();
                System.out.println(fileLine);
                Matcher matcher = pattern.matcher(fileLine);
                if (matcher.find()) {
                    String name = matcher.group(1);

                    InetAddress ip = InetAddress.getByName(matcher.group(2));

                    Position pos = new Position(Integer.parseInt(matcher.group(4)), Integer.parseInt(matcher.group(5)));

                    res.add(new TowerInfo(name, ip, pos));
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
        if (!Constants.linux) {
           // System.out.println(args[0]);

            List<TowerInfo> towers = parseFile(args[0]);
            System.out.println(towers);
            Position pos = new PositionCarWindows(0,0);
            CarInfo info = new CarInfo(pos);
            // Depois separar em 2 threads: comunicações e terminal
            CarMove carMove = new CarMove(info, towers);
            carMove.run();
        }
    }
}
