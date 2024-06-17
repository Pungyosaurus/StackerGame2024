package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import entities.Building;
import entities.Cable;
import entities.Dot;
import entities.GameObject;
import entities.Ground;
import listeners.KeyHandler;
import listeners.MouseHandler;

public class Stacker extends GamePanel {

	private static final JLabel[][] JLabel = null;
	private int groundWidth = 90;
	private int groundHeight = 80;

	private ArrayList<Ground> groundObjectList1 = new ArrayList<Ground>();
	private ArrayList<Building> stack = new ArrayList<Building>();

	private Cable cable;
	private Sound soundEffects = new Sound();
	private Sound bgMusic = new Sound();
	private Building currentBuilding;
	private KeyHandler keyH;
	private MouseHandler mouseH;
	private BufferedImage background, iGround, rope, iBuilding;

	private ImageIcon heartIcon;
	private final int TOTAL_HEARTS = 5;
	private int heartCount = TOTAL_HEARTS;
	private JLabel[] heartList = new JLabel[TOTAL_HEARTS];

	private JPanel pausedMenu, instructionsPanel ;
	private JLabel pausedScoreDisplay, resume, quit, instructionsLabel;
	public static int score;
	private boolean s1, s2; // going to be used to play different sound effects
							// after consecutive successful
							// placements
	private int counter = 0;
	private int counter2 = 11;
	private int numBuildings;

	private Building prev;

	private int buildingMovementY;
	private final int BUILDING_MOVEMENT_Y_SPEED = 5;

	private boolean firstBuilding = true;
	private int startTopMiddleX, startTopMiddleY;

	private int perfectDrops;
	Random rand;

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

			iGround = ImageIO.read(getClass().getResourceAsStream("/textures/IceBlock.png"));
			background = ImageIO.read(getClass().getResourceAsStream("/textures/background.png"));
			rope = ImageIO.read(getClass().getResourceAsStream("/textures/blueCgain.png"));
			iBuilding = ImageIO.read(getClass().getResourceAsStream("/textures/building1.png"));

			int newWidth = 150; // New width of the scaled image
			int newHeight = 100; // New height of the scaled image
			Image heart = ImageIO.read(getClass().getResourceAsStream("/textures/Health.png"))
					.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
			heartIcon = new ImageIcon(heart);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void initHearts() {
		int startLocation = (int) (screenWidth / 4);
		int distance = 50;

		// draw x number of hearts and add to array
		for (int i = 0; i < TOTAL_HEARTS; i++) {
			JLabel heart = drawHeart();
			heart.setLocation(startLocation + i * distance, 50);
			heartList[i] = heart;
		}
	}

	/**
	 * Reset hearts if player wants to play again
	 * 
	 * @param paddle
	 */
	public void resetHearts() {
		for (int i = 0; i < TOTAL_HEARTS; i++) {
			heartList[i].setVisible(true);
		}
	}

	/**
	 * Method for creating heart labels
	 * 
	 * @return JLabel
	 */
	public JLabel drawHeart() {
		JLabel label = new JLabel();
		label.setIcon(heartIcon);
		label.setVisible(true);
		label.setBounds(200, 200, 50, 100);
		add(label);
		return label;
	}

	/**
	 * Add and remove hearts depending on score
	 * 
	 * @param paddle
	 * @param change
	 *            pre: change must be either 1 or -1;
	 */
	public void updateHearts(int change) {

		// remove heart
		if (change == -1) {

			JLabel heart = heartList[heartCount - 1];
			heart.setVisible(false);
			heartCount--;

		}
		// add heart
		if (change == 1) {
			JLabel heart = heartList[heartCount - 1];
			heart.setVisible(true);
			heartCount++;
		}

	}

	public void setup() {
		keyH = new KeyHandler();
		mouseH = new MouseHandler();

		addMouseListener(mouseH);
		addKeyListener(keyH);

		Building.loadImages(0, 0);
		getImages();

		setBackgroundImage(background);

		drawMenu();

		readAndDisplayInstructions();

		cable = new Cable((int) screenWidth / 2, -100, 550, 550, rope);
		add(cable);

		startTopMiddleX = (int) (screenWidth / 2 + groundWidth / 2);
		startTopMiddleY = (int) (screenHeight / 4 * 2.2 + groundHeight * 6 * .4);
		Dot dot = new Dot(startTopMiddleX, startTopMiddleY, 50, 50);
		add(dot);

		makePlatform(12, (int) (screenWidth / 2), (int) (screenHeight / 4 * 2.2), groundObjectList1);



		setUpJLabel();
		bgMusic.setFile(0);
		bgMusic.play();
		bgMusic.setVolume((float) 0.7);
		bgMusic.loop();
		initHearts();
		rand = new Random();

	}

