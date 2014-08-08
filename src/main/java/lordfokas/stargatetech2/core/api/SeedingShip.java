package lordfokas.stargatetech2.core.api;

import java.util.UUID;

import com.mojang.authlib.GameProfile;

import lordfokas.stargatetech2.api.stargate.IStargatePlacer;
import lordfokas.stargatetech2.transport.ModuleTransport;
import lordfokas.stargatetech2.transport.item.ItemBlockStargate;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;

public class SeedingShip implements IStargatePlacer{
	public static final SeedingShip SHIP = new SeedingShip();
	
	private SeedingShip(){}
	
	@Override
	public boolean placeStargate(World world, int x, int y, int z, int facing) {
		if(world instanceof WorldServer){
			WorldServer w = (WorldServer) world;
			FakePlayer fake = new FakePlayer(w, new GameProfile(UUID.randomUUID(), "AnquietasGatePlacer"));
			fake.rotationYaw = facing * 90;
			return ItemBlockStargate.instance.placeBlockAt(new ItemStack(ModuleTransport.stargate), fake, w, x, y, z, 0, 0, 0, 0, 0);
		}
		return false;
	}
}