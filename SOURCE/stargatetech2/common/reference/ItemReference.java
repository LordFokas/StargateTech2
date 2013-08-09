package stargatetech2.common.reference;

import java.util.ArrayList;

public class ItemReference {
	public static final ArrayList<String> ALL_ITEMS = new ArrayList<String>(10);
	
	public static final String TABLET_PC = "tabletPC";
	
	static{
		ALL_ITEMS.add(TABLET_PC);
	}
}
