package me.joshlarson.graphics;

public class Window {
	
	private int width;
	private int height;
	private int fps;
	private WindowUpdater updater;
	
	public Window(int width, int height, int fps) {
		this.width = width;
		this.height = height;
		this.fps = fps;
		this.updater = new WindowUpdater(this);
	}
	
	public void start() {
		updater.start();
	}
	
	public boolean isRunning() {
		return updater.isRunning();
	}
	
	public void destroy() {
		updater.stop();
	}
	
	public int getFPS() {
		return fps;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
}
