package lordfokas.stargatetech2.core.api;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.FakePlayer;
import lordfokas.stargatetech2.api.stargate.IStargatePlacer;
import lordfokas.stargatetech2.transport.ModuleTransport;
import lordfokas.stargatetech2.transport.item.ItemBlockStargate;

public class SeedingShip implements IStargatePlacer{
	public static final SeedingShip SHIP = new SeedingShip();
	
	private SeedingShip(){}
	
	@Override
	public boolean placeStargate(World w, int x, int y, int z, int facing) {
		FakePlayer fake = new FakePlayer(w, "AnquietasGatePlacer");
		fake.rotationYaw = facing * 90;
		return ItemBlockStargate.instance.placeBlockAt(new ItemStack(ModuleTransport.stargate), fake, w, x, y, z, 0, 0, 0, 0, 0);
	}
}