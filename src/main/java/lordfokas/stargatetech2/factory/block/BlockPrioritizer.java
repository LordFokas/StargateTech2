package lordfokas.stargatetech2.factory.block;

import java.util.List;

import lordfokas.stargatetech2.core.machine.BlockMachine;
import lordfokas.stargatetech2.core.machine.TileMachine;
import lordfokas.stargatetech2.core.reference.BlockReference;
import lordfokas.stargatetech2.core.util.GUIHandler.Screen;
import lordfokas.stargatetech2.factory.item.ItemBlockPrioritizer;
import lordfokas.stargatetech2.factory.tileentity.TilePrioritizer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;

public class BlockPrioritizer extends BlockMachine {

	public BlockPrioritizer() {
		super(BlockReference.PRIORITIZER, false, Screen.PRIORITIZER);
	}

	@Override
	protected TileMachine createTileEntity(int metadata) {
		return new TilePrioritizer();
	}
	
	@Override
	public void getSubBlocks(Item i, CreativeTabs tab, List list){
		list.add(new ItemStack(this, 1, 0));
		list.add(new ItemStack(this, 1, 1));
		list.add(new ItemStack(this, 1, 2));
	}
	
	@Override
	protected void registerBlock(){
		GameRegistry.registerBlock(this, ItemBlockPrioritizer.class, getUnlocalizedName());
	}
}