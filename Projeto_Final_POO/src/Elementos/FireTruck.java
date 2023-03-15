package Elementos;

import java.util.List;

import GameEngine.GameEngine;
import pt.iul.ista.poo.gui.ImageMatrixGUI;
import pt.iul.ista.poo.utils.Direction;
import pt.iul.ista.poo.utils.Point2D;

public class FireTruck extends Truck{

	public FireTruck(int x, int y) {
		super(x, y);
	}

	@Override
	public String getName() {
		return "firetruck";
	}

	@Override
	public void move(Point2D position) {
		setPosition(position);
		GameEngine.getInstance().getFireman().setPosition(position);
	}

	@Override
	public void dontMove(Point2D position) {
		List<Point2D> aux = position.getFrontRect(Direction.directionFor(ImageMatrixGUI.getInstance().keyPressed()), 2, 3);
		for(Point2D p : aux) {
			if(GameEngine.getInstance().isOnFire(p)) {
				GameEngine.getInstance().deleteFire((BurnableTerrain) GameEngine.getInstance().getTerreno(p));
				GameEngine.getInstance().changePontos(50);
			}
			GameEngine.getInstance().addElement(Water.criar(p));
		}
	}
	
	

}
