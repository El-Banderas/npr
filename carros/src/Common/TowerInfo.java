package Common;

public class TowerInfo
{
	private String name;
	public Position pos;


	/**
	 * @param name
	 * @param pos
	 */
	public TowerInfo(String name, Position pos)
	{
		this.name = name;
		this.pos = pos;
	}


	public String getName(){return name;}
}
