package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import entities.Building;
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
	

	private Cable cable;
	private Crane crane1;
	private Building building;
	private KeyHandler keyH;
	private MouseHandler mouseH;
	private BufferedImage background, iGround, iCrane,  rope;
	

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
//			rope = ImageIO.read(getClass().getResourceAsStream("/bgANGLED.png"));
			rope = ImageIO.read(getClass().getResourceAsStream("/noTrim.png"));
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

		int startCraneX = (int) (screenWidth / 8-600);
		int startCraneY = (int) (screenHeight / 8+200);

		crane1 = new Crane(startCraneX, startCraneY, 800, 800, iCrane);
		add(crane1);
		cable = new Cable((int)screenWidth/4, -200, 550, 550, rope);
		add(cable);
		
		
		
		// create and place ground objects

		// keep odd

		makePlatform(14, (int) (screenWidth / 4), (int) (screenHeight/4 *3), groundObjectList1);
		makePlatform(14, (int) (screenWidth / 4 * 3), (int) (screenHeight/4 *3), groundObjectList2);
		
		
		
		repaint();
//		ground = new Ground (120,150,90,125,iGround);
//		add(ground);
//		

	}

	

	int counter = 0;
	int counter2 = 11;

	public void update() {
		if (keyH.isEscape()) {
			keyH.setEscape(false);
			isPaused = !isPaused;
		}
		if (!isPaused) {
			
			if(building == null){
//				building = new Building((int)(screenWidth/4),(int)(screenHeight/2),groundWidth*7,groundHeight*7,iGround);
				building = new Building((int)cable.getEndX(),(int)cable.getEndY(),groundWidth*7,groundHeight*7,iGround);
				add(building,3);
			}
			if(building!= null){
				if(!building.getDrop()){
//					building.act();
					building.setX(cable.getEndX());
					building.setY(cable.getEndY());
					building.setSize(building.getWidth()+cable.getScale()/2,building.getHeight()+cable.getScale()/2);
//					System.out.println(cable.getDx()+" "+cable.getDy());
					
					if (mouseH.isClicked() == true || keyH.isSpacebar()) {
						mouseH.setClicked(false);
						keyH.setSpacebar(false);
						
						System.out.println("in");
						building.drop(cable.getDx(),cable.getDy(), cable.getDirection());
						
						

					}
				}else{
					building.act();
					if(building.getY()>4000){
						building = null;
					}
					
				}
			}
			
			cable.act();
			// add code to drop the block here

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

//			ground.setX(ground.getX() + 1);
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
			
			for(int i = 0; i<=amount;i++) {
				try {
					TimeUnit.MILLISECONDS.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				Ground ground = new Ground( startX + groundWidth*i - 40*i -40*amount , startY + (int) (j*groundHeight*.25) , groundWidth, groundHeight, iGround, 180/depth*j);
				Ground ground = new Ground( startX + (groundWidth-30)*i - ((groundWidth-30)/2)*amount , startY + (int) (j*groundHeight*.20) , groundWidth, groundHeight, iGround, 180/depth*j);

				add(ground,1);

			
//				try {
//					TimeUnit.MILLISECONDS.sleep(1);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				setComponentZOrder(ground, 1);
				list.add(ground);
				repaint();
			
		}

	}
	}

}