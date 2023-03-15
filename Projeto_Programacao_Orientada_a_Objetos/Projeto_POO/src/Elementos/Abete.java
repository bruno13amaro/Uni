package Elementos;

public class Abete extends BurnableTerrain{

	private static int burned = 20;
	private static int probabilidade = 5;
	
	public Abete(int x, int y) {
		super(x, y, burned, probabilidade);
	}

	@Override
	public String getName() {
		if(isBurnt())
			return "burntabies";
		return "abies";
	}
	
	

}
