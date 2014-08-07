package lordfokas.stargatetech2.core.api;

import java.util.ArrayList;

import net.minecraft.block.Block;

public class WeakBlockRegistry{
	// TODO: allow API access to add weak blocks;
	private static final class RemovableWeakBlock{
		private int id, meta;
		
		public RemovableWeakBlock(int id, int meta){
			this.id = id;
			this.meta = meta;
		}
		
		public RemovableWeakBlock(int id){
			this(id, -1);
		}
		
		@Override
		public boolean equals(Object o){
			if(o instanceof RemovableWeakBlock){
				RemovableWeakBlock sr = (RemovableWeakBlock) o;
				if(meta == -1 || sr.meta == -1){
					return id == sr.id;
				}else{
					return id == sr.id && meta == sr.meta;
				}
			}
			return false;
		}
	}
	
	public static final ArrayList<RemovableWeakBlock> removables = new ArrayList<RemovableWeakBlock>(10);
	
	static{
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
			removables.add(new RemovableWeakBlock(id));
	}
	
	public static final void setShieldRemovable(int id, int meta){
		if(!isRemovable(id, meta))
			removables.add(new RemovableWeakBlock(id, meta));
	}
	
	public static final boolean isRemovable(int id){
		return removables.contains(new RemovableWeakBlock(id));
	}
	
	public static final boolean isRemovable(int id, int meta){
		return removables.contains(new RemovableWeakBlock(id, meta));
	}
}