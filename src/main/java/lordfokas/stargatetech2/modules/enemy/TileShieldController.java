package lordfokas.stargatetech2.modules.enemy;

import lordfokas.stargatetech2.lib.tileentity.BaseTileEntity;
import lordfokas.stargatetech2.modules.enemy.tileentity.ShieldControllerClient;
import lordfokas.stargatetech2.modules.enemy.tileentity.ShieldControllerServer;


public class TileShieldController extends BaseTileEntity<ShieldControllerClient, ShieldControllerServer>{
	public TileShieldController() {
		super(ShieldControllerClient.class, ShieldControllerServer.class);
	}
	
}