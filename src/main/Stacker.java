package main;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import entities.Ground;

public class Stacker extends GamePanel{

	private Ground ground;
	private BufferedImage iGround;

	
	public static void main(String[] args) {
		// TODO Auto-generated method stub


		JFrame window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setTitle("Construction Crane Chaos");
		
		Stacker stacker = new Stacker();
		window.add(stacker);
		
	
		window.pack();
		
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		
		stacker.startGameThread();		
	}
	
	public void getImages(){
		try {
			iGround = ImageIO.read(getClass().getResourceAsStream(""));
			resizedBricks = bricks.getScaledInstance(width, length, Image.SCALE_SMOOTH);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setup() {
		
		getImages();
		
		
		ground = new Ground(100, 100, null);
		ground.setX(100);
		ground.setY(100);
		ground.setColor(Color.blue);
		add(ground);
	}
	
	public void update() {
		ground.setX(ground.getX()+1);
		
	}
	
	
	

}
