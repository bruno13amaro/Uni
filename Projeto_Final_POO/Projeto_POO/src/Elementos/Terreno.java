package Elementos;

public abstract class Terreno extends GameElement{

	public Terreno(int x, int y) {
		super(x, y, false);
	}
	
	@Override
	public int getLayer() {
		return 0;
	}

	public abstract boolean isBurnable();
	
	public abstract boolean isMata();
	
	public abstract boolean isBurning();
	
	public abstract boolean isBurnt();
	
	

}
