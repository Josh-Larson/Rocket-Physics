package me.joshlarson;

import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Toolkit;

import me.joshlarson.graphics.Window;

public class Simulator {
	
	public static void main(String [] args) {
		Insets i = Toolkit.getDefaultToolkit().getScreenInsets(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration());
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int)screenSize.getWidth() - i.left - i.right;
		int height = (int)screenSize.getHeight() - i.top - i.bottom - 30;
		Window w = new Window(width, height, 60);
		w.start();
		while (w.isRunning()) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		w.destroy();
	}
	
}
