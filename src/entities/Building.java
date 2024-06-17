package entities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ServiceLoader;

import javax.imageio.ImageIO;

public class Building extends GameObject {

	
	private double xSpeed=0,ySpeed=0;
	private int gravity = 1;
	private boolean drop = false;

	private static BufferedImage img1, img2, img3;
//	private BufferedImage croppedLeftFace, croppedRightFace, croppedImage;
	private static int  TOP_WIDTH, TOP_HEIGHT;

	private int backLeft, frontLeft , frontRight, backRight;
	public int[] totalCutValues = new int[4];



	private double by;
	private double bx;
	private double tx;
	private double ty;
	
	public double rightFaceWidth;
	public double rightFaceHeight;
	private int leftFaceWidth;
	
	
	public Building(int x, int y, int width, int height){
		 super(x , y, width, height);
		 
		 BufferedImage combinedImage = null;
		 combinedImage = combineImages(img1, img2, img3);
		 setSprite(combinedImage);
		}
	
//	
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
	
	
	
	public static void loadImages(int width, int height) {
		try {

			img1 = ImageIO.read(Building.class.getResourceAsStream("/textures/leftFace.png"));
			img2 = ImageIO.read(Building.class.getResourceAsStream("/textures/rightFace.png"));
			img3 = ImageIO.read(Building.class.getResourceAsStream("/textures/topFace.png"));
			
			img1 = resizeBuffImage(img1, img1.getWidth() + width, img1.getHeight()+ height);
			img2 = resizeBuffImage(img2, img2.getWidth()+ width, img2.getHeight()+ height);
			img3 = resizeBuffImage(img3, img3.getWidth() + 2 * width, img3.getHeight()+ height);
			
			TOP_WIDTH = img3.getWidth();
			TOP_HEIGHT = img3.getHeight();


		} catch (IOException e) {

			e.printStackTrace();

		}
	}
	
	public void act() {

		ySpeed += gravity;
		setY(getY()+(int)ySpeed);
		setX(getX()+(int)xSpeed);
		
	}
	

	public void setDrop(boolean state) {
		this.drop = state;
	}
	public void drop(double dx, double dy, boolean direction){
		
		if(direction){
			xSpeed = dx*1.5;
		}else{
			xSpeed=-dx*1.5;
		}
		ySpeed= dy;
		drop = true;

	}
	public void drop(double dx, double dy){
		xSpeed = -dx*3;
		ySpeed =  dy;
		drop = true;
	}
		public boolean getDrop(){
		return drop;
	}
		
		
		public int[] collides(double xo, double yo, int direction) {
			//values to cut by
			
			double x = getX()+getBottomMiddleX();
			double y = getY()+getBottomMiddleY();
			
			if(direction ==1) {
				double bo =  yo + 1/Math.sqrt(3)*xo;
				double b =  y +1/Math.sqrt(3)*x;
				if(bo-b <20 && bo-b >-20) {
		
//					System.out.println(b+ " "+ bo);
					int[] offsetValues = new int[6];
	
					if(Math.sqrt(Math.pow(x-xo,2)+Math.pow(y-yo,2)) < rightFaceWidth/Math.sqrt(3)*2) {
						int offset =(int) Math.sqrt(Math.pow(x-xo,2)+Math.pow(y-yo,2));
						
						if(x>xo) {
							offsetValues[3] = offset;
						}else {
							offsetValues[2] = offset;
						}
						offsetValues[4] = (int) (bo-b);
						offsetValues[5] = (int) (rightFaceHeight+minYAdjustment);
						
//						System.out.println("collides");
						return offsetValues;
					}
				}
			}
			//top left to bottom right
			if(direction ==0) {
				double bo =  yo - 1/Math.sqrt(3)*xo;
				double b =  y - 1/Math.sqrt(3)*x;
				if(bo-b <20 && bo-b >-20) {
					int[] offsetValues = new int[6];
	
					if(Math.sqrt(Math.pow(x-xo,2)+Math.pow(y-yo,2)) < leftFaceWidth/Math.sqrt(3)*2) {
						int offset =(int) Math.sqrt(Math.pow(x-xo,2)+Math.pow(y-yo,2));
						
						if(x>xo) {
							offsetValues[1] = offset;
						}else {
							offsetValues[0] = offset;
						}
						offsetValues[4] = (int) (bo-b);
						offsetValues[5] = (int) (rightFaceHeight+minYAdjustment);

						
//						System.out.println("collides");
						return offsetValues;
					}
				}
			}
			// no array values
			return null;

		}
		public BufferedImage combineImages(BufferedImage leftFace, BufferedImage rightFace, BufferedImage topFace) {


			
			
			
			
			int combinedWidth = leftFace.getWidth() + rightFace.getWidth();
			rightFaceWidth = rightFace.getWidth();
			leftFaceWidth = leftFace.getWidth();
			
			int combinedHeight =(int) ( img1.getHeight() - img1.getWidth()/Math.sqrt(3) + topFace.getHeight());
			rightFaceHeight = (int) ( combinedHeight - topFace.getHeight());

			
			BufferedImage combined = new BufferedImage(combinedWidth, combinedHeight, BufferedImage.TYPE_INT_ARGB);

			Graphics2D g2d = combined.createGraphics();

			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


			// Draw the top face
			g2d.drawImage(leftFace, 0,(int) leftAdjustmentY - minYAdjustment, null);

			g2d.drawImage(topFace,0 , 0, null);


		
			g2d.drawImage(rightFace, combinedWidth-rightFace.getWidth(),(int) RightAdjustmentY - minYAdjustment, null);


			this.setSize(combinedWidth,combinedHeight);



			

		 
		    
			int x1 = (int) (topFace.getWidth() - rightFace.getWidth());
			int y1 = topFace.getHeight();
			setTopMiddleX(x1);
			setTopMiddleY(y1- minYAdjustment);
			combined.setRGB(x1,y1, Color.pink.getRGB());

				
			y1 = combined.getHeight();
			setBottomMiddleX(x1);
			setBottomMiddleY(y1- minYAdjustment);
			combined.setRGB(x1,y1-1, Color.pink.getRGB());

		    
		    
		    
		    
		    
		  
			 
			 
			g2d.drawImage(combined, 0, 0, null);

			g2d.dispose();

		    
		    
		    

		

			return combined;

		}

