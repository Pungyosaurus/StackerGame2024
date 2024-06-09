package entities;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

public class Building extends GameObject {
	private int width, height;
	private double xSpeed=0,ySpeed=0;
	private int gravity = 1;
	private boolean drop = false;
	
	public Building(int x, int y, int width, int height, BufferedImage image ){
		 super(x , y, width, height, image);
		 BufferedImage resizediGround = resizeBuffImage(image, width, height);
		 setSprite(resizediGround);
		}
	
	public void act() {
//		if(getX() >= 900 || getX()<100){
//			xSpeed = -xSpeed;
//			ySpeed = -ySpeed;
//		}
//		System.out.println(getX()+ " " + getY());
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
//		System.out.println(dx + " : " + dy);
		drop = true;
	}
		public boolean getDrop(){
		return drop;
	}
		
		public void cut(int side, int depth){
			if(side == 1){
				//left facing side cut by depth
				//right facing side stays the same length
				//top facing side gets cut by depth at the same 
			}
			
		}
		
		public boolean collides(Building prev, boolean direction) {
			double x = getX()+getWidth()/2;
			double y = getY();
			Graphics2D g2d = (Graphics2D) this.getGraphics();
			
			drawPoint(g2d,(int) x,(int) y);
			
			double xo = prev.getX()+prev.getWidth()/2;
			double yo = prev.getY();
//			double px = prev.getX() + prev.getWidth() / 2;
//			double py = prev.getY() + prev.getWidth() / (2 * Math.sqrt(3));
//			
//			double x = getX() + getWidth() / 2;
//			double y = getY() + getHeight();
//			
			System.out.println(y + "   :   " + yo);
			if(y > yo)
				return true;
			
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
			
//			double x = getX()+getWidth()/2;
//			double y = getY()+getHeight();
//			Graphics2D g2d = (Graphics2D) this.getGraphics();
//			
//			drawPoint(g2d,(int) x,(int) y);
//			
//			double xo = prev.getX()+prev.getWidth()/2;
//			double yo = prev.getY();
////			System.out.println(xo+ " "+ yo);
//			
//			double bo = getHeight() - yo - 1/Math.sqrt(3)*xo;
//			double b = getHeight() - y -1/Math.sqrt(3)*x;
////			System.out.println(b + " "+bo);
//			if(bo-b <20 && bo-b >-20) {
//				if(Math.sqrt(Math.pow(x-xo,2)+Math.pow(y-yo,2)) > Math.sqrt(Math.pow(0-width/2,2)+Math.pow(height/2-0,2))) {
//					System.out.println("collides");
//					return true;
//				}
//			}
			
			
//			Dimension t = this.getSize();
//			double m;
//			if(direction) {
//				m = 9/16;
//			}else
//				m = -16/9;
		

			return false;

		}
		
		private void drawPoint(Graphics2D g2d, int x, int y) {

			g2d.setColor(Color.RED);

			int size = 100;

			g2d.fillOval(x - size / 2, y - size / 2, size, size);



		}
		
}
