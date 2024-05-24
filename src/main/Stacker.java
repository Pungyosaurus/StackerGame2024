package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import entities.Ground;

public class Stacker extends GamePanel{

	private Ground ground;
	private BufferedImage iGround, resizediGround;

	
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
	
	public void getImages(){
		try {
			iGround = ImageIO.read(getClass().getResourceAsStream("/IsoGround.png"));
			// Image is 800x600 and needs to be scaled down	

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void setup() {
		
		getImages();
		
		
		ground = new Ground(120, 90, iGround);
//		ground.setSprite("/IsoGround.png");
		ground.setX(100);
		ground.setY(100);
		ground.setColor(Color.blue);
		add(ground);
		System.out.println("hi dave");
	}
	
	public void update() {
		ground.setX(400);
//		ground.setX(ground.getX()+1);
		
	}
	
	
	

}
