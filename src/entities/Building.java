package entities;

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
		System.out.println(dx + " : " + dy);
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
}
