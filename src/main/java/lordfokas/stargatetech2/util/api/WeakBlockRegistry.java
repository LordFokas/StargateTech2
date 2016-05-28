package lordfokas.stargatetech2.util.api;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class WeakBlockRegistry{
	// TODO: allow API access to add weak blocks; Consider using IMC.
	private static final class RemovableWeakBlock{
		private Block block;
		private int meta;
		
		public RemovableWeakBlock(Block b, int meta){
			this.block = b;
			this.meta = meta;
		}
		
		public RemovableWeakBlock(Block b){
			this(b, -1);
		}
		
		@Override
		public boolean equals(Object o){
			if(o instanceof RemovableWeakBlock){
				RemovableWeakBlock sr = (RemovableWeakBlock) o;
				if(meta == -1 || sr.meta == -1){
					return block == sr.block;
				}else{
					return block == sr.block && meta == sr.meta;
				}
			}
			return false;
		}
	}
	
	public static final ArrayList<RemovableWeakBlock> removables = new ArrayList<RemovableWeakBlock>(10);
	
	static{
		// TODO: completely redo this
		/*setWeakBlock(Blocks.deadbush);
		setWeakBlock(Blocks.fire);
		setWeakBlock(Blocks.leaves);
		setWeakBlock(Blocks.red_flower);
		setWeakBlock(Blocks.yellow_flower);
		setWeakBlock(Blocks.redstone_wire);
		setWeakBlock(Blocks.sapling);
		setWeakBlock(Blocks.snow);
		setWeakBlock(Blocks.tallgrass);
		setWeakBlock(Blocks.vine);
		setWeakBlock(Blocks.waterlily);
		setWeakBlock(Blocks.water);
		setWeakBlock(Blocks.flowing_water);
		setWeakBlock(Blocks.lava);
		setWeakBlock(Blocks.flowing_lava);*/
	}
	
	public static final void setWeakBlock(Block b){
		if(!isRemovable(b))
			removables.add(new RemovableWeakBlock(b));
	}
	
	public static final void setWeakBlock(Block b, int meta){
		if(!isRemovable(b, meta))
			removables.add(new RemovableWeakBlock(b, meta));
	}
	
	public static final boolean isRemovable(Block b){
		return removables.contains(new RemovableWeakBlock(b));
	}
	
	public static final boolean isRemovable(Block b, int meta){
		return removables.contains(new RemovableWeakBlock(b, meta));
	}
}