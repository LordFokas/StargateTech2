package stargatetech2.common.reference;

import java.util.ArrayList;

public class TextureReference {
	public static final ArrayList<String> BLOCKS = new ArrayList<String>(10);
	public static final ArrayList<String> ITEMS  = new ArrayList<String>(10);
	
	public static final String IONIZED_PARTICLES = "ionizedParticles";
	
	static{
		for(String block : BlockReference.ALL_BLOCKS){
			BLOCKS.add(block);
		}
		for(String item : ItemReference.ALL_ITEMS){
			ITEMS.add(item);
		}
	}
}