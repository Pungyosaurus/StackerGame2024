package entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * @author Dave Singh June 2024 A Block Builder that allows you to cut, resize and manipulate isometric blocks
 */

public class Building extends GameObject {
	
	private int[] totalCutValues = new int[4];

	private double xSpeed = 0, ySpeed = 0;
	private int gravity = 1;
	private boolean drop = false;

	private static BufferedImage img1, img2, img3;
	private static int TOP_WIDTH, TOP_HEIGHT;

	private int backLeft, frontLeft, frontRight, backRight;

	private double by;
	private double bx;
	private double tx;
	private double ty;

	private double rightFaceWidth;
	private double rightFaceHeight;
	private int leftFaceWidth;
	
	
	private int leftAdjustmentY;
	private int RightAdjustmentY;
	private int minYAdjustment;
	

	
	/**
	 * Initializes fields and combines the image to make the inital sprite
	 * 
	 * @param x     is an integer
	 * @param y     is an integer
	 * @param w     is an integer
	 * @param h     is an integer
	 */
	public Building(int x, int y, int width, int height) {
		super(x, y, width, height);

		BufferedImage combinedImage = null;
		combinedImage = combineImages(img1, img2, img3);
		setSprite(combinedImage);
	}

// Copy constructor that never worked :(
//	public Building(Building original) {
//        super(original.getX(), original.getY(), original.getWidth(), original.getHeight());
//
// 
//        this.drop = original.drop;
//
//        this.backLeft = original.backLeft;
//        this.frontLeft = original.frontLeft;
//        this.frontRight = original.frontRight;
//        this.backRight = original.backRight;
//
//        this.by = original.by;
//        this.bx = original.bx;
//        this.tx = original.tx;
//        this.ty = original.ty;
//
//        this.rightFaceWidth = original.rightFaceWidth;
//        this.rightFaceHeight = original.rightFaceHeight;
//        this.leftFaceWidth = original.leftFaceWidth;
//
//        this.leftAdjustmentY = original.leftAdjustmentY;
//        this.RightAdjustmentY = original.RightAdjustmentY;
//        this.minYAdjustment = original.minYAdjustment;
//
//        BufferedImage combinedImage = combineImages(original.croppedLeftFace, original.croppedRightFace , original.croppedImage);
//        setSprite(combinedImage);
//    }

	
	/**
	 * Loads images, can add whatever image you like in place of the file paths
	 * @param width is an integer
	 * @param height is an integer
	 */
	public static void loadImages(int width, int height) {
		
		
		try {
			//static loadImages
			img1 = ImageIO.read(Building.class.getResourceAsStream("/textures/leftFace.png"));
			img2 = ImageIO.read(Building.class.getResourceAsStream("/textures/rightFace.png"));
			img3 = ImageIO.read(Building.class.getResourceAsStream("/textures/topFace.png"));

			//resize them to suite needs
			img1 = resizeBuffImage(img1, img1.getWidth() + width, img1.getHeight() + height);
			img2 = resizeBuffImage(img2, img2.getWidth() + width, img2.getHeight() + height);
			img3 = resizeBuffImage(img3, img3.getWidth() + 2 * width, img3.getHeight() + height);
			
			// set static topWidth and topHeight
			TOP_WIDTH = img3.getWidth();
			TOP_HEIGHT = img3.getHeight();

		} catch (IOException e) {

			e.printStackTrace();

		}
	}

	
	/**
	 * Called every tick to move the block when dropped
	 */
	public void act() {

		//move down at gravity speed
		ySpeed += gravity;
		setY(getY() + (int) ySpeed);
		//keep x speed constant
		setX(getX() + (int) xSpeed);

	}

	
	/**
	 * Sets the drop speed and location based on how it was released
	 * @param dx
	 * @param dy
	 * @param direction
	 */
	public void drop(double dx, double dy, boolean direction) {

		if (direction) {
			xSpeed = dx * 1.5;
		} else {
			xSpeed = -dx * 1.5;
		}
		ySpeed = dy;
		drop = true;

	}
	
	/**
	 * overloaded constructor that does'nt include direction for setting drop speed and location
	 * @param dx
	 * @param dy
	 */

