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
        this.options = new TreeMap<Integer, Option>();
        options.put(TerminalConstants.BreakOption, new BreakOption());
        options.put(TerminalConstants.Refresh, new RefreshOption());
        Scanner scan = new Scanner(System.in);
        while(true) {
            System.out.println("################");
            shared.printMessagesInfo();
            System.out.println("Introduza uma das opções");
            for (Map.Entry<Integer, Option> entry : options.entrySet()) {
                System.out.println("(" + entry.getKey() + ") " + entry.getValue().text);
            }
            System.out.println("################");

            int option = scan.nextInt();
            handleOption(option);
            
            if(option < 0) break; //só para tirar o warning
        }
        scan.close();
    }

    private void handleOption(int option) {
        if (option == TerminalConstants.Refresh) return;
        options.get(option).action(shared.info.sendSocket());
    }
}
