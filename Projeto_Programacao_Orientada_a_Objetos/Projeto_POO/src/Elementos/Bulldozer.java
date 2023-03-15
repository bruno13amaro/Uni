package Elementos;


import GameEngine.GameEngine;
import pt.iul.ista.poo.utils.Point2D;

public class Bulldozer extends Truck implements Updatable{
	
	
	public Bulldozer(int x, int y) {
		super(x, y);
	}

	@Override
	public String getName() {
		return "bulldozer";
	}

	@Override
	public void move(Point2D position) {
		Terreno aux = GameEngine.getInstance().getTerreno(position);
		if(aux.isBurnable() && !aux.isBurnt())
			createMata(aux);
		setPosition(position);
		GameEngine.getInstance().getFireman().setPosition(position);
		
	}
	
	private void createMata(Terreno t) {
		GameEngine.getInstance().removeElement(t);
		Mata aux = new Mata(t.getPosition().getX(), t.getPosition().getY());
		GameEngine.getInstance().addElement(aux);
	}

	@Override
	public void dontMove(Point2D position){}
	
	

}