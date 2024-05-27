package entities;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Ground extends GameObject{
	private double sinStart;
			
	public Ground(int x, int y, int w, int h, BufferedImage image, double sinStart) {
		super(x, y, w, h, image);
		
        BufferedImage resizediGround = resizeBuffImage(image, w, h);
        setSprite(resizediGround);
        this.sinStart = sinStart;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void act() {
		// TODO Auto-generated method stub
		System.out.println("in");
		setY(getY()+ Math.sin(sinStart)*3);
		sinStart+=5;
	}
	
	

}
