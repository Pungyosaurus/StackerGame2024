package listeners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MouseHandler implements MouseListener{

	private boolean clicked, pressed, released;
	private static int mouseX, mouseY;
	
/**
 * Gets the location of the cursor and sets booleans pressed and running to true
 */
	@Override
	public void mousePressed(MouseEvent e) {
		pressed = true;
		mouseX = e.getX();
		mouseY = e.getY();

	}
/**
 * Gets the location of where the user has released and sets the released boolean to true
 */
	@Override
	public void mouseReleased(MouseEvent e) {
		released = true;
		mouseX = e.getX();
		mouseY = e.getY();
	}

	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		setClicked(true);
//		mouseX = e.getX();
//		mouseY = e.getY();
	}
	
	// Ignore these methods, they are not being used

	@Override
	public void mouseEntered(MouseEvent e) {		
	}

	@Override
	public void mouseExited(MouseEvent e) {	
	}
	/**
	 * @return the clicked
	 */
	public boolean isClicked() {
		return clicked;
	}
	/**
	 * @param clicked the clicked to set
	 */
	public void setClicked(boolean clicked) {
		this.clicked = clicked;
	}

}
