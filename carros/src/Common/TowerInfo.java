package Common;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class TowerInfo
{
	private String name;
	private Position pos;
	private float max_speed;


	/**
	 * @param name
	 * @param pos
	 */
	public TowerInfo(String name, Position pos)
	{
		this.name = name;
		this.pos = pos;
		this.max_speed = 120;
	}
	
	public TowerInfo(String name, Position pos, float max_speed)
	{
		this.name = name;
		this.pos = pos;
		this.max_speed = max_speed;
	}
	public static TowerInfo getNearestTower(Position pos, List<TowerInfo> towers){
		double minDist = Double.MAX_VALUE;
		TowerInfo closestTower = null;
		for (TowerInfo oneTower : towers){
			double thisDistance = Position.distance(pos, oneTower.getPosition());
			if (thisDistance < minDist) {
				minDist = thisDistance;
				closestTower = oneTower;
			}
		}
		return closestTower;
	}
	public static TowerInfoWithIP getNearestTowerPosition(Position posToLook, Set<TowerInfoWithIP> towers){

		return (TowerInfoWithIP) getNearestTower(posToLook, new ArrayList<>(towers));
	}
	public String getName() {return this.name;}
	public Position getPosition() {return this.pos;}
	public float getMaxSpeed() {return this.max_speed;}
	public void setMaxSpeed(float max_speed) {this.max_speed = max_speed;}

	public void setPos(Position pos) {
		this.pos = pos;
	}

	@Override
	public String toString()
	{
		return new String(
				"{"
			+	"name=" + name
			+	", pos=" + pos.toString()
			+	", max_speed=" + max_speed
			+	"}"
			);
	}
}
