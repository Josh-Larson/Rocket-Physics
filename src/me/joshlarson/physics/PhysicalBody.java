package me.joshlarson.physics;

import me.joshlarson.graphics.utils.Point3f;

public class PhysicalBody {
	
	public static final double G = 6.67385E-11; // m^3 / (kg * s^2)
	private String name;
	private double mass; // kg
	private Tuple3d position; // km
	private Tuple3d velocity; // km / s
	private Tuple3d acceleration; // km / s / s
	private Tuple3d forces; // N
	
	public PhysicalBody(String name, double mass) {
		this.name = name;
		this.mass = mass;
		position = new Tuple3d();
		velocity = new Tuple3d();
		acceleration = new Tuple3d();
		forces = new Tuple3d();
	}
	
	public final void clearForces() {
		forces.set(0, 0, 0);
	}
	
	public final void calculateGravity(PhysicalBody body, double gravity) {
		Tuple3d force = Tuple3d.sub(body.position, position);
		double distance = force.magnitude();
		force.setX(Math.sin(force.getX() / distance));
		force.setY(Math.sin(force.getY() / distance));
		force.setZ(Math.sin(force.getZ() / distance));
		force.scale(gravity);
		addForce(force);
	}
	
	protected void addForce(Tuple3d force) {
		forces.add(force);
	}
	
	public void updateAll(PhysicsEngine engine, final double delta) {
		// km * kg / s^2
		// km / s^2
		final double deltaSq = delta * delta;
		acceleration.set(forces);
		acceleration.scale(1 / mass); // a = F / m
		velocity.add(acceleration.getX()*delta, acceleration.getY()*delta, acceleration.getZ()*delta);
		position.addX(velocity.getX()*delta + .5 * acceleration.getX() * deltaSq);
		position.addY(velocity.getY()*delta + .5 * acceleration.getY() * deltaSq);
		position.addZ(velocity.getZ()*delta + .5 * acceleration.getZ() * deltaSq);
		clearForces();
	}
	
	public void render(double delta) {
		System.err.println("Render method is empty. This should be overridden!");
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public double getMass() {
		return mass;
	}
	
	public void addMass(double mass) {
		this.mass += mass;
	}
	
	public void removeMass(double mass) {
		this.mass -= mass;
	}
	
	public void setMass(double mass) {
		this.mass = mass;
	}
	
	public Point3f getPosition() {
		return new Point3f((float)position.getX(), (float)position.getY(), (float)position.getZ());
	}
	
	public void setAcceleration(double x, double y, double z) {
		acceleration.set(x, y, z);
	}
	
	public void setAcceleration(Tuple3d t) {
		acceleration = t;
	}
	
	public void setVelocity(double x, double y, double z) {
		velocity.set(x, y, z);
	}
	
	public void setVelocity(Tuple3d t) {
		velocity = t;
	}
	
	public void setPosition(double x, double y, double z) {
		position.set(x, y, z);
	}
	
	public void setPosition(Tuple3d t) {
		position = t;
	}
	
	public double getX() {
		return position.getX();
	}
	
	public double getY() {
		return position.getY();
	}
	
	public double getZ() {
		return position.getZ();
	}
	
	public double getVelocityX() {
		return velocity.getX();
	}
	
	public double getVelocityY() {
		return velocity.getY();
	}
	
	public double getVelocityZ() {
		return velocity.getZ();
	}
	
	public double getAccelerationX() {
		return acceleration.getX();
	}
	
	public double getAccelerationY() {
		return acceleration.getY();
	}
	
	public double getAccelerationZ() {
		return acceleration.getZ();
	}
	
	public static final double calculateGravity(PhysicalBody body1, PhysicalBody body2) {
		Tuple3d distance = Tuple3d.sub(body1.position, body2.position);
		// m^3 / (kg * s^2)
		// m^3 * kg / s^2
		// m^9 * kg / s^2
		// km^3 * kg / s^2
		// km * kg / s^2
		return G * body1.mass * body2.mass / distance.magnitudeSquared() / 1E9;
	}
	
}
