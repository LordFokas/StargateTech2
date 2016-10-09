package lordfokas.stargatetech2.ZZ_THRASH;

@Deprecated
public abstract class BaseISBRH_THRASH {
	/*protected static final float F01 = 0.0625F;
	protected static final float F02 = F01 *  2F;
	protected static final float F03 = F01 *  3F;
	protected static final float F04 = F01 *  4F;
	protected static final float F05 = F01 *  5F;
	protected static final float F06 = F01 *  6F;
	protected static final float F07 = F01 *  7F;
	protected static final float F08 = F01 *  8F;
	protected static final float F09 = F01 *  9F;
	protected static final float F10 = F01 * 10F;
	protected static final float F11 = F01 * 11F;
	protected static final float F12 = F01 * 12F;
	protected static final float F13 = F01 * 13F;
	protected static final float F14 = F01 * 14F;
	protected static final float F15 = F01 * 15F;
	
	private final int renderID;
	
	protected BaseISBRH(){
		renderID = RenderingRegistry.getNextAvailableRenderId();
	}
	
	@Override
	public boolean shouldRender3DInInventory(int modelId) {
		return true;
	}
	
	@Override
	public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer){
		block.setBlockBoundsForItemRender();
		renderInventoryCuboid(block, metadata, renderer);
	}
	
	protected Color getRenderColor(int metadata){ return null; }

	@Override
	public abstract boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer);
	
	@Override
	public int getRenderId(){
		return renderID;
	}
	
	public final void register(){
		RenderingRegistry.registerBlockHandler(renderID, this);
	}
	
	protected final void renderWorldCuboid(int x, int y, int z, Block block, RenderBlocks renderer){
		renderer.renderStandardBlock(block, x, y, z);
	}
	
	protected final void renderInventoryCuboid(Block block, int meta, RenderBlocks renderer){
		renderInventoryCuboid(block, meta, renderer, true);
	}
	
	protected final void renderInventoryCuboid(Block block, int meta, RenderBlocks renderer, boolean useBlockBounds){
		IIcon[] tmap = new IIcon[6];
		for(int i = 0; i < 6; i++){
			tmap[i] = block.getIcon(i, meta);
		}
		
		if(useBlockBounds) renderer.setRenderBoundsFromBlock(block);
		Tessellator tessellator = Tessellator.instance;
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		tessellator.startDrawingQuads();
		
		Color color = getRenderColor(meta);
		if(color != null){
			tessellator.setColorOpaque(color.r, color.g, color.b);
		}
		
		tessellator.setNormal(0.0F, -1F, 0.0F);
		renderer.renderFaceYNeg(block, 0, 0, 0, tmap[0]);
		tessellator.setNormal(0.0F,  1F, 0.0F);
		renderer.renderFaceYPos(block, 0, 0, 0, tmap[1]);
		tessellator.setNormal(0.0F, 0.0F, -1F);
		renderer.renderFaceZNeg(block, 0, 0, 0, tmap[2]);
		tessellator.setNormal(0.0F, 0.0F,  1F);
		renderer.renderFaceZPos(block, 0, 0, 0, tmap[3]);
		tessellator.setNormal(-1F, 0.0F, 0.0F);
		renderer.renderFaceXNeg(block, 0, 0, 0, tmap[4]);
		tessellator.setNormal( 1F, 0.0F, 0.0F);
		renderer.renderFaceXPos(block, 0, 0, 0, tmap[5]);
		
		tessellator.draw();
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}
	
	protected final void renderWorldCuboidWithBounds(Block block, RenderBlocks renderer, int x, int y, int z, double minX, double minY, double minZ, double maxX, double maxY, double maxZ){
		renderer.setRenderBounds(minX, minY, minZ, maxX, maxY, maxZ);
		renderWorldCuboid(x, y, z, block, renderer);
	}*/
}