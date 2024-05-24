package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import entities.Crane;
import entities.Ground;
import listeners.KeyHandler;
import listeners.MouseHandler;

public class Stacker extends GamePanel {

	private Ground ground;
	private Crane crane1;
	private KeyHandler keyH;
	private MouseHandler mouseH;
	private BufferedImage background, iGround, iCrane;

	public static void main(String[] args) {

		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setTitle("Construction Crane Chaos");
		window.setUndecorated(true);
		Stacker stacker = new Stacker();
		window.add(stacker);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		stacker.startGameThread();
	}

	public void getImages() {
		try {
			iGround = ImageIO.read(getClass().getResourceAsStream("/IsoGround.png"));
			iCrane = ImageIO.read(getClass().getResourceAsStream("/noback.png"));
			background = ImageIO.read(getClass().getResourceAsStream("/background.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setup() {
		keyH = new KeyHandler();
		mouseH = new MouseHandler();
		addMouseListener(mouseH);
		addKeyListener(keyH);

		getImages();
		setBackgroundImage(background);

		int startCraneX = (int) (screenWidth / 8);
		int startCraneY = (int) (screenHeight / 8);

		crane1 = new Crane(startCraneX, startCraneY, 800, 800, iCrane);
		add(crane1);

		ground = new Ground(100, 100, 120, 90, iGround);
		add(ground);

		repaint();
	}

	public void update() {

		if (keyH.isEscape()) {
			keyH.setEscape(false);
			isPaused = !isPaused;
		}
		if (!isPaused) {

			if (mouseH.isClicked() == true || keyH.isSpacebar()) {
				mouseH.setClicked(false);
				keyH.setSpacebar(false);
				// add code to drop the block here
			}
			ground.setX(ground.getX() + 1);
		}
	}

}
