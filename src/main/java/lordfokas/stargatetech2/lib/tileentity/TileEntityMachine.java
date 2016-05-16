package lordfokas.stargatetech2.lib.tileentity;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedList;

import cofh.api.tileentity.IReconfigurableFacing;
import cofh.api.tileentity.IReconfigurableSides;
import cofh.api.tileentity.IRedstoneControl;
import lordfokas.stargatetech2.lib.packet.PacketMachineConfiguration;
import lordfokas.stargatetech2.lib.packet.PacketMachineRedstone;
import lordfokas.stargatetech2.lib.tileentity.ITileContext.Client;
import lordfokas.stargatetech2.lib.tileentity.ITileContext.Server;
import lordfokas.stargatetech2.lib.tileentity.component.ICapabilityTileComponent;
import lordfokas.stargatetech2.lib.tileentity.component.IComponentProvider;
import lordfokas.stargatetech2.lib.tileentity.component.IComponentRegistrar;
import lordfokas.stargatetech2.lib.tileentity.component.ITileComponent;
import lordfokas.stargatetech2.lib.tileentity.faces.Face;
import lordfokas.stargatetech2.lib.tileentity.faces.FaceColor;
import lordfokas.stargatetech2.lib.tileentity.faces.IFacingAware;
import lordfokas.stargatetech2.lib.tileentity.faces.IFacingProvider;
import lordfokas.stargatetech2.util.Helper;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraftforge.common.capabilities.Capability;

/**
 * An advanced TileEntity. Supplies services like automatic rotation handling,
 * side colors, components, among others.<br>
 * <br>
 * If a Context implements {@link IFacingAware} it will be provided with access
 * to this TileEntity's facing data.<br>
 * <br>
 * If a Context implements {@link IComponentProvider} it will be allowed to register
 * components to this machine. Components should implement {@link ITileComponent}.<br>
 * <br>
 * If a Context implements {@link IRedstoneAware} it will be notified of whether or
 * not it should run based on the TE's redstone control mode.<br>
 * <br>
 * Components that implement {@link ICapabilityTileComponent} will be exposed to
 * the outside of the TileEntity.<br>
 * Components that implement {@link IFacingAware} will be allowed to read this
 * TileEntity's facing data.<br>
 * Components that implement {@link ISyncedGUI.Flow} will automatically be synced.
 *
 * @param <C> The type {@link BaseTileEntity#getClientContext()} should return.
 * @param <S> The type {@link BaseTileEntity#getServerContext()} should return.
 * 
 * @author LordFokas
 */
