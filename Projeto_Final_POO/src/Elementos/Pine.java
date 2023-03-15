package Elementos;



public class Pine extends BurnableTerrain{
	
	private static int BURN = 5;
	private static int BURNED = 10;
	
	public Pine(int x, int y) {
		super(x,y, BURNED, BURN);
	}

	@Override
	public String getName() {
		if(isBurnt())
			return "burntpine";
		return "pine";
	}

	

	


}
