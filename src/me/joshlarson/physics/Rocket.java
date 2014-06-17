package me.joshlarson.physics;

import java.util.List;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import me.joshlarson.graphics.World;
import me.joshlarson.graphics.utils.Point3f;

public class Rocket extends Planet {
	
	private World w;
	private List <Point3f> rocketPath;
	private double totalThrust;
	
	public Rocket(World w, String name, double radius, double mass) {
		super(w, name, radius, mass);
		this.w = w;
		rocketPath = new ArrayList<Point3f>();
	}
	
	@Override
	public void render(double delta) {
		rocketPath.add(getPosition());
		w.getCamera().render();
		GL11.glBegin(GL11.GL_LINE_STRIP);
		for (Point3f point : rocketPath) {
			GL11.glVertex3f(point.getX(), point.getY(), point.getZ());
		}
		GL11.glEnd();
		super.render(delta);
	}
	
	public double totalThrust() {
		return totalThrust;
	}
	
	@Override
	public void updateAll(PhysicsEngine engine, double delta) {
		Planet earth = (Planet) engine.getPhysicalBody("Earth");
		double dist = earth.getPosition().distanceTo(getPosition());
		double net = 0;
		if (dist <= earth.getRadius() + 15000) { // Initial push
			Tuple3d force = new Tuple3d(getX(), getY(), getZ());
			force.sub(earth.getX(), earth.getY(), earth.getZ());
			force.setX(Math.sin(force.getX() / dist));
			force.setY(Math.sin(force.getY() / dist));
			force.setZ(Math.sin(force.getZ() / dist));
			net = PhysicalBody.calculateGravity(this, earth) + .05;
			force.scale(net);
			addForce(force);
		} else {
			Planet moon = (Planet) engine.getPhysicalBody("Moon");
			Tuple3d force = new Tuple3d(moon.getX(), moon.getY(), moon.getZ());
			force.sub(getX(), getY(), getZ());
			force.setX(Math.sin(force.getX() / dist));
			force.setY(Math.sin(force.getY() / dist));
			force.setZ(Math.sin(force.getZ() / dist));
			net = .005;
			force.scale(net);
			addForce(force);
		}
		totalThrust += net * 1000 * delta;
		super.updateAll(engine, delta);
	}
}
