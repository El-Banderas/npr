package Car;

import Common.Constants;
import Common.Messages.SendMessages;
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
				// Depois meter um if aqui para que no linux não atualize a posição
				info.pos.getPosition();
				System.out.println("Posição atual: " + info.pos.x + " | " + info.pos.y);
				checkPossibleCommunication();
				Thread.sleep(Constants.refreshRate);
			}
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	private void checkPossibleCommunication() {
		System.out.println("Check Possible communication");
		for (TowerInfo tower : towers){
			if (Position.distance(info.pos, tower.pos) < Constants.towerCommunicationRadius){
				SendMessages.carHelloTower(info.sendInfo, tower.connectionInfoWindowsReceive);
				System.out.println("Send message");
			}
		}
	}
}
