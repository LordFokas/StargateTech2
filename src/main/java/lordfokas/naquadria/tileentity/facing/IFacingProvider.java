package lordfokas.naquadria.tileentity.facing;

import net.minecraft.util.EnumFacing;

public interface IFacingProvider {
	public FaceColor getColorForSide(EnumFacing side);
}
