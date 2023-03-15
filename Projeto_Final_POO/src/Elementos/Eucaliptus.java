package Elementos;

public class Eucaliptus extends BurnableTerrain{
	
	private static int BURN = 15;
	private static int BURNED = 5;
	
	public Eucaliptus(int x, int y) {
		super(x,y, BURNED, BURN);
	}

	@Override
	public String getName() {
		if(isBurnt())
			return "burnteucaliptus";
		return "eucaliptus";
	}

	
	

}