	public void readAndDisplayInstructions() {
		StringBuffer instructions = new StringBuffer();

		try {
			InputStream is = getClass().getResourceAsStream("/dialogue/instructions.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			String line;
			while ((line = br.readLine()) != null) {
				instructions.append(line).append("<br>");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		instructions.insert(0, "<html>");
		instructions.append("</html>");
		System.out.println(instructions);
		
		 instructionsLabel = new JLabel(instructions.toString()){;
		 @Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g.create();
				g2d.drawImage(background, 0, 0, getWidth(), getHeight(), this);
				g2d.setColor(new Color(0, 0, 0, 190)); // Semi-transparent black
														// color
				g2d.fillRect(0, 0, getWidth(), getHeight());
			}
		 };
		 instructionsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
		 instructionsLabel.setForeground(Color.WHITE);
		 instructionsLabel.setVerticalAlignment(SwingConstants.TOP);
		
		 instructionsPanel = new JPanel(new BorderLayout());
		 instructionsPanel.setOpaque(false); // Transparent background
		 instructionsPanel.add(instructionsLabel, BorderLayout.CENTER);
		 instructionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Margin 		
		 this.add(instructionsPanel, BorderLayout.NORTH);
		 instructionsLabel.requestFocusInWindow();
		 repaint();
//		 revalidate();
		 while (!mouseH.isClicked()) {
				try {
					TimeUnit.MILLISECONDS.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			mouseH.setClicked(false);
			remove(instructionsLabel);		 
	}

	public void update() {

		if (keyH.isEscape()) {
			keyH.setEscape(false);
			openPausedMenu();
			return; // not sure if this does anything
		}

		if (currentBuilding == null) {
			currentBuilding = addBuilding();
			cable.changeMode();
		}

		cable.act();
		animateGround();
		animateGround();

		// move buildings down smoothly
		if (buildingMovementY != 0) {

			for (int i = 0; i < stack.size(); i++) {
				Building building = stack.get(i);
				building.setY(building.getY() + BUILDING_MOVEMENT_Y_SPEED);
			}

			for (Ground ground : groundObjectList1) {
				ground.setY(ground.getY() + BUILDING_MOVEMENT_Y_SPEED);
			}

			buildingMovementY -= BUILDING_MOVEMENT_Y_SPEED;
			if (BUILDING_MOVEMENT_Y_SPEED > buildingMovementY) {
				buildingMovementY = 0;
			}
		}

		// if not dropping
		if (!currentBuilding.getDrop()) {
			currentBuilding.setX(cable.getEndX() - currentBuilding.getSize().getWidth() / 2);
			currentBuilding.setY(cable.getEndY() - currentBuilding.getSize().getHeight() / 2 - cable.getScale());

			if (mouseH.isClicked() == true || keyH.isSpacebar()) {
				keyH.setSpacebar(false);
				mouseH.setClicked(false);
				currentBuilding.drop(cable.getDx(), cable.getDy(), cable.getDirection());

			}
		} else {
			currentBuilding.act();
			if (firstBuilding) {
				if (checkCollision(startTopMiddleX, startTopMiddleY)) {
					firstBuilding = false;
				} else if (currentBuilding.getY() > 2000) {
					currentBuilding.setDrop(false);
				}
			} else if (!checkCollision(prev.getX() + prev.getTopMiddleX(), prev.getY() + prev.getTopMiddleY())
					&& (currentBuilding.getY() > 2000)) {
				System.out.println("you failed");
				updateHearts(-1);
				currentBuilding.setDrop(false);
			}

		}
	}

	public boolean checkCollision(double xo, double yo) {
		int[] collisionValues = currentBuilding.collides(xo, yo, cable.getMode());
		if (collisionValues != null) {
			return collisionUpdate(collisionValues);

		}
		return false;
	}

	public boolean collisionUpdate(int[] collisionValues) {
		boolean addedBorder = false;

		if (collisionValues[0] + collisionValues[1] + collisionValues[2] + collisionValues[3] < 30 && !firstBuilding) {
			perfectDrops++;
			System.out.println(perfectDrops);
			System.out.println("made it 0");

			if (perfectDrops >= 2) {
				if (perfectDrops > 4) {
					updateHearts(1);
				}
				// add Sound
				int rando = rand.nextInt(3);
				System.out.println(currentBuilding.totalCutValues.length);
				for (int i = rando; i < currentBuilding.totalCutValues.length; i++) {

					if (currentBuilding.totalCutValues[i] >= 30) {
						currentBuilding.totalCutValues[i] -= 30;
						addedBorder = true;
						break;
					}
					i = rand.nextInt(3);
				}

				if (!addedBorder) {
					System.out.println("made it 2");

					for (int i = 0; i < currentBuilding.totalCutValues.length; i++) {
						if (currentBuilding.totalCutValues[i] > 0) {
							currentBuilding.totalCutValues[i] -= currentBuilding.totalCutValues[i];
							addedBorder = true;
							break;
						}
					}
				}
				System.out.println("perfect");
			}
		} else {
			perfectDrops = 0;
		}

		if (currentBuilding.cut(collisionValues[0], collisionValues[1], collisionValues[2], collisionValues[3])) {
			if (addedBorder) {
				currentBuilding.addBorder(perfectDrops * 2, Color.white);
			}
			currentBuilding.setY(
					currentBuilding.getY() + collisionValues[3] / 2 + collisionValues[4] + collisionValues[1] / 2);
			currentBuilding.setX(currentBuilding.getX() + collisionValues[2] * Math.sqrt(3) / 2
					+ collisionValues[0] * Math.sqrt(3) / 2);
			currentBuilding.setDrop(false);
			buildingMovementY = (int) (startTopMiddleY - currentBuilding.getY());
			stack.add(currentBuilding);
			numBuildings++;

			prev = stack.get(numBuildings - 1);
			currentBuilding = null;

			score++;
			return true;
		} else {
			System.out.println("you tried to cut too much");
		}
		return false;

	}

	public void animateGround() {
		groundObjectList1.get(counter).act();
		groundObjectList1.get(counter2).act();

		if (counter == groundObjectList1.size() - 1) {
			counter = -1;
		}
		if (counter2 == groundObjectList1.size() - 1) {
			counter2 = -1;
		}
		counter++;
		counter2++;
	}

	public Building addBuilding() {

		Building temp = new Building((int) cable.getEndX(), (int) cable.getEndY(), groundWidth * 5, groundHeight * 5);
		if (prev != null) {
			temp.cut(prev.getBackLeft(), prev.getFrontLeft(), prev.getFrontRight(), prev.getBackRight());
			add(temp, this.getComponentZOrder(prev));

		} else {
			temp.cut(0, 0, 0, 0);
			add(temp, 0);
		}

		// Building temp = new Building(prev);
		return temp;

	}

	public void makePlatform(int depth, int startX, int startY, ArrayList<Ground> list) {
		int amount = -1;
		setLayout(null);

		for (int j = 0; j < depth; j++) {

			if (j >= (depth + 1) / 2) {
				amount -= 1;
			} else {
				amount += 1;

			}

			for (int i = 0; i <= amount; i++) {

				Ground ground = new Ground(startX + (groundWidth - 30) * i - ((groundWidth - 30) / 2) * amount,
						startY + (int) (j * groundHeight * .20), groundWidth, groundHeight, iGround, 180 / depth * j);
				add(ground, 0);
				list.add(ground);
				repaint();
			}
		}
	}

	public void playSE(int i) {

		soundEffects.setFile(i);
		soundEffects.play();
	}

	/**
	 * Draws the starting menu
	 */
	public void drawMenu() {
		JPanel overlay = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g.create();
				g2d.drawImage(background, 0, 0, getWidth(), getHeight(), this);
				g2d.setColor(new Color(0, 0, 0, 190)); // Semi-transparent black
														// color
				g2d.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		overlay.setLayout(null);
		overlay.setBounds(0, 0, (int) screenWidth, (int) screenHeight);
		add(overlay);

		JLabel title = new JLabel("Chaos Constructor");
		title.setFont(new Font("Arial", Font.BOLD, (int) (screenWidth / 21)));
		title.setForeground(Color.white);
		int labelWidth = (int) (screenWidth / 2.3);
		int labelHeight = (int) (screenHeight / 10);
		int lx = (int) ((screenWidth - labelWidth) / 2);
		int ly = (int) ((screenHeight - labelHeight) / 3.0);
		title.setBounds(lx, ly, labelWidth, labelHeight);
		overlay.add(title);

		JLabel click = new JLabel("Click to play!");
		click.setFont(new Font("Arial", Font.BOLD, (int) (screenWidth / 30)));
		click.setForeground(Color.white);
		labelWidth = (int) (screenWidth / 4.5);
		lx = (int) ((screenWidth - labelWidth) / 2);
		ly += screenHeight / 3;
		click.setBounds(lx, ly, labelWidth, labelHeight);
		overlay.add(click);
		// title.setFocusable(true);
		// title.requestFocusInWindow();
		repaint();

		// Add ground objects and wait for input
		// revalidate(); // May be needed on some systems
		while (!mouseH.isClicked()) {
			try {
				TimeUnit.MILLISECONDS.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		mouseH.setClicked(false);
		remove(overlay);
		remove(title);
	}

	/**
	 * Opens the paused menu and enters a new loop (this way we don't have to
	 * track the gamestate and have an if statement that evaluates every tick)
	 * Displays the score and dialogue
	 */
	public void openPausedMenu() {
		bgMusic.pause();
		bgMusic.setFile(1);
		bgMusic.play();
		bgMusic.setVolume((float) 0.7);

		pausedScoreDisplay = new JLabel("Your Score: " + score);
		pausedScoreDisplay.setFont(new Font("Arial", Font.BOLD, (int) (pausedMenu.getWidth() / 15)));
		pausedScoreDisplay.setForeground(Color.white);
		int labelWidth = (int) (pausedMenu.getWidth() / 2);
		int labelHeight = (int) (pausedMenu.getHeight() / 3);
		int lx = (int) ((pausedMenu.getWidth() - labelWidth) / 2);
		int ly = 50;

		pausedScoreDisplay.setBounds(lx, ly, labelWidth, labelHeight);

		pausedMenu.add(pausedScoreDisplay);

		add(pausedMenu);
		setComponentZOrder(pausedMenu, 0); // Bring to front
		repaint();
		while (!keyH.isEscape()) {

			if (keyH.isUp()) {
				keyH.setUp(false);
				bgMusic.setVolume(bgMusic.getVolume() + 0.1f);
			}
			if (keyH.isDown()) {
				keyH.setDown(false);
				bgMusic.setVolume((bgMusic.getVolume() - 0.1f));
			}

			if (keyH.isDelete())
				System.exit(0);
			try {
				TimeUnit.MILLISECONDS.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		keyH.setEscape(false);
		remove(pausedMenu);
		repaint();
		bgMusic.close();
		bgMusic.setFile(0);
		bgMusic.play();
		bgMusic.setVolume((float) 0.7);

	}

	/**
	 * Creates the JLabels for the pausing menu and adds an translucent overlay
	 */
	public void setUpJLabel() {
		pausedMenu = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g.create();
				g2d.setColor(new Color(0, 0, 0, 210)); // Semi-transparent black
														// color
				g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 120, 120);
			}
		};

		pausedMenu.setOpaque(false); // Making the panel non-opaque to remove
										// corners
		pausedMenu.setLayout(null);
		// Dynamically calculating the width and height and positioning it in
		// the middle of the panel
		int mWidth = (int) (screenWidth / 2);
		int mHeight = (int) (screenHeight / 2);
		pausedMenu.setBounds((int) (screenWidth - mWidth) / 2, (int) (screenHeight - mHeight) / 2, mWidth, mHeight);

		int labelWidth = (int) (pausedMenu.getWidth() / 2);
		int labelHeight = (int) (pausedMenu.getHeight() / 4);

		resume = new JLabel("Esc to Resume");
		resume.setFont(new Font("Arial", Font.BOLD, (int) (pausedMenu.getWidth() / 25)));
		resume.setForeground(Color.white);
		resume.setBounds((pausedMenu.getWidth() - labelWidth) / 4, pausedMenu.getHeight() - labelHeight, labelWidth,
				labelHeight);

		quit = new JLabel("Delete to Quit");
		quit.setFont(new Font("Arial", Font.BOLD, (int) (pausedMenu.getWidth() / 25)));
		quit.setForeground(Color.white);
		quit.setBounds(pausedMenu.getWidth() / 2 + (pausedMenu.getWidth() - labelWidth) / 4,
				pausedMenu.getHeight() - labelHeight, labelWidth, labelHeight);

		pausedMenu.add(resume);
		pausedMenu.add(quit);
		repaint();

	}

}