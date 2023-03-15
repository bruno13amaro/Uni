package Elementos;

import GameEngine.GameEngine;
import pt.iul.ista.poo.utils.Point2D;

public abstract class Truck extends GameElement implements Updatable{
	
private boolean fireman;
	
	public Truck(int x, int y) {
		super(x, y, true);
		fireman = false;
	}

	@Override
	public int getLayer() {
		return 3;
	}
	
	public boolean haveFireman() {
		return fireman;
	}
	
	public void putFireman() {
		fireman = true;
	}
	
	public void retirarFireman() {
		fireman = false;
	}
	
	@Override
	public void update() {
		GameEngine.getInstance().getFireman().retirarTruck();
	}

	@Override
	public boolean isUpdatable() {
		if(!haveFireman() && GameEngine.getInstance().getFireman().getTruck() != null && GameEngine.getInstance().getFireman().getTruck().equals(this))
			return true;
		return false;
	}
	
	public void willMove(Point2D position) {
		if(!GameEngine.getInstance().isPoint(position))
			return;
		if(canMoveTo(position))
			move(position);
		else
			dontMove(position);
	}
	
	public boolean canMoveTo(Point2D position) {
		if(GameEngine.getInstance().isOnFire(position))
			return false;
		return true;
	}
	
	public abstract void move(Point2D position);
	public abstract void dontMove(Point2D position);

}
