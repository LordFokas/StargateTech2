package stargatetech2.core.tileentity;

import buildcraft.api.power.IPowerEmitter;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerHandler;
import buildcraft.api.power.PowerHandler.PerditionCalculator;
import buildcraft.api.power.PowerHandler.PowerReceiver;
import buildcraft.api.power.PowerHandler.Type;
import buildcraft.api.transport.IPipeConnection;
import buildcraft.api.transport.IPipeTile.PipeType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import stargatetech2.common.base.BaseTileEntity;
import stargatetech2.common.machine.NearZeroPerdition;

public class TileNaquadahCapacitor extends BaseTileEntity implements IPowerReceptor, IPowerEmitter{
	private static final Settings[] TIERS = new Settings[]{
		new Settings(  8,   40000, "I"  ),
		new Settings( 16,  160000, "II" ),
		new Settings( 80,  800000, "III"),
	};
	
	private PowerHandler capacitor;
	private Settings settings;
	private int tier;
	
	private static class Settings{
		public final int maxPowerRate;
		public final int maxPower;
		public final String name;
		
		public Settings(int rate, int mp, String nm){
			maxPowerRate = rate;
			maxPower = mp;
			name = nm;
		}
		
		public void configure(PowerHandler capacitor){
			capacitor.configure(1, maxPowerRate, maxPower + 1, maxPower);
			capacitor.setPerdition(new NearZeroPerdition(0.1F));
		}
	}
	
	public TileNaquadahCapacitor(){
		capacitor = new PowerHandler(this, Type.STORAGE);
	}
	
	public TileNaquadahCapacitor(int tier){
		this();
		configure(tier);
	}
	
	public int upgrade(int tier){
		int old = this.tier;
		configure(tier);
		return old;
	}
	
	public boolean canUpgrade(int tier){
		return tier > this.tier;
	}
	
	private void configure(int tier){
		settings = TIERS[tier];
		settings.configure(capacitor);
		this.tier = tier;
	}
	
	@Override
	public void updateEntity(){
		if(!worldObj.isRemote){
			sendPower();
		}
	}
	
	@ServerLogic
	private void sendPower(){
		for(ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS){
			TileEntity tile = worldObj.getBlockTileEntity(xCoord + dir.offsetX, yCoord + dir.offsetY, zCoord + dir.offsetZ);
			if(tile instanceof IPowerReceptor){
				PowerReceiver powerReceiver = ((IPowerReceptor)tile).getPowerReceiver(dir.getOpposite());
				if(powerReceiver != null && (powerReceiver.getType() == Type.MACHINE || powerReceiver.getType() == Type.PIPE)){
					float availablePower = capacitor.getEnergyStored();
					float maxPowerToSend = availablePower > settings.maxPowerRate ? settings.maxPowerRate : availablePower;
					float powerNeeded = powerReceiver.powerRequest();
					float powerToSend = powerNeeded < maxPowerToSend ? powerNeeded : maxPowerToSend;
					float powerReceived = powerReceiver.receiveEnergy(Type.STORAGE, powerToSend, dir.getOpposite());
					capacitor.useEnergy(powerReceived, powerReceived, true);
				}
			}
		}
	}
	
	public void setPower(int power){
		capacitor.setEnergy(power);
	}
	
	public int getPower(){
		return Math.round(capacitor.getEnergyStored());
	}
	
	public int getMaxPower(){
		return settings.maxPower;
	}
	
	public String getTierName(){
		return settings.name;
	}
	
	@Override
	protected void readNBT(NBTTagCompound nbt){
		tier = nbt.getInteger("tier");
		settings = TIERS[tier];
		capacitor.readFromNBT(nbt, "capacitor");
		settings.configure(capacitor);
	}

	@Override
	protected void writeNBT(NBTTagCompound nbt){
		nbt.setInteger("tier", tier);
		capacitor.writeToNBT(nbt, "capacitor");
		
		// Display Data
		NBTTagCompound data = new NBTTagCompound();
		data.setString("tier", settings.name);
		data.setString("power", Math.round(capacitor.getEnergyStored()) + " / " + (int)capacitor.getMaxEnergyStored());
		nbt.setCompoundTag("data", data);
	}
	
	@Override
	public PowerReceiver getPowerReceiver(ForgeDirection side){
		return capacitor.getPowerReceiver();
	}
	
	@Override public boolean canEmitPowerFrom(ForgeDirection side){ return true; }
	@Override public void doWork(PowerHandler workProvider){}
	@Override public World getWorld(){ return worldObj; }
}