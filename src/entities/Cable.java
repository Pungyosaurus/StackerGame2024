package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * 
 * @author Jason Wong June 2024 A cable with pendulum movement!
 */
public class Cable extends GameObject {

	private final int WIDTH, HEIGHT;
	// DA = change in angle
	private final double DA = 30, STARTINGANGLE = Math.PI / 4, d2a = 1;

	private double da = DA;
	private boolean direction;
	private int sizeChanger = -1;
	private int mode;
	private double endX, endY;

	/**
	 * Initializes fields and resets the sprite angle to the default
	 * 
	 * @param x     is an integer
	 * @param y     is an integer
	 * @param w     is an integer
	 * @param h     is an integer
	 * @param image that is not null
	 */
	public Cable(int x, int y, int w, int h, BufferedImage image) {
		super(x, y, w, h, image);
		spriteAngle = STARTINGANGLE;
		this.WIDTH = w;
		HEIGHT = h;
	}

	/**
	 * Changes the pendulum "mode" Using variable sizeChanger to alter the shrinking
	 * and growing of the cable (reduces an unnecessary comparison) Resets the size
	 * and angle of the sprite Switches and returns the mode (this isn't used but
	 * could be useful)
	 * 
	 */
	public boolean changeMode() {
		setSize(WIDTH, HEIGHT);
		spriteAngle = STARTINGANGLE;
		da = DA;
		if (mode == 1) {
			mode = 0;
		} else {
			mode = 1;
		}
		sizeChanger *= -1;
		return false;
	}


	/**
	 * Calculates the end position of the cable using Pythagorean Theorem with some
	 * degree of error
	 */
	public void calculateEndPosition() {
		// Calculating the diagonal using pythagorean theorem
		double diagonalLength = Math.sqrt(Math.pow(getWidth(), 2) + Math.pow(getHeight(), 2));
		double endAngle = spriteAngle + Math.PI / 4; // Starting angle position is 45 degrees
//		endX = getX() + getWidth() * Math.cos(endAngle) - getHeight() * Math.sin(endAngle);
//		endY = getY() + getHeight() * Math.cos(endAngle) + getWidth() * Math.sin(endAngle);

		// translating the points
		endX = getX() + diagonalLength * Math.cos(endAngle);
		endY = getY() + diagonalLength * Math.sin(endAngle);

	}

	/**
	 * Changes the angle using a second difference to model a real life pendulum
	 * Resizes the sprite to shrink and grow
	 */
	@Override
	public void act() {
		
		if (da < 0) {
			direction = true;
			if (spriteAngle < Math.PI / 4) // if the cable is going against gravity, slow down
				da += d2a;
			else			// if the cable is going with gravity, speed up
				da -= d2a;
		} else if (da > 0) {
			direction = false;
			if (spriteAngle > Math.PI / 4)
				da -= d2a; // if the cable is going against gravity, slow down
			else			// if the cable is going with gravity, speed up
				da += d2a;
		} else if (!direction) // accounting for all cases, da may = 0
			da -= d2a;
		else
			da += d2a;
		// Scaling the image to make it more realistic
		setSize(getWidth() + sizeChanger * (int) (da / 2), getHeight());
		// Rotating the image
		spriteAngle += Math.toRadians(da / 15);
		// getting the end position after movement to update the building position
		calculateEndPosition();

	}

	/**
	 * Overriding the paint method because AffineTransform bounds were not working 
	 * Uses graphics to rotate the image
	 */
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		try {
			if (spriteAngle != 0) {
				// using a temporary rotation to get the updated bounds
				AffineTransform rotation = AffineTransform.getRotateInstance(spriteAngle);
				Rectangle rotatedBounds = rotation
						.createTransformedShape(new Rectangle(0, 0, sprite.getWidth(), sprite.getHeight())).getBounds();
				g2d.setClip(rotatedBounds);
				// using graphics to rotate the image
				g2d.rotate(spriteAngle);
				g2d.drawImage(sprite, 0, 0, getWidth(), getHeight(), this);
			}
		} catch (Exception e) {
			g.drawImage(sprite, 0, 0, getWidth(), getHeight(), this);
		}

	}

	/**
	 * 
	 * @return the end x location of the cable
	 */
	public double getEndX() {
		return endX;
	}

	/**
	 * 
	 * @return the end y location of the cable
	 */
	public double getEndY() {
		return endY;
	}

	/**
	 * 
	 * @return the positive angle and some alterations
	 */
	public double getSpriteAngle() {
		return Math.abs(this.spriteAngle / 2 - Math.PI / 4) * 5;
	}

	/**
	 * 
	 * @return the scale in which we change the cable This may be applied to the
	 *         building
	 */
	public int getScale() {
		return (int) da / 2;
	}

	/**
	 * 
	 * @return the direction of the cable. Moving left is
	 */
	public boolean getDirection() {
		return direction;
	}

	/**
	 * 
	 * @return the change in x (momentum)
	 */
	public double getDx() {
		return Math.cos(spriteAngle) * 10;
	}

	/**
	 * 
	 * @return the change in y (momentum)
	 */
	public double getDy() {
		return (Math.sin(spriteAngle - Math.toRadians(10)) * 5);
	}

	/**
	 * 
	 * @return the cable mode
	 */
	public int getMode() {
		return mode;
	}
}
