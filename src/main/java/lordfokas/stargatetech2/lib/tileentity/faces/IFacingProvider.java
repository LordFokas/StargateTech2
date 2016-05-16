package lordfokas.stargatetech2.lib.tileentity.faces;

import net.minecraft.util.EnumFacing;

public interface IFacingProvider {
	public FaceColor getColorForSide(EnumFacing side);
}
