package lordfokas.stargatetech2.lib.tileentity.component;

import net.minecraftforge.common.util.ForgeDirection;

public interface IAccessibleTileComponent extends ITileComponent{
	public boolean accessibleOnSide(ForgeDirection side);
	public boolean canInputOnSide(ForgeDirection side);
	public boolean canOutputOnSide(ForgeDirection side);
}
