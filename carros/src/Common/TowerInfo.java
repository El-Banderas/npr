package Common;

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


	public String getName() {return this.name;}
	public Position getPosition() {return this.pos;}
	public float getMaxSpeed() {return this.max_speed;}
	public void setMaxSpeed(float max_speed) {this.max_speed = max_speed;}
	
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
