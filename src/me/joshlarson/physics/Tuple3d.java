package me.joshlarson.physics;

public class Tuple3d {
	
	private double x;
	private double y;
	private double z;
	
	public Tuple3d() {
		this(0, 0, 0);
	}
	
	public Tuple3d(Tuple3d t) {
		this(t.getX(), t.getY(), t.getZ());
	}
	
	public Tuple3d(double [] t) {
		set(t);
	}
	
	public Tuple3d(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void setX(double x) {
		this.x = x;
	}
	
	public void setY(double y) {
		this.y = y;
	}
	
	public void setZ(double z) {
		this.z = z;
	}
	
	public void set(double x, double y, double z) {
		setX(x);
		setY(y);
		setZ(z);
	}
	
	public void set(Tuple3d t) {
		set(t.x, t.y, t.z);
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public double getZ() {
		return z;
	}

	public double magnitude() {
		return Math.sqrt(magnitudeSquared());
	}
	
	public double magnitudeSquared() {
		return x*x + y*y + z*z;
	}
	
	public void get(double [] t) {
		if (t.length < 3)
			return;
		t[0] = getX();
		t[1] = getY();
		t[2] = getZ();
	}
	
	public void absolute() {
		x = Math.abs(x);
		y = Math.abs(y);
		z = Math.abs(z);
	}
	
	public void add(Tuple3d t) {
		add(t.getX(), t.getY(), t.getZ());
	}
	
	public void add(double x, double y, double z) {
		addX(x);
		addY(y);
		addZ(z);
	}
	
	public void addX(double x) {
		this.x += x;
	}
	
	public void addY(double y) {
		this.y += y;
	}
	
	public void addZ(double z) {
		this.z += z;
	}
	
	public void sub(Tuple3d t) {
		sub(t.getX(), t.getY(), t.getZ());
	}
	
	public void sub(double x, double y, double z) {
		subX(x);
		subY(y);
		subZ(z);
	}
	
	public void subX(double x) {
		this.x -= x;
	}
	
	public void subY(double y) {
		this.y -= y;
	}
	
	public void subZ(double z) {
		this.z -= z;
	}
	
	public void clamp(double min, double max) {
		clampMin(min);
		clampMax(max);
	}
	
	public void clampMin(double min) {
		if (x < min)
			x = min;
		if (y < min)
			y = min;
		if (z < min)
			z = min;
	}
	
	public void clampMax(double max) {
		if (x > max)
			x = max;
		if (y > max)
			y = max;
		if (z > max)
			z = max;
	}
	
	public void interpolate(Tuple3d t) {
		interpolate(t, 0.5);
	}
	
	public void interpolate(Tuple3d t, double alpha) {
		x = (1 - alpha) * getX() + alpha * t.getX();
		y = (1 - alpha) * getY() + alpha * t.getY();
		z = (1 - alpha) * getZ() + alpha * t.getZ();
	}
	
	public void negate() {
		x = -x;
		y = -y;
		z = -z;
	}
	
	public void scale(double s) {
		x *= s;
		y *= s;
		z *= s;
	}
	
	public void set(double [] t) {
		if (t.length < 3)
			throw new ArrayIndexOutOfBoundsException("Invalid array size of " + t.length + " should be 3 or greater");
		x = t[0];
		y = t[1];
		z = t[2];
	}
	
	public static final Tuple3d add(Tuple3d t1, Tuple3d t2) {
		Tuple3d t = new Tuple3d(t1);
		t.add(t2);
		return t;
	}
	
	public static final Tuple3d sub(Tuple3d t1, Tuple3d t2) {
		Tuple3d t = new Tuple3d(t1);
		t.sub(t2);
		return t;
	}
	
}
