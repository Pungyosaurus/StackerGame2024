package entities;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Ground extends GameObject{
	
			
	public Ground(int x, int y, int w, int h, BufferedImage image) {
		super(x, y, w, h, image);
		
        BufferedImage resizediGround = resizeBuffImage(image, w, h);
        setSprite(resizediGround);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void act() {
		// TODO Auto-generated method stub
		
	}

}
