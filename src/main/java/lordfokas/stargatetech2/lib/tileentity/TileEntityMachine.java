package lordfokas.stargatetech2.lib.tileentity;

import java.util.EnumMap;

import lordfokas.stargatetech2.lib.tileentity.ITileContext.Client;
import lordfokas.stargatetech2.lib.tileentity.ITileContext.Server;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.api.tileentity.IReconfigurableFacing;
import cofh.api.tileentity.IReconfigurableSides;

public class TileEntityMachine<C extends Client, S extends Server> extends BaseTileEntity<C, S>
implements IReconfigurableSides, IReconfigurableFacing{
	private EnumMap<Face, FaceWrapper> faces = new EnumMap(Face.class);
	private Face[] faceMap = Face.values();
	private ForgeDirection facing;
	
	public TileEntityMachine(Class<? extends C> client, Class<? extends S> server) {
		super(client, server);
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

	@Override
	public boolean setFacing(int side) {
		facing = ForgeDirection.getOrientation(side);
		remapSides();
		return true;
	}
	
	private void remapSides(){
		// these 2 are static as for now we don't
		// allow vertical orientations
		setMap(ForgeDirection.UP, Face.TOP);
		setMap(ForgeDirection.DOWN, Face.BOTTOM);
		
		setMap(facing, Face.FRONT);
		setMap(facing.getOpposite(), Face.BACK);
		
		ForgeDirection left = facing.getRotation(ForgeDirection.UP); // rotate on Y axis
		setMap(left, Face.LEFT);
		setMap(left.getOpposite(), Face.RIGHT);
		
		super.updateClients();
	}
	
	private void setMap(ForgeDirection dir, Face face){
		faceMap[dir.ordinal()] = face;
	}
	
	// ##########################################################
	// Reconfigurable Sides
	
	@Override
	public boolean decrSide(int side) {
		FaceWrapper face = getFaceForSide(side);
		if(face.count() < 2) return false;
		face.decrease();
		return true;
	}

	@Override
	public boolean incrSide(int side) {
		FaceWrapper face = getFaceForSide(side);
		if(face.count() < 2) return false;
		face.increase();
		return true;
	}

	@Override
	public boolean setSide(int side, int config) {
		return false;
	}

	@Override
	public boolean resetSides() {
		for(FaceWrapper fw : faces.values()){
			fw.reset();
		}
		return true;
	}

	@Override
	public int getNumConfig(int side) {
		return getFaceForSide(side).count();
	}
	
	private FaceWrapper getFaceForSide(int side){
		return faces.get(faceMap[side]);
	}
	
	protected FaceColor getColorForSide(int side){
		return getFaceForSide(side).getColor();
	}
	
	// ##########################################################
	// Face Wrapper
	
	private static class FaceWrapper{
		private final FaceColor[] faces;
		private int face;
		
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
}