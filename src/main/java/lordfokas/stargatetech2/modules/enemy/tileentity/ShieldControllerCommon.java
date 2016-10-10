package lordfokas.stargatetech2.modules.enemy.tileentity;

import lordfokas.naquadria.tileentity.ISyncedGUI;
import lordfokas.naquadria.tileentity.ITile;
import lordfokas.naquadria.tileentity.ITileContext;
import lordfokas.naquadria.tileentity.component.IComponentProvider;
import lordfokas.naquadria.tileentity.component.IComponentRegistrar;
import lordfokas.naquadria.tileentity.component.IFilter;
import lordfokas.naquadria.tileentity.component.tank.FluidFilter;
import lordfokas.naquadria.tileentity.component.tank.Tank;
import lordfokas.naquadria.tileentity.component.tank.TankComponent;
import lordfokas.naquadria.tileentity.facing.FaceColor;
import lordfokas.naquadria.tileentity.facing.FaceColorFilter;
import lordfokas.stargatetech2.api.shields.ShieldPermissions;
import lordfokas.stargatetech2.modules.enemy.IonizedParticles;
import lordfokas.stargatetech2.modules.enemy.ShieldControllerBusDriver;
import net.minecraft.nbt.NBTTagCompound;

public class ShieldControllerCommon implements ITileContext, ISyncedGUI.Flow, IComponentProvider{
	private static final int[] KEYS = new int[]{0, 1};
	protected Tank tank = new Tank.Filtered(16000, new FluidFilter(IonizedParticles.fluid));
	protected ShieldPermissions permissions = ShieldPermissions.getDefault();
	protected ShieldControllerBusDriver driver = new ShieldControllerBusDriver(this);
	protected boolean active;
	protected boolean enabled;
	
	@Override
	public void registerComponents(IComponentRegistrar registrar) {
		TankComponent tankComponent = new TankComponent.Advanced(tank, new FaceColorFilter.MatchColors(FaceColor.BLUE), IFilter.NONE);
		BusComponent busComponent = new BusComponent(driver);
		
		registrar.registerComponent(tankComponent);
		registrar.registerComponent(busComponent.setGenericAccessFilter(new FaceColorFilter.MatchColors(FaceColor.BLUE)));
	}
	
	public void readNBTData(NBTTagCompound nbt) {
		permissions = ShieldPermissions.readFromNBT(nbt.getCompoundTag("permissions"));
		active = nbt.getBoolean("active");
		enabled = nbt.getBoolean("enabled");
	}
	
	public void writeNBTData(NBTTagCompound nbt) {
		nbt.setTag("permissions", permissions.writeToNBT());
		nbt.setBoolean("active", active);
		nbt.setBoolean("enabled", enabled);
	}
	
	@Override
	public int[] getKeyArray() {
		return KEYS;
	}
	
	@Override
	public int getValue(int key) {
		switch(key){
			case 0: return active ? 1 : 0;
			case 1: return enabled ? 1 : 0;
		}
		return -1;
	}
	
	@Override
	public void setValue(int key, int val) {
		switch(key){
			case 0:
				active = (val == 1);
				break;
			case 1:
				enabled = (val == 1);
				break;
		}
	}

	@Override public void tick(){}
	@Override public boolean canTick() { return false; }
	
	@Override
	public void setTile(ITile tile){}
	
	public boolean isShieldOn(){
		return active;
	}
	
	public ShieldPermissions getPermissions(){
		return permissions;
	}
	
	// is just a stub for the server.
	public boolean setShieldStatus(boolean enabled){ return false; }
}