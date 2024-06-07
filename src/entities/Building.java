package entities;

import java.awt.Dimension;
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
		
		public boolean collides(Building other, boolean direction) {
			Dimension o = other.getSize();
			Dimension t = this.getSize();
			
			if(direction) {
				
			}
			
			Line2D oLine = new Line2D(
					(float)other.getX(), 
					(float)(other.getY() + o.getHeight() / 2), 
					(float)(other.getX() + o.getWidth() / 2), 
					(float)(other.getY() + o.getHeight()));

//			Line tLine = new Line();


			return false;
		}
}
