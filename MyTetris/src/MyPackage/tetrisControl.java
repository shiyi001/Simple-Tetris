package MyPackage;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class tetrisControl implements KeyListener{
	tetrisModel model;
	
	public tetrisControl(tetrisModel model){
		this.model = model;
	}
	
	public void keyPressed(KeyEvent e){
		int keyCode = e.getKeyCode();
		switch(keyCode){
		case KeyEvent.VK_UP:
		case KeyEvent.VK_W:
			model.changeShape();
			break;
		case KeyEvent.VK_LEFT:
		case KeyEvent.VK_A:
			model.leftMove();
			break;
		case KeyEvent.VK_RIGHT:
		case KeyEvent.VK_D:
			model.rightMove();
			break;
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_S:
			model.speedUp();
			break;
		case KeyEvent.VK_P:
			model.changePauseState();
			break;
		default:
			break;
		}
	}
	
	public void keyReleased(KeyEvent e){
		int keyCode = e.getKeyCode();
		switch(keyCode){
		case KeyEvent.VK_DOWN:
		case KeyEvent.VK_S:
			model.recoverSpeed();
			break;
		default:
			break;
		}
	}
	
	public void keyTyped(KeyEvent e){
		
	}
}
