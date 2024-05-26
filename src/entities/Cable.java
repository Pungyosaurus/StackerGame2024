package entities;

import java.awt.image.BufferedImage;

public class Cable extends GameObject {
	
	private final double MAX_ANGLE = 0.785398163397449 + 0.7689920684287006
;
	private final double MIN_ANGLE = -0.9599310885968821 + 0.7689920684287006
;
	private final int WIDTH;
	// zero is to the left (adding), one is to the right (subtracting)
	private boolean direction;
	private double da = 49;
	private final double d2a = 2;
	

	public Cable(int x, int y, int w, int h, BufferedImage image) {
		super(x, y, w, h, image);
		this.WIDTH = w;
		spriteAngle = Math.PI / 4;
	}

	@Override
	public void act() {
		
		if(da < 0) {
			if(spriteAngle < Math.PI / 4) 
				da += d2a;
			else
				da -= d2a;
		}else if(da > 0) {
			if(spriteAngle > Math.PI / 4) 
				da -= d2a;
			else
				da += d2a;
		}
		
		
		
		
		spriteAngle += Math.toRadians(da / 10);

		
		
		
		

		
	}
	
	public double getSpriteAngle() {
		return this.spriteAngle;
	}

}
