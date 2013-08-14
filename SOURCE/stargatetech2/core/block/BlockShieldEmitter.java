package stargatetech2.core.block;

import stargatetech2.api.ITabletAccess;
import stargatetech2.common.machine.BlockMachine;
import stargatetech2.common.reference.BlockReference;
import stargatetech2.common.reference.TextureReference;
import stargatetech2.common.util.GUIHandler.Screen;
import stargatetech2.core.tileentity.TileShieldEmitter;

public class BlockShieldEmitter extends BlockMachine implements ITabletAccess{

	public BlockShieldEmitter() {
		super(BlockReference.SHIELD_EMITTER, Screen.SHIELD_EMITTER);
	}

	@Override
	protected TileShieldEmitter createTileEntity(int metadata) {
		return new TileShieldEmitter();
	}

	@Override
	public String getFace() {
		return TextureReference.FACE_SHIELD_EMITTER;
	}

	@Override
	public String getGlow() {
		return TextureReference.GLOW_SHIELD_EMITTER;
	}	
}