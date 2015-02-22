package lordfokas.stargatetech2.lib.block;

import cpw.mods.fml.common.FMLCommonHandler;
import lordfokas.stargatetech2.StargateTech2;
import lordfokas.stargatetech2.lib.render.RenderMachine;
import lordfokas.stargatetech2.lib.tileentity.TileEntityMachine;
import lordfokas.stargatetech2.lib.util.TileEntityHelper;
import lordfokas.stargatetech2.reference.TextureReference;
import lordfokas.stargatetech2.util.GUIHandler.Screen;
import lordfokas.stargatetech2.util.Helper;
import lordfokas.stargatetech2.util.IconRegistry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import buildcraft.api.tools.IToolWrench;

public class BlockMachine extends BaseBlockContainer {
	private Class<? extends TileEntityMachine> tile;
	
	public BlockMachine(String uName, Class<? extends TileEntityMachine> tile) {
		super(uName, true, true);
		this.tile = tile;
	}
	
	@Override
	public int getRenderType() {
		return RenderMachine.instance().getRenderId();
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack stack) {
		super.onBlockPlacedBy(world, x, y, z, entity, stack);
		if(entity instanceof EntityPlayerMP){
			TileEntityMachine machine = TileEntityHelper.getTileEntityAs(world, x, y, z, TileEntityMachine.class);
			machine.setFacingFrom(entity);
			System.err.println("PLACING!");
		}
	}
	
	@Override
	public boolean onBlockActivated(World w, int x, int y, int z, EntityPlayer p, int s, float hx, float hy, float hz) {
		ItemStack hand = p.getCurrentEquippedItem();
		TileEntityMachine machine = TileEntityHelper.getTileEntityAs(w, x, y, z, TileEntityMachine.class);
		if(hand != null && hand.getItem() instanceof IToolWrench){
			if(p.isSneaking()){
				super.dropSelf(w, x, y, z);
			}else{
				machine.rotateBlock();
			}
		}else if(!p.isSneaking()){
			p.openGui(StargateTech2.instance, Screen.SHIELD_CONTROLLER.ordinal(), w, x, y, z);
		}
		return super.onBlockActivated(w, x, y, z, p, s, hx, hy, hz);
	}
	
	@Override
	public TileEntityMachine createNewTileEntity(World world, int metadata) {
		try {
			return tile.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Error creating instance of Machine TileEntity", e);
		}
	}
	
	@Override
	public IIcon getBaseIcon(int side, int meta) {
		String texture = null;
		switch(side){
			case 0: texture = TextureReference.MACHINE_BOTTOM; break;
			case 1: texture = TextureReference.MACHINE_TOP; break;
			case 3: return blockIcon;
			default: texture = TextureReference.MACHINE_SIDE;
		}
		return IconRegistry.blockIcons.get(texture);
	}
}