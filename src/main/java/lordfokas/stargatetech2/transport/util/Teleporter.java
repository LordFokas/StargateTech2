package lordfokas.stargatetech2.transport.util;

import java.util.Iterator;

import lordfokas.stargatetech2.core.util.Vec3Int;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S07PacketRespawn;
import net.minecraft.network.play.server.S1DPacketEntityEffect;
import net.minecraft.network.play.server.S1FPacketSetExperience;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import cpw.mods.fml.common.FMLCommonHandler;

public class Teleporter{
	private static MinecraftServer mcServer = null;

	public static void teleport(World worldFrom, Entity entity, World worldTo, Vec3Int position, float yaw){
		if(worldFrom.isRemote) return;
		if(mcServer == null) mcServer = FMLCommonHandler.instance().getMinecraftServerInstance();
		teleport(worldTo, entity, position, yaw);
	}
	
	private static Entity teleport(World world, Entity entity, Vec3Int position, float yaw){
		// If there is a mount, unmount, tp, and save for later.
		Entity mount = entity.ridingEntity;
		if(entity.ridingEntity != null){
			entity.mountEntity(null);
			mount = teleport(world, mount, position, yaw);
		}
		// check if we're moving to a different world.
		boolean differentWorld = entity.worldObj != world;
		
		
		//##################################################################################
		entity.worldObj.updateEntityWithOptionalForce(entity, false);
		if(entity instanceof EntityPlayerMP){ // PLAYER
			EntityPlayerMP player = (EntityPlayerMP)entity;
			player.closeScreen();
			if(differentWorld){
				player.dimension = world.provider.dimensionId;
				player.playerNetServerHandler.sendPacket(new S07PacketRespawn(player.dimension, player.worldObj.difficultySetting, world.getWorldInfo().getTerrainType(), player.theItemInWorldManager.getGameType()));
				((WorldServer)entity.worldObj).getPlayerManager().removePlayer(player);
			}
		}
		
		if(differentWorld){
			if(entity instanceof EntityPlayer){
				World w = entity.worldObj;
				EntityPlayer player = (EntityPlayer)entity;
				player.closeScreen();
				w.playerEntities.remove(player);
				w.updateAllPlayersSleepingFlag();
				int i = entity.chunkCoordX;
				int j = entity.chunkCoordZ;
				if((entity.addedToChunk) && (w.getChunkProvider().chunkExists(i, j))){
					w.getChunkFromChunkCoords(i, j).removeEntity(entity);
					w.getChunkFromChunkCoords(i, j).isModified = true;
				}
				w.loadedEntityList.remove(entity);
				w.onEntityRemoved(entity);
			}
			entity.isDead = false;
		}
		entity.setLocationAndAngles(position.x + 0.5D, position.y, position.z + 0.5D, yaw, entity.rotationPitch);
		((WorldServer)world).theChunkProviderServer.loadChunk(position.x >> 4, position.z >> 4);
		if(differentWorld){
			if(!(entity instanceof EntityPlayer)) { // NOT PLAYER
				NBTTagCompound entityNBT = new NBTTagCompound();
				entity.isDead = false;
				entity.writeToNBTOptional(entityNBT);
				entity.isDead = true;
				entity = EntityList.createEntityFromNBT(entityNBT, world);
				if(entity == null) return null;
				entity.dimension = world.provider.dimensionId;
			}
			world.spawnEntityInWorld(entity);
			entity.setWorld(world);
		}
		//##################################################################################
		
		
		//##################################################################################
		entity.setLocationAndAngles(position.x + 0.5D, position.y, position.z + 0.5D, yaw, entity.rotationPitch);
		world.updateEntityWithOptionalForce(entity, false);
		entity.setLocationAndAngles(position.x + 0.5D, position.y, position.z + 0.5D, yaw, entity.rotationPitch);
		if(entity instanceof EntityPlayerMP){ // PLAYER
			EntityPlayerMP player = (EntityPlayerMP)entity;
			if(differentWorld){
				player.mcServer.getConfigurationManager().func_72375_a(player, (WorldServer)world);
			}
			player.playerNetServerHandler.setPlayerLocation(position.x + 0.5D, position.y, position.z + 0.5D, player.rotationYaw, player.rotationPitch);
		}
		world.updateEntityWithOptionalForce(entity, false);
		if(entity instanceof EntityPlayerMP && differentWorld){ // PLAYER  CHANGED WORLD
			EntityPlayerMP player = (EntityPlayerMP)entity;
			player.theItemInWorldManager.setWorld((WorldServer)world);
			player.mcServer.getConfigurationManager().updateTimeAndWeatherForPlayer(player, (WorldServer)world);
			player.mcServer.getConfigurationManager().syncPlayerInventory(player);
			Iterator potions = player.getActivePotionEffects().iterator();
			while (potions.hasNext()){
				PotionEffect effect = (PotionEffect)potions.next();
				player.playerNetServerHandler.sendPacket(new S1DPacketEntityEffect(player.getEntityId(), effect));
			}
			player.playerNetServerHandler.sendPacket(new S1FPacketSetExperience(player.experience, player.experienceTotal, player.experienceLevel));
		}
		entity.setLocationAndAngles(position.x + 0.5D, position.y, position.z + 0.5D, yaw, entity.rotationPitch);
		//##################################################################################
		
		
		// If we had a mount before, hop back on.
		if(mount != null) {
			if(entity instanceof EntityPlayerMP) {
				world.updateEntityWithOptionalForce(entity, true);
			}
			entity.mountEntity(mount);
		}
		return entity; // return ourselves in case we're a mount being teleported.
	}
}