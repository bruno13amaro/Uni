package Elementos;


import java.util.List;


import GameEngine.GameEngine;
import pt.iul.ista.poo.utils.Point2D;

public class Fire extends GameElement implements Updatable{

	public Fire(int x, int y) {
		super(x, y, false);
	}

	@Override
	public String getName() {
		return "fire";
	}

	@Override
	public int getLayer() {
		return 1;
	}
	
	@Override
	public void update() {
		List<Point2D> aux = getPosition().getNeighbourhoodPoints();
		for(Point2D p : aux) {
			if(GameEngine.getInstance().isPoint(p) && canBurn(p))
				GameEngine.getInstance().createFire(p);
		}
	}
	
	public void propagarExplosao() {
		List<Point2D> aux = getPosition().getWideNeighbourhoodPoints();
		for(Point2D p : aux)
			if(GameEngine.getInstance().isPoint(p)  && canBurnExplosion(p))
				GameEngine.getInstance().createFire(p);
	}
	
	private boolean canBurn(Point2D p) {
		if(GameEngine.getInstance().haveMore(p))
			return false;
		Terreno aux = GameEngine.getInstance().getTerreno(p);
		if(!aux.isBurnable())
			return false;
		BurnableTerrain aux2 = (BurnableTerrain) aux;
		if(aux2.isBurnt())
			return false;
		if(!aux2.willBurn())
			return false;
		return true;
	}
	
	private boolean canBurnExplosion(Point2D p) {
		if(GameEngine.getInstance().haveMore(p))
			return false;
		Terreno aux = GameEngine.getInstance().getTerreno(p);
		if(!aux.isBurnable())
			return false;
		BurnableTerrain aux2 = (BurnableTerrain) aux;
		if(aux2.isBurnt())
			return false;
		return true;
	}

	@Override
	public boolean isUpdatable() {
		return true;
	}

	
	
	

	
	
	
	

}
