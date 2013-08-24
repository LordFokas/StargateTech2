package stargatetech2.common.reference;

import java.util.ArrayList;

public class BlockReference {
	public static final ArrayList<String> ALL_BLOCKS = new ArrayList<String>(10);
	
	public static final String SHIELD_EMITTER = "shieldEmitter";
	public static final String PARTICLE_IONIZER = "particleIonizer";
	public static final String SHIELD = "shield";
	public static final String NAQUADAH_RAIL = "naquadahRail";
	public static final String NAQUADAH_ORE = "naquadahOre";
	
	static{
		ALL_BLOCKS.add(SHIELD_EMITTER);
		ALL_BLOCKS.add(PARTICLE_IONIZER);
		ALL_BLOCKS.add(SHIELD);
		ALL_BLOCKS.add(NAQUADAH_RAIL);
		ALL_BLOCKS.add(NAQUADAH_ORE);
	}
}