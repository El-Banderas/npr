package Car.Terminal;

import Car.SharedClass;

import java.util.*;

public class CarTerminal implements Runnable {


    private TreeMap<Integer, Option> options;
    private SharedClass shared;

    public CarTerminal(SharedClass shared) {
        this.shared = shared;
    }

    @Override
    public void run() {

        this.options = new TreeMap();
        options.put(TerminalConstants.BreakOption, new BreakOption());
        Scanner scan = new Scanner(System.in);
        while(true) {
            System.out.println("Introduza uma das opções");
            for (Map.Entry<Integer, Option> entry : options.entrySet()) {
                System.out.println("(" + entry.getKey() + ") " + entry.getValue().text);
            }
            int option = scan.nextInt();
            handleOption(option);
        }
    }

    private void handleOption(int option) {
        options.get(option).action(shared.info.sendSocket());
    }
}
