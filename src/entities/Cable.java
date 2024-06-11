package entities;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Cable extends GameObject {
	private final int WIDTH, HEIGHT;
	// zero is moving to the left, 1 is moving to the right
	private boolean direction;
	private double da = 30;
	private final double d2a = 1;

	public Cable(int x, int y, int w, int h, BufferedImage image) {
		super(x, y, w, h, image);
		spriteAngle = Math.PI / 4;
		this.WIDTH = w;
		HEIGHT = h;
	}

	@Override
	public void act() {

		if (da < 0) {
			// going right
			direction = true;
			if (spriteAngle < Math.PI / 4)
				// going to the right and past 90 (slowing down)
				da += d2a;
			else
				// going to the right and not past 90 (speeding up)
				da -= d2a;
		} else if (da > 0) {
			// going left
			direction = false;
			if (spriteAngle > Math.PI / 4)
				// going to the left and past 90 (slowing down)
				da -= d2a;

			else

				// going to the left and not past 90 (speeding up)
				da += d2a;
		} else if (!direction) // cases when da = 0;
			da -= d2a;
		else
			da += d2a;

		setSize(getWidth() + (int) (da / 2), getHeight() + (int) (da / 10));
		spriteAngle += Math.toRadians(da / 15);
	}

	@Override
	public void paintComponent(Graphics g) {

		Graphics2D g2d = (Graphics2D) g;
		try {
			if (spriteAngle != 0) {

				// bounding box of rotated object
				AffineTransform rotation = AffineTransform.getRotateInstance(spriteAngle);
				Rectangle rotatedBounds = rotation
						.createTransformedShape(new Rectangle(0, 0, sprite.getWidth(), sprite.getHeight())).getBounds();

				// KEY CHANGE
				g2d.setClip(rotatedBounds);
				g2d.rotate(spriteAngle);
				g2d.drawImage(sprite, 0, 0, getWidth(), getHeight(), this);
			}
		} catch (Exception e) {
			g.drawImage(sprite, 0, 0, getWidth(), getHeight(), this);

		}

	}

	public double getSpriteAngle() {
		return Math.abs(this.spriteAngle/2 - Math.PI / 4) * 5;
	}

	public double getEndX() {
		return  getX() - (Math.cos(getSpriteAngle())+1) +250 -getWidth();


	}

	public double getEndY() {
		return getY() + getHeight()/1.3 + (Math.sin(getSpriteAngle()))*100;
	}

	public int getScale() {
		return (int) da / 2;
	}

	public boolean getDirection() {
		return direction;
	}

	public double getDx() {

		return Math.cos(spriteAngle) * 10;
	}

	public double getDy() {
		return (Math.sin(spriteAngle - Math.toRadians(10)) * 5);

	}

}
