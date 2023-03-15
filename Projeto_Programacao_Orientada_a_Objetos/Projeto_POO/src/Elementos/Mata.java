package Elementos;

public class Mata extends Terreno{
	
	public Mata(int x, int y) {
		super(x,y);
	}

	@Override
	public String getName() {
		return "land";
	}

	@Override
	public boolean isBurnable() {
		return false;
	}

	@Override
	public boolean isMata() {
		return true;
	}
	
	@Override
	public boolean isBurning() {
		return false;
	}

	@Override
	public boolean isUpdatable() {
		return false;
	}

	@Override
	public boolean isBurnt() {
		return false;
	}

	

}