	public void drop(double dx, double dy) {
		xSpeed = -dx * 3;
		ySpeed = dy;
		drop = true;
	}

	

	
	/**
	 * checks for collision with previous block and return new values to cut by
	 * @param previousX
	 * @param previousY
	 * @param direction
	 * @return array of cut values
	 */
	public int[] collides(double previousX, double previousY, int direction) {
		
		
		//get current location for intersection test
		double x = getX() + getBottomMiddleX();
		double y = getY() + getBottomMiddleY();

		
		
		// if moving bottom left to top right
		if (direction == 1) {
			
			//calculate the b values in y=mx+b to check if blocks are in line
			double previousB = previousY + 1 / Math.sqrt(3) * previousX;
			double b = y + 1 / Math.sqrt(3) * x;
			
			
			//check if the collision is in the same plane and should be checked farther
			if (previousB - b < 20 && previousB - b > -20) {

				//distance between points
				int distance = (int) Math.sqrt(Math.pow(x - previousX, 2) + Math.pow(y - previousY, 2));

				//compare distance between current collision point and previous and confirm collisions
				if ( distance < rightFaceWidth / Math.sqrt(3) * 2) {
					
					//init values to show how much of the block to cut
					int[] offsetValues = new int[6];
					
					//calculate how far collision points are from one another
					 distance = (int) Math.sqrt(Math.pow(x - previousX, 2) + Math.pow(y - previousY, 2));

					 // set cut values of correct faces, 
					if (x > previousX) {
						offsetValues[3] = distance;
					} else {
						offsetValues[2] = distance;
					}
					
					//return how much of an offset there was due to the speed of the collision
					offsetValues[4] = (int) (previousB - b);
					//return how much the image was cropped by to adjust for it
					offsetValues[5] = (int) (rightFaceHeight + minYAdjustment);

					return offsetValues;
				}
			}
		}
		
		
		
		// if moving top left to bottom right
		if (direction == 0) {
			
			
			//calculate the b values in y=mx+b to check if blocks are in line
			double previousB = previousY - 1 / Math.sqrt(3) * previousX;
			double b = y - 1 / Math.sqrt(3) * x;
			
			
			//check if the collision is in the same plane and should be checked farther
			if (previousB - b < 20 && previousB - b > -20) {
					
				//distance between points
				int distance = (int) Math.sqrt(Math.pow(x - previousX, 2) + Math.pow(y - previousY, 2));

				
				//compare distance between current collision point and previous and confirm collisions
				if (distance< leftFaceWidth / Math.sqrt(3) * 2) {
					
					//init values to show how much of the block to cut
					int[] offsetValues = new int[6];
					
					 // set cut values of correct faces, 
					if (x > previousX) {
						offsetValues[1] = distance;
					} else {
						offsetValues[0] = distance;
					}
					
					//return how much of an offset there was due to the speed of the collision
					offsetValues[4] = (int) (previousB - b);
					
					//return how much the image was cropped by to adjust for it
					offsetValues[5] = (int) (rightFaceHeight + minYAdjustment);

					return offsetValues;
				}
			}
		}
		
		
		// no array values collision was a fail :(
		return null;

	}

	
	
