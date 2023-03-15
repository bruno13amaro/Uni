package Elementos;

import GameEngine.GameEngine;


public class Fuel extends BurnableTerrain{

	private static int burned = 3;
	private static int probabilidade = 90;
	private boolean explosion = false;
	
	public Fuel(int x, int y) {
		super(x, y, burned, probabilidade);
	}
	
	@Override
	public String getName() {
		if(isBurnt() && explosion)
			return "explosion";
		if(isBurnt() && !explosion)
			return "burntfuelbarrel";
		return "fuelbarrel";
	}
	
	@Override
	public boolean isUpdatable() {
		if(isBurning() || isBurnt() && explosion)
			return true;
		return false;
	}
	
	@Override
	public void update() {
		if(isBurnt())
			manageExplosion();
		else
			super.update();
	}
	
	public void manageExplosion() {
		explosion = false;
	}
	
	@Override
	public void setBurned() {
		super.setBurned();
		explosion = true;
		GameEngine.getInstance().getFire(getPosition()).propagarExplosao();
	}
	

}
