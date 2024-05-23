package main;

import javax.swing.JFrame;

public class Stacker extends GamePanel{

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
		System.out.println("HsI");
	}
	
	public void update() {
		System.out.println("pooo");
	}
	
	
	

}
