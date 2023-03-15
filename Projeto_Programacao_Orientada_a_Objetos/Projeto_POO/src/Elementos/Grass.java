package Elementos;

public class Grass extends BurnableTerrain{
	
	private static int BURN = 10;
	private static int BURNED = 3;
	
	public Grass(int x, int y) {
		super(x,y,BURNED, BURN);
	}

	@Override
	public String getName() {
		if(isBurnt())
			return "burntgrass";
		return "grass";
	}




}
