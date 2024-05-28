package entities;

import java.awt.image.BufferedImage;

public class Building extends GameObject {
	private int width, height;
	private int xSpeed=3,ySpeed=3;
	public Building(int x, int y, int width, int height, BufferedImage image ){
		 super(x , y, width, height, image);
		 BufferedImage resizediGround = resizeBuffImage(image, width, height);
		 setSprite(resizediGround);
		}
	
	public void act() {
		if(getX() >= 600){
			xSpeed = -xSpeed;
			ySpeed = -ySpeed;
		}
		System.out.println(getX()+ " " + getY());
		setX(getX()+xSpeed);
		setY(getY()+ySpeed);
	}
	public void drop(){
		
		xSpeed = 0;
		ySpeed=0;
		setY(getY()+1);
	}
}
