package main;

import java.awt.Color;

import javax.swing.JFrame;

import entities.Ground;

public class Stacker extends GamePanel{

	private Ground ground;
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
	
	public void setup() {
		System.out.println("HsdfssI");
		ground = new Ground(100, 100, null);
		ground.setX(100);
		ground.setY(100);
		ground.setColor(Color.blue);
		add(ground);
	}
	
	public void update() {
		ground.setX(400);
		System.out.println(ground);
		ground.setX(ground.getX()+1);
		
	}
	
	
	

}
