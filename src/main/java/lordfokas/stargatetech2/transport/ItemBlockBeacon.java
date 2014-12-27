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
		return s.getItemDamage() == 0 ? "block.beaconTransceiver" : "block.beaconAntenna";
	}
}
