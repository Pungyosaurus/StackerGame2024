package entities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

public abstract class GameObject extends JComponent {
	private Color c = Color.white;
	private static Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	protected static int windowWidth = (int) screenSize.getWidth();
	protected static int windowHeight = (int) screenSize.getHeight();
	protected BufferedImage sprite;
	protected double spriteAngle;
	protected boolean backgroundElement;
	
	//delete 
	private int width,height;

	public GameObject(int w, int h, BufferedImage image) {
		setSize(w, h);
		sprite = image;
	}
	/**
	 * Overloaded constructor!
	 * @param x
	 * @param y
	 * @param w
	 * @param h
	 * @param image
	 */
	public GameObject(int x, int y, int w, int h, BufferedImage image) {
		this(w, h, image);
		setX(x);
		setY(y);
		//delete
		this.width = w;
		this.height = h;
	}

	public void setSize(int width, int height) {
		super.setSize(width, height);
		//delete
		this.width = width;
		this.height = height;
	}

	public int getWindowWidth() {
		return windowWidth;
	}

	public int getWindowHeight() {
		return windowHeight;
	}

	/**
	 * Gets the x component of the coordinate of the upper left corner of this
	 * object
	 * 
	 * The coordinate is relative to the playing field, with <code>0</code>
	 * being the far left of the field, and positive values moving toward the
	 * right of the field
	 */
	public int getX() {
		return getLocation().x;
	}

	/**
	 * Gets the y component of the coordinate of the upper left corner of this
	 * object
	 * 
	 * The coordinate is relative to the playing field, with <code>0</code>
	 * being the top of the field, and positive values moving toward the bottom
	 * of the field
	 */
	public int getY() {
		return getLocation().y;
	}

	/**
	 * Sets the x (horizontal) position of this object
	 * 
	 * Setting the x position will not affect the y position
	 * 
	 * @param x
	 *            the x position of the upper left corner of this object
	 */
	public void setX(int x) {
		super.setLocation(x, getLocation().y);
	}

	/**
	 * Sets the y (vertical) position of this object
	 * 
	 * Setting the y position will not affect the x position
	 * 
	 * @param y
	 *            the y position of the upper left corner of this object
	 */
	public void setY(int y) {
		super.setLocation(getLocation().x, y);
	}

	public int getCX() {
		return getLocation().x + getWidth() / 2;
	}

	public int getCY() {
		return getLocation().y + getHeight() / 2;
	}

	public void setX(double x) {
		super.setLocation((int) (x + 0.5), getLocation().y);
	}

	public void setY(double y) {
		super.setLocation(getLocation().x, (int) (y + 0.5));
	}

	public double getAngle(int x1, int y1, int x2, int y2) {
		return Math.atan2(y2 - y1, x2 - x1);
	}

	public double getDistance(int x1, int y1, int x2, int y2) {
		return Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
	}

	public void pushIn(int xBound, int yBound) {
		if (getX() < 0) {
			setLocation(0, getY());
		}
		if (getX() > xBound - getWidth()) {
			setLocation(xBound - getWidth(), getY());
		}
		if (getY() < 0) {
			setLocation(getX(), 0);
		}
		if (getY() > yBound - getHeight()) {
			setLocation(getX(), yBound - getHeight());
		}
	}

	public boolean outOfBounds() {
		return getX() < 0 || getX() + getWidth() > windowWidth || getY() < 0 || getY() + getHeight() > windowHeight;
	}

	public boolean outOfScreen() {
		return getX() + getWidth() < 0 || getX() > windowWidth || getY() + getHeight() < 0 || getY() > windowHeight;
	}

	public void setColor(Color c) {
		this.c = c;
	}

	public BufferedImage setSprite(String fileName) {
		Image image = null;
		try {
			image = ImageIO.read(getClass().getResourceAsStream(fileName));
		} catch (IOException e) {
		}

		ImageFilter filter = new RGBImageFilter() {
			public final int filterRGB(int x, int y, int rgb) {
				if (rgb == Color.BLACK.getRGB()) {
					return 0x00FFFFFF & rgb;
				}
				return rgb;
			}
		};
		image = Toolkit.getDefaultToolkit().createImage(new FilteredImageSource(image.getSource(), filter));
		BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null),
				BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = bufferedImage.createGraphics();
		g2d.drawImage(image, 0, 0, null);
		g2d.dispose();
		return bufferedImage;
	}
	
	public void setSprite(BufferedImage image) {
		sprite = image;
	}
	
	
	public BufferedImage resizeBuffImage(BufferedImage originalImage, int width, int height) {
        Image temp = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resizedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        resizedImage.getGraphics().drawImage(temp, 0, 0, null);
        return resizedImage;
	}

	public void paintComponent(Graphics g) {
		// if no image set, then just draw a rectangle
		if (sprite == null) {
			g.setColor(c);
			g.fillRect(0, 0, (int) getBounds().getWidth(), (int) getBounds().getHeight());
		} else {
			try {
				AffineTransform at = new AffineTransform();
				if (spriteAngle != 0) {
					at.rotate(spriteAngle, getWidth() / 2, getHeight() / 2);
				}
				at.scale(getWidth() * 1.0 / sprite.getWidth(), getHeight() * 1.0 / sprite.getHeight());
				Graphics2D g2d = (Graphics2D) g;
				g2d.drawImage(sprite, at, null);
			} catch (Exception e) {
				g.drawImage(sprite, 0, 0, getWidth(), getHeight(), this);
			}
		}
		
		g.setColor(Color.RED);

		int size = 100;

//		g.fillOval(width/2-50, height-50, size, size);

	}

	public boolean collides(GameObject o) {
		return getBounds().intersects(o.getBounds());
	}

	public abstract void act();
}