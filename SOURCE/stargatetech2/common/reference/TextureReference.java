package stargatetech2.common.reference;

import java.util.ArrayList;

public class TextureReference {
	public static final ArrayList<String> BLOCKS = new ArrayList<String>(10);
	
	static{
		for(String block : BlockReference.ALL_BLOCKS){
			BLOCKS.add(block);
		}
	}
}