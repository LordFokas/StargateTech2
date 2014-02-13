package stargatetech2.core.machine;

import stargatetech2.core.base.BaseTileEntity;

public abstract class TileEntityMachine extends BaseTileEntity {
	public abstract FaceColor getColorOnSide(int side);
}