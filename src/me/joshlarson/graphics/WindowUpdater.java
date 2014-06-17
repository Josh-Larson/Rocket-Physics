package me.joshlarson.graphics;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import me.joshlarson.UniverseDateFormat;
import me.joshlarson.graphics.utils.Point3f;
import me.joshlarson.graphics.utils.TextureLoader;
import me.joshlarson.physics.PhysicsEngine;
import me.joshlarson.physics.Planet;
import me.joshlarson.physics.Rocket;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

class WindowUpdater implements Runnable {
	
	private static final int FPS = 30;
	
	private Thread thread;
	private boolean running;
	private int frameNum;
	private double framesPerSecond;
	private DecimalFormat decimalFormat;
	private PhysicsEngine physicsEngine;
	private Window window;
	private World world;
	private Planet earth;
	private Planet moon;
	private Rocket rocket;
	private double earthLockRotation;
	private int earthTexture;
	private int moonTexture;
	
	public WindowUpdater(Window window) {
		this.window = window;
		this.frameNum = 0;
		this.running = false;
		this.world = new World();
		earthLockRotation = -1;
		framesPerSecond = 0;
		decimalFormat = new DecimalFormat("0.###E0");
	}
	
	public int getFrameNumber() {
		return frameNum;
	}
	
	private void render() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		world.enable2D();
		GL11.glColor3f(1, 1, 1);
		world.drawString(String.format("Current Time: %s", new UniverseDateFormat().format((long) (physicsEngine.getTime() * 100))), 5, 5);
		world.drawString(String.format("FPS: %.0f", framesPerSecond), 5, 25);
		world.drawString(String.format("ROT (%.0f, %.0f, %.0f)", world.getCamera().getRotation(), world.getCamera().getPitch(), world.getCamera().getRoll()), 5, window.getHeight() - 20);
		Point3f earthPosition = earth.getPosition();
		double dist = earthPosition.distanceTo(rocket.getPosition()) - earth.getRadius();
		world.drawString(String.format("Distance to Rocket: %.1f kilometers", dist), 5, world.getHeight() - 40);
		world.drawString(String.format("Rocket Velocity: %.3f, %.3f, %.3f km/s", rocket.getVelocityX(), rocket.getVelocityY(), rocket.getVelocityZ()), 5, world.getHeight() - 60);
		world.drawString(String.format("Rocket Thrust: %.3f N", rocket.totalThrust()), 5, world.getHeight() - 80);
		world.drawString(String.format("Rocket Thrust: %s N", decimalFormat.format(rocket.totalThrust())), 5, world.getHeight() - 100);
		world.enable3D();
		physicsEngine.renderAll();
		if (earthLockRotation != -1) {
			final double distance = 30000;
			final double angle = Math.toRadians(earth.getRotation() - earthLockRotation);
			final double x = distance * Math.cos(angle);
			final double z = distance * Math.sin(angle);
			world.getCamera().setPosition((float) x, 0, (float) z);
		} else {
			world.getCamera().setPosition((float)earth.getX(), (float)earth.getY()+1000000, (float)earth.getZ()+100);
		}
		world.getCamera().lookAt(earth.getPosition());
//		GL11.glVertex3d(moon.getX() - earth.getX(), moon.getY() - earth.getY() - 500000, moon.getZ() - earth.getZ());
	}
	
	public void start() {
		if (!running) {
			running = true;
			thread = new Thread(this);
			thread.setName("Window Updater Thread");
			thread.start();
		}
	}
	
	public void stop() {
		thread.interrupt();
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public void run() {
		initialize();
		Map<Integer, Boolean> keyStates = new HashMap<Integer, Boolean>();
		framesPerSecond = FPS;
		long lastFrame = System.nanoTime();
		while (running) {
			render();
			Display.update();
			if (isNewEvent(keyStates, Keyboard.KEY_L)) {
				if (earthLockRotation == -1)
					earthLockRotation = earth.getRotation();
				else
					earthLockRotation = -1;
			}
			if (isNewEvent(keyStates, Keyboard.KEY_RIGHT))
				world.getCamera().setRotation(world.getCamera().getRotation() + 5);
			if (isNewEvent(keyStates, Keyboard.KEY_LEFT))
				world.getCamera().setRotation(world.getCamera().getRotation() - 5);
			frameNum++;
			running = !Display.isCloseRequested();
			framesPerSecond = framesPerSecond * .95 + .05 * (1 / ((System.nanoTime() - lastFrame) / 1E9));
			lastFrame = System.nanoTime();
			Display.sync(FPS);
			if (isNewEvent(keyStates, Keyboard.KEY_Q) || isNewEvent(keyStates, Keyboard.KEY_ESCAPE)) {
				running = false;
			}
			keyStates.put(Keyboard.KEY_Q, Keyboard.isKeyDown(Keyboard.KEY_Q));
			keyStates.put(Keyboard.KEY_ESCAPE, Keyboard.isKeyDown(Keyboard.KEY_ESCAPE));
			keyStates.put(Keyboard.KEY_L, Keyboard.isKeyDown(Keyboard.KEY_L));
			keyStates.put(Keyboard.KEY_RIGHT, Keyboard.isKeyDown(Keyboard.KEY_RIGHT));
			keyStates.put(Keyboard.KEY_LEFT, Keyboard.isKeyDown(Keyboard.KEY_LEFT));
		}
		destroy();
	}
	
	private boolean isNewEvent(Map<Integer, Boolean> keyStates, int key) {
		if (!keyStates.containsKey(key))
			return Keyboard.isKeyDown(key);
		return keyStates.get(key) != Keyboard.isKeyDown(key) && Keyboard.isKeyDown(key);
	}
	
	private void destroy() {
		physicsEngine.stop();
		Display.destroy();
	}
	
	private void initialize() {
		world.setSize(window.getWidth(), window.getHeight());
		world.initialize("Rocket Physics", Float.MAX_VALUE, 60);
		physicsEngine = new PhysicsEngine();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		world.enable2D();
		drawProgress(0, "Initializing textures...", window.getHeight() - 20);
		earthTexture = TextureLoader.loadTexture("res/earth.png");
		drawProgress(50, "Earth Loaded.", window.getHeight() - 40);
		moonTexture = TextureLoader.loadTexture("res/moon.png");
		drawProgress(100, "Moon Loaded.", window.getHeight() - 60);
		drawProgress(100, "Initializing physics...", window.getHeight() - 80);
		earth = new Planet(world, "Earth", 6378.1, 5.97219E24); // km and kg
		earth.setRotationRate(-0.00417807413); // per second
		earth.setTilt(23.439281); // degrees
		earth.setTexture(earthTexture);
		earth.setPosition(0, 0, 0);
		earth.setVelocity(0, 0, 0);
		physicsEngine.addPhysicalBody(earth);
		moon = new Planet(world, "Moon", 1738.14, 7.34767309E22); // km and kg
		moon.setRotationRate(0); // per second
		moon.setTilt(0); // degrees
		moon.setTexture(moonTexture);
		moon.setPosition(363104, 0, 0);
		moon.setVelocity(0, 0, 1.022);
		physicsEngine.addPhysicalBody(moon);
		rocket = new Rocket(world, "Rocket", .03, 45.3592);
		rocket.setTexture(moonTexture);
		rocket.setPosition(earth.getRadius(), 0, 0);
		physicsEngine.addPhysicalBody(rocket);
		physicsEngine.start();
		world.getCamera().setPosition(30000, 0, 0);
	}
	
	private void drawProgress(double progress, String text, int height) {
		world.drawString(String.format("%.0f%%", progress), 5, height);
		world.drawString(text, 40, height);
		Display.update();
	}
	
}
