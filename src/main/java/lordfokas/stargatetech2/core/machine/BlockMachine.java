package lordfokas.stargatetech2.core.machine;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import lordfokas.stargatetech2.StargateTech2;
import lordfokas.stargatetech2.core.base.BaseBlockContainer;
import lordfokas.stargatetech2.core.reference.TextureReference;
import lordfokas.stargatetech2.core.util.GUIHandler.Screen;
import lordfokas.stargatetech2.core.util.Helper;
import lordfokas.stargatetech2.core.util.IconRegistry;
import buildcraft.api.tools.IToolWrench;

public abstract class BlockMachine extends BaseBlockContainer {
	private boolean useVertical = false;
	private Screen screen;
	
	public BlockMachine(String uName, boolean owned){
		this(uName, owned, null);
	}
	
	public BlockMachine(String uName, boolean owned, Screen screen) {
		super(uName, !owned, true);
		this.screen = screen;
		if(!owned){
			this.setResistance(80000F);
			this.setHardness(4.0F);
			setHarvestLevel("pickaxe", 0);
		}
	}
	
	protected void setUseVertical(){
		useVertical = true;
	}
	
	@Override
	public int getRenderType(){
		return RenderBlockMachine.instance().getRenderId();
	}
	
	public IIcon getFaceForMeta(int meta){
		return getBaseIcon(3, 0);
	}
	
	@Override
	public IIcon getBaseIcon(int side, int meta){
		switch(side){
			case 0: return IconRegistry.blockIcons.get(TextureReference.MACHINE_BOTTOM);
			case 1: return IconRegistry.blockIcons.get(TextureReference.MACHINE_TOP);
			case 3: return blockIcon;
			default: return IconRegistry.blockIcons.get(TextureReference.MACHINE_SIDE);
		}
	}
	
	@Override
	public boolean onBlockActivated(World w, int x, int y, int z, EntityPlayer p, int s, float hx, float hy, float hz){
		ItemStack stack = p.inventory.getCurrentItem();
		Item item = stack != null ? stack.getItem() : null;
		if(item instanceof IToolWrench && canPlayerAccess(p, w, x, y, z) && p.isSneaking()){
			IToolWrench wrench = (IToolWrench) item;
			if(wrench.canWrench(p, x, y, z)){
				dropBlockAsItem(w, x, y, z, 0, 0);
				w.setBlock(x, y, z, (Block) Block.blockRegistry.getObjectById(0), 0, 3);
				wrench.wrenchUsed(p, x, y, z);
				return true;
			}
		}else if(!p.isSneaking() && screen != null && canPlayerAccess(p, w, x, y, z)){
			p.openGui(StargateTech2.instance, screen.ordinal(), w, x, y, z);
			return true;
		}
		return false;
	}
	
	protected final boolean canPlayerAccess(EntityPlayer player, World world, int x, int y, int z){
		TileEntity te = world.getTileEntity(x, y, z);
		if(te instanceof IOwnedMachine){
			return ((IOwnedMachine)te).hasAccess(player.getDisplayName());
		}else{
			return true;
		}
	}
	
	@Override
	public void onBlockPlacedBy(World w, int x, int y, int z, EntityLivingBase living, ItemStack stack){
		ForgeDirection dir = Helper.yaw2dir(living.rotationYaw, living.rotationPitch, useVertical);
		w.setBlockMetadataWithNotify(x, y, z, dir.ordinal(), 2);
		if(living instanceof EntityPlayer){
			TileEntity te = w.getTileEntity(x, y, z);
			if(te instanceof IOwnedMachine){
				((IOwnedMachine)te).setOwner(((EntityPlayer)living).getDisplayName());
			}
			onPlacedBy(w, x, y, z, (EntityPlayer)living, dir);
		}
	}
	
	protected void onPlacedBy(World w, int x, int y, int z, EntityPlayer player, ForgeDirection facing){}
	
	public final FaceColor[] getTextureMap(IBlockAccess w, int x, int y, int z){
		TileEntity te = w.getTileEntity(x, y, z);
		FaceColor[] map = new FaceColor[]{FaceColor.VOID, FaceColor.VOID, FaceColor.VOID, FaceColor.VOID, FaceColor.VOID, FaceColor.VOID};
		if(te instanceof TileMachine){
			TileMachine machine = (TileMachine) te;
			for(int i = 0; i < 6; i++){
				map[i] = machine.getColor(i);
			}
		}
		return map;
	}
	
	@Override protected abstract TileMachine createTileEntity(int metadata);
}