		public boolean cut( int backLeftP, int frontLeftP, int frontRightP, int backRightP) {

		
			totalCutValues[0] +=  backLeftP;
			totalCutValues[1] +=  frontLeftP;
			totalCutValues[2] +=  frontRightP;
			totalCutValues[3] +=  backRightP;

			
			
			backLeft += backLeftP;
			frontLeft += frontLeftP;
			frontRight += frontRightP;
			backRight += backRightP;

			if(backLeftP>leftFaceWidth/Math.sqrt(3)*2 ||  frontLeftP>leftFaceWidth/Math.sqrt(3)*2 || frontRightP>rightFaceWidth/Math.sqrt(3)*2 ||  backRightP>rightFaceWidth/Math.sqrt(3)*2 ) {
				System.out.println("CUT IS TOOO MUCH");
				return false;
			}
			int startX = (int) (backLeft / 2 * Math.sqrt(3));
			int startY = (int) (backLeft / 2) ;
			int width = (int) (img1.getWidth()- backLeft/ 2 * Math.sqrt(3) - frontLeft/ 2 * Math.sqrt(3));
			int height = img1.getHeight() - backLeft /2 - frontLeft/2;
			BufferedImage croppedLeftFace = img1.getSubimage(startX,startY,width,height);

			

			
			startX = (int) (frontRight/2*Math.sqrt(3));
			startY = (int) (backRight/2) ;
			width = (int) (img2.getWidth()  - frontRight/2*Math.sqrt(3) - backRight/2*Math.sqrt(3));
			height = img2.getHeight() - frontRight/2 - backRight/2;
			BufferedImage croppedRightFace = img2.getSubimage(startX,startY,width,height);

			
			
			BufferedImage croppedTopFace = new BufferedImage(TOP_WIDTH, TOP_HEIGHT, BufferedImage.TYPE_INT_ARGB);
			
			Graphics2D g2d = croppedTopFace.createGraphics();

			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			Path2D.Double topPath = new Path2D.Double();
			
			
			
			topPath.moveTo(0, 0);
			
			topPath.lineTo(frontRight*Math.sqrt(3)/2, TOP_HEIGHT / 2 - frontRight / 2); 
			
			double Xint = TOP_WIDTH/2 + frontRight*Math.sqrt(3)/2 - frontLeft*Math.sqrt(3)/2;
			double Yint = TOP_HEIGHT - frontRight/2 - frontLeft/2;
			topPath.lineTo(Xint,Yint);

			
			topPath.lineTo(TOP_WIDTH - frontLeft*Math.sqrt(3)/2, TOP_HEIGHT / 2 - frontLeft / 2); 
			
			topPath.lineTo(TOP_WIDTH, 0);
			topPath.lineTo(0, 0);

			
			
			
//			topPath.lineTo(TOP_WIDTH/2+totalLeftDepth*Math.sqrt(3)/2, TOP_HEIGHT - totalLeftDepth/2); 
//
//			topPath.lineTo(TOP_WIDTH, TOP_HEIGHT);
//			topPath.lineTo(TOP_WIDTH, 0);


			
	
	
//			topDistance = (int) Math.sqrt(Math.pow((TOP_WIDTH/2 - Xint),2) + Math.pow(0 - Yint,2));	
			g2d.setClip(topPath);

			g2d.drawImage(img3, 0, 0, null);
			
			
			

				BufferedImage croppedcroppedTopFace = new BufferedImage(TOP_WIDTH, TOP_HEIGHT, BufferedImage.TYPE_INT_ARGB);
				
			Graphics2D g2d2 = croppedcroppedTopFace.createGraphics();

			g2d2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			Path2D.Double topPath2 = new Path2D.Double();
			
			topPath2.moveTo(0, 0);
			topPath2.lineTo(backLeft/2*Math.sqrt(3), TOP_HEIGHT/2+backLeft/2); // gud
			
			Xint = TOP_WIDTH/2 + backLeft/2*Math.sqrt(3) - backRight/2*Math.sqrt(3);
			Yint = backLeft/2 + backRight/2;
			
			topPath2.lineTo(Xint,Yint);
			// everything was move to not lineto
			topPath2.lineTo(TOP_WIDTH - backRight/2*Math.sqrt(3) , TOP_HEIGHT/2 + backRight/2);// minus was a plus
			
			topPath2.lineTo(TOP_WIDTH,TOP_HEIGHT);
			
			topPath2.lineTo(0,TOP_HEIGHT);

			topPath2.lineTo(backLeft/2*Math.sqrt(3), TOP_HEIGHT/2+backLeft/2); // gud


			
			

			
			g2d2.setClip(topPath2);

			g2d2.drawImage(croppedTopFace, 0, 0, null);

			
			
			
			
			

//		

			
			
			g2d.dispose();
			g2d2.dispose();




			 int[] boundingBox = getBoundingBox(croppedcroppedTopFace);
//			 highlightBoundingBox(croppedcroppedTopFace, boundingBox);
	         int x = boundingBox[0];
	         int y = boundingBox[1];
	         int width2 =boundingBox[2] - boundingBox[0];
	         int height2 = boundingBox[3] - boundingBox[1];
	         

	         BufferedImage croppedImage= new BufferedImage(width2, height2, BufferedImage.TYPE_INT_ARGB);
	        		 croppedImage = croppedcroppedTopFace.getSubimage(x, y, width2, height2);
			
			String desktopPath = javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath() + File.separator + "croppedTopFace.png";
			String desktopPath1 = javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath() + File.separator + "croppedLeftFace.png";
			String desktopPath2 = javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath() + File.separator + "croppedRightFace.png";

			File outputFile = new File(desktopPath);
			File outputFile1 = new File(desktopPath1);
			File outputFile2 = new File(desktopPath2);


			try {
			    ImageIO.write( croppedImage, "PNG", outputFile);
			    ImageIO.write(croppedLeftFace, "PNG", outputFile1);
			    ImageIO.write(croppedRightFace, "PNG", outputFile2);

//			    System.out.println("Image saved successfully at: " + desktopPath);
			} catch (IOException e) {
			    System.out.println("Error saving image: " + e.getMessage());
			}
			
		
			
			
			
			
			setSprite(combineImages(croppedLeftFace,croppedRightFace, croppedImage));
//			System.out.println("its been cut");
			return true;
		}

