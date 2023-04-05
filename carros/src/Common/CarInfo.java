package Common;

import java.io.IOException;


public class CarInfo
{
	private String id;
	
	//timestamps in positions
	private Position pos_1; // least recent
	private Position pos_2;
	private Position pos_3; // most recent
	
	private Vector velocity_1; // pos2 - pos1
	private Vector velocity_2; // pos3 - pos2
	
	private Vector acceleration; //velocity2 - velocity1


	public CarInfo(String id, Position pos) throws IOException
	{
		this.id = id;
		
		this.pos_1 = new Position(pos);
		this.pos_2 = new Position(pos);
		this.pos_3 = new Position(pos);
		
		this.velocity_1 = new Vector(0,0);
		this.velocity_2 = new Vector(0,0);
		
		this.acceleration = new Vector(0,0);
	}

	public String getID() {return new String(this.id);}
	public Position getPosition() {return new Position(this.pos_3);}
	public float getSpeed() {return (float) (this.velocity_2.length() / (pos_3.timestamp - pos_2.timestamp));}
	public float getAccelerationRate() {return (float) (this.acceleration.length() / (pos_3.timestamp - pos_1.timestamp));}
	public Vector getDirection() {return Vector.normalize(velocity_2);}
	
	public void update() {
		this.pos_1 = this.pos_2;
		this.pos_2 = this.pos_3;
		this.pos_3 = new Position();
		
		this.velocity_1 = this.velocity_2;
		this.velocity_2 = new Vector(pos_2, pos_3);
		
		this.acceleration = Vector.sub(velocity_2, velocity_1);
	}
	
	public void update(Position newPos) {
		this.pos_1 = this.pos_2;
		this.pos_2 = this.pos_3;
		this.pos_3 = new Position(newPos);
		
		this.velocity_1 = this.velocity_2;
		this.velocity_2 = new Vector(pos_2, pos_3);
		
		this.acceleration = Vector.sub(velocity_2, velocity_1);
	}
	
	public String toString()
	{
		return new String(
				"{"
			+	" ; id: " + this.id
			+	" ; pos: " + this.getPosition().toString()
			+	" ; speed: " + this.getSpeed()
			+	" ; accel: " + this.getAccelerationRate()
			+	" ; dir: " + this.getDirection().toString()
			+	"}"
			);
	}
}
