package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import entities.Building;
import entities.BuildingCut;
import entities.Cable;
import entities.Crane;
import entities.Ground;
import listeners.KeyHandler;
import listeners.MouseHandler;

public class Stacker extends GamePanel {

	private int groundWidth = 90;
	private int groundHeight = 80;

	private ArrayList<Ground> groundObjectList1 = new ArrayList<Ground>();
	private ArrayList<Ground> groundObjectList2 = new ArrayList<Ground>();
	private ArrayList<Building> stack = new ArrayList<Building>();

	private Cable cable;
	private Sound soundEffects = new Sound();
	private Sound bgMusic = new Sound();
	private Building currentBuilding;
	private KeyHandler keyH;
	private MouseHandler mouseH;
	private BufferedImage background, iGround, iCrane, rope,iBuilding;

	private boolean s1, s2; // going to be used to paly different sound effects after consecutive successful placements
	private int counter = 0;
	private int counter2 = 11;
	private int numBuildings;
	
	private Building prev;
	
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
			iGround = ImageIO.read(getClass().getResourceAsStream("/IceBlock.png"));
			iCrane = ImageIO.read(getClass().getResourceAsStream("/noback.png"));
			background = ImageIO.read(getClass().getResourceAsStream("/background.png"));
			rope = ImageIO.read(getClass().getResourceAsStream("/blueCgain.png"));
			iBuilding = ImageIO.read(getClass().getResourceAsStream("/building1.png"));

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


		cable = new Cable((int) screenWidth / 4, -100, 550, 550, rope);
		add(cable);

		Building groundZero = new Building((int) (screenWidth / 4), (int) (screenHeight / 4*3), groundWidth * 5, groundHeight * 5,iGround);
		add(groundZero);
		stack.add(groundZero);
		numBuildings++;
		
		// create and place ground objects
		makePlatform(14, (int) (screenWidth / 4), (int) (screenHeight / 4 * 3), groundObjectList1);
		makePlatform(14, (int) (screenWidth / 4 * 3), (int) (screenHeight / 4 * 3), groundObjectList2);

	
		
		prev = stack.get(numBuildings - 1);

		//cut code
//		BuildingCut temp = new BuildingCut(1000, 500, groundWidth * 7, groundHeight * 7, iGround);
//		add(temp);
//		
//		temp.cut(40,40,40,40);
//		


		repaint();
	
	


	}
	

	public void update() {
		if (keyH.isEscape()) {
			keyH.setEscape(false);
			isPaused = !isPaused;
		}
		if (!isPaused) {
			 
			cable.act();

			if(currentBuilding == null){
				currentBuilding = new Building((int)cable.getEndX(),(int)cable.getEndY(),groundWidth*5,groundHeight*5,iBuilding);
				add(currentBuilding,this.getComponentZOrder(prev)-1);
				System.out.println(this.getComponentZOrder(prev)+" "+this.getComponentZOrder(currentBuilding));

			}
			
			
			//if not dropping
			if (!currentBuilding.getDrop()) {

				currentBuilding.setX(cable.getEndX());
				currentBuilding.setY(cable.getEndY());
				
				if (mouseH.isClicked() == true || keyH.isSpacebar()) {
					keyH.setSpacebar(false);
					mouseH.setClicked(false);
					currentBuilding.drop(cable.getDx(), cable.getDy(), cable.getDirection());
					
				}
			} else {
				
				
				currentBuilding.act();
				// only works when there is more than one building... Solution: make an invisible building with bounds of the platform class
				if ( currentBuilding.collides(prev, cable.getDirection()) ) {
					System.out.println(" jcollided");
					currentBuilding.setDrop(false);
					Dimension r = currentBuilding.getSize();
					stack.add(currentBuilding);
					numBuildings++;
					
					prev = stack.get(numBuildings - 1);
					// adding a buildng what
					currentBuilding = null;
					
					for(int i = 0; i<stack.size();i++){
						Building building = stack.get(i);
						building.setY(building.getY()+150);
					}

				}
				else if(currentBuilding.getY()>2000){
					System.out.println("you failed");
					currentBuilding = null;
				}

			}
		
			
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

				add(ground, 1);


				list.add(ground);
				repaint();

			}

		}
	}
	public void playSE(int i) {

		soundEffects.setFile(i);
		soundEffects.play();
	}
	

}