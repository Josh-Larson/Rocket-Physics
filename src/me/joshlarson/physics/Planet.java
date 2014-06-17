package me.joshlarson.physics;

import me.joshlarson.graphics.World;
import me.joshlarson.graphics.drawing.Sphere;

public class Planet extends PhysicalBody {
	
	private Sphere sphere;
	private double radius;
	private double rotationRate;
	private double tilt;
	
	public Planet(World w, String name, double radius, double mass) {
		super(name, mass);
		this.radius = radius;
		this.rotationRate = 0;
		this.tilt = 0;
		sphere = new Sphere(w, (float)radius, 1f);
	}
	
	/**
	 * @return a floating-point value that will rotate this much every second
	 */
	public double getRotationRate() {
		return rotationRate;
	}
	
	public double getRotation() {
		return sphere.getZRot();
	}
	
	public void setRotationRate(double rotationRate) {
		this.rotationRate = rotationRate;
	}
	
	public double getTilt() {
		return tilt;
	}
	
	public void setTilt(double tilt) {
		this.tilt = tilt;
	}
	
	public double getRadius() {
		return radius;
	}
	
	public void setTexture(int texture) {
		sphere.setTexture(texture);
	}
	
	@Override
	public void render(double delta) {
		sphere.setPosition((float)getX(), (float)getY(), (float)getZ());
		sphere.rotate(90, tilt, sphere.getZRot()+rotationRate*delta);
		sphere.render();
	}
	
}