		private int leftAdjustmentY;
		private int RightAdjustmentY;
		private int minYAdjustment;
		
		private int[] getBoundingBox(BufferedImage image) {
		    int minX = image.getWidth();
		    int minY = image.getHeight();
		    int maxX = 0;
		    int maxY = 0;

		    for (int y = 0; y < image.getHeight(); y++) {
		        for (int x = 0; x < image.getWidth(); x++) {
		            if (isNonEmptyPixel(image, x, y)) {
		            	 if (x < minX) { minX = x; leftAdjustmentY = y;}
		                    if (y < minY) minY = y;
		                    if (x > maxX) {maxX = x; RightAdjustmentY =y;}
		                    if (y > maxY) maxY = y;
		            }
		        }
		    }

		    // Ensure we don't return an invalid bounding box
		    if (minX > maxX || minY > maxY) {
		        return new int[]{0, 0, 0, 0}; // No non-empty pixels found
		    }

	        image.setRGB(minX+1, leftAdjustmentY, Color.RED.getRGB());
	        image.setRGB(maxX-10, RightAdjustmentY, Color.RED.getRGB());
	        minYAdjustment = minY;

		    return new int[]{minX, minY, maxX, maxY};
		}
		/**
		 * should be added to gameobject
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
		
		private void highlightBoundingBox(BufferedImage image, int[] boundingBox) {
		    int minX = boundingBox[0];
		    int minY = boundingBox[1];
		    int maxX = boundingBox[2];
		    int maxY = boundingBox[3];

		    for (int x = minX; x <= maxX; x++) {
		        image.setRGB(x, minY, Color.RED.getRGB());
		        image.setRGB(x, maxY, Color.RED.getRGB());
		    }
		    for (int y = minY; y <= maxY; y++) {
		        image.setRGB(minX, y, Color.RED.getRGB());
		        image.setRGB(maxX, y, Color.RED.getRGB());
		    }

		    try {
		        File outputFile = new File("highlightedBoundingBox.png");
		        ImageIO.write(image, "PNG", outputFile);
//		        System.out.println("Highlighted image saved successfully.");
		    } catch (IOException e) {
//		        System.out.println("Error saving highlighted image: " + e.getMessage());
		    }
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
