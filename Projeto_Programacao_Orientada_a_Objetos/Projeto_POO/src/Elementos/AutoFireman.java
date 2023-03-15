package Elementos;


import GameEngine.GameEngine;
import pt.iul.ista.poo.utils.Direction;
import pt.iul.ista.poo.utils.Point2D;

public class AutoFireman extends Fireman implements Updatable{

	public AutoFireman(int x, int y) {
		super(x, y);
	}
	
	//Problema: o bombeiro automatico cria agua mas pode-se deslocar para outra casa pois o metodo update so acaba quando o bombeiro se desloca para
	// uma nova casa, o que faz que por vezes ele se desloque para a casa com agua que ele criou
	@Override
	public void update() {
		boolean hasMoved = false;
		while (hasMoved == false)  {
			Direction randDir = Direction.random();
			Point2D newPosition = getPosition().plus(randDir.asVector());
			if (super.canMoveto(newPosition)) {
				setPosition(newPosition);
				hasMoved = true;
			}
		}
	}
	
	@Override
	public boolean isUpdatable() {
		return true;
	}
	
	@Override
	public String getName() {
		return "firemanbot";
	}
	
	@Override
	public void createWater(Point2D p) {
		GameEngine.getInstance().addElement(new Water(p.getX(), p.getY(), "water"));
		GameEngine.getInstance().changePontos(50);
		BurnableTerrain aux = (BurnableTerrain)GameEngine.getInstance().getTerreno(p);
		GameEngine.getInstance().deleteFire(aux);
	}

}
