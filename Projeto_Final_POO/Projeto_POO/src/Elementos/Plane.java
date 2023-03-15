package Elementos;


import pt.iul.ista.poo.utils.Direction;
import pt.iul.ista.poo.utils.Point2D;
import GameEngine.*;

public class Plane extends GameElement implements Updatable{
	
	private boolean ready ;

	public Plane(int x, int y) {
		super(x, y, true);
		ready = false;
	}

	@Override
	public String getName() {
		return "plane";
	}

	@Override
	public int getLayer() {
		return 5;
	}
	
	private boolean isReady() {
		return ready;
	}
	
	private void putReady() {
		ready = true;
	}
	
	private void move() {
		Point2D aux = getPosition();
		if(GameEngine.getInstance().isOnFire(aux)) 
			deleteFire(aux);
	
		if(!canMove())
			GameEngine.getInstance().removeElement(this);
		else 
			for(int i = 0; i < 2; i ++) {
				aux = aux.plus(Direction.UP.asVector());
				if(GameEngine.getInstance().isOnFire(aux)) 
					deleteFire(aux);
			}
		aux = aux.plus(Direction.UP.asVector());
		setPosition(aux);
	}
	
	private void deleteFire(Point2D p) {
		BurnableTerrain b = (BurnableTerrain) GameEngine.getInstance().getTerreno(p);
		GameEngine.getInstance().deleteFire(b);
		createWater(p);
	}
	
	private void createWater(Point2D p) {
		Water w = new Water(p.getX(), p.getY(), "water_down");
		w.deactivate();
		GameEngine.getInstance().addElement(w);
		GameEngine.getInstance().changePontos(50);
	}
	
	private boolean canMove() {
		return getPosition().getY() != 0;
	}
	
	public void update() {
		if(isReady())
			move();
		else
			putReady();
	}

	@Override
	public boolean isUpdatable() {
		return true;
	}
	
	
	
	

}
