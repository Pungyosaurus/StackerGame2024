package entities;

import java.awt.image.BufferedImage;

public class Ground extends GameObject{

	public Ground(int w, int h, BufferedImage image) {
		super(w, h, image);
        BufferedImage resizediGround = resizeBuffImage(image, w, h);
        setSprite(resizediGround);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void act() {
		// TODO Auto-generated method stub
		
	}

}
