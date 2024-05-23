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
            resizediGround = resizeBuffImage(iGround, 120, 90);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private BufferedImage resizeBuffImage(BufferedImage originalImage, int width, int height) {
        Image temp = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        resizedImage.getGraphics().drawImage(temp, 0, 0, null);
        return resizedImage;
	}
	
//	private BufferedImage resizeImage(BufferedImage originalImage, int width, int height) {
//        BufferedImage resizedImage = new BufferedImage(width, height, originalImage.getType());
//        Graphics2D g2d = resizedImage.createGraphics();
//        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
//        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        g2d.drawImage(originalImage, 0, 0, width, height, null);
//        g2d.dispose();
//        return resizedImage;
//    }
	
	public void setup() {
		
		getImages();
		
		
		ground = new Ground(120, 90, resizediGround);
		ground.setX(100);
		ground.setY(100);
		ground.setColor(Color.blue);
		add(ground);
	}
	
	public void update() {
		ground.setX(400);
//		ground.setX(ground.getX()+1);
		
	}
	
	
	

}
