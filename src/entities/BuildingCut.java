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

	private int TOP_WIDTH, TOP_HEIGHT;
	private int width, height;

	private int totalTopLeftCutDepth;

	private int totalTopRightCutDepth;
	private double[] middle = new double[2];

	public BuildingCut(int x, int y, int width, int height, BufferedImage image) {



		super(x, y, width, height, image);
		this.width = width;
		this.height = height;
		BufferedImage combinedImage = null;

		try {

			// Load the images

			img1 = ImageIO.read(getClass().getResourceAsStream("/leftFace.png"));



			img2 = ImageIO.read(getClass().getResourceAsStream("/rightFace.png"));



			img3 = ImageIO.read(getClass().getResourceAsStream("/topFace.png"));

			TOP_WIDTH = img3.getWidth();
			TOP_HEIGHT = img3.getHeight();

			
			// Combine the images

			combinedImage = combineImages(img1, img2, img3);



		} catch (IOException e) {

			e.printStackTrace();

		}



//		 BufferedImage resizediGround = resizeBuffImage(image, width, height);

		setSprite(combinedImage);

	}

	public double getMx(){
		return middle[0];
	}
	public double getMy(){
		return middle[1];
	}

	public BufferedImage combineImages(BufferedImage leftFace, BufferedImage rightFace, BufferedImage topFace) {



		// Calculate the dimensions of the combined image

		int combinedWidth = leftFace.getWidth() + rightFace.getWidth();

		int combinedHeight = leftFace.getHeight() + topFace.getHeight();



		// Create a new BufferedImage with the combined dimensions
//		System.out.println("combined Widths" + combinedWidth +" "+ combinedHeight);
		BufferedImage combined = new BufferedImage(combinedWidth, combinedHeight, BufferedImage.TYPE_INT_ARGB);



		// Get the Graphics2D object to draw on the combined image

		Graphics2D g2d = combined.createGraphics();

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);



		// Draw the top face
		
		g2d.drawImage(topFace,0 , 0, null);
//		System.out.println(topFace.getWidth() + " " + topFace.getHeight());


		// Draw the left face

		g2d.drawImage(leftFace, 0,topFace.getHeight() / 2, null);

