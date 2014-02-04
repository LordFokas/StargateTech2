package stargatetech2.core.reference;

import java.util.ArrayList;

public class BlockReference {
	public static final ArrayList<String> ALL_BLOCKS = new ArrayList<String>(10);
	
	// CORE
	public static final String SHIELD_EMITTER = "shieldEmitter";
	public static final String PARTICLE_IONIZER = "particleIonizer";
	public static final String SHIELD = "shield";
	public static final String NAQUADAH_RAIL = "naquadahRail";
	public static final String NAQUADAH_ORE = "naquadahOre";
	public static final String TRANSPORT_RING = "transportRing";
	public static final String INVISIBLE = "invisible";
	public static final String LANTEAN_WALL = "lanteanWall";
	public static final String STARGATE = "stargate";
	public static final String BUS_CABLE = "busCable";
	
	// INTEGRATION
	public static final String BUS_ADAPTER = "busAdapter";
	
	static{
		ALL_BLOCKS.add(SHIELD_EMITTER);
		ALL_BLOCKS.add(PARTICLE_IONIZER);
		ALL_BLOCKS.add(SHIELD);
		ALL_BLOCKS.add(NAQUADAH_RAIL);
		ALL_BLOCKS.add(NAQUADAH_ORE);
		ALL_BLOCKS.add(TRANSPORT_RING);
		ALL_BLOCKS.add(INVISIBLE);
		ALL_BLOCKS.add(LANTEAN_WALL);
		ALL_BLOCKS.add(STARGATE);
		ALL_BLOCKS.add(BUS_CABLE);
		
		ALL_BLOCKS.add(BUS_ADAPTER);
	}
}