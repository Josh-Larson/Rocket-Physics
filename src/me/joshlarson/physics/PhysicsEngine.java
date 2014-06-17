package me.joshlarson.physics;

import java.util.ArrayList;
import java.util.List;

public class PhysicsEngine implements Runnable {
	
	private static final long DELTA_TIME = 1000000;
	
	private List <PhysicalBody> bodies;
	private Thread thread;
	private boolean running;
	private long lastRender = 0;
	private long time; // the current time in nanoseconds
	
	public PhysicsEngine() {
		bodies = new ArrayList<PhysicalBody>();
		lastRender = 0;
		time = 0;
	}
	
	public void start() {
		thread = new Thread(this);
		running = true;
		thread.setName("Physics Engine Thread");
		thread.start();
	}
	
	public void stop() {
		running = false;
		thread.interrupt();
	}
	
	public void addPhysicalBody(PhysicalBody body) {
		bodies.add(body);
	}
	
	public void removePhysicalBody(PhysicalBody body) {
		bodies.remove(body);
	}
	
	public PhysicalBody getPhysicalBody(String name) {
		for (PhysicalBody body : bodies) {
			if (body.getName().equals(name))
				return body;
		}
		return null;
	}
	
	public void renderAll() {
		double delta = (time - lastRender) / 1E9;
		lastRender = time;
		for (PhysicalBody body : bodies) {
			body.render(delta);
		}
	}
	
	public double getTime() {
		return time / 1E9;
	}
	
	@Override
	public void run() {
		time = 0;
		while (running) {
			calculateGravity();
			for (PhysicalBody body : bodies) {
				body.updateAll(this, DELTA_TIME / 1E9);
			}
			time += DELTA_TIME;
		}
	}
	
	private void calculateGravity() {
		for (int i = 0; i < bodies.size()-1; i++) {
			for (int j = i+1; j < bodies.size(); j++) {
				final PhysicalBody body1 = bodies.get(i);
				final PhysicalBody body2 = bodies.get(j);
				final double force = PhysicalBody.calculateGravity(body1, body2);
				body1.calculateGravity(body2, force);
				body2.calculateGravity(body1, force);
			}
		}
	}
	
}
