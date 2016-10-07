package lordfokas.stargatetech2.reference;

import java.util.ArrayList;

public class ItemReference {
	public static final ArrayList<String> ALL_ITEMS = new ArrayList<String>(10);
	
	public static final String TABLET_PC = "tablet_pc";
	public static final String PERSONAL_SHIELD = "personal_shield";
	public static final String NAQUADAH = "naquadah";
	
	static{
		ALL_ITEMS.add(TABLET_PC);
		ALL_ITEMS.add(PERSONAL_SHIELD);
		ALL_ITEMS.add(NAQUADAH);
	}
}