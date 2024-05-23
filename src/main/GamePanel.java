package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {

	private static final int FPS = 60;
	private JFrame frame;
	private Thread gameThread;
	private final int originalTileSize = 16;
	private final int scale = 3;

	private final int tileSize = originalTileSize * scale;
	private final int maxScreenCol = 24;
	private final int maxScreenRow = 14;
	private final int screenWidth = tileSize * maxScreenCol; // 1152 pixels
	private final int screenHeight = tileSize * maxScreenRow; // 672 pixels
	private long startTime;

	public GamePanel() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.setFocusable(true);
	}

	public void setup() {

	}

	public void startGameThread() {
		this.frame = new JFrame();
		gameThread = new Thread(this);
		gameThread.start();
		setup();

	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		this.startTime = System.currentTimeMillis();

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
			// FPS Display
			if (timer >= 1000000000) {
				System.out.println("FPS: " + drawCount);
				drawCount = 0;
				timer = 0;
			}
		}

	}
	/**
	 * paintComponent() handles the graphics of the program<br>
	 * pre: none<br>
	 * post: tileM, cannon, target, ui, and bar should be drawn and disposed each frame
	 */

//		public void paintComponent(Graphics g) {
//
//			super.paintComponent(g);
//
//			Graphics2D g2 = (Graphics2D) g;
//
//
//			g2.dispose();
//		}
	public void update() {
		// TODO Auto-generated method stub
		
	}
}
