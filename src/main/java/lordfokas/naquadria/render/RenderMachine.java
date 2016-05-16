package lordfokas.naquadria.render;

@Deprecated
public class RenderMachine extends BaseISBRH{
	/*private static final RenderMachine INSTANCE = new RenderMachine();
	private static final int[] PASSES = new int[]{0, 1};
	
	public static RenderMachine instance(){
		return INSTANCE;
	}
	
	@Override
	public boolean renderWorldBlock(IBlockAccess w, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
		BlockMachine machine = (BlockMachine) block;
		TileEntityMachine tile = (TileEntityMachine) w.getTileEntity(x, y, z);
		for(int pass : PASSES){
			machine.setOverride(getMap(tile, pass));
			renderer.renderStandardBlock(machine, x, y, z);
		}
		machine.restoreTextures();
		return true;
	}
	
	private IIcon[] getMap(TileEntityMachine machine, int pass){
		IIcon[] map = new IIcon[6];
		for(int side = 0; side < 6; side++){
			map[side] = machine.getTexture(side, pass, side == machine.getFacing());
		}
		return map;
	}*/
}