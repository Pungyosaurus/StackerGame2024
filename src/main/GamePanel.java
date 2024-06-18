package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {

	private static final int FPS = 60;
	private JFrame frame;
	private Thread gameThread;
	protected boolean isPaused = false;

	// Screen Settings
	private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public final static double screenWidth = screenSize.getWidth();
	public static double screenHeight = screenSize.getHeight();

	private BufferedImage backgroundImage;
	private double backgroundY; // Y-coordinate for scrolling

	public GamePanel() {
		this.setPreferredSize(screenSize);
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.setFocusable(true);
	}

	public void setup() {
	}

	public void startGameThread() {
		this.frame = new JFrame();
		gameThread = new Thread(this);
		setup();
		gameThread.start();
	}

	@Override
	public void run() {
		// Setting the frame cap to 60
		double drawInterval = 1000000000 / FPS;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		long timer = 0;
		int drawCount = 0;

		// Game loop
		while (gameThread != null) {
			currentTime = System.nanoTime();
			delta += (currentTime - lastTime) / drawInterval;
			timer += currentTime - lastTime;
			lastTime = currentTime;

			if (delta >= 1) {
				update();
				repaint();
				delta--;
				drawCount++;
			}
		}

	}

	public void setBackgroundImage(BufferedImage backgroundImage) {
		this.backgroundImage = backgroundImage;
		backgroundY = screenHeight - backgroundImage.getHeight();
	}

	@Override
	public void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    Graphics2D g2 = (Graphics2D) g;
	    if (backgroundImage != null) {
	        int imageHeight = backgroundImage.getHeight();

	        // If the image doesn't cover the entire screen vertically, fill the arear
	        if (backgroundY >= screenHeight) {
	            g2.setColor(new Color(184, 147, 165, 255));
	            g2.fillRect(0, 0, (int) screenWidth, (int) screenHeight);
	        }

	        g2.drawImage(backgroundImage, 0, (int) backgroundY, (int) screenWidth, (int) backgroundY + imageHeight, 0,
	                0, backgroundImage.getWidth(), imageHeight, null);
	    }
	}



	public double getBackgroundY() {
		return this.backgroundY;
	}

	public void setBackgroundY(double d) {
		backgroundY = d;

	}
	
	public void update() {

	}

}
