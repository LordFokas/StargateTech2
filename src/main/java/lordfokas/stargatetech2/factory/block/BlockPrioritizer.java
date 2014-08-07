package lordfokas.stargatetech2.factory.block;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import lordfokas.stargatetech2.core.machine.BlockMachine;
import lordfokas.stargatetech2.core.machine.TileMachine;
import lordfokas.stargatetech2.core.reference.BlockReference;
import lordfokas.stargatetech2.core.util.GUIHandler.Screen;
import lordfokas.stargatetech2.factory.item.ItemBlockPrioritizer;
import lordfokas.stargatetech2.factory.tileentity.TilePrioritizer;
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
	public void getSubBlocks(int id, CreativeTabs tab, List list){
		list.add(new ItemStack(id, 1, 0));
		list.add(new ItemStack(id, 1, 1));
		list.add(new ItemStack(id, 1, 2));
	}
	
	@Override
	public void registerBlock(){
		GameRegistry.registerBlock(this, ItemBlockPrioritizer.class, getUnlocalizedName());
	}
}