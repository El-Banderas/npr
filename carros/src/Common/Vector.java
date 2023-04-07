package Common;


public class Vector
{
	public float x;
	public float y;


	public Vector(float x, float y)
	{
		this.x = x;
		this.y = y;
	}
	
	public Vector(double x, double y)
	{
		this.x = (float) x;
		this.y = (float) y;
	}

	public Vector(Position p)
	{
		this(p.x, p.y);
	}

	public Vector(Position p1, Position p2)
	{
		this(p2.x - p1.x, p2.y - p1.y);
	}

	public double length() {
		return Math.sqrt(Math.pow(x, 2) +  Math.pow(y, 2));
	}

	public static Vector normalize(Vector v) {
		double length = v.length();
		return new Vector(v.x / length, v.y / length);
	}
	
	public static Vector sum(Vector v1, Vector v2)
	{
		return new Vector(v1.x + v2.x, v1.y + v2.y);
	}
	
	public static Vector sub(Vector v1, Vector v2)
	{
		return new Vector(v1.x - v2.x, v1.y - v2.y);
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
