package entities;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
/**
 * 
 * @author Jason and Dave
 * June 2024
 * Small blocks that form a platform for the game to be played on
 *
 */
public class Ground extends GameObject{
	private double sinStart;
			
	public Ground(int x, int y, int w, int h, BufferedImage image, double sinStart) {
		super(x, y, w, h, image);
		
        BufferedImage resizediGround = resizeBuffImage(image, w, h);
        setSprite(resizediGround);
        this.sinStart = sinStart;
		// TODO Auto-generated constructor stub
	}
	/**
	 * Moves the blocks in a cyclic motion
	 */
	@Override
	public void act() {
		// TODO Auto-generated method stub
		setY(getY()+ Math.sin(sinStart));
		sinStart+=1;
	}
	
	

}
