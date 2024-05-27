package entities;

import java.awt.image.BufferedImage;

public class Crane extends GameObject {

	public Crane(int x, int y, int w, int h, BufferedImage image) {
		super(x, y, w, h, image);
		BufferedImage resizedCrane = resizeBuffImage(image, w, h);
        setSprite(resizedCrane);
		
	}
	
	private int connectX, connectY;

	@Override
	public void act() {
		// TODO Auto-generated method stub
		
	}

}
