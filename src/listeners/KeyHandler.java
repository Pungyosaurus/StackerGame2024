package listeners;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseListener;

public class KeyHandler implements KeyListener {

    private boolean spacebar, escape, delete;
    
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

}
