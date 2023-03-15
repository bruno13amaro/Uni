package Elementos;

import pt.iul.ista.poo.gui.ImageMatrixGUI;
import pt.iul.ista.poo.utils.*;

import java.util.ArrayList;

import GameEngine.GameEngine;

public class Fireman extends GameElement{
	
	Truck truck;
	
	public Fireman(int x, int y) {
		super(x, y, true);
	}

	@Override
	public String getName() {
		return "fireman";
	}

	@Override
	public int getLayer() {
		return 4;
	}
	
	public Truck getTruck() {
		return truck;
	}
	
	public void move(int keycode) {
			Direction d = Direction.directionFor(keycode);
			Point2D position = getPosition().plus(d.asVector());
			if(truck != null) {
				truck.willMove(position);
				GameEngine.getInstance().changePontos(-5);
			}else{
				if(canMoveto(position)) {
					setPosition(position);
					putBulldozer(position);
				}
			}
	}
	
	private void putBulldozer(Point2D position) {
		ArrayList<Truck> bull = GameEngine.getInstance().getTrucks();
		for(Truck b : bull)
			if(b.getPosition().equals(position)) {
				truck = b;
				truck.putFireman();
				ImageMatrixGUI.getInstance().removeImage(this);
				break;
			}
	}
	
	public void retirarTruck() {
		truck.retirarFireman();
		truck = null;
		ImageMatrixGUI.getInstance().addImage(this);
	}
	
	
	public boolean canMoveto(Point2D p) {
		if(!GameEngine.getInstance().isPoint(p))
			return false;
		if(GameEngine.getInstance().isOnFire(p)) {
			createWater(p);
			return false;
		}
		return true;
	}
	
	public void createWater(Point2D p) {
		GameEngine.getInstance().addElement(Water.criar(p));
		GameEngine.getInstance().changePontos(50);
		BurnableTerrain aux = (BurnableTerrain)GameEngine.getInstance().getTerreno(p);
		GameEngine.getInstance().deleteFire(aux);
	}

	@Override
	public boolean isUpdatable() {
		return false;
	}

	
	

	
	
	

}
