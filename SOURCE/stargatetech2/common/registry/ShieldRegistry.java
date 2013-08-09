package stargatetech2.common.registry;

import java.util.ArrayList;

import net.minecraft.block.Block;

public class ShieldRegistry{
	private static final class ShieldRemovable{
		private int id, meta;
		
		public ShieldRemovable(int id, int meta){
			this.id = id;
			this.meta = meta;
		}
		
		public ShieldRemovable(int id){
			this(id, -1);
		}
		
		@Override
		public boolean equals(Object o){
			if(o instanceof ShieldRemovable){
				ShieldRemovable sr = (ShieldRemovable) o;
				if(meta == -1 || sr.meta == -1){
					return id == sr.id;
				}else{
					return id == sr.id && meta == sr.meta;
				}
			}
			return false;
		}
	}
	
	public static final ArrayList<ShieldRemovable> removables = new ArrayList<ShieldRemovable>(10);
	
	static{
		setShieldRemovable(0); // Air
		setShieldRemovable(Block.deadBush.blockID);
		setShieldRemovable(Block.fire.blockID);
		setShieldRemovable(Block.leaves.blockID);
		setShieldRemovable(Block.plantRed.blockID);
		setShieldRemovable(Block.plantYellow.blockID);
		setShieldRemovable(Block.redstoneWire.blockID);
		setShieldRemovable(Block.sapling.blockID);
		setShieldRemovable(Block.snow.blockID);
		setShieldRemovable(Block.tallGrass.blockID);
		setShieldRemovable(Block.vine.blockID);
		setShieldRemovable(Block.waterlily.blockID);
		setShieldRemovable(Block.waterMoving.blockID);
	}
	
	public static final void setShieldRemovable(int id){
		if(!isRemovable(id))
			removables.add(new ShieldRemovable(id));
	}
	
	public static final void setShieldRemovable(int id, int meta){
		if(!isRemovable(id, meta))
			removables.add(new ShieldRemovable(id, meta));
	}
	
	public static final boolean isRemovable(int id){
		return removables.contains(new ShieldRemovable(id));
	}
	
	public static final boolean isRemovable(int id, int meta){
		return removables.contains(new ShieldRemovable(id, meta));
	}
}