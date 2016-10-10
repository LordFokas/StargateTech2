package lordfokas.stargatetech2.modules.enemy;

import lordfokas.naquadria.tileentity.TileEntityOwnedMachine;
import lordfokas.naquadria.tileentity.facing.FaceColor;
import lordfokas.stargatetech2.modules.automation.ISyncBusDevice;
import lordfokas.stargatetech2.modules.enemy.tileentity.ShieldControllerClient;
import lordfokas.stargatetech2.modules.enemy.tileentity.ShieldControllerServer;
import net.minecraftforge.fluids.IFluidHandler;

public class TileShieldController extends TileEntityOwnedMachine<ShieldControllerClient, ShieldControllerServer>{
	public TileShieldController() {
		super(ShieldControllerClient.class, ShieldControllerServer.class, FaceColor.VOID, FaceColor.BLUE);
	}
}