//	        g2d.drawImage(leftFace, 0, topFace.getHeight() / 2 - 0, null);



		// Draw the right face
		middle[0] = leftFace.getWidth();
		middle[1] = leftFace.getWidth() / Math.sqrt(3);
		g2d.drawImage(rightFace, leftFace.getWidth(),(int) (leftFace.getWidth() / Math.sqrt(3)), null);



		// Dispose the graphics object

		g2d.dispose();

		BufferedImage whitespace = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		g2d = whitespace.createGraphics();
	    g2d.setBackground(new java.awt.Color(0, 0, 0, 0));
	    g2d.clearRect(0, 0, width,height);
	    g2d.drawImage(combined, 0, 0, null);
	

		return whitespace;

	}



	public void act() {



	}



	public void cut( int leftCutDepth, int rightCutDepth, int topRightCutDepth, int topLeftCutDepth) {

		totalLeftDepth += leftCutDepth;
		totalRightDepth += rightCutDepth;
		totalTopLeftCutDepth += topLeftCutDepth;
		totalTopRightCutDepth += topRightCutDepth;
		
		int startX = totalTopLeftCutDepth;
		int startY = (int) (totalTopLeftCutDepth / Math.sqrt(3)) ;
		int width = img1.getWidth()- totalRightDepth - totalTopLeftCutDepth;
		int height = img1.getHeight() - (int) (totalTopLeftCutDepth / Math.sqrt(3));
				
		BufferedImage croppedLeftFace = img1.getSubimage(startX,startY,width,height);

		

		
		startX = totalLeftDepth;
		startY = (int) (totalTopRightCutDepth / Math.sqrt(3)) ;
		width = img2.getWidth()  - totalLeftDepth - totalTopRightCutDepth;
		height = img2.getHeight() - (int) (totalTopRightCutDepth / Math.sqrt(3));
		BufferedImage croppedRightFace = img2.getSubimage(startX,startY,width,height);

		
		
		BufferedImage croppedTopFace = new BufferedImage(TOP_WIDTH, TOP_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g2d = croppedTopFace.createGraphics();

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		Path2D.Double topPath = new Path2D.Double();
		
		
		
		topPath.moveTo(0, 0);
		
		topPath.lineTo(totalLeftDepth*Math.sqrt(3)/2, TOP_HEIGHT / 2 - totalLeftDepth / 2); 
		
		topPath.lineTo(TOP_WIDTH/2+totalLeftDepth*Math.sqrt(3)/2, TOP_HEIGHT - totalLeftDepth/2); 

		topPath.lineTo(TOP_WIDTH, TOP_HEIGHT);
		topPath.lineTo(TOP_WIDTH, 0);

//
//		topPath.lineTo(totalLeftDepth*Math.sqrt(3)/2, TOP_HEIGHT / 2 - totalRightDepth / 2); 
//
		double Xint = TOP_WIDTH/2 + totalRightDepth*Math.sqrt(3)/2 - totalLeftDepth*Math.sqrt(3)/2;
		double Yint = TOP_HEIGHT - totalRightDepth/2 - totalLeftDepth/2;
//
//		topPath.lineTo(Xint,Yint);
//
		topDistance = (int) Math.sqrt(Math.pow((TOP_WIDTH/2 - Xint),2) + Math.pow(0 - Yint,2));
//
//
//		topPath.lineTo(TOP_WIDTH - totalLeftDepth / Math.sqrt(2), TOP_HEIGHT / 2 - totalLeftDepth / Math.sqrt(2));
//
//
//		topPath.lineTo(TOP_WIDTH, 0);
//		
		g2d.setClip(topPath);

		g2d.drawImage(img3, 0, 0, null);
//		

		

		BufferedImage croppedcroppedTopFace = new BufferedImage(TOP_WIDTH, TOP_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g2d2 = croppedcroppedTopFace.createGraphics();

		g2d2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		Path2D.Double topPath2 = new Path2D.Double();
		
		
		
		topPath2.moveTo(TOP_WIDTH - totalRightDepth *Math.sqrt(3)/2, TOP_HEIGHT / 2 - totalRightDepth / 2);
		
		topPath2.lineTo(TOP_WIDTH/2-totalRightDepth*Math.sqrt(3)/2, TOP_HEIGHT - totalRightDepth/2); 
		
		topPath2.lineTo(0,TOP_HEIGHT); 

		topPath2.lineTo(0, 0);
		topPath2.lineTo(TOP_WIDTH, 0);
		
		g2d2.setClip(topPath2);

		g2d2.drawImage(croppedTopFace, 0, 0, null);

		
		
		
		
		
		
		
		
		
		
		
		BufferedImage croppedcroppedcroppedTopFace = new BufferedImage(TOP_WIDTH, TOP_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g2d3 = croppedcroppedcroppedTopFace.createGraphics();

		g2d3 = croppedcroppedcroppedTopFace.createGraphics();

		g2d3.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);		
			
		Path2D.Double topPath3 = new Path2D.Double();
		
		
		
		topPath3.moveTo(totalTopLeftCutDepth,(int) (TOP_HEIGHT/2 + totalTopLeftCutDepth / Math.sqrt(3)));

		double topXInt =  TOP_WIDTH / 2 + totalTopLeftCutDepth * Math.sqrt(3)/2 ;
		double topYInt = 0 + totalTopLeftCutDepth / 2 ;
		
		topPath3.lineTo(topXInt, topYInt);

		topPath3.lineTo(TOP_WIDTH, 0);

		topPath3.lineTo(TOP_WIDTH, TOP_HEIGHT);

		topPath3.lineTo(0,TOP_HEIGHT);

		
		g2d3.setClip(topPath);

		g2d3.drawImage(croppedcroppedTopFace, 0, 0, null);
		
		

		
		
		
		
		
		
		
		
		BufferedImage croppedcroppedcroppedcroppedTopFace = new BufferedImage(TOP_WIDTH, TOP_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		
		Graphics2D g2d4 = croppedcroppedcroppedcroppedTopFace.createGraphics();

		g2d4 = croppedcroppedcroppedcroppedTopFace.createGraphics();

		g2d4.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);		
			
		Path2D.Double topPath4 = new Path2D.Double();
		
		
		
		topPath4.moveTo(TOP_WIDTH - totalTopRightCutDepth,(int) (TOP_HEIGHT/2 + totalTopRightCutDepth / Math.sqrt(3)));

		
		double topXInt2 =  TOP_WIDTH / 2 - totalTopRightCutDepth * Math.sqrt(3)/2 ;
		double topYInt2 = 0 + totalTopRightCutDepth / 2 ;
		
		topPath4.lineTo(topXInt2, topYInt2);

		topPath4.lineTo(0, 0);

		topPath4.lineTo(0,TOP_HEIGHT);

		topPath4.lineTo(TOP_WIDTH,TOP_HEIGHT);

		
		g2d4.setClip(topPath4);

		g2d4.drawImage(croppedcroppedcroppedTopFace, 0, 0, null);
	

		
		
		
		
		g2d.dispose();
		g2d2.dispose();
		g2d3.dispose();
		g2d4.dispose();



		
		 int[] boundingBox = getBoundingBox(croppedcroppedcroppedcroppedTopFace);
         int x = boundingBox[0];
         int y = boundingBox[1];
         int width2 = boundingBox[2] - boundingBox[0] + 1;
         int height2 = boundingBox[3] - boundingBox[1] + 1;
         
         BufferedImage croppedImage = croppedcroppedcroppedcroppedTopFace.getSubimage(x, y, width2, height2);
		
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
		
		
		
		

//		int temp = 		totalTopRightCutDepth / 2 + totalTopLeftCutDepth / 2;
//		croppedcroppedcroppedcroppedTopFace = croppedcroppedcroppedcroppedTopFace.getSubimage(TOP_WIDTH / 2 - croppedLeftFace.getWidth(),temp , croppedLeftFace.getWidth() + croppedRightFace.getWidth(), topDistance-temp);

		
	
		
		setSprite(combineImages(croppedLeftFace,croppedRightFace,croppedImage));

		
		
		
	
	}

	private static int[] getBoundingBox(BufferedImage image) {
        int minX = image.getWidth();
        int minY = image.getHeight();
        int maxX = 0;
        int maxY = 0;

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if (isNonEmptyPixel(image, x, y)) {
                    if (x < minX) minX = x;
                    if (y < minY) minY = y;
                    if (x > maxX) maxX = x;
                    if (y > maxY) maxY = y;
                }
            }
        }

        return new int[]{minX, minY, maxX, maxY};
    }

    private static boolean isNonEmptyPixel(BufferedImage image, int x, int y) {
        int pixel = image.getRGB(x, y);
        Color color = new Color(pixel, true);
        // Assuming empty spaces are white or fully transparent
        return !(color.getAlpha() == 0 || (color.getRed() > 240 && color.getGreen() > 240 && color.getBlue() > 240));
    }


	private void drawPoint(Graphics2D g2d, int x, int y) {

		g2d.setColor(Color.RED);

		int size = 100;

		g2d.fillOval(x - size / 2, y - size / 2, size, size);



	}



}

