package stargatetech2.core.block;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.MinecraftForge;
import stargatetech2.api.ITabletAccess;
import stargatetech2.api.bus.BusEvent;
import stargatetech2.api.stargate.ITileStargate;
import stargatetech2.api.stargate.ITileStargateBase;
import stargatetech2.common.base.BaseBlockContainer;
import stargatetech2.common.base.BaseTileEntity;
import stargatetech2.common.reference.BlockReference;
import stargatetech2.common.reference.TextureReference;
import stargatetech2.common.util.IconRegistry;
import stargatetech2.core.item.ItemBlockStargate;
import stargatetech2.core.packet.PacketPrintAddress;
import stargatetech2.core.rendering.RenderStargateBlock;
import stargatetech2.core.tileentity.TileStargate;
import stargatetech2.core.tileentity.TileStargateBase;
import stargatetech2.core.tileentity.TileStargateRing;
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
                TileEntity te = world.getBlockTileEntity(x, y, z);
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
                        TileEntity te = w.getBlockTileEntity(x, y, z);
                        if(te instanceof ITileStargateBase && wrench.canWrench(p, x, y, z)){
                                TileStargate stargate = null;
                                if(te instanceof TileStargateBase){
                                        stargate = ((TileStargateBase)te).getStargate();
                                }else{
                                        stargate = (TileStargate)te;
                                }
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
                Icon bottom = IconRegistry.blockIcons.get(TextureReference.MACHINE_BOTTOM);
                Icon top = IconRegistry.blockIcons.get(TextureReference.STARGATE_BASE_TOP);
                Icon side = IconRegistry.blockIcons.get(TextureReference.MACHINE_SIDE);
                this.setOverride(new Icon[]{bottom, top, side, side, side, side});
        }
        
        @Override
        public void registerBlock(){
                GameRegistry.registerBlock(this, ItemBlockStargate.class, getUnlocalizedName());
        }
        
        @Override
        public boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection side){
                return world.getBlockMetadata(x, y, z) != META_RING;
        }
        
        @Override
        public void onBlockAdded(World w, int x, int y, int z){
                super.onBlockAdded(w, x, y, z);
                int m = w.getBlockMetadata(x, y, z);
                if(m != META_RING) MinecraftForge.EVENT_BUS.post(new BusEvent.AddToNetwork(w, x, y, z));
        }
        
        @Override
        public void breakBlock(World w, int x, int y, int z, int i, int m){
                super.breakBlock(w, x, y, z, i, m);
                if(m != META_RING) MinecraftForge.EVENT_BUS.post(new BusEvent.RemoveFromNetwork(w, x, y, z));
        }
}