	/**
	 * Combine the 2d faces of an isometric cube into one face.
	 * @param leftFace
	 * @param rightFace
	 * @param topFace
	 * @return buffered combined image 
	 */
	public BufferedImage combineImages(BufferedImage leftFace, BufferedImage rightFace, BufferedImage topFace) {

		//get the width of the new buffered image
		int combinedWidth = leftFace.getWidth() + rightFace.getWidth();
		
		//store this widths for use in other classes and adjustments
		rightFaceWidth = rightFace.getWidth();
		leftFaceWidth = leftFace.getWidth();

		
		//get the height of the new buffered image

		int combinedHeight = (int) (img1.getHeight() - img1.getWidth() / Math.sqrt(3) + topFace.getHeight());
		
		//store this widths for use in other classes and adjustments
		rightFaceHeight = (int) (combinedHeight - TOP_HEIGHT - topFace.getHeight());

		
		//create the new buffered image
		BufferedImage combined = new BufferedImage(combinedWidth, combinedHeight, BufferedImage.TYPE_INT_ARGB);

		//create graphics on buffered image
		Graphics2D g2d = combined.createGraphics();

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		

		//Draw topFace at 0,0 because it is the corner
		g2d.drawImage(topFace, 0, 0, null);
		
		// Draw the left face at the correct location keeping in mind the adjustments
		g2d.drawImage(leftFace, 0, (int) leftAdjustmentY - minYAdjustment, null);

		//draw the right face where the left face ends keeping in mind the adjustments
		g2d.drawImage(rightFace, combinedWidth - rightFace.getWidth(), (int) RightAdjustmentY - minYAdjustment, null);

		
		//set size of the object to the buffered image so that sizing is correct
		this.setSize(combinedWidth, combinedHeight);

		
		
		
		// sets the location of the collision points that will be used in the collision code
		
		// sets this location at the bottom middle of the top face
		int x1 = (int) (topFace.getWidth() - rightFace.getWidth());
		int y1 = topFace.getHeight();
		setTopMiddleX(x1);
		setTopMiddleY(y1 - minYAdjustment);

		//sets this location at the bottom middle of the image
		y1 = combined.getHeight();
		setBottomMiddleX(x1);
		setBottomMiddleY(y1 - minYAdjustment);

		g2d.drawImage(combined, 0, 0, null);

		g2d.dispose();

		return combined;

	}

	
	/**|
	 * Pass the parameters of the lengths of each size you want to cut, and cuts the 2d isometric cube to the correct dimensions
	 * @param backLeftP
	 * @param frontLeftP
	 * @param frontRightP
	 * @param backRightP
	 * @return boolean based on if the cut was successful or not
	 */
	public boolean cut(int backLeftP, int frontLeftP, int frontRightP, int backRightP) {

		//store values in array for checking in other classes, stores total cut values accumulated ie. you can cut the same block multiple times in succession
		totalCutValues[0] += backLeftP;
		totalCutValues[1] += frontLeftP;
		totalCutValues[2] += frontRightP;
		totalCutValues[3] += backRightP;

		
		//set variables to the values for easy code reading
		backLeft = totalCutValues[0];
		frontLeft = totalCutValues[1];
		frontRight = totalCutValues[2];
		backRight = totalCutValues[3];

		
		//ensure that the cut values are less than the dimensions of the image, don't want to cut negative 
		if (backLeftP > leftFaceWidth / Math.sqrt(3) * 2 || frontLeftP > leftFaceWidth / Math.sqrt(3) * 2
				|| frontRightP > rightFaceWidth / Math.sqrt(3) * 2 || backRightP > rightFaceWidth / Math.sqrt(3) * 2) {
			System.out.println("CUT IS TOOO MUCH");
			
			//indicate the cut was a fail
			return false;
		}
		
		
		//cut the left face by both the back left and front left amount keeping in mind the trig involved with a 1:sqrt(3) isometric cube
		int startX = (int) (backLeft / 2 * Math.sqrt(3));
		int startY = (int) (backLeft / 2);
		int width = (int) (img1.getWidth() - backLeft / 2 * Math.sqrt(3) - frontLeft / 2 * Math.sqrt(3));
		int height = img1.getHeight() - backLeft / 2 - frontLeft / 2;
		
		//if width or height is less than 0, the image cannot be cut and will report failed
		if (width <= 0 || height <= 0) {
			return false;
		}
		//crop the left face
		BufferedImage croppedLeftFace = img1.getSubimage(startX, startY, width, height);

		
		
		//same thing as above but for right face
		startX = (int) (frontRight / 2 * Math.sqrt(3));
		startY = (int) (backRight / 2);
		width = (int) (img2.getWidth() - frontRight / 2 * Math.sqrt(3) - backRight / 2 * Math.sqrt(3));
		height = img2.getHeight() - frontRight / 2 - backRight / 2;
		if (width <= 0 || height <= 0) {
			return false;
		}
		//crop right face
		BufferedImage croppedRightFace = img2.getSubimage(startX, startY, width, height);

		
		
		
		//cropping top face is difficult because it is a randomly shaped Quadrilateral, therefore we will use the path class
		BufferedImage croppedTopFace = new BufferedImage(TOP_WIDTH, TOP_HEIGHT, BufferedImage.TYPE_INT_ARGB);

		//create a new buffered image of the orginal size
		Graphics2D g2d = croppedTopFace.createGraphics();

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		//create a new path
		Path2D.Double topPath = new Path2D.Double();

		//start at 0,0
		topPath.moveTo(0, 0);

		//move the crop region around to save the correct part we require again keeping in mind the trig
		topPath.lineTo(frontRight * Math.sqrt(3) / 2, TOP_HEIGHT / 2 - frontRight / 2);
		double Xint = TOP_WIDTH / 2 + frontRight * Math.sqrt(3) / 2 - frontLeft * Math.sqrt(3) / 2;
		double Yint = TOP_HEIGHT - frontRight / 2 - frontLeft / 2;
		topPath.lineTo(Xint, Yint);
		topPath.lineTo(TOP_WIDTH - frontLeft * Math.sqrt(3) / 2, TOP_HEIGHT / 2 - frontLeft / 2);
		topPath.lineTo(TOP_WIDTH, 0);
		topPath.lineTo(0, 0);


		//crop the bottom part of the top face
		g2d.setClip(topPath);
		g2d.drawImage(img3, 0, 0, null);

		
		
		//Now we crop the top part of the top face, we will use the already cropped image as the base and crop it again
		BufferedImage croppedcroppedTopFace = new BufferedImage(TOP_WIDTH, TOP_HEIGHT, BufferedImage.TYPE_INT_ARGB);

		//create second crop 
		Graphics2D g2d2 = croppedcroppedTopFace.createGraphics();

		g2d2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		//create second path
		Path2D.Double topPath2 = new Path2D.Double();

		//start at 0,0 and define top part of the crop area
		topPath2.moveTo(0, 0);
		topPath2.lineTo(backLeft / 2 * Math.sqrt(3), TOP_HEIGHT / 2 + backLeft / 2); // gud
		Xint = TOP_WIDTH / 2 + backLeft / 2 * Math.sqrt(3) - backRight / 2 * Math.sqrt(3);
		Yint = backLeft / 2 + backRight / 2;
		topPath2.lineTo(Xint, Yint);
		topPath2.lineTo(TOP_WIDTH - backRight / 2 * Math.sqrt(3), TOP_HEIGHT / 2 + backRight / 2);// minus was a plus
		topPath2.lineTo(TOP_WIDTH, TOP_HEIGHT);
		topPath2.lineTo(0, TOP_HEIGHT);
		topPath2.lineTo(backLeft / 2 * Math.sqrt(3), TOP_HEIGHT / 2 + backLeft / 2); // gud

		//create the second crop region
		g2d2.setClip(topPath2);
		//draw the cropped image from before on the new cropped region to crop it twice
		g2d2.drawImage(croppedTopFace, 0, 0, null);


		g2d.dispose();
		g2d2.dispose();
		
		
		// Find the exact dimensions of the newly cropped top face by going pixel by pixel 
		int[] boundingBox = getBoundingBox(croppedcroppedTopFace);
		
		//area that contains the iamge
		int x = boundingBox[0];
		int y = boundingBox[1];
		int width2 = boundingBox[2] - boundingBox[0];
		int height2 = boundingBox[3] - boundingBox[1];

		// crop the top face a third time to fit it into the perfect dimensions makes the math much easier
		BufferedImage croppedImage = new BufferedImage(width2, height2, BufferedImage.TYPE_INT_ARGB);
		croppedImage = croppedcroppedTopFace.getSubimage(x, y, width2, height2);


	
		//the three images are now perfectly cropped and ready to be combined
		setSprite(combineImages(croppedLeftFace, croppedRightFace, croppedImage));
		//return true to indicate cut was a success
		return true;
	}
	
