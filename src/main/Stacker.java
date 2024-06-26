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
import javax.swing.SwingUtilities;

import entities.Building;
import entities.Cable;
import entities.GameObject;
import entities.Ground;
import listeners.KeyHandler;
import listeners.MouseHandler;
/**
 * 
 * @author Jason and Dave June 2024
 * A fun, simple isometric stacker game
 *
 */
public class Stacker extends GamePanel {

	private final int TOTAL_HEARTS = 7;
	private int heartCount = TOTAL_HEARTS;

	public static int score;
	private boolean s1, s2; // going to be used to play different sound effects
							// after consecutive successful
	// placements
	private int counter = 0;
	private int counter2 = 11;
	private int numBuildings;

	private int buildingMovementY;
	private final int BUILDING_MOVEMENT_Y_SPEED = 5;

	private boolean firstBuilding = true;
	private int startTopMiddleX, startTopMiddleY;

	private int perfectDrops;
	private static final JLabel[][] JLabel = null;
	private int groundWidth = 90;
	private int groundHeight = 80;

	private ArrayList<Ground> groundObjectList1 = new ArrayList<Ground>();
	private ArrayList<Building> stack = new ArrayList<Building>();

	private Cable cable;

	private Sound soundEffects = new Sound();
	private Sound bgMusic = new Sound();

	private Building currentBuilding;
	private Building prev;

	private KeyHandler keyH;
	private MouseHandler mouseH;

	private BufferedImage background, iGround, rope, iBuilding;
	private JPanel pausedMenu;
	private JLabel[] heartList = new JLabel[TOTAL_HEARTS];
	private JLabel pausedScoreDisplay, resume, quit, instructionsLabel;
	private ImageIcon heartIcon;
	private Random rand = new Random();

