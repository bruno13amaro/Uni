package Elementos;

import java.util.Random;

import GameEngine.GameEngine;

public abstract class BurnableTerrain extends Terreno implements Updatable{
	
	private int turnosBurning;
	private boolean isBurning = false;
	private boolean isBurned = false;
	private int burned;
	private int probabilidade;
	
	
	public BurnableTerrain(int x, int y, int burned, int probabilidade) {
		super(x,y);
		turnosBurning = 0;
		this.burned = burned;
		this.probabilidade = probabilidade;
	}
	
	public void resetTurnos() {
		turnosBurning = 0;
		isBurning = false;
	}
	
	public void setBurned() {
		isBurned = true;
	}
	
	@Override
	public boolean isBurnt() {
		return isBurned;
	}
	
	
	public void addBurning() {
		turnosBurning ++;
	}
	
	public int getTurnosBurning() {
		return turnosBurning;
	}
	
	@Override
	public boolean isBurning() {
		return isBurning;
	}
	
	public void putBurning() {
		isBurning = true;
	}
	
	@Override
	public boolean isBurnable() {
		return true;
	}
	
	@Override
	public boolean isMata() {
		return false;
	}
	
	@Override
	public void update() {
		if(isBurned()) {
			createBurned();
			return;
		}
		turnosBurning ++;
	}
	
	public void createBurned() {
		setBurned();
		GameEngine.getInstance().deleteFire(this);
		GameEngine.getInstance().changePontos(-50);
	}
	
	@Override
	public boolean isUpdatable() {
		if(isBurning)
			return true;
		return false;
	}
	
	public boolean isBurned() {
		if(getTurnosBurning() == burned)
			return true;
		return false;
	}
	
	public boolean willBurn() {
		Random r = new Random();
		int aux = r.nextInt(100);
		if(aux < probabilidade)
			return true;
		return false;
	}

	
	

}