public class TileEntityMachine<C extends Client, S extends Server> extends BaseTileEntity<C, S>
implements IReconfigurableSides, IReconfigurableFacing, IFacingProvider, IComponentRegistrar,
IRedstoneControl{
	
	private static final int COMPONENT_KEYS = 100;
	
	private boolean isComponentRegistrationAllowed = false;
	private EnumMap<Face, FaceWrapper> faces = new EnumMap(Face.class);
	private Face[] faceMap = new Face[6];
	private EnumFacing facing;
	private ControlMode redstoneControl = ControlMode.DISABLED;
	private boolean redstonePower = false;
	private IRedstoneAware rsContext;
	
	private ArrayList<ITileComponent> allComponents = new ArrayList();
	private ArrayList<ICapabilityTileComponent> capableComponents = new ArrayList();
	// TODO: split this into 2 lists:
	private ArrayList<ISyncedGUI.Flow> syncComponents = new ArrayList();
	private int[] syncKeys; // Cached value set for those ^
	private HashMap<CapabilityOnSide, Object> capabilities = new HashMap();
	
	/** Wrapper class to represent a given Capability on a given EnumFacing inside an HashMap */
	private static class CapabilityOnSide{
		private final Capability capability;
		private final EnumFacing side;
		
		public CapabilityOnSide(Capability capability, EnumFacing side){
			this.capability = capability;
			this.side = side;
		}
		
		@Override
		public int hashCode() {
			return capability.hashCode() ^ side.hashCode();
		}
		
		@Override
		public boolean equals(Object obj) {
			if(!(obj instanceof CapabilityOnSide)) return false;
			CapabilityOnSide other = (CapabilityOnSide) obj;
			return other.capability.equals(capability) && other.side == side;
		}
	}
	
	public TileEntityMachine(Class<? extends C> client, Class<? extends S> server, FaceColor ... colors) {
		super(client, server);
		facing = EnumFacing.SOUTH;
		setMap(EnumFacing.UP, Face.TOP);
		setMap(EnumFacing.DOWN, Face.BOTTOM);
		remapSides(false); // don't update the clients; we may even be on the client side!
		for(Face face : faceMap){
			faces.put(face, new FaceWrapper(colors));
		}
		if(context instanceof IFacingAware){
			((IFacingAware)context).setProvider(this);
		}
		if(context instanceof IRedstoneAware){
			rsContext = (IRedstoneAware) context;
		}
		isComponentRegistrationAllowed = true;
		if(context instanceof IComponentProvider){
			((IComponentProvider)context).registerComponents(this);
		}
		isComponentRegistrationAllowed = false;
		cacheSyncValueArray();
	}
	
	@Override
	public void registerComponent(ITileComponent component) {
		if(!isComponentRegistrationAllowed)
			throw new RuntimeException("ITileComponent registration CANNOT be delayed. Respect the f**king API!");
		
		// this must be first because the component may be discarded!
		if(component instanceof ICapabilityTileComponent){
			ICapabilityTileComponent capable = (ICapabilityTileComponent) component;
			Capability capability = capable.getCapability();
			if(capability != null)
				capableComponents.add(capable);
			else if(capable.discardIfNull())
				return;
		}// end "must be first"
		
		allComponents.add(component);
		if(component instanceof IFacingAware){
			((IFacingAware)component).setProvider(this);
		}
		if(component instanceof ISyncedGUI.Flow){
			syncComponents.add((ISyncedGUI.Flow) component);
		}
		
		/*if(component instanceof IBusComponent){ // FIXME: find a better way to work around this
			((IBusComponent)component).setBusDevice((ISyncBusDevice)this);
		}*/
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing){
		return capabilities.containsKey(new CapabilityOnSide(capability, facing));
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing){
		return (T) capabilities.get(new CapabilityOnSide(capability, facing));
	}
	
	// ##########################################################
	// Reconfigurable Facing
	
	@Override
	public int getFacing() {
		return facing.ordinal();
	}
	
	@Override
	public boolean allowYAxisFacing() {
		return false;
	}
	
	@Override
	public boolean rotateBlock() {
		int side = facing.ordinal() + 1;
		if(side == 6) side = 2;
		setFacing(side);
		return true;
	}
	
	public boolean setFacing(EnumFacing facing){
		this.facing = facing;
		remapSides(true);
		return true;
	}
	
	public void setFacingFrom(Entity entity){
		setFacing(Helper.yaw2dir(entity.rotationYaw, 0, allowYAxisFacing()));
	}
	
	public boolean setFacing(int side) {
		return setFacing(EnumFacing.getFront(side));
	}
	
	private void remapSides(boolean update){
		setMap(facing, Face.FRONT);
		setMap(facing.getOpposite(), Face.BACK);
		
		EnumFacing left = facing.rotateAround(Axis.Y); // rotate on Y axis
		setMap(left, Face.LEFT);
		setMap(left.getOpposite(), Face.RIGHT);
		
		if(update) super.updateClients();
		
		capabilities.clear();
		for(ICapabilityTileComponent c : capableComponents){
			Capability capability = c.getCapability();
			for(EnumFacing side : EnumFacing.values()){
				Object impl = c.getCapability(side);
				if(c != null)
					capabilities.put(new CapabilityOnSide(capability, side), impl);
			}
		}
	}
	
	private void setMap(EnumFacing dir, Face face){
		faceMap[dir.ordinal()] = face;
	}
	
	// ##########################################################
	// Reconfigurable Sides
	
	@Override
	public boolean decrSide(EnumFacing side) {
		FaceWrapper face = getFaceForSide(side);
		if(face.count() < 2) return false;
		if(this.side.isClient()){
			PacketMachineConfiguration pmc = new PacketMachineConfiguration();
			pmc.coordinates = pos;
			pmc.increase = false;
			pmc.side = side;
			pmc.sendToServer();
		}else{
			face.decrease();
			super.updateClients();
		}
		return true;
	}
	
	@Override
	public boolean incrSide(EnumFacing side) {
		FaceWrapper face = getFaceForSide(side);
		if(face.count() < 2) return false;
		if(this.side.isClient()){
			PacketMachineConfiguration pmc = new PacketMachineConfiguration();
			pmc.coordinates = pos;
			pmc.increase = false; // TODO check weird logic, possible copy-paste bug.
			pmc.side = side;
			pmc.sendToServer();
		}else{
			face.increase();
			super.updateClients();
		}
		return true;
	}

	@Override // TODO why is this unused?
	public boolean setSide(EnumFacing side, int config) {
		return false;
	}

	@Override
	public boolean resetSides() {
		if(this.side.isClient()){
			PacketMachineConfiguration pmc = new PacketMachineConfiguration();
			pmc.coordinates = pos;
			pmc.reset = true;
			pmc.sendToServer();
		}else{
			for(FaceWrapper fw : faces.values()){
				fw.reset();
			}
			super.updateClients();
		}
		return true;
	}

	@Override
	public int getNumConfig(EnumFacing side) {
		return getFaceForSide(side).count();
	}
	
	private FaceWrapper getFaceForSide(EnumFacing side){
		return faces.get(faceMap[side.ordinal()]);
	}
	
	@Override
	public FaceColor getColorForSide(EnumFacing side){
		if(side.ordinal() > 5) return FaceColor.VOID;
		return getFaceForSide(side).getColor();
	}
	
	/*@Override TODO: no longer used, find alternative and remove.
	public IIcon getTexture(int side, int pass) {
		return getTexture(side, pass, side == facing.ordinal() && !getFaceForSide(side).getColor().isColored());
	}*/
	
	// TODO: see above. This remains to avoid logic loss.
	/*public IIcon getTexture(int side, int pass, boolean useFace) {
		FaceColor color = getFaceForSide(side).getColor();
		if(pass == 0){
			if(useFace) return getBlockType().getIcon(3, 0);
			String texture = null;
			if(side == 0) texture = TextureReference.MACHINE_BOTTOM;
			else if(side == 1) texture = TextureReference.MACHINE_TOP;
			else texture = TextureReference.MACHINE_SIDE;
			if(color.isColored()) texture += "I";
			return IconRegistry.blockIcons.get(texture);
		}else if(pass == 1 && !useFace){
			return IconRegistry.blockIcons.get(color.getTexture());
		}else return IconRegistry.blockIcons.get(TextureReference.TEXTURE_INVISIBLE);
	}*/
	
	// ##########################################################
	// Face Wrapper
	
	private static class FaceWrapper{
		private final FaceColor[] faces;
		private int face;
		
		// NBT ****************************************
		public FaceWrapper(int face, int ... faces){
			this.faces = new FaceColor[faces.length];
			this.face = face;
			for(int i = 0; i < faces.length; i++){
				this.faces[i] = FaceColor.values()[faces[i]];
			}
		}
		
		public int getFace(){return face;}
		
		public int[] getColors(){
			int[] faces = new int[this.faces.length];
			for(int i = 0; i < faces.length; i++){
				faces[i] = this.faces[i].ordinal();
			}
			return faces;
		}
		// End NBT ************************************
		
		public FaceWrapper(FaceColor ... faces){
			this.faces = faces;
			this.face = 0;
		}
		
		public FaceColor getColor(){
			return faces[face];
		}
		
		public int count(){
			return faces.length;
		}
		
		public void increase(){
			face++;
			if(face == count()){
				face -= count();
			}
		}
		
		public void decrease(){
			face--;
			if(face < 0){
				face += count();
			}
		}
		
		public void reset(){
			face = 0;
		}
	}
	
	// ##########################################################
	// Redstone Control
	
	@Override
	public void setPowered(boolean isPowered) {
		if(side.isServer()){
			redstonePower = isPowered;
			updateRS();
		}
	}

	@Override
	public boolean isPowered() {
		return redstonePower;
	}

	@Override
	public void setControl(ControlMode control) {
		if(side.isClient()){
			PacketMachineRedstone pmr = new PacketMachineRedstone();
			pmr.coordinates = pos;
			pmr.mode = control;
			pmr.sendToServer();
		}else{
			redstoneControl = control;
			updateRS();
			updateClients();
		}
	}

	@Override
	public ControlMode getControl() {
		return redstoneControl;
	}
	
	private void updateRS(){
		rsContext.setUsesRedstone(!redstoneControl.isDisabled());
		rsContext.onRedstoneState(shouldRun());
	}
	
	private boolean shouldRun(){
		switch(redstoneControl){
			case DISABLED: return true;
			case LOW: return !redstonePower;
			case HIGH: return redstonePower;
			default: throw new RuntimeException("Is RedstoneControl null?");
		}
	}
	
	// ##########################################################
	// Nice little NBT handling
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		redstoneControl = ControlMode.values()[nbt.getInteger("rsControl")];
		redstonePower = nbt.getBoolean("rsPower");
		NBTTagCompound facingNBT = nbt.getCompoundTag("facing");
		facing = EnumFacing.values()[facingNBT.getInteger("facing")];
		faces = new EnumMap(Face.class);
		for(int i = 0; i < faceMap.length; i++){
			int f = facingNBT.getInteger("face_" + i);
			Face face = Face.values()[i];
			faceMap[i] = face;
			NBTTagCompound wrapper = facingNBT.getCompoundTag("fw_" + f);
			int fc = wrapper.getInteger("face");
			int[] c = wrapper.getIntArray("colors");
			faces.put(face, new FaceWrapper(fc, c));
		}
		NBTTagCompound components = nbt.getCompoundTag("components");
		int size = components.getInteger("size");
		for(int i = 0; i < size; i++){
			allComponents.get(i).readFromNBT(components.getCompoundTag("comp_" + i));
		}
		if(side.isClient() && worldObj != null){
			worldObj.markBlockForUpdate(pos);
		}
	}
	
	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setInteger("rsControl", redstoneControl.ordinal());
		nbt.setBoolean("rsPower", redstonePower);
		NBTTagCompound facingNBT = new NBTTagCompound();
		facingNBT.setInteger("facing", facing.ordinal());
		for(int i = 0; i < faceMap.length; i++){
			Face face = faceMap[i];
			facingNBT.setInteger("face_" + i, face.ordinal());
			FaceWrapper fw = faces.get(face);
			NBTTagCompound wrapper = new NBTTagCompound();
			wrapper.setInteger("face", fw.getFace());
			wrapper.setIntArray("colors", fw.getColors());
			facingNBT.setTag("fw_" + face.ordinal(), wrapper);
		}
		nbt.setTag("facing", facingNBT);
		NBTTagCompound components = new NBTTagCompound();
		for(int i = 0; i < allComponents.size(); i++){
			components.setTag("comp_" + i, allComponents.get(i).writeToNBT(new NBTTagCompound()));
		}
		components.setInteger("size", allComponents.size());
		nbt.setTag("components", components);
	}
	
	// ##########################################################
	// SYNC OVERRIDE FOR COMPONENTS
	
	// I know, I know.
	// This only runs ONCE per TE at construction phase.
	private void cacheSyncValueArray(){
		LinkedList<Integer> keys = new LinkedList();
		int i;
		for(i = 0; i < syncComponents.size(); i++){
			ISyncedGUI.Flow sync = syncComponents.get(i);
			int[] vals = sync.getKeyArray();
			for(int val : vals){
				keys.add((COMPONENT_KEYS * i) + val);
			}
		}
		if(context instanceof ISyncedGUI.Source){
			int[] vals = ((ISyncedGUI.Source)context).getKeyArray();
			for(int val : vals){
				keys.add((COMPONENT_KEYS * i) + val);
			}
		}
		syncKeys = new int[keys.size()];
		int pos = 0;
		for(Integer key : keys){
			syncKeys[pos++] = key;
		}
	}
	
	@Override
	public int[] getKeyArray() {
		return syncKeys;
	}
	
	@Override
	public int getValue(int key) {
		int component = key / COMPONENT_KEYS;
		int actualKey = key % COMPONENT_KEYS;
		if(component < syncComponents.size()){
			return syncComponents.get(component).getValue(actualKey);
		}else{
			return super.getValue(actualKey);
		}
	}
	
	@Override
	public void setValue(int key, int val) {
		int component = key / COMPONENT_KEYS;
		int actualKey = key % COMPONENT_KEYS;
		if(component < syncComponents.size()){
			syncComponents.get(component).setValue(actualKey, val);
		}else{
			super.setValue(actualKey, val);
		}
	}
}