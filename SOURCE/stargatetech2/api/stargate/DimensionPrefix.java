package stargatetech2.api.stargate;

import java.util.Random;


public class DimensionPrefix {
	private Symbol[] symbols;
	
	public static DimensionPrefix generateRandom(){
		Random random = new Random();
		boolean[] used = new boolean[40];
		Symbol[] symbols = new Symbol[3];
		for(int i = 0; i < symbols.length; i++){
			int s;
			do{
				s = random.nextInt(39) + 1;
			}while(used[s]);
			used[s] = true;
			symbols[i] = Symbol.values()[s];
		}
		return new DimensionPrefix(symbols);
	}
	
	public DimensionPrefix(Symbol[] symbols){
		this.symbols = symbols;
	}
	
	public Symbol[] getSymbols(){
		return symbols;
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof DimensionPrefix){
			DimensionPrefix dp = (DimensionPrefix) o;
			for(int i = 0; i < symbols.length; i++){
				if(symbols[i] != dp.symbols[i]){
					return false;
				}
			}
			return true;
		}
		return false;
	}
}
