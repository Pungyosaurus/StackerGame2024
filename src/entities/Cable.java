package entities;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Cable extends GameObject {
	private final int WIDTH, HEIGHT;
	private boolean direction;
	private double da = 30;
	private final double d2a = 1;
	private int sizeChanger = -1;
	private final double DA = da, startingAngle = Math.PI / 4;
	private int mode = 1;
	private double endX, endY;

	public Cable(int x, int y, int w, int h, BufferedImage image) {
		super(x, y, w, h, image);
		spriteAngle = startingAngle;
		this.WIDTH = w;
		HEIGHT = h;
	}

	public void changeMode() {
		setSize(WIDTH, HEIGHT);
		spriteAngle = startingAngle;
		da = DA;
		mode = (mode == 1) ? 0 : 1;
		sizeChanger *= -1;
	}

	public void calculateEndPosition() {
		double diagonalLength = Math.sqrt(Math.pow(getWidth(), 2) + Math.pow(getHeight(), 2));
		double endAngle = spriteAngle + Math.PI / 4; // Starting angle position is 45 degrees

		
		
//		endX= getX() + getWidth() / 2;
//		endY = getY() + getHeight();
		endX = getX() + diagonalLength * Math.cos(endAngle);
		endY = getY() + diagonalLength * Math.sin(endAngle);

		if (direction) {
			endX-= da/ 4;
			endY-= da / 4;
		} else {
			endX+= da / 4;
			endY+= da / 4;
		}
	}
	@Override
	public void act() {

		
		if (da < 0) {
			direction = true;
			if (spriteAngle < Math.PI / 4)
				da += d2a;
			else
				da -= d2a;
		} else if (da > 0) {
			direction = false;
			if (spriteAngle > Math.PI / 4)
				da -= d2a;
			else
				da += d2a;
		} else if (!direction)
			da -= d2a;
		else
			da += d2a;

//		setSize(getWidth() + sizeChanger * (int) (da / 2), getHeight());
		spriteAngle += Math.toRadians(da / 15);
		calculateEndPosition();
		System.out.println(da + "cabl");

	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		try {
			if (spriteAngle != 0) {
				AffineTransform rotation = AffineTransform.getRotateInstance(spriteAngle);
				Rectangle rotatedBounds = rotation
						.createTransformedShape(new Rectangle(0, 0, sprite.getWidth(), sprite.getHeight())).getBounds();
				g2d.setClip(rotatedBounds);
				g2d.rotate(spriteAngle);
				g2d.drawImage(sprite, 0, 0, getWidth(), getHeight(), this);
			}
		} catch (Exception e) {
			g.drawImage(sprite, 0, 0, getWidth(), getHeight(), this);
		}
	}

	

	public double getEndX() {
		return endX;
	}

	public double getEndY() {
		return endY;
	}

	public int getMode() {
		return mode;
	}

	public double getSpriteAngle() {
		return Math.abs(this.spriteAngle / 2 - Math.PI / 4) * 5;
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
