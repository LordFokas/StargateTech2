package lordfokas.stargatetech2.transport;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockBeacon extends ItemBlock{

	public ItemBlockBeacon(Block block) {
		super(block);
		this.setHasSubtypes(true);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack s) {
		switch(s.getItemDamage()){
			case BlockBeacon.META_TRANSCEIVER: return "block.beacon.transceiver";
			case BlockBeacon.META_ANTENNA: return "block.beacon.antenna";
			case BlockBeacon.META_CONSOLE: return "block.beacon.console";
			case BlockBeacon.META_MATTERGRID: return "block.beacon.mattergrid";
		}
		return "UNKNOWN_BEACON_BLOCK";
	}
	
	public int getMetadata(int meta){
        return meta;
    }
}
