package main;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {

	private JFrame frame;
	private final int originalTileSize = 16;
	private final int scale = 3;

	private final int tileSize = originalTileSize * scale;
	private final int maxScreenCol = 24;
	private final int maxScreenRow = 14;
	private final int screenWidth = tileSize * maxScreenCol; // 1152 pixels
	private final int screenHeight = tileSize * maxScreenRow; // 672 pixels
	
	public GamePanel() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.black);
		this.setDoubleBuffered(true);
		this.setFocusable(true);
	}
	
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
}
