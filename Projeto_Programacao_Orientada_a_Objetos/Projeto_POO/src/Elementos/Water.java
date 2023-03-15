package Elementos;

import java.awt.event.KeyEvent;
import GameEngine.GameEngine;
import pt.iul.ista.poo.gui.ImageMatrixGUI;
import pt.iul.ista.poo.utils.Point2D;

public class Water extends GameElement implements Updatable{
	
	private boolean active = true;
	private String name;

	public Water(int x, int y, String name) {
		super(x, y, false);
		this.name = name;
	}
	
	public static Water criar(Point2D p) {
		int key = ImageMatrixGUI.getInstance().keyPressed();
		switch(key) {
		case KeyEvent.VK_UP: return new Water(p.getX(), p.getY(), "water_up");
		case KeyEvent.VK_DOWN: return new Water(p.getX(), p.getY(),"water_down");
		case KeyEvent.VK_RIGHT: return new Water(p.getX(), p.getY(), "water_right");
		case KeyEvent.VK_LEFT: return new Water(p.getX(), p.getY(), "water_left");
		
		default: throw new IllegalArgumentException();
		}
	}
	
	private boolean isActive() {
		return active;
	}
	
	public void deactivate() {
		active = false;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getLayer() {
		return 2;
	}
	
	public void update() {
		if(isActive())
			deactivate();
		else
			GameEngine.getInstance().removeElement(this);
	}

	@Override
	public boolean isUpdatable() {
		return true;
	}
	

}
