package listeners;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

public class KeyHandler implements KeyListener {

	private boolean spacebar, escape, delete, up, down;

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		switch (e.getKeyChar()) {
		case ' ':
			spacebar = true;
			break;
		case 27:
			escape = true;
			break;
		case 127:
			delete = true;
			break;
		

		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		switch(e.getKeyCode()) {
		case KeyEvent.VK_UP:
			up = true;
			break;
		case KeyEvent.VK_DOWN:
			down = true;
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * @return the spacebar
	 */
	public boolean isSpacebar() {
		return spacebar;
	}

	/**
	 * @param spacebar the spacebar to set
	 */
	public void setSpacebar(boolean spacebar) {
		this.spacebar = spacebar;
	}

	/**
	 * @return the escape
	 */
	public boolean isEscape() {
		return escape;
	}

	/**
	 * @param escape the escape to set
	 */
	public void setEscape(boolean escape) {
		this.escape = escape;
	}

	/**
	 * @return the delete
	 */
	public boolean isDelete() {
		return delete;
	}

	/**
	 * @param delete the delete to set
	 */
	public void setDelete(boolean delete) {
		this.delete = delete;
	}

	/**
	 * @return the down
	 */
	public boolean isDown() {
		return down;
	}

	/**
	 * @param down the down to set
	 */
	public void setDown(boolean down) {
		this.down = down;
	}

	/**
	 * @return the up
	 */
	public boolean isUp() {
		return up;
	}

	/**
	 * @param up the up to set
	 */
	public void setUp(boolean up) {
		this.up = up;
	}

}