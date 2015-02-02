package lordfokas.stargatetech2.lib.tileentity;

import lordfokas.stargatetech2.lib.tileentity.ITileContext.Client;
import lordfokas.stargatetech2.lib.tileentity.ITileContext.Server;
import cofh.api.tileentity.IReconfigurableSides;

public class TileEntityMachine<C extends Client, S extends Server> extends BaseTileEntity<C, S>
implements IReconfigurableSides{
	private FaceWrapper[] faces = new FaceWrapper[6];
	
	public TileEntityMachine(Class<? extends C> client, Class<? extends S> server) {
		super(client, server);
	}

	@Override
	public boolean decrSide(int side) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean incrSide(int side) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean setSide(int side, int config) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean resetSides() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getNumConfig(int side) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	private static class FaceWrapper{
		
	}
}