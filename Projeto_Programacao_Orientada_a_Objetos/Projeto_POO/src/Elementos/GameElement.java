package Elementos;

import pt.iul.ista.poo.gui.ImageTile;
import pt.iul.ista.poo.utils.Point2D;
import GameEngine.GameEngine;

public abstract class GameElement implements ImageTile{
	
	private Point2D position;
	private boolean move;
	
	public GameElement(int x, int y, boolean move) {
		Point2D position = new Point2D(x, y);
		if(! GameEngine.getInstance().isPoint(position))
			throw new IllegalArgumentException("Agumento inválido na construção de um dos elementos do jogo");
		this.position = position;
		this.move = move;
	}
	
	public static GameElement criar(String info, int x, int y) {
		switch(info){
			
		case "p": return new Pine(x,y);
		case "e": return new Eucaliptus(x, y);
		case "m": return new Grass(x, y);
		case "_": return new Mata(x, y);
		case "a": return new Abete(x,y);
		case "b": return new Fuel(x,y);
		case "Bulldozer": return new Bulldozer(x, y);
		case "FireTruck": return new FireTruck(x,y);
		case "FiremanBot": return new AutoFireman(x,y);
		
		
		default: throw new IllegalArgumentException();
		}
	}
	
	@Override
	public Point2D getPosition() {
		return position;
	}
	
	public void setPosition(Point2D position) {
		if(move);
			this.position = position;
	}
	
	public abstract boolean isUpdatable();
	
	
}