	/**
	 * returns current drop status
	 * @return drop status
	 */
	public boolean getDrop() {
		return drop;
	}
	
	/**
	 * Sets building to drop
	 * @param state
	 */
	public void setDrop(boolean state) {
		this.drop = state;
	}
	/**
	 * 
	 * @return the sprite of the object
	 */
	public BufferedImage getSprite() {
		return sprite;
	}
	/**
	 * Sets the sprite to the image
	 */
	public void setSprite(BufferedImage sprite) {
		this.sprite = sprite;
	}
	
	
	/**
	 * adds border around the image only where it borders pixels or an edge block to indicate that you achieved a perfect drop
	 * @param borderWidth
	 * @param borderColor
	 */
	public void addBorder(int borderWidth, Color borderColor) {
		
		//get width and height
		int width = sprite.getWidth();
		int height = sprite.getHeight();
		BufferedImage imageWithBorder = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = imageWithBorder.createGraphics();

		// draw previous image
		g2d.drawImage(sprite, 0, 0, null);
		g2d.dispose();

		// edge detection and add border
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (isEdgePixel(x, y, borderWidth)) {
					setBorderPixels(imageWithBorder, x, y, borderWidth, borderColor);
				}
			}
		}

		setSprite(imageWithBorder);
		System.out.println("Added border");
	}

	/**
	 * checks if pixel is an edge
	 * @param x
	 * @param y
	 * @param borderWidth
	 * @return
	 */
	private boolean isEdgePixel(int x, int y, int borderWidth) {
		
		int currentPixel = sprite.getRGB(x, y);
		// if pixel is empty return false
		if (isTransparentOrEmpty(currentPixel)) {
			return false;
		}

		//code from https://stackoverflow.com/questions/15351604/adding-a-border-to-an-image-in-java
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (i == 0 && j == 0)
					continue;
				int neighborX = x + i;
				int neighborY = y + j;
				if (neighborX < 0 || neighborY < 0 || neighborX >= sprite.getWidth()
						|| neighborY >= sprite.getHeight()) {
					return true; // Edge of the image
				}
				if (isTransparentOrEmpty(sprite.getRGB(neighborX, neighborY))) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * set the border pixels to the width and color required
	 * @param image
	 * @param x
	 * @param y
	 * @param borderWidth
	 * @param borderColor
	 */
	private void setBorderPixels(BufferedImage image, int x, int y, int borderWidth, Color borderColor) {
		
		//code from https://stackoverflow.com/questions/15351604/adding-a-border-to-an-image-in-java
		for (int i = -borderWidth; i <= borderWidth; i++) {
			for (int j = -borderWidth; j <= borderWidth; j++) {
				int newX = x + i;
				int newY = y + j;
				if (newX >= 0 && newX < image.getWidth() && newY >= 0 && newY < image.getHeight()) {
					image.setRGB(newX, newY, borderColor.getRGB());
				}
			}
		}
	}

	/**
	 * Helper method to check if a pixel is transparent or empty.
	 * 
	 * @param pixel the RGB value of the pixel.
	 * @return true if the pixel is transparent or empty, false otherwise.
	 */
	private boolean isTransparentOrEmpty(int pixel) {
		Color color = new Color(pixel, true);
		return color.getAlpha() == 0; // checks if the pixel is transparent
	}

	

	
	/**
	 * helper method to get the bounding box of a given BufferedImage.
	 * 
	 * @param image the BufferedImage.
	 * @return an array containing the x, y, width, and height of the bounding box.
	 */
	private int[] getBoundingBox(BufferedImage image) {
		int minX = image.getWidth();
		int minY = image.getHeight();
		int maxX = 0;
		int maxY = 0;

		// loop through image and find min x,y and max x,y
		//also set the left and y value to use in combine code later
		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				if (isNonEmptyPixel(image, x, y)) {
					if (x < minX) {
						minX = x;
						leftAdjustmentY = y;
					}
					if (y < minY)
						minY = y;
					if (x > maxX) {
						maxX = x;
						RightAdjustmentY = y;
					}
					if (y > maxY)
						maxY = y;
				}
			}
		}

		// ensure we don't return an invalid bounding box
		if (minX > maxX || minY > maxY) {
			return new int[] { 0, 0, 0, 0 }; // no non-empty pixels found
		}

		image.setRGB(minX + 1, leftAdjustmentY, Color.RED.getRGB());
		image.setRGB(maxX - 10, RightAdjustmentY, Color.RED.getRGB());
		minYAdjustment = minY;

		return new int[] { minX, minY, maxX, maxY };
	}
	
	
	
	/**
	 * 
	 * @return totalCutValues array
	 */
	public int[] getTotalCutValues() {
		return totalCutValues;
	}

	
	
	/**
	 * Checks if pixel is empty
	 * @param image
	 * @param x
	 * @param y
	 * @return
	 */
	private boolean isNonEmptyPixel(BufferedImage image, int x, int y) {
		int pixel = image.getRGB(x, y);
		Color color = new Color(pixel, true);
		// Check if the pixel is not fully transparent
		return color.getAlpha() != 0;
	}

	/**
	 * @return the bx
	 */
	public double getBottomMiddleX() {
		return bx;
	}

	/**
	 * @param mx the mx to set
	 */
	public void setBottomMiddleX(double bx) {
		this.bx = bx;
	}

	/**
	 * @return the my
	 */
	public double getBottomMiddleY() {
		return by;
	}

	/**
	 * @param my the my to set
	 */
	public void setBottomMiddleY(double by) {
		this.by = by;
	}

	/**
	 * @return the mx
	 */
	public double getTopMiddleX() {
		return tx;
	}

	/**
	 * @param mx the mx to set
	 */
	public void setTopMiddleX(double tx) {
		this.tx = tx;
	}

	/**
	 * @return the my
	 */
	public double getTopMiddleY() {
		return ty;
	}

	/**
	 * @param my the my to set
	 */
	public void setTopMiddleY(double ty) {
		this.ty = ty;
	}

	public int getBackLeft() {
		return backLeft;
	}

	public int getFrontLeft() {
		return frontLeft;
	}

	public int getFrontRight() {
		return frontRight;
	}

	public int getBackRight() {
		return backRight;
	}

}
