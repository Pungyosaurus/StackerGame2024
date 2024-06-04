package entities;



import java.awt.Color;

import java.awt.Graphics;

import java.awt.Graphics2D;

import java.awt.Rectangle;

import java.awt.RenderingHints;

import java.awt.geom.AffineTransform;

import java.awt.geom.Path2D;

import java.awt.image.BufferedImage;

import java.io.File;

import java.io.IOException;



import javax.imageio.ImageIO;



public class BuildingCut extends GameObject {



	BufferedImage img1, img2, img3;

	private int totalLeftDepth, totalRightDepth, totalTopLeftDepth, totalTopRightDepth, totalTopDepth;

	BufferedImage lf, rf, tf;

	private int SIDE_WIDTH, SIDE_HEIGHT, TOP_WIDTH, TOP_HEIGHT;



	public BuildingCut(int x, int y, int width, int height, BufferedImage image) {



		super(x, y, width, height, image);

		BufferedImage combinedImage = null;

		try {

			// Load the images

			img1 = ImageIO.read(getClass().getResourceAsStream("/leftFace.png"));



			img2 = ImageIO.read(getClass().getResourceAsStream("/rightFace.png"));



			img3 = ImageIO.read(getClass().getResourceAsStream("/topFace.png"));

			SIDE_WIDTH = img1.getWidth();

			SIDE_HEIGHT = img1.getHeight();
			TOP_WIDTH = img3.getWidth();
			TOP_HEIGHT = img3.getHeight();

			lf = img1;

			rf = img2;

			tf = img3;

			// Combine the images

			combinedImage = combineImages(img1, img2, img3);



			// Save the combined image to a file

//	            ImageIO.write(combinedImage, "PNG", new File("path/to/combinedImage.png"));

		} catch (IOException e) {

			e.printStackTrace();

		}



//		 BufferedImage resizediGround = resizeBuffImage(image, width, height);

		setSprite(combinedImage);

	}



	public BufferedImage combineImages(BufferedImage leftFace, BufferedImage rightFace, BufferedImage topFace) {

		int faceWidth = leftFace.getWidth();

		int faceHeight = leftFace.getHeight();

		// Calculate the dimensions of the combined image

		int combinedWidth = leftFace.getWidth() + rightFace.getWidth() - totalLeftDepth - totalRightDepth;

		int combinedHeight = leftFace.getHeight() + topFace.getHeight() / 2;



		// Create a new BufferedImage with the combined dimensions

		BufferedImage combined = new BufferedImage(combinedWidth, combinedHeight, BufferedImage.TYPE_INT_ARGB);



		// Get the Graphics2D object to draw on the combined image

		Graphics2D g2d = combined.createGraphics();

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);



		// Draw the top face

		g2d.drawImage(topFace, 0, 0, null);



		// Draw the left face

		g2d.drawImage(leftFace, (int) (totalRightDepth * Math.sqrt(2)),

				topFace.getHeight() / 2 - (int) (totalRightDepth * .57), null);

//	        g2d.drawImage(leftFace, 0, topFace.getHeight() / 2 - 0, null);



		// Draw the right face

		g2d.drawImage(rightFace, faceWidth - (int) (totalLeftDepth),

				topFace.getHeight() / 2 - (int) (totalLeftDepth * .57), null);



		// Dispose the graphics object

		g2d.dispose();



