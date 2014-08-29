package lordfokas.stargatetech2.transport.block;

import lordfokas.stargatetech2.api.ITabletAccess;
import lordfokas.stargatetech2.api.bus.BusEvent;
import lordfokas.stargatetech2.api.stargate.ITileStargate;
import lordfokas.stargatetech2.api.stargate.ITileStargateBase;
import lordfokas.stargatetech2.api.stargate.StargateEvent;
import lordfokas.stargatetech2.core.base.BaseBlockContainer;
import lordfokas.stargatetech2.core.base.BaseTileEntity;
import lordfokas.stargatetech2.core.reference.BlockReference;
import lordfokas.stargatetech2.core.reference.TextureReference;
import lordfokas.stargatetech2.core.util.IconRegistry;
import lordfokas.stargatetech2.transport.item.ItemBlockStargate;
import lordfokas.stargatetech2.transport.packet.PacketPrintAddress;
import lordfokas.stargatetech2.transport.rendering.RenderStargateBlock;
import lordfokas.stargatetech2.transport.tileentity.TileStargate;
import lordfokas.stargatetech2.transport.tileentity.TileStargateBase;
import lordfokas.stargatetech2.transport.tileentity.TileStargateRing;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import buildcraft.api.tools.IToolWrench;
import cpw.mods.fml.common.registry.GameRegistry;

public class BlockStargate extends BaseBlockContainer implements ITabletAccess{
        public static final int META_BASE = 0x0E;
        public static final int META_RING = 0x0F;

        public BlockStargate() {
                super(BlockReference.STARGATE);
        }
        
        @Override
        public int getRenderType(){
                return RenderStargateBlock.instance().getRenderId();
        }
        
        @Override
        public boolean isOpaqueCube(){
                return false;
        }
        
        @Override
        protected BaseTileEntity createTileEntity(int metadata){
                switch(metadata){
                        case META_BASE: return new TileStargateBase();
                        case META_RING: return new TileStargateRing();
                        default: return new TileStargate();
                }
        }

        @Override
        public boolean onTabletAccess(EntityPlayer player, World world, int x, int y, int z){
                TileEntity te = world.getTileEntity(x, y, z);
                if(te instanceof ITileStargate){
                	PacketPrintAddress ppa = new PacketPrintAddress();
                	ppa.x = x;
                	ppa.y = y;
                	ppa.z = z;
                	ppa.sendToServer();
                }
                return true;
        }
        
        @Override
        public boolean onBlockActivated(World w, int x, int y, int z, EntityPlayer p, int s, float hx, float hy, float hz){
                ItemStack stack = p.inventory.getCurrentItem();
                Item item = stack != null ? stack.getItem() : null;
                if(item instanceof IToolWrench){
                        IToolWrench wrench = (IToolWrench) item;
                        TileEntity te = w.getTileEntity(x, y, z);
                        if(te instanceof ITileStargateBase && wrench.canWrench(p, x, y, z)){
                                TileStargate stargate = null;
                                if(te instanceof TileStargateBase){
                                        stargate = ((TileStargateBase)te).getStargate();
                                }else{
                                        stargate = (TileStargate)te;
                                }
                                
                                if (MinecraftForge.EVENT_BUS.post(new StargateEvent.StargateWrenched(stargate.getAddress(), stargate.getWorldObj(), stargate.xCoord, stargate.yCoord, stargate.zCoord))) return false;
                                stargate.destroyStargate();
                                wrench.wrenchUsed(p, x, y, z);
                                return true;
                        }
                }
                return false;
        }
        
        public void dropStargate(World w, int x, int y, int z){
                w.setBlockToAir(x, y, z);
                dropItemStack(w, x, y, z, new ItemStack(this));
        }
        
        public void setBaseOverride(){
                IIcon bottom = IconRegistry.blockIcons.get(TextureReference.MACHINE_BOTTOM);
                IIcon top = IconRegistry.blockIcons.get(TextureReference.STARGATE_BASE_TOP);
                IIcon side = IconRegistry.blockIcons.get(TextureReference.MACHINE_SIDE);
                this.setOverride(new IIcon[]{bottom, top, side, side, side, side});
        }
        
        @Override
        protected void registerBlock(){
                GameRegistry.registerBlock(this, ItemBlockStargate.class, getUnlocalizedName());
        }
        
        @Override
        public boolean isSideSolid(IBlockAccess world, int x, int y, int z, ForgeDirection side){
                return world.getBlockMetadata(x, y, z) != META_RING;
        }
        
        @Override
        public void onBlockAdded(World w, int x, int y, int z){
                super.onBlockAdded(w, x, y, z);
                int m = w.getBlockMetadata(x, y, z);
                if(m != META_RING) MinecraftForge.EVENT_BUS.post(new BusEvent.AddToNetwork(w, x, y, z));
        }
        
        @Override
        public void breakBlock(World w, int x, int y, int z, Block b, int m){
                super.breakBlock(w, x, y, z, b, m);
                if(m != META_RING) MinecraftForge.EVENT_BUS.post(new BusEvent.RemoveFromNetwork(w, x, y, z));
        }
}