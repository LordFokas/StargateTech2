package lordfokas.stargatetech2.lib.tileentity.faces;

import net.minecraftforge.common.util.ForgeDirection;

public interface IFacingProvider {
	public FaceColor getColorForSide(int side);
	public FaceColor getColorForDirection(ForgeDirection dir);
}
