package Car;

import Common.Constants;
import Common.Position;
import Common.TowerInfo;

import java.util.List;

public class CarMove {
    private CarInfo info;
    private  List<TowerInfo> towers;

    public CarMove(CarInfo info, List<TowerInfo> towers) {
        this.info = info;
        this.towers = towers;
    }
    public void run(){
        try {

        while(true){
            info.pos.getPosition();
            System.out.println("Posição atual: " + info.pos.x + " | " + info.pos.y);
            checkPossibleCommunication();
            Thread.sleep(100);
        }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    private void checkPossibleCommunication() {
        for (TowerInfo tower : towers){
            if (Position.distance(info.pos, tower.pos) < Constants.towerCommunicationRadius){
                System.out.println("Posso comunicar!!!");
            }
        }
    }
}
