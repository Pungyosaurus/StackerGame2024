package entities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Building extends GameObject {
	private int width, height;
	private double xSpeed=0,ySpeed=0;
	private int gravity = 1;
	private boolean drop = false;

	private BufferedImage img1, img2, img3;
	private int backLeft, frontLeft , frontRight, backRight;

	private int TOP_WIDTH, TOP_HEIGHT;


	private double by;
	private double bx;
	private double tx;
	private double ty;
	
	public double rightFaceWidth;
	public double rightFaceHeight;
	private int leftFaceWidth;
	public Building(int x, int y, int width, int height, BufferedImage image ){
		 super(x , y, width, height, image);
		 
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
		 
		 setSprite(combinedImage);
		 //make this the value of the true cut zeros are temporrary
//		 cut(0,0,0,0);
		 
		 
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
		
		
		public int[] collides(Building prev, int direction) {
			//values to cut by
			
			double xo = prev.getX()+ prev.getTopMiddleX();
			double yo = prev.getY()+ prev.getTopMiddleY();
			
			double x = getX()+getBottomMiddleX();
			double y = getY()+getBottomMiddleY();
			
			
//			if(y > yo)
//				return true;			
//			if(direction) {
//				if(x >= xo) {
//					return true;
//				}
//
//			}else if(x <= xo) {
//				return true;
//			}else {
//				return false;
//			}			
			
			//bottom left to top right
			if(direction ==1) {
				double bo =  yo + 1/Math.sqrt(3)*xo;
				double b =  y +1/Math.sqrt(3)*x;
				if(bo-b <20 && bo-b >-20) {
					setY(getY()+ bo-b);
					int[] offsetValues = new int[4];
	
					if(Math.sqrt(Math.pow(x-xo,2)+Math.pow(y-yo,2)) < rightFaceWidth/Math.sqrt(3)*2) {
						int offset =(int) Math.sqrt(Math.pow(x-xo,2)+Math.pow(y-yo,2));
						
						if(x>xo) {
							offsetValues[2] = offset;
						}else {
							offsetValues[0] = offset;
						}
						
						System.out.println("collides");
						return offsetValues;
					}
				}
			}
			//top left to bottom right
			if(direction ==2) {
				double bo =  yo - 1/Math.sqrt(3)*xo;
				double b =  y - 1/Math.sqrt(3)*x;
				if(bo-b <20 && bo-b >-20) {
					int[] offsetValues = new int[4];
	
					if(Math.sqrt(Math.pow(x-xo,2)+Math.pow(y-yo,2)) < leftFaceWidth/Math.sqrt(3)*2) {
						int offset =(int) Math.sqrt(Math.pow(x-xo,2)+Math.pow(y-yo,2));
						
						if(x>xo) {
							offsetValues[1] = offset;
						}else {
							offsetValues[3] = offset;
						}
						
						System.out.println("collides");
						return offsetValues;
					}
				}
			}
			// no array values
			return null;

		}
		public BufferedImage combineImages(BufferedImage leftFace, BufferedImage rightFace, BufferedImage topFace) {


			
			
			double hyp = Math.sqrt(Math.pow(TOP_HEIGHT / 2, 2) + Math.pow(TOP_WIDTH / 2, 2));
			System.out.println(hyp);
			
			
			int combinedWidth = leftFace.getWidth() + rightFace.getWidth();
			rightFaceWidth = rightFace.getWidth();
			leftFaceWidth = leftFace.getWidth();
			
			int combinedHeight =(int) ( img1.getHeight() - img1.getWidth()/Math.sqrt(3) + topFace.getHeight());
			rightFaceHeight = (int) ( combinedHeight - topFace.getHeight());

			
			this.setSize(combinedWidth,combinedHeight);
			BufferedImage combined = new BufferedImage(combinedWidth, combinedHeight, BufferedImage.TYPE_INT_ARGB);

			Graphics2D g2d = combined.createGraphics();

			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);


			// Draw the top face
			
			g2d.drawImage(topFace,0 , 0, null);


			// Draw the left face
//			g2d.drawImage(leftFace, 0, topFace.getHeight() /2, null);
//			g2d.drawImage(leftFace, 0,(int) (topFace.getHeight() / 2 - adjustment), null);
			g2d.drawImage(leftFace, 0,(int) leftAdjustmentY, null);

			// Draw the right face


//			g2d.drawImage(rightFace, leftFace.getWidth(),(int) (leftFace.getWidth() / Math.sqrt(3)), null);
			g2d.drawImage(rightFace, combinedWidth-rightFace.getWidth(),(int) RightAdjustmentY, null);





			
//			BufferedImage whitespace = new BufferedImage(combinedWidth, combinedHeight, BufferedImage.TYPE_INT_ARGB);
//			g2d = whitespace.createGraphics();
//		    g2d.setBackground(new java.awt.Color(0, 0, 0, 0));
//		    g2d.clearRect(0, 0, combinedWidth,combinedHeight);
		 
		    
			int x1 = (int) (topFace.getWidth() - rightFace.getWidth());
			int y1 = topFace.getHeight();
			setTopMiddleX(x1);
			setTopMiddleY(y1);
				
				
				for(int i =0; i<10;i++) {
					combined.setRGB(x1-i,y1-i, Color.pink.getRGB());

				}
				
			y1 = combined.getHeight();
			setBottomMiddleX(x1);
			setBottomMiddleY(y1);
				
				 for(int i =0; i<10;i++) {
					 combined.setRGB(x1-i,y1-i-1, Color.pink.getRGB());

				}
		    
		    
		    
		    
		    
		  
			 
			 
			g2d.drawImage(combined, 0, 0, null);

			g2d.dispose();

		    
		    
		    

		

			return combined;

		}

		public void cut( int backLeftP, int frontLeftP, int frontRightP, int backRightP) {

			// 
			backLeft += backLeftP;
			frontLeft += frontLeftP;
			frontRight += frontRightP;
			backRight += backRightP;
			System.out.println(backLeft + " : " + frontLeft + " : "+ frontRight + " : "+ backRight);
//			adjustment = (leftCutDepth+ topRightCutDepth)/ 2 / 2 / Math.sqrt(3);
			int startX = (int) (backLeft / 2 * Math.sqrt(3));
			int startY = (int) (backLeft / 2) ;
			int width = (int) (img1.getWidth()- backLeft/ 2 * Math.sqrt(3) - frontLeft/ 2 * Math.sqrt(3));
			int height = img1.getHeight() - backLeft /2 - frontLeft/2;
			System.out.println(startX + " : " + startY);
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
			
			
			topPath2.moveTo(backLeft/2*Math.sqrt(3), TOP_HEIGHT/2+backLeft/2);
			
			Xint = TOP_WIDTH/2 + backLeft/2*Math.sqrt(3) - backRight/2*Math.sqrt(3);
			Yint = backLeft/2 + backRight/2;
			
			topPath2.lineTo(Xint,Yint);
			// everything was move to not lineto
			topPath2.lineTo(TOP_WIDTH - backRight/2*Math.sqrt(3) , TOP_HEIGHT - backRight/2);// minus was a plus
			
			
			topPath2.lineTo(TOP_WIDTH,TOP_HEIGHT);
			
			topPath2.lineTo(0,TOP_HEIGHT);


			
			

			
			g2d2.setClip(topPath2);

			g2d2.drawImage(croppedTopFace, 0, 0, null);

			
			
			
			
			
			
			
			
//			
//			BufferedImage croppedcroppedcroppedTopFace = new BufferedImage(TOP_WIDTH, TOP_HEIGHT, BufferedImage.TYPE_INT_ARGB);
//			
//			Graphics2D g2d3 = croppedcroppedcroppedTopFace.createGraphics();
//
//			g2d3 = croppedcroppedcroppedTopFace.createGraphics();
//
//			g2d3.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);		
//				
//			Path2D.Double topPath3 = new Path2D.Double();
//			
//			
//			
//			topPath3.moveTo(totalTopLeftCutDepth,(int) (TOP_HEIGHT/2 + totalTopLeftCutDepth / Math.sqrt(3)));
//
//			double topXInt =  TOP_WIDTH / 2 + totalTopLeftCutDepth * Math.sqrt(3)/2 ;
//			double topYInt = 0 + totalTopLeftCutDepth / 2 ;
//			
//			topPath3.lineTo(topXInt, topYInt);
//
//			topPath3.lineTo(TOP_WIDTH, 0);
//
//			topPath3.lineTo(TOP_WIDTH, TOP_HEIGHT);
//
//			topPath3.lineTo(0,TOP_HEIGHT);
//
//			
//			g2d3.setClip(topPath);
//
//			g2d3.drawImage(croppedcroppedTopFace, 0, 0, null);
//			
//			
//			
//			
//			BufferedImage croppedcroppedcroppedcroppedTopFace = new BufferedImage(TOP_WIDTH, TOP_HEIGHT, BufferedImage.TYPE_INT_ARGB);
//			
//			Graphics2D g2d4 = croppedcroppedcroppedcroppedTopFace.createGraphics();
//
//			g2d4 = croppedcroppedcroppedcroppedTopFace.createGraphics();
//
//			g2d4.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);		
//				
//			Path2D.Double topPath4 = new Path2D.Double();
//			
//			
//			
//			topPath4.moveTo(TOP_WIDTH - totalTopRightCutDepth,(int) (TOP_HEIGHT/2 + totalTopRightCutDepth / Math.sqrt(3)));
//
//			
//			double topXInt2 =  TOP_WIDTH / 2 - totalTopRightCutDepth * Math.sqrt(3)/2 ;
//			double topYInt2 = 0 + totalTopRightCutDepth / 2 ;
//			
//			topPath4.lineTo(topXInt2, topYInt2);
//
//			topPath4.lineTo(0, 0);
//
//			topPath4.lineTo(0,TOP_HEIGHT);
//
//			topPath4.lineTo(TOP_WIDTH,TOP_HEIGHT);
//
//			
//			g2d4.setClip(topPath4);
//
//			g2d4.drawImage(croppedcroppedcroppedTopFace, 0, 0, null);
//		

			
			
			g2d.dispose();
			g2d2.dispose();
//			g2d3.dispose();
//			g2d4.dispose();


//
//			 int[] boundingBox = getBoundingBox(croppedcroppedTopFace);
//	         int x = boundingBox[0];
//	         int y = boundingBox[1];
//	         int width2 = Math.abs(boundingBox[2] - boundingBox[0] ) -1;
//	         int height2 =Math.abs( boundingBox[3] - boundingBox[1])-1;
//	         
//	         System.out.println(croppedcroppedTopFace.getWidth()+" "+croppedcroppedTopFace.getHeight() );
//	         System.out.println(x+" "+ y);
//	         System.out.println(width2+" " +height2);
//	         croppedTopFace = croppedcroppedTopFace.getSubimage(x, y, width2, height2);
			
			String desktopPath = javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath() + File.separator + "croppedTopFace.png";
			String desktopPath1 = javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath() + File.separator + "croppedLeftFace.png";
			String desktopPath2 = javax.swing.filechooser.FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath() + File.separator + "croppedRightFace.png";

			File outputFile = new File(desktopPath);
			File outputFile1 = new File(desktopPath1);
			File outputFile2 = new File(desktopPath2);


			try {
			    ImageIO.write(croppedcroppedTopFace, "PNG", outputFile);
			    ImageIO.write(croppedLeftFace, "PNG", outputFile1);
			    ImageIO.write(croppedRightFace, "PNG", outputFile2);

			    System.out.println("Image saved successfully at: " + desktopPath);
			} catch (IOException e) {
			    System.out.println("Error saving image: " + e.getMessage());
			}
			
		
			
			
			
			
			setSprite(combineImages(croppedLeftFace,croppedRightFace,croppedTopFace));
		}

		private int leftAdjustmentY;
		private int RightAdjustmentY;
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

	        return new int[]{minX, minY, maxX, maxY};
	    }

	    private boolean isNonEmptyPixel(BufferedImage image, int x, int y) {
	        int pixel = image.getRGB(x, y);
	        Color color = new Color(pixel, true);
	        //empty spaces are white or fully transparent
	        return !(color.getAlpha() == 0);
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
