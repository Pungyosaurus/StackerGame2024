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

	private int totalLeftDepth, totalRightDepth, topDistance;

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



		// Calculate the dimensions of the combined image

		int combinedWidth = leftFace.getWidth() + rightFace.getWidth() - totalLeftDepth - totalRightDepth;

		int combinedHeight = leftFace.getHeight() + (topFace.getHeight()-topDistance) / 2;



		// Create a new BufferedImage with the combined dimensions
		System.out.println("combined Widths" + combinedWidth +" "+ combinedHeight);
		BufferedImage combined = new BufferedImage(combinedWidth, combinedHeight, BufferedImage.TYPE_INT_ARGB);



		// Get the Graphics2D object to draw on the combined image

		Graphics2D g2d = combined.createGraphics();

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);



		// Draw the top face
		
		g2d.drawImage(topFace,-55 , 0, null);
		System.out.println(topFace.getWidth() + " " + topFace.getHeight());


		// Draw the left face

		g2d.drawImage(leftFace, 0,topDistance, null);

//	        g2d.drawImage(leftFace, 0, topFace.getHeight() / 2 - 0, null);



		// Draw the right face

		g2d.drawImage(rightFace, 160-totalLeftDepth,topDistance, null);



		// Dispose the graphics object

		g2d.dispose();



		return combined;

	}



	public void act() {



	}



	public void cut( int leftCutDepth, int rightCutDepth) {

		totalLeftDepth += leftCutDepth;
		totalRightDepth += rightCutDepth;

		BufferedImage resultLeftImage = img1.getSubimage(0,0,img1.getWidth()-totalLeftDepth, img1.getHeight());
		
        BufferedImage croppedLeftFace = new BufferedImage(img1.getWidth(), img2.getHeight(), BufferedImage.TYPE_INT_ARGB);
		 Graphics2D g2d = croppedLeftFace.createGraphics();
         g2d.setBackground(new java.awt.Color(0, 0, 0, 0));
         g2d.clearRect(0, 0, croppedLeftFace.getWidth(),croppedLeftFace.getHeight());
         g2d.drawImage(resultLeftImage, 0, 0, null);
         g2d.dispose();
         

		BufferedImage resultRightImage = img2.getSubimage(totalRightDepth,0,img2.getWidth()-totalRightDepth, img2.getHeight());
		BufferedImage croppedRightFace = new BufferedImage(img1.getWidth(), img2.getHeight(), BufferedImage.TYPE_INT_ARGB);
		g2d = croppedRightFace.createGraphics();
	    g2d.setBackground(new java.awt.Color(0, 0, 0, 0));
	    g2d.clearRect(0, 0, croppedRightFace.getWidth(),croppedRightFace.getHeight());
	    g2d.drawImage(resultRightImage, 0, 0, null);
	    g2d.dispose();
		
		
		BufferedImage croppedTopFace = new BufferedImage(TOP_WIDTH, TOP_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		g2d = croppedTopFace.createGraphics();

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		Path2D.Double topPath = new Path2D.Double();
		topPath.moveTo(0, 0);



		topPath.lineTo(totalRightDepth / Math.sqrt(2), TOP_HEIGHT / 2 - totalRightDepth / Math.sqrt(2)); 

		double Xint = TOP_WIDTH/2 + totalRightDepth*Math.sqrt(3)/2 - totalLeftDepth*Math.sqrt(3)/2;
		double Yint = TOP_HEIGHT - totalRightDepth/2 - totalLeftDepth/2;

		topPath.lineTo(Xint,Yint);

		topDistance += (int) Math.sqrt(Math.pow((TOP_WIDTH/2 - Xint),2) + Math.pow(TOP_HEIGHT - Yint,2));


		topPath.lineTo(TOP_WIDTH - totalLeftDepth / Math.sqrt(2), TOP_HEIGHT / 2 - totalLeftDepth / Math.sqrt(2));



		topPath.lineTo(TOP_WIDTH, TOP_HEIGHT);

		topPath.lineTo(TOP_WIDTH, 0);
		


		// Fill the path with the image

		g2d.setClip(topPath);

		g2d.drawImage(img3, 0, 0, null);

		g2d.dispose();
		String desktopPath = javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath() + File.separator + "croppedTopFace.png";
		String desktopPath1 = javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath() + File.separator + "croppedLeftFace.png";
		String desktopPath2 = javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath() + File.separator + "croppedRightFace.png";

		// Create a File object with the specified file path
		File outputFile = new File(desktopPath);
		File outputFile1 = new File(desktopPath1);
		File outputFile2 = new File(desktopPath2);


		try {
		    // Write the BufferedImage to the specified file path
		    ImageIO.write(croppedTopFace, "PNG", outputFile);
		    ImageIO.write(croppedLeftFace, "PNG", outputFile1);
		    ImageIO.write(croppedRightFace, "PNG", outputFile2);

		    System.out.println("Image saved successfully at: " + desktopPath);
		} catch (IOException e) {
		    System.out.println("Error saving image: " + e.getMessage());
		}
		
	
		
		setSprite(combineImages(croppedLeftFace,croppedRightFace,croppedTopFace));

		
		
		
	
	}



	private void drawPoint(Graphics2D g2d, int x, int y) {

		g2d.setColor(Color.RED);

		int size = 100;

		g2d.fillOval(x - size / 2, y - size / 2, size, size);



	}



}

