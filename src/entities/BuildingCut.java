package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class BuildingCut extends GameObject {

	BufferedImage img1, img2, img3 ;
 

	public BuildingCut(int x, int y, int width, int height, BufferedImage image ){
		
		 super(x , y, width, height, image);
		 BufferedImage combinedImage = null;
		 try {
	            // Load the images
	             img1 =ImageIO.read(getClass().getResourceAsStream("/leftFace.png"));
	             img2 =ImageIO.read(getClass().getResourceAsStream("/rightFace.png"));
	             img3 =ImageIO.read(getClass().getResourceAsStream("/topFace.png"));

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
	
	 public static BufferedImage combineImages(BufferedImage leftFace, BufferedImage rightFace, BufferedImage topFace) {
		  int faceWidth = leftFace.getWidth();
	        int faceHeight = leftFace.getHeight();

	        // Calculate the dimensions of the combined image
	        int combinedWidth = 2 * faceWidth;
	        int combinedHeight = faceHeight + topFace.getHeight() / 2;

	        // Create a new BufferedImage with the combined dimensions
	        BufferedImage combined = new BufferedImage(combinedWidth, combinedHeight, BufferedImage.TYPE_INT_ARGB);

	        // Get the Graphics2D object to draw on the combined image
	        Graphics2D g2d = combined.createGraphics();
	        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	        // Draw the top face
	        g2d.drawImage(topFace, 0, 0, null);

	        // Draw the left face
	        g2d.drawImage(leftFace, 0, topFace.getHeight() / 2, null);

	        // Draw the right face
	        g2d.drawImage(rightFace, faceWidth, topFace.getHeight() / 2, null);

	        // Dispose the graphics object
	        g2d.dispose();

	        return combined;
	    }
	
	public void act() {

		
	}
	
	
		public void cut(int side, double depth){
			//left facing side cut by depth
			//right facing side stays the same length
			//top facing side gets cut by depth at the same 
			if(side == 1){
	            double newWidth = img1.getWidth() - depth;
	            
	            int width = img1.getWidth();
	            int height = img1.getHeight();

	            // Create a new image to hold the cropped top face
	            BufferedImage croppedLeftFace = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	            Graphics2D g2d = croppedLeftFace.createGraphics();
	            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
	            Path2D.Double path = new Path2D.Double();
	            depth = 50;
	            path.moveTo(0, 0); // Top-left corner
	            
	            path.lineTo(0, height); // Top-right corner
	            
	            path.lineTo(width-depth,height);
	            drawPoint(g2d, 200,200);
	            
	            path.lineTo(width-depth,0);
	            drawPoint(g2d, 200,200);
	            
	            path.lineTo(width,0);


	            // Fill the path with the image
	            g2d.setClip(path);
	            g2d.drawImage(img1, 0, 0, null);
	            g2d.dispose();

	            img1 = croppedLeftFace;
	            
	            
	            
	            
	            
//	            
	            width = img3.getWidth();
	            height = img3.getHeight();
	            depth = depth/Math.sqrt(2);
	            // Create a new image to hold the cropped top face
	            BufferedImage croppedTopFace = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	            g2d = croppedTopFace.createGraphics();
	            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	            // Create a path to define the area to keep
	            path = new Path2D.Double();
	            path.moveTo(0, 0); // Top-left corner
	            drawPoint(g2d, 0, 0);
	            
	            path.lineTo(0, height); // Top-right corner
	            drawPoint(g2d, 0, height);
	            
	            path.lineTo(width/2-depth,height-depth);
	            drawPoint(g2d, 200,200);
	            
	            path.lineTo(width-depth,height/2-depth);
	            drawPoint(g2d, 200,200);
	            
	            path.lineTo(width,0);


	            // Fill the path with the image
	            g2d.setClip(path);
	            g2d.drawImage(img3, 0, 0, null);
	            g2d.dispose();

	            img3 = croppedTopFace;

			}

			if (side == 2) {
		        // Calculate the new dimensions after cropping
//		       img3 = img.getSubImage(0,0,)
		    }

	            
	        
			
			
			
			setSprite(combineImages(img1,img2,img3));
			
		}
		
		  private void drawPoint(Graphics2D g2d, int x, int y) {
		        g2d.setColor(Color.RED);
		        int size = 100;
		        g2d.fillOval(x - size / 2, y - size / 2, size, size);

		    }
		
}
