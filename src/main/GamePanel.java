package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;
/**
 * 
 * @author Jason Wong June 2024
 * A runnable panel with paint and background features!
 */
public class GamePanel extends JPanel implements Runnable {

	private static final int FPS = 60;
	private JFrame frame;
	private Thread gameThread;
	protected boolean isPaused = false; // not used

	// Screen Settings
	private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	public static final  double screenWidth = screenSize.getWidth();
	public static final double screenHeight = screenSize.getHeight();
	
	// Background image
	private BufferedImage backgroundImage;
	private double backgroundY; // Y-coordinate for scrolling
	/**
	 * Setting up the panel
	 */
	public GamePanel() {
		this.setPreferredSize(screenSize);
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.setFocusable(true);
	}
	/**
	 * Called before the game starts, meant to be overridden
	 */
	public void setup() {
	}
	/**
	 * Starts the game thread, runs setup, and initializes the JFrame
	 */
	public void startGameThread() {
		this.frame = new JFrame();
		gameThread = new Thread(this);
		setup();
		gameThread.start();
	}

	/**
	 * Loops the game
	 */
	@Override
	public void run() {
		// Setting the frame cap to 60
		double drawInterval = 1000000000 / FPS;
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		long timer = 0;

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
			}
		}

	}
	/**
	 * Sets the background image to be displayed and initializes backgroundY
	 * @param backgroundImage is not a null BufferedImage
	 */
	public void setBackgroundImage(BufferedImage backgroundImage) {
		this.backgroundImage = backgroundImage;
		backgroundY = screenHeight - backgroundImage.getHeight();
	}
	/**
	 * Paints the background image and scrolling feature
	 */
	@Override
	public void paintComponent(Graphics g) {
	    super.paintComponent(g);
	    Graphics2D g2 = (Graphics2D) g;
	    if (backgroundImage != null) {
	        int imageHeight = backgroundImage.getHeight();

	        // If the image doesn't cover the entire screen vertically, fill the area
	        if (backgroundY >= screenHeight) {
	            g2.setColor(new Color(184, 147, 165, 255));
	            g2.fillRect(0, 0, (int) screenWidth, (int) ((int) screenHeight + backgroundY));
	        }

	        g2.drawImage(backgroundImage, 0, (int) backgroundY, (int) screenWidth, (int) backgroundY + imageHeight, 0,
	                0, backgroundImage.getWidth(), imageHeight, null);
	    }
	}


	/**
	 * 
	 * @return the backgroundY
	 */
	public double getBackgroundY() {
		return this.backgroundY;
	}
	/**
	 * Sets the backgroundY to d
	 * @param d should be a double
	 */
	public void setBackgroundY(double d) {
		backgroundY = d;

	}
	/**
	 * Meant to be overridden (loops the game)
	 */
	public void update() {

	}

}