	/**
	 * Starts the game and adds a new Stacker object
	 * @param args
	 */
	public static void main(String[] args) {

		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setTitle("Chaos Constructor");
		window.setUndecorated(true);
		Stacker stacker = new Stacker();
		window.add(stacker);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		stacker.startGameThread();
	}
	/**
	 * Loads the images in from the resource folder
	 */
	public void getImages() {
		try {

			iGround = ImageIO.read(getClass().getResourceAsStream("/textures/IceBlock.png"));
			background = ImageIO.read(getClass().getResourceAsStream("/textures/latest.jpg"));
			rope = ImageIO.read(getClass().getResourceAsStream("/textures/blueCgain.png"));
			iBuilding = ImageIO.read(getClass().getResourceAsStream("/textures/building1.png"));
			// scaling the heart images
			int newWidth = 150;
			int newHeight = 100;
			Image heart = ImageIO.read(getClass().getResourceAsStream("/textures/Health.png"))
					.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
			heartIcon = new ImageIcon(heart);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * init the hearts with the correct amount and location
	 */
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
	 * @param change pre: change must be either 1 or -1;
	 */
	public boolean updateHearts(int change) {

		// remove heart
		if (change == -1) {
			if (heartCount - 1 < 0)
				return false;
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

		return true;

	}
	
	/**
	 * Initializes objects, adds them to the frame, runs the images, calls the menu and instructions screens, and starts the background music
	 */
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

		makePlatform(12, (int) (screenWidth / 2), (int) (screenHeight / 4 * 2.2), groundObjectList1);

		setUpJLabel();
		bgMusic.setFile(0);
		bgMusic.play();
		bgMusic.setVolume((float) 0.7);
		bgMusic.loop();
		initHearts();

	}

	/**
	 * Reading the instructions text file and displaying it onto a JPanel
	 * 
	 */
	public void readAndDisplayInstructions() {
		// Making it HTML
		StringBuffer instructions = new StringBuffer("<html>");

		try {
			InputStream is = getClass().getResourceAsStream("/dialogue/instructions.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(is));

			String line;
			while ((line = br.readLine()) != null) {
				// adding a line break so it displays properly
				instructions.append(line).append("<br>");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		instructions.append("</html>");
		// adding the instructions into a JLabel
		instructionsLabel = new JLabel(instructions.toString());
		instructionsLabel.setFont(new Font("Arial", Font.PLAIN, 20));
		instructionsLabel.setForeground(Color.WHITE);
		instructionsLabel.setVerticalAlignment(SwingConstants.TOP);
		
		// Panel with grey overlay to read instructions
		JPanel instructionsPanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g.create();
				g2d.setColor(new Color(0, 0, 0, 190)); // Semi-transparent
				g2d.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		instructionsPanel.setOpaque(false);
		instructionsPanel.add(instructionsLabel, BorderLayout.CENTER);
		instructionsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		// using borderlayout to center it instead of doing math
		add(instructionsPanel, BorderLayout.NORTH);
		instructionsLabel.requestFocusInWindow();
		repaint();
		revalidate(); // used to ensure the panel displays
		while (!mouseH.isClicked()) {
			// breaking out when mouse is clicked
			try {
				TimeUnit.MILLISECONDS.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		mouseH.setClicked(false);
		remove(instructionsPanel);
	}
	/**
	 * Run 60 times a second
	 * Updates the location of the cable, buildings
	 */
	public void update() {
		
		//open pause menu
		if (keyH.isEscape()) {
			keyH.setEscape(false);
			openPausedMenu();
			return; // not sure if this does anything
		}

		//add a new building any time it becomes null, try to replace with copy constructor
		if (currentBuilding == null) {
			currentBuilding = addBuilding();
			cable.changeMode();
		}
		
		// Swinging the cable
		cable.act();
		animateGround();
		animateGround();

		// move buildings down smoothly
		if (buildingMovementY != 0) {
			// moving the arraylist of buildings down
			for (int i = 0; i < stack.size(); i++) {
				Building building = stack.get(i);
				building.setY(building.getY() + BUILDING_MOVEMENT_Y_SPEED);
				// Sliding the background by a smaller amount
				setBackgroundY(getBackgroundY() + BUILDING_MOVEMENT_Y_SPEED / 5);
			}
			// for each loop to move down the ground objects
			for (Ground ground : groundObjectList1) {
				ground.setY(ground.getY() + BUILDING_MOVEMENT_Y_SPEED);
			}
			
			buildingMovementY -= BUILDING_MOVEMENT_Y_SPEED;
			
			//if the building is moved to the correct location stop moving
			if (BUILDING_MOVEMENT_Y_SPEED > buildingMovementY) {
				buildingMovementY = 0;
			}
		}

		// if not dropping
		if (!currentBuilding.getDrop()) {
			currentBuilding.setX(cable.getEndX() - currentBuilding.getSize().getWidth() / 2);
			currentBuilding.setY(cable.getEndY() - currentBuilding.getSize().getHeight() / 2 - cable.getScale());
			// Dropping the block if a click is detected
			if (mouseH.isClicked() == true || keyH.isSpacebar()) {
				keyH.setSpacebar(false);
				mouseH.setClicked(false);
				currentBuilding.drop(cable.getDx(), cable.getDy(), cable.getDirection());

			}
		} else {
			// dropping the block and checking for collisions
			currentBuilding.act();
			if (firstBuilding) {
				if (checkCollision(startTopMiddleX, startTopMiddleY)) {
					// sound when collides
					playSE(3);
					firstBuilding = false;
				} else if (currentBuilding.getY() > 2000) {
					currentBuilding.setDrop(false);
				}
			} 
			//checks if collision was a fail
			else if (!checkCollision(prev.getX() + prev.getTopMiddleX(), prev.getY() + prev.getTopMiddleY()) && (currentBuilding.getY() > 2000)) {
				System.out.println("you failed");
				
				//remove hearts if failed
				if (!updateHearts(-1)) {
					drawFinished();
				}
				// reset the block
				currentBuilding.setDrop(false);
			}

		}
	}
	/**
	 * Draws the finished menu and displays the final score
	 */
	public void drawFinished() {
		JPanel doneScreen = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g.create();
				g2d.setColor(new Color(0, 0, 0, 210)); // Semi-transparent black
				g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 120, 120);
			}
		};

		doneScreen.setOpaque(false); // Making the panel non-opaque to remove corners
		// Manually setting the bounds and positioning
		doneScreen.setLayout(null);

		int mWidth = (int) (screenWidth / 2);
		int mHeight = (int) (screenHeight / 2);
		doneScreen.setBounds((int) (screenWidth - mWidth) / 2, (int) (screenHeight - mHeight) / 2, mWidth, mHeight);

		JLabel r = new JLabel("R to restart!");
		r.setFont(new Font("Arial", Font.BOLD, (int) (screenWidth / 30)));
		r.setForeground(Color.white);
		// Sizing the label
		int labelWidth = r.getPreferredSize().width;
		int labelHeight = r.getPreferredSize().height;
		int lx = (doneScreen.getWidth() - labelWidth) / 2;
		int ly = (doneScreen.getHeight() - labelHeight) / 2;
		r.setBounds(lx, ly, labelWidth, labelHeight);

		doneScreen.add(r);

		add(doneScreen);
		doneScreen.requestFocus(true); // Request focus for keyboard input
		repaint();
		doneScreen.addKeyListener(keyH); // adding the listener to ensure it works
		
		while (!keyH.isR()) {
			// closing the game if delete is pressed
			if (keyH.isDelete())
				System.exit(0);
			
			// adding a delay to reduce the cpu usage (doesn't have to be checked 1000 times a second)
			try {
				TimeUnit.MILLISECONDS.sleep(100);

			} catch (Exception e) {

			}	
		}
		keyH.setR(false);
		doneScreen.remove(r);
		// Repainting to update the screen
		repaint();
		resetAll();

	}
	/**
	 * Displays the final score, deletes the old window and creates a new Stacker object
	 */
	public void resetAll() {
		// Reusing the same scoredisplay object
		pausedScoreDisplay = new JLabel("Your Score: " + score);
		pausedScoreDisplay.setFont(new Font("Arial", Font.BOLD, (int) (screenWidth / 23)));
		pausedScoreDisplay.setForeground(Color.white);

		int labelWidth = pausedScoreDisplay.getPreferredSize().width;
		int labelHeight = pausedScoreDisplay.getPreferredSize().height;
		int lx = (int) ((screenWidth - labelWidth) / 2);
		int ly = (int) ((screenHeight - labelHeight) / 2);

		pausedScoreDisplay.setBounds(lx, ly, labelWidth, labelHeight);

		add(pausedScoreDisplay);
		// Bring to front so it displays
		setComponentZOrder(pausedScoreDisplay, 0); 
		repaint();
		// Displaying the final score before removing
		try {
			TimeUnit.SECONDS.sleep(4);

		} catch (Exception e) {

		}

		bgMusic.close();
		
		// getting the current frame and deleting it
		JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
		currentFrame.dispose();
		// remaking the frame to restart the game
		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setTitle("Chaos Constructor");
		window.setUndecorated(true);
		
		// restarting the game from scratch
		Stacker two = new Stacker();
		window.add(two);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		two.startGameThread();
	}
	
	
	/**
	 * Checks collision for previous block and current block
	 * @param previousX
	 * @param previousY
	 * @return boolean
	  * @author Dave Singh
	 */
	public boolean checkCollision(double previousX, double previousY) {
		//if collision works return the cut values
		int[] collisionValues = currentBuilding.collides(previousX, previousY, cable.getMode());
		if (collisionValues != null) {
			playSE(3);
			//send cut values to update the blocks
			return collisionUpdate(collisionValues);

		}
		return false;
	}
	
	/**
	 * check if the drop was perfect and use random values to add to the side as a reward
	 * @param collisionValues
	 * @return return if drop was perfect
	  * @author Dave Singh
	 */
	public boolean perfectDropCheck(int[] collisionValues) {
		// add border only if it was a perfect landing
				boolean addedBorder = false;

				//check if the total offset of all cuts was less than thirty to ensure it was almost a perfect cut
				if (collisionValues[0] + collisionValues[1] + collisionValues[2] + collisionValues[3] < 30 && !firstBuilding) {
					perfectDrops++;
					System.out.println(perfectDrops);
					
					
					if (perfectDrops >= 1) {
						
						//get health for 4 perfect drops in a row
						if (perfectDrops > 4) {
							updateHearts(1);
						}

						
						
						int rando = rand.nextInt(3);
						int counter = 0;
						
						//loop through the cut values starting at a random location, then add size to the block to reward perfect drops
						for (int i = rando; i < currentBuilding.getTotalCutValues().length; i++) {

							if (currentBuilding.getTotalCutValues()[i] >= 30) {
								currentBuilding.getTotalCutValues()[i] -= 30;
								for (int y = 0; y < currentBuilding.getTotalCutValues().length; y++) {
									if (y != i) {
										collisionValues[i] = 0;
									}
								}
								addedBorder = true;
								break;
							}
							counter++;
							if (counter > 10) {
								break;
							}
							i = rand.nextInt(3);
						}

						// if none of the values were greater than thirty add to one side by its full amount
						if (!addedBorder) {
							for (int i = 0; i < currentBuilding.getTotalCutValues().length; i++) {
								if (currentBuilding.getTotalCutValues()[i] > 0) {
									currentBuilding.getTotalCutValues()[i] -= currentBuilding.getTotalCutValues()[i];
									addedBorder = true;
									break;
								}
							}
						}
					}
				} 
				//reset perfect Drops
				else {
					perfectDrops = 0;
				}
				
				return addedBorder;
	}

	/**
	 * update the current block to show new cut size
	 * @param collisionValues
	 * @return boolean
	 * @author Dave Singh
	 */
	public boolean collisionUpdate(int[] collisionValues) {
		
		//check for perfect drop and if border should be added
		boolean addedBorder = perfectDropCheck(collisionValues);

		//ensure face can be cut without breakign the game
		if (currentBuilding.cut(collisionValues[0], collisionValues[1], collisionValues[2], collisionValues[3])) {
			//add border now
			if (addedBorder) {
				currentBuilding.addBorder(perfectDrops * 2, Color.white);
			}
			//stop the building from falling
			currentBuilding.setDrop(false);
			
			//set x and y to new locations\
			currentBuilding.setY(
					currentBuilding.getY() + collisionValues[3] / 2 + collisionValues[4] + collisionValues[1] / 2);
			currentBuilding.setX(currentBuilding.getX() + collisionValues[2] * Math.sqrt(3) / 2
					+ collisionValues[0] * Math.sqrt(3) / 2);
			
			// check how much to move screen down
			buildingMovementY = (int) (startTopMiddleY - currentBuilding.getY());
			
			// add current building to stack
			stack.add(currentBuilding);
			numBuildings++;

			//set previous to last item in stack;
			prev = stack.get(numBuildings - 1);
			currentBuilding = null;

			//increase score
			score++;
			return true;
		} else {
			System.out.println("you tried to cut too much");
		}
		return false;

	}
	/**
	 * Animates the ground by calling ground act methods and passing counter
	 * @author Dave Singh
	 */
	public void animateGround() {
		
		// loop through and move each block
		groundObjectList1.get(counter).act();
		groundObjectList1.get(counter2).act();

		// create two waves of movement
		if (counter == groundObjectList1.size() - 1) {
			counter = -1;
		}
		if (counter2 == groundObjectList1.size() - 1) {
			counter2 = -1;
		}
		counter++;
		counter2++;
	}
	/**
	 * Creates a new building object and cuts it into the previous ones size 
	 * @return the new building object with the adjusted size
	 * @author Dave Singh
	 */
	public Building addBuilding() {
		
		// add building
		Building temp = new Building((int) cable.getEndX(), (int) cable.getEndY(), groundWidth * 5, groundHeight * 5);
		if (prev != null) {
			//cut first building to previous buildigns size;
			temp.cut(prev.getBackLeft(), prev.getFrontLeft(), prev.getFrontRight(), prev.getBackRight());
			add(temp, this.getComponentZOrder(prev));

		} else {
			//cut by 0000 no matter what to ensure proper sprite
			temp.cut(0, 0, 0, 0);
			add(temp, 0);
		}

		return temp;

	}
	/**
	 * 
	 * Create starting platforms
	 * @param depth
	 * @param startX
	 * @param startY
	 * @param list
	 *
	 * @author Dave Singh
	 */
	public void makePlatform(int depth, int startX, int startY, ArrayList<Ground> list) {
		int amount = -1;
		setLayout(null);

		// put block in correct place
		for (int j = 0; j < depth; j++) {

			if (j >= (depth + 1) / 2) {
				amount -= 1;
			} else {
				amount += 1;

			}

			//add ground objects
			for (int i = 0; i <= amount; i++) {

				Ground ground = new Ground(startX + (groundWidth - 30) * i - ((groundWidth - 30) / 2) * amount,
						startY + (int) (j * groundHeight * .20), groundWidth, groundHeight, iGround, 180 / depth * j);
				add(ground, 0);
				list.add(ground);
				repaint();
			}
		}
	}
	
	
	/**
	 * Sets the sound file and plays it
	 * @param i is an element of the array in Sound.java
	 */
	public void playSE(int i) {

		soundEffects.setFile(i);
		soundEffects.play();
	}

	/**
	 * Draws the starting menu, plays the menu music
	 */
	public void drawMenu() {
		
		// Playing loading music
		bgMusic.setFile(2);
		bgMusic.play();
		bgMusic.loop();
		// blurring the background 
		JPanel overlay = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g.create();
				g2d.drawImage(background, 0, 0, getWidth(), getHeight(), this);
				g2d.setColor(new Color(0, 0, 0, 190)); // Semi-transparent black color
				g2d.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		overlay.setLayout(null);
		overlay.setBounds(0, 0, (int) screenWidth, (int) screenHeight);
		add(overlay);

		// Adding and positioning the title
		JLabel title = new JLabel("Chaos Constructor");
		title.setFont(new Font("Arial", Font.BOLD, (int) (screenWidth / 21)));
		title.setForeground(Color.white);
		int labelWidth = (int) (screenWidth / 2.3);
		int labelHeight = (int) (screenHeight / 10);
		int lx = (int) ((screenWidth - labelWidth) / 2);
		int ly = (int) ((screenHeight - labelHeight) / 3.0);
		title.setBounds(lx, ly, labelWidth, labelHeight);
		overlay.add(title);

		// positioning click to play labels
		JLabel click = new JLabel("Click to play!");
		click.setFont(new Font("Arial", Font.BOLD, (int) (screenWidth / 30)));
		click.setForeground(Color.white);
		labelWidth = (int) (screenWidth / 4.5);
		lx = (int) ((screenWidth - labelWidth) / 2);
		// Moving this label down
		ly += screenHeight / 3;
		click.setBounds(lx, ly, labelWidth, labelHeight);
		overlay.add(click);
		// title.setFocusable(true);
		// title.requestFocusInWindow();
		repaint();

		// Add ground objects and wait for input
		// revalidate(); // May be needed on some systems
		while (!mouseH.isClicked()) {
			// waiting until the user clicks mouse
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
		bgMusic.close();
	}

	/**
	 * Opens the paused menu and enters a new loop (this way we don't have to track
	 * the gamestate and have an if statement that evaluates every tick) Displays
	 * the score and dialogue
	 */
	public void openPausedMenu() {
		
		// Changing audio
		bgMusic.pause();
		bgMusic.setFile(1);
		bgMusic.play();
		bgMusic.setVolume((float) 0.7);

		// Adding the score display and positioning it
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
		// Bringing the menu to the front (not sure if this does anything, it varies per computer)
		setComponentZOrder(pausedMenu, 0); 
		repaint();
		while (!keyH.isEscape()) {
			// volume up
			if (keyH.isUp()) {
				keyH.setUp(false);
				bgMusic.setVolume(bgMusic.getVolume() + 0.1f);
			}
			// volume down
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
		// Changing audio tracks
		bgMusic.close();
		bgMusic.setFile(0);
		bgMusic.play();
		bgMusic.setVolume((float) 0.7);

	}

	/**
	 * Creates the JLabels for the pausing menu and adds an translucent overlay 
	 * The objects don't need to be recreated each time they are used
	 */
	public void setUpJLabel() {
		pausedMenu = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				Graphics2D g2d = (Graphics2D) g.create();
				g2d.setColor(new Color(0, 0, 0, 210)); // Semi-transparent black color
				g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 120, 120);
			}
		};
		// Making the panel non-opaque to remove corners
		pausedMenu.setOpaque(false); 
		pausedMenu.setLayout(null);
		// calculating the width and height
		int mWidth = (int) (screenWidth / 2);
		int mHeight = (int) (screenHeight / 2);
		pausedMenu.setBounds((int) (screenWidth - mWidth) / 2, (int) (screenHeight - mHeight) / 2, mWidth, mHeight);

		int labelWidth = (int) (pausedMenu.getWidth() / 2);
		int labelHeight = (int) (pausedMenu.getHeight() / 4);
		
		// adding options to unpause and quit the game
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