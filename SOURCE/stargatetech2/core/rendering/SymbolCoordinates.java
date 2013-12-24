package stargatetech2.core.rendering;

public class SymbolCoordinates {
	
	public final float x0, x1, y0, y1;
	
	private SymbolCoordinates(float x, float y){
		this.x0 = x / 294F;
		this.x1 = (x + 96) / 294F;
		this.y0 = y / 1284F;
		this.y1 = (y + 96) / 1284F;
	}
	
	public static SymbolCoordinates get(int symbol){
		if(symbol > 0 && symbol < 40){
			symbol--;
			int line = symbol / 3;
			int colm = symbol % 3;
			return new SymbolCoordinates((float)(colm * 99), (float)(line * 99));
		}
		return null;
	}
}