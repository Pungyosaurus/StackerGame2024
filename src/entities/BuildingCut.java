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
	            combinedImage = combineImages(img1, img2, img3,0,0);

	            // Save the combined image to a file
//	            ImageIO.write(combinedImage, "PNG", new File("path/to/combinedImage.png"));
	        } catch (IOException e) {
	            e.printStackTrace();
	       }
	    
	
//		 BufferedImage resizediGround = resizeBuffImage(image, width, height);
		 setSprite(combinedImage);
		}
	
	 public static BufferedImage combineImages(BufferedImage leftFace, BufferedImage rightFace, BufferedImage topFace, int leftFaceDepth, int rightFaceDepth) {
		  	int faceWidth = leftFace.getWidth();
	        int faceHeight = leftFace.getHeight();

	        // Calculate the dimensions of the combined image
	        int combinedWidth = leftFace.getWidth() + rightFace.getWidth();
	        int combinedHeight = faceHeight + topFace.getHeight() / 2;

	        // Create a new BufferedImage with the combined dimensions
	        BufferedImage combined = new BufferedImage(combinedWidth, combinedHeight, BufferedImage.TYPE_INT_ARGB);

	        // Get the Graphics2D object to draw on the combined image
	        Graphics2D g2d = combined.createGraphics();
	        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	        // Draw the top face
	        g2d.drawImage(topFace, 0, 0, null);

	        
	       

	        // Draw the left face
	        g2d.drawImage(leftFace, (int)(leftFaceDepth*Math.sqrt(2)), topFace.getHeight() / 2 - (int) (leftFaceDepth*.8), null);
//	        g2d.drawImage(leftFace, 0, topFace.getHeight() / 2 - 0, null);

	        // Draw the right face
	        g2d.drawImage(rightFace, faceWidth- (int)(rightFaceDepth*Math.sqrt(2)), topFace.getHeight() / 2 -  (int) (rightFaceDepth*.8) , null);

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
			
			// using an if statement to avoid duplicate variable names
			
			int leftDepth = 0;
		    int rightDepth = 0;
		    
			if(side == 1){
	            int width = img1.getWidth();
	            int height = img1.getHeight();
	            int newWidth =(int) ( width - depth);
	            int newHeight = (int)(height - depth);

	            BufferedImage croppedLeftFace = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	            Graphics2D g2d = croppedLeftFace.createGraphics();
	            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
	            Path2D.Double path = new Path2D.Double();
	            path.moveTo(0, 0); 
	            
	            path.lineTo(0, height);
	            
	            path.lineTo(width-depth,height);
	            
	            path.lineTo(width-depth,0);
	            
	            path.lineTo(width,0);


	            // Fill the path with the image
	            g2d.setClip(path);
	            BufferedImage cropped = img1.getSubimage(0, 0, newWidth, newHeight);
	            
	         
	            
	            g2d.drawImage(img1, 0, 0, null);
	            g2d.dispose();
//	            img1 = resizeBuffImage(croppedLeftFace, newWidth, height);
	            
	            System.out.println("corpped" + croppedLeftFace.getWidth());
	            img1 = cropped;
//	            img1 = resizeBuffImage(croppedLeftFace, newWidth, height);
	            System.out.println(img1.getWidth());

	            
	            
	             
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
	            
	            path.lineTo(0, height); // Top-right corner
	            
	            path.lineTo(width/2-depth,height-depth);
	            
	            path.lineTo(width-depth,height/2-depth);
	            
	            path.lineTo(width,0);


	            // Fill the path with the image
	            g2d.setClip(path);
	            g2d.drawImage(img3, 0, 0, null);
	            g2d.dispose();

	            img3 = croppedTopFace;
	            
	            rightDepth = (int)depth;
			}

			if (side == 2) {
		        // Calculate the new dimensions after cropping
//		       img3 = img.getSubImage(0,0,)
				double newWidth = img2.getWidth() - depth;
	            
	            int width = img2.getWidth();
	            int height = img2.getHeight();

	            // Create a new image to hold the cropped top face
	            BufferedImage croppedRightFace = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	            Graphics2D g2d = croppedRightFace.createGraphics();
	            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
	            Path2D.Double path = new Path2D.Double();
	            path.moveTo(width, 0); // Top-right corner
	            
	            path.lineTo(width, height); // bottom-right corner
	            
	            path.lineTo(depth,height);
	            
	            path.lineTo(depth,0);
	            
	            path.lineTo(width,0);

	            // Fill the path with the image
	            g2d.setClip(path);
	            g2d.drawImage(img2, 0, 0, null);
	            g2d.dispose();

	            img2 = croppedRightFace;

	            width = img3.getWidth();
	            height = img3.getHeight();
	            depth /= Math.sqrt(2);
	            // Create a new image to hold the cropped top face
	            BufferedImage croppedTopFace = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	            g2d = croppedTopFace.createGraphics();
	            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	            // Create a path to define the area to keep
	            path = new Path2D.Double();
	            path.moveTo(width, 0); // Top-left corner
	            
	            path.lineTo(width, height); // Top-right corner
	            
	            path.lineTo(width / 2 + depth,height-depth);
	            
	            path.lineTo(depth, height / 2-depth);
	            
	            path.lineTo(0,0);


	            // Fill the path with the image
	            g2d.setClip(path);
	            g2d.drawImage(img3, 0, 0, null);
	            g2d.dispose();

	            img3 = croppedTopFace;
	            
	            leftDepth = (int)depth;

				
				
				
		    }

			if(side == 3) {
				double newWidth = img1.getWidth() - depth;
	            
	            int width = img1.getWidth();
	            int height = img1.getHeight();

	            // Create a new image to hold the cropped top face
	            BufferedImage croppedLeftFace = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	            Graphics2D g2d = croppedLeftFace.createGraphics();
	            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
	            Path2D.Double path = new Path2D.Double();
	            path.moveTo(depth, 0); // Top-right corner
	            
	            path.lineTo(depth, height); // bottom-right corner
	            
	            path.lineTo(width,height);
	            
	            path.lineTo(width,0);
	            
	            path.lineTo(depth,0);

	            // Fill the path with the image
	            g2d.setClip(path);
	            g2d.drawImage(img1, 0, 0, null);
	            g2d.dispose();

	            img1 = croppedLeftFace;
	         
	            width = img3.getWidth();
	            height = img3.getHeight();
//	            depth /= Math.sqrt(2);
	            // Create a new image to hold the cropped top face
	            BufferedImage croppedTopFace = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	            g2d = croppedTopFace.createGraphics();
	            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	            // Create a path to define the area to keep
	            path = new Path2D.Double();
//	            path.moveTo(depth, depth  * 2 + 20); // Top-left corner
	            path.moveTo(depth, height / 2 + depth / Math.sqrt(3));
//	            System.out.println(depth * 2 + 20);
//	            System.out.println(height / 2 + depth / Math.sqrt(3));
	            path.lineTo(depth, height); // Top-right corner
	            
	            path.lineTo(width, height);
	            
	            path.lineTo(width, depth);
	            
	            path.lineTo(width / 2 + depth , depth / Math.sqrt(2));


	            // Fill the path with the image
	            g2d.setClip(path);
	            g2d.drawImage(img3, 0, 0, null);
	            g2d.dispose();

	            img3 = croppedTopFace;
			}
			
			if(side == 4) {
				double newWidth = img2.getWidth() - depth;
	            
	            int width = img2.getWidth();
	            int height = img2.getHeight();

	            // Create a new image to hold the cropped top face
	            BufferedImage croppedRightFace = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	            Graphics2D g2d = croppedRightFace.createGraphics();
	            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
	            Path2D.Double path = new Path2D.Double();
	            path.moveTo(width - depth, 0); // Top-right corner
	            
	            path.lineTo(width - depth, height); // bottom-right corner
	            
	            path.lineTo(0, height);
	            
	            path.lineTo(0,0);
	            
//	            path.lineTo(depth,0);

	            // Fill the path with the image
	            g2d.setClip(path);
	            g2d.drawImage(img2, 0, 0, null);
	            g2d.dispose();

	            img2 = croppedRightFace;
	         
	            width = img3.getWidth();
	            height = img3.getHeight();
//	            depth /= Math.sqrt(2);
	            // Create a new image to hold the cropped top face
	            BufferedImage croppedTopFace = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	            g2d = croppedTopFace.createGraphics();
	            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

	            // Create a path to define the area to keep
	            path = new Path2D.Double();
//	            path.moveTo(depth, depth  * 2 + 20); // Top-left corner
	            path.moveTo(width - depth, height / 2 + depth / Math.sqrt(3));
//	            path.moveTo(depth, height / 2 + depth );

//	            System.out.println(depth * 2 + 20);
//	            System.out.println(height / 2 + depth / Math.sqrt(3));
	            path.lineTo(width / 2, height); // Top-right corner
	            
	            path.lineTo(0, height / 2);
	            
	            path.lineTo(0, depth);
	            
	            path.lineTo(width / 2 - depth , depth / Math.sqrt(2));


	            // Fill the path with the image
	            g2d.setClip(path);
	            g2d.drawImage(img3, 0, 0, null);
	            g2d.dispose();

	            img3 = croppedTopFace;
			}    
	        
			
			
			
			setSprite(combineImages(img1,img2,img3, leftDepth, rightDepth));
			
		}
		
		  private void drawPoint(Graphics2D g2d, int x, int y) {
		        g2d.setColor(Color.RED);
		        int size = 100;
		        g2d.fillOval(x - size / 2, y - size / 2, size, size);

		    }
		
}
