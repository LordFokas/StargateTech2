package lordfokas.stargatetech2.modules.enemy;

import lordfokas.stargatetech2.lib.tileentity.TileEntityOwnedMachine;
import lordfokas.stargatetech2.lib.tileentity.faces.FaceColor;
import lordfokas.stargatetech2.modules.automation.ISyncBusDevice;
import lordfokas.stargatetech2.modules.enemy.tileentity.ShieldControllerClient;
import lordfokas.stargatetech2.modules.enemy.tileentity.ShieldControllerServer;
import net.minecraftforge.fluids.IFluidHandler;

public class TileShieldController extends TileEntityOwnedMachine<ShieldControllerClient, ShieldControllerServer>
implements IFluidHandler, ISyncBusDevice{
	public TileShieldController() {
		super(ShieldControllerClient.class, ShieldControllerServer.class, FaceColor.VOID, FaceColor.BLUE);
	}
}