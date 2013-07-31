package stargatetech2.common.reference;

import java.util.ArrayList;

public class BlockReference {
	public static final ArrayList<String> ALL_BLOCKS = new ArrayList<String>(10);
	
	public static final String SUPPORT_FRAME	= "supportframe";
	
	static{
		ALL_BLOCKS.add(SUPPORT_FRAME);
	}
}