		return combined;

	}



	public void act() {



	}



	public void cut( int leftCutDepth, int rightCutDepth) {



		totalLeftDepth += leftCutDepth;

		totalRightDepth += rightCutDepth;

//		totalTopDepth += (leftCutDepth + rightCutDepth) / Math.sqrt(2);

		BufferedImage croppedLeftFace = new BufferedImage(SIDE_WIDTH, SIDE_HEIGHT, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = croppedLeftFace.createGraphics();

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		Path2D.Double leftPath = new Path2D.Double();

		leftPath.moveTo(0, 0);

		leftPath.lineTo(0, SIDE_HEIGHT);

		leftPath.lineTo(SIDE_WIDTH - totalLeftDepth, SIDE_HEIGHT);

		leftPath.lineTo(SIDE_WIDTH - totalLeftDepth, 0);

		g2d.setClip(leftPath);

		g2d.drawImage(img1, 0, 0, null);

		g2d.dispose();



		BufferedImage croppedRightFace = new BufferedImage(SIDE_WIDTH, SIDE_HEIGHT, BufferedImage.TYPE_INT_ARGB);

		g2d = croppedRightFace.createGraphics();

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		Path2D.Double rightPath = new Path2D.Double();

		rightPath.moveTo(SIDE_WIDTH, 0);



		rightPath.lineTo(SIDE_WIDTH, SIDE_HEIGHT); 



		rightPath.lineTo(totalRightDepth, SIDE_HEIGHT);



		rightPath.lineTo(totalRightDepth, 0);



		g2d.setClip(rightPath);

		g2d.drawImage(img2, 0, 0, null);

		g2d.dispose();



		BufferedImage croppedTopFace = new BufferedImage(TOP_WIDTH, TOP_HEIGHT,BufferedImage.TYPE_INT_ARGB);

		g2d = croppedTopFace.createGraphics();

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		Path2D.Double topPath = new Path2D.Double();

		topPath.moveTo(TOP_WIDTH / 2, 0); 



		topPath.lineTo(totalRightDepth / Math.sqrt(2), TOP_HEIGHT / 2 - totalRightDepth / Math.sqrt(2)); 


		double line1B = (TOP_HEIGHT / 2 - totalRightDepth / Math.sqrt(2)) -(-16.0/9*(totalRightDepth / Math.sqrt(2)));
		double line2B =  (TOP_HEIGHT / 2 - totalLeftDepth / Math.sqrt(2)) - (9.0/16*(TOP_WIDTH - totalLeftDepth / Math.sqrt(2)));
		
		double Xint = (line2B-line1B)/ (-337.0/144.0);
		double Yint = 9.0/16.0*Xint+line1B;
				
		topPath.lineTo(Xint,Yint);



		topPath.lineTo(TOP_WIDTH - totalLeftDepth / Math.sqrt(2), TOP_HEIGHT / 2 - totalLeftDepth / Math.sqrt(2));



		topPath.lineTo(TOP_WIDTH, TOP_HEIGHT);

		topPath.lineTo(TOP_WIDTH, 0);


		// Fill the path with the image

		g2d.setClip(topPath);

		g2d.drawImage(img3, 0, 0, null);

		g2d.dispose();

		
		setSprite((croppedTopFace));

		

		

//		// left facing side cut by depth

//		// right facing side stays the same length

//		// top facing side gets cut by depth at the same

//

//		// using an if statement to avoid duplicate variable names

//

//		if (side == 1) {

//			// Cutting left face by depth and topface by depth / sqrt2

//			

//			int width = img1.getWidth();// never changes 160

//			int height = img1.getHeight();

//			int topWidth =  img3.getWidth();

//			int topHeight = img3.getHeight();

//			System.out.println(width);

//

//			BufferedImage croppedLeftFace = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

//			Graphics2D g2d = croppedLeftFace.createGraphics();

//			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

//			totalLeftDepth += depth;

//			Path2D.Double path = new Path2D.Double();

//			path.moveTo(0, 0);

//

//			path.lineTo(0, height);

//

//			path.lineTo(width - totalLeftDepth, height);

//

//			path.lineTo(width - totalLeftDepth, 0);

//

//			path.lineTo(width, 0);

//

//			// Fill the path with the image

//			g2d.setClip(path);

////	            BufferedImage cropped = img1.getSubimage(0, 0, newWidth, newHeight);

//			g2d.drawImage(lf, 0, 0, null);

//			g2d.dispose();

//			lf = croppedLeftFace;

//			System.out.println( lf.getWidth());

//			depth = (int) (totalLeftDepth / Math.sqrt(2));

//			// Create a new image to hold the cropped top face

//			BufferedImage croppedTopFace = new BufferedImage(img3.getWidth(), img3.getHeight(),

//					BufferedImage.TYPE_INT_ARGB);

//			g2d = croppedTopFace.createGraphics();

//			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

//

//			// Create a path to define the area to keep

//			path = new Path2D.Double();

//			path.moveTo(0, 0); // Top-left corner

//

//			path.lineTo(0, topHeight); // Top-right corner

//

//			path.lineTo(topWidth / 2 - depth, topHeight - depth);

//

//			path.lineTo(topWidth - depth, topHeight / 2 - depth);

//

//			path.lineTo(topWidth, 0);

//

//			// Fill the path with the image

//			g2d.setClip(path);

//			g2d.drawImage(tf, 0, 0, null);

//			g2d.dispose();

//

//			tf = croppedTopFace;

//		}

//

//		if (side == 2) {

//			// Calculate the new dimensions after cropping

//			int width = img2.getWidth();

//			int height = img2.getHeight();

//			int topWidth =  (img3.getWidth());

//			int topHeight =  (img3.getHeight());

//			totalRightDepth += depth;

//

//			// Create a new image to hold the cropped top face

//			BufferedImage croppedRightFace = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

//			Graphics2D g2d = croppedRightFace.createGraphics();

//			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

//

//			Path2D.Double path = new Path2D.Double();

//			path.moveTo(width, 0); // Top-right corner

//

//			path.lineTo(width, height); // bottom-right corner

//

//			path.lineTo(totalRightDepth, height);

//

//			path.lineTo(totalRightDepth, 0);

//

//

//			// Fill the path with the image

//			g2d.setClip(path);

//			g2d.drawImage(rf, 0, 0, null);

//			g2d.dispose();

//

//			rf = croppedRightFace;

//			depth = totalRightDepth / Math.sqrt(2);

//			// Create a new image to hold the cropped top face

//			BufferedImage croppedTopFace = new BufferedImage(img3.getWidth(), img3.getHeight(),

//					BufferedImage.TYPE_INT_ARGB);

//			g2d = croppedTopFace.createGraphics();

//			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

//

//			// Create a path to define the area to keep

//			path = new Path2D.Double();

//			path.moveTo(topWidth, 0); // Top-left corner

//

//			path.lineTo(topWidth, topHeight); // Top-right corner

//

//			path.lineTo(topWidth / 2 + depth, topHeight - depth);

//

//			path.lineTo(depth, topHeight / 2 - depth);

//

//			path.lineTo(0, 0);

//

//			// Fill the path with the image

//			g2d.setClip(path);

//			g2d.drawImage(tf, 0, 0, null);

//			g2d.dispose();

//

//			tf = croppedTopFace;

//

//		}

//

//		if (side == 3) {

//			double newWidth = img1.getWidth() - depth;

//

//			int width = img1.getWidth();

//			int height = img1.getHeight();

//

//			// Create a new image to hold the cropped top face

//			BufferedImage croppedLeftFace = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

//			Graphics2D g2d = croppedLeftFace.createGraphics();

//			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

//

//			Path2D.Double path = new Path2D.Double();

//			path.moveTo(depth, 0); // Top-right corner

//

//			path.lineTo(depth, height); // bottom-right corner

//

//			path.lineTo(width, height);

//

//			path.lineTo(width, 0);

//

//			path.lineTo(depth, 0);

//

//			// Fill the path with the image

//			g2d.setClip(path);

//			g2d.drawImage(img1, 0, 0, null);

//			g2d.dispose();

//

//			img1 = croppedLeftFace;

//

//			width = img3.getWidth();

//			height = img3.getHeight();

////	            depth /= Math.sqrt(2);

//			// Create a new image to hold the cropped top face

//			BufferedImage croppedTopFace = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

//			g2d = croppedTopFace.createGraphics();

//			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

//

//			// Create a path to define the area to keep

//			path = new Path2D.Double();

////	            path.moveTo(depth, depth  * 2 + 20); // Top-left corner

//			path.moveTo(depth, height / 2 + depth / Math.sqrt(3));

////	            System.out.println(depth * 2 + 20);

////	            System.out.println(height / 2 + depth / Math.sqrt(3));

//			path.lineTo(depth, height); // Top-right corner

//

//			path.lineTo(width, height);

//

//			path.lineTo(width, depth);

//

//			path.lineTo(width / 2 + depth, depth / Math.sqrt(2));

//

//			// Fill the path with the image

//			g2d.setClip(path);

//			g2d.drawImage(img3, 0, 0, null);

//			g2d.dispose();

//

//			img3 = croppedTopFace;

//		}

//

//		if (side == 4) {

//			double newWidth = img2.getWidth() - depth;

//

//			int width = img2.getWidth();

//			int height = img2.getHeight();

//

//			// Create a new image to hold the cropped top face

//			BufferedImage croppedRightFace = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

//			Graphics2D g2d = croppedRightFace.createGraphics();

//			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

//

//			Path2D.Double path = new Path2D.Double();

//			path.moveTo(width - depth, 0); // Top-right corner

//

//			path.lineTo(width - depth, height); // bottom-right corner

//

//			path.lineTo(0, height);

//

//			path.lineTo(0, 0);

//

////	            path.lineTo(depth,0);

//

//			// Fill the path with the image

//			g2d.setClip(path);

//			g2d.drawImage(img2, 0, 0, null);

//			g2d.dispose();

//

//			img2 = croppedRightFace;

//

//			width = img3.getWidth();

//			height = img3.getHeight();

////	            depth /= Math.sqrt(2);

//			// Create a new image to hold the cropped top face

//			BufferedImage croppedTopFace = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

//			g2d = croppedTopFace.createGraphics();

//			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

//

//			// Create a path to define the area to keep

//			path = new Path2D.Double();

////	            path.moveTo(depth, depth  * 2 + 20); // Top-left corner

//			path.moveTo(width - depth, height / 2 + depth / Math.sqrt(3));

////	            path.moveTo(depth, height / 2 + depth );

//

////	            System.out.println(depth * 2 + 20);

////	            System.out.println(height / 2 + depth / Math.sqrt(3));

//			path.lineTo(width / 2, height); // Top-right corner

//

//			path.lineTo(0, height / 2);

//

//			path.lineTo(0, depth);

//

//			path.lineTo(width / 2 - depth, depth / Math.sqrt(2));

//

//			// Fill the path with the image

//			g2d.setClip(path);

//			g2d.drawImage(img3, 0, 0, null);

//			g2d.dispose();

//

//			img3 = croppedTopFace;

//		}






	}



	private void drawPoint(Graphics2D g2d, int x, int y) {

		g2d.setColor(Color.RED);

		int size = 100;

		g2d.fillOval(x - size / 2, y - size / 2, size, size);



	}



}

