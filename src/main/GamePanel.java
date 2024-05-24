package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {

	private static final int FPS = 60;
	private JFrame frame;
	private Thread gameThread;
	
	// Screen Settings
	private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	final static double screenWidth = screenSize.getWidth();
	final static double screenHeight = screenSize.getHeight();
	
	// old screen settings
	private final int originalTileSize = 16;
	private final int scale = 3;

	private final int tileSize = originalTileSize / scale;
	private final int maxScreenCol = 24;
	private final int maxScreenRow = 14;
    private BufferedImage backgroundImage;
//	private final int screenWidth = tileSize * maxScreenCol; // 1152 pixels
//	private final int screenHeight = tileSize * maxScreenRow; // 672 pixels

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
//				repaint();
				delta--;
				drawCount++;
			}
			// FPS Display
			if (timer >= 1000000000) {
				System.out.println("FPS: " + drawCount);
				drawCount = 0;
				timer = 0;
			}
		}

	}
	
	public void setBackgroundImage(BufferedImage backgroundImage) {
        this.backgroundImage = backgroundImage;
    }
	
	public void paintComponent(Graphics g) {
		
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, (int)screenWidth, (int)screenHeight, null);
        }

	}
	public void update() {
		// TODO Auto-generated method stub
		
	}
}
