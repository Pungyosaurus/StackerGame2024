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
			iGround = ImageIO.read(getClass().getResourceAsStream("/IsoGround.png"));
			// Image is 800x600 and needs to be scaled down			
            resizediGround = resizeImage(iGround, 160, 120);
			//resizediGround = iGround.getScaledInstance(200, 150, Image.SCALE_SMOOTH);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
        BufferedImage resizedImage = new BufferedImage(width, height, originalImage.getType());
        Graphics2D g2d = resizedImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.drawImage(originalImage, 0, 0, width, height, null);
        g2d.dispose();
        return resizedImage;
    }
	
	public void setup() {
		
		getImages();
		
		
		ground = new Ground(200, 150, resizediGround);
		ground.setX(100);
		ground.setY(100);
		ground.setColor(Color.blue);
		add(ground);
	}
	
	public void update() {
		ground.setX(ground.getX()+1);
		
	}
	
	
	